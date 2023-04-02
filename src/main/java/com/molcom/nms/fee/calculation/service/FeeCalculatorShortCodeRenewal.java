package com.molcom.nms.fee.calculation.service;

import com.molcom.nms.fee.calculation.dto.GenerateShortCodeInvoiceRes;
import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.fee.schedule.repository.IFeeScheduleRepo;
import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeeCalculatorShortCodeRenewal {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;

    @Autowired
    private IFeeScheduleRepo feeScheduleRepo;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    /**
     * Fee calculator short code renewal
     *
     * @param applicationId
     * @throws Exception
     */
    public void generateShortCodeRenewal(String applicationId) throws Exception {
        String response = "";
        List<Integer> shortCodeFeeRenewal = new ArrayList<>();
        Integer finalAllocationFee = 0;

        // get fee selected for this applicationId
        List<SelectedFeeScheduleModel> list = selectedFeeScheduleRepository.findByApplicationIdRenewalFee(applicationId);
        log.info("Selected fees for the application {} ", list);

        // For each of the selected fee for the application
        // get the fee in fee schedule and get out the renewal value
        // add the renewal value to an array list and sum it up
        list.forEach(val -> {
            log.info("Selected fees for the application {} ", val.getFeeName());
            try {
                List<FeeScheduleModel> feeScheduleModelList =
                        feeScheduleRepo.getShortCodeFeeByFeeName(val.getFeeName().toUpperCase());
                if (feeScheduleModelList != null &&
                        feeScheduleModelList.size() >= 1) {
                    String fff = feeScheduleModelList.get(0).getRenewableValueType();
                    shortCodeFeeRenewal.add(Integer.parseInt(fff));


                    // save the renewal fee in selected fee table
                    SelectedFeeScheduleModel selectedFee = new SelectedFeeScheduleModel();
                    selectedFee.setApplicationId(applicationId);
                    selectedFee.setFeeAmount(String.valueOf(feeScheduleModelList.get(0).getRenewableValueType()));
                    selectedFee.setFeeName(feeScheduleModelList.get(0).getFeeType());
                    selectedFee.setFeeDescription(feeScheduleModelList.get(0).getFeeType());
                    selectedFee.setInvoiceType("RENEWAL");
                    int isFeePersisted = selectedFeeScheduleRepository.save(selectedFee);
                    log.info("Fee persist status for short code renewal application with application id "
                            + applicationId + "is  {} ", isFeePersisted);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Integer sum = shortCodeFeeRenewal.stream()
                .collect(Collectors.summingInt(Integer::intValue));

        // Multiply by total number selected if number selected is greater than one
        // else calculate for just number selected

        GenerateShortCodeInvoiceRes ress = new GenerateShortCodeInvoiceRes();

//        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(applicationId);
//        if (selectedNumberCount > 1) {
//            finalAllocationFee = sum * selectedNumberCount;
//            ress.setAllocationFee(finalAllocationFee);
//        } else {
//            finalAllocationFee = sum;
//            ress.setAllocationFee(sum);
//        }

        finalAllocationFee = sum;
        ress.setAllocationFee(sum);

        // Persist short code renewal fee to invoice table
        // sendInvoiceToEservices job will pick it since the ShouldSendToEservices
        // flag will be set to true on persist
        int invoiceResCode = invoiceService.persistInvoice(applicationId,
                "SHORT-CODE", finalAllocationFee, "UNPAID", true, "", "RENEWAL");
        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

        if (invoiceResCode >= 1) {
            int res = selectedNumbersRepo.updateApplicationNotToBePickedRenewal(applicationId);

            log.info("Short code" + " " + applicationId +
                    " is updated not to be sent for renewal code {} ", res);

            log.info("Short code renewal generated as saved successfully " +
                    "for application id {} and renewal fee {}", applicationId, sum);
        } else {
            log.info("Short code renewal failed for application {} ", applicationId);
        }
    }
}
