package com.molcom.nms.fee.calculation.service;

import com.molcom.nms.fee.calculation.dto.GenerateShortCodeInvoiceReq;
import com.molcom.nms.fee.calculation.dto.GenerateShortCodeInvoiceRes;
import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeeCalculatorShortCodeNew {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    /**
     * Fee calculator new short code
     *
     * @param req
     * @return
     * @throws Exception
     */
    public GenericResponse<GenerateShortCodeInvoiceRes> generateShortCodeAllocation(GenerateShortCodeInvoiceReq req) throws Exception {
        GenericResponse<GenerateShortCodeInvoiceRes> response = new GenericResponse<>();
        String appId = req.getApplicationId();
        Integer finalAllocationFee = 0;
        log.info("APPLICATION ID {}", req.getApplicationId());
        try {
            List<Integer> shortCodeFee = new ArrayList<>();

            req.getShortCodeFeeList().forEach(fee -> {
                shortCodeFee.add(fee.getAmount());

                try {
                    // persist in selected fee table so it will be retrieved during renewal
                    SelectedFeeScheduleModel selectedFee = new SelectedFeeScheduleModel();
                    selectedFee.setApplicationId(appId);
                    selectedFee.setFeeAmount(String.valueOf(fee.getAmount()));
                    selectedFee.setFeeName(fee.getFeename());
                    selectedFee.setFeeDescription(fee.getFeeDescription());
                    selectedFee.setInvoiceType("NEW");
                    int isFeePersisted = selectedFeeScheduleRepository.save(selectedFee);
                    log.info("Fee persist status for short code application with application id "
                            + appId + "is  {} ", isFeePersisted);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            Integer sum = shortCodeFee.stream()
                    .collect(Collectors.summingInt(Integer::intValue));

            // Multiply by total number selected if number selected is greater than one
            // else calculate for just number selected
            GenerateShortCodeInvoiceRes ress = new GenerateShortCodeInvoiceRes();

//            int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(appId);
//            if (selectedNumberCount > 1) {
//                finalAllocationFee = sum * selectedNumberCount;
//                ress.setAllocationFee(finalAllocationFee);
//            } else {
//                finalAllocationFee = sum;
//                ress.setAllocationFee(sum);
//            }

            // Total as the sum of the selected fees
            finalAllocationFee = sum;
            ress.setAllocationFee(sum);

            // Persist short code allocation fee to invoice table
            // sendInvoiceToEservices job will pick it since the ShouldSendToEservices
            // flag will be set to true on persist
            int invoiceResCode = invoiceService.persistInvoice(appId,
                    "SHORT-CODE", finalAllocationFee, "UNPAID", true, "", "NEW");
            log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

            response.setResponseCode(ResponseStatus.SUCCESS.getCode());
            response.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            response.setOutputPayload(ress);

        } catch (Exception exception) {
            response.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
            response.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return response;
    }
}
