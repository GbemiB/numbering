package com.molcom.nms.fee.calculation.service;

import com.molcom.nms.fee.calculation.dto.FeeCalculationRequest;
import com.molcom.nms.fee.calculation.dto.FeeCalculationResponse;
import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.fee.schedule.repository.IFeeScheduleRepo;
import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.invoice.service.IInvoiceService;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class FeeCalculatorRenewal {
    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private IFeeScheduleRepo feeScheduleRepo;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;


    //Get new fee to determine access code
    public Integer getSpecificNewFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        List<FeeScheduleModel> listOfFee = feeScheduleRepo.getSpecificFeeSchedule(feeType, numberType, numberSubType);
        FeeScheduleModel model = listOfFee.get(0);
        // Get initial value for the fee
        log.info("Fee value {}", model.getInitialValue());
        Integer val = Integer.parseInt(model.getInitialValue());
        return val;
    }

    // Get renewal values
    public Integer getSpecificRenewalFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        List<FeeScheduleModel> listOfFee = feeScheduleRepo.getSpecificFeeSchedule(feeType, numberType, numberSubType);
        FeeScheduleModel model = listOfFee.get(0);
        // Get renewal value for the fee
        log.info("Fee value renewal {}", model.getRenewableType());
        Integer val = Integer.parseInt(model.getRenewableType());
        return val;
    }

    /**
     * Fee calaculator renewal
     *
     * @param request
     * @return
     * @throws Exception
     */
    public GenericResponse<FeeCalculationResponse> feeRenewalCalculator(FeeCalculationRequest request) throws Exception {
        GenericResponse<FeeCalculationResponse> response = new GenericResponse<>();
        FeeCalculationResponse fee = new FeeCalculationResponse();
        Integer accessFee = 0;
        Integer lineFee = 0;
        Integer adminFee = 0;
        Integer oldAccessFee = 0;
        Integer finalLineFee = 0;
        Integer finalAccessFee = 0;
        Integer finalAdminFee = 0;
        String numberSubType = (request.getNumberSubType() != null ? request.getNumberSubType().toUpperCase() : "");
        Integer adminCharge;
        Integer totalRenewalFee = 0;
        Integer finalAllocationFee = 0;
        int invoiceResCode = 0;

        try {
            switch (numberSubType) {
                case "NATIONAL":
                    // Admin fee (e.g 5 as per 5%) must be configured on portal as passed as request
                    // Line fee must be configured on portal as passed as request
                    // Access fee must be configured on portal as passed as request
                    // Processing fee must be configured on portal as passed as request
                    // Application fee must be configured on portal as passed as request

                    adminFee = getSpecificRenewalFeeSchedule("ADMIN FEE", "STANDARD", numberSubType);
                    lineFee = getSpecificRenewalFeeSchedule("LINE FEE", "STANDARD", numberSubType);
                    accessFee = getSpecificRenewalFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);
                    oldAccessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0 && oldAccessFee != 0) {
                        finalLineFee = lineFee * 1000000 ;
                        finalAccessFee = ((accessFee * oldAccessFee) / 100) / 10; // percentage of old access code fee
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalRenewalFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Renewal Fee is {}", totalRenewalFee);

                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected

                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        if (selectedNumberCount > 1) {
                            finalAllocationFee = totalRenewalFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalRenewalFee;
                            fee.setAllocationFee(totalRenewalFee);
                        }
                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "RENEWAL");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);
                        if (invoiceResCode >= 1) {
                            int res = selectedNumbersRepo.updateApplicationNotToBePickedRenewal(request.getApplicationId());

                            log.info(request.getNumberSubType() + " " + request.getApplicationId() +
                                    " is updated not to be sent for renewal code {} ", res);

                            // save the renewal fee in selected fee table
                            SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                            selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                            selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setInvoiceType("RENEWAL");
                            selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                            int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                            log.info("Allocation fee persist status for standard  "
                                    + request.getNumberSubType() + "number is : {} ", saveSelectedFee);

                        }

                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        log.info("Final Fees for standard number {} ", response);
                    }

                    return response;
                case "GEOGRAPHICAL":
                    // Admin fee (e.g 5 as per 5%) must be configured on portal as passed as request
                    // Line fee must be configured on portal as passed as request
                    // Access fee must be configured on portal as passed as request
                    // Processing fee must be configured on portal as passed as request
                    // Application fee must be configured on portal as passed as request

                    adminFee = getSpecificRenewalFeeSchedule("ADMIN FEE", "STANDARD", numberSubType);
                    lineFee = getSpecificRenewalFeeSchedule("LINE FEE", "STANDARD", numberSubType);
                    accessFee = getSpecificRenewalFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);
                    oldAccessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0 && oldAccessFee != 0) {
                        finalLineFee = lineFee * 10000;
                        finalAccessFee = (accessFee * oldAccessFee) / 100; // percentage of old access code fee
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalRenewalFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees for geographical number is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Renewal Fee is {}", totalRenewalFee);


                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected

                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        if (selectedNumberCount > 1) {
                            finalAllocationFee = totalRenewalFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalRenewalFee;
                            fee.setAllocationFee(totalRenewalFee);
                        }

                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "RENEWAL");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);
                        if (invoiceResCode >= 1) {
                            int res = selectedNumbersRepo.updateApplicationNotToBePickedRenewal(request.getApplicationId());

                            log.info(request.getNumberSubType() + " " + request.getApplicationId() +
                                    " is updated not to be sent for renewal code {} ", res);

                            // save the renewal fee in selected fee table
                            SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                            selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                            selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setInvoiceType("RENEWAL");
                            selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                            int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                            log.info("Allocation fee persist status for standard  "
                                    + request.getNumberSubType() + "number is : {} ", saveSelectedFee);

                        }

                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        log.info("Final Fees for standard number {} ", response);
                    }

                    return response;

                case "VANITY":
                case "TOLL-FREE":

                    // Admin fee (e.g 5 as per 5%) must be configured on portal as passed as request
                    // Line fee must be configured on portal as passed as request
                    // Access fee must be configured on portal as passed as request
                    // Processing fee must be configured on portal as passed as request
                    // Application fee must be configured on portal as passed as request

                    adminFee = getSpecificRenewalFeeSchedule("ADMIN FEE", "SPECIAL", numberSubType);
                    lineFee = getSpecificRenewalFeeSchedule("LINE FEE", "SPECIAL", numberSubType);
                    accessFee = getSpecificRenewalFeeSchedule("ACCESS CODE FEE", "SPECIAL", numberSubType);
                    oldAccessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "SPECIAL", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0) {

                        finalLineFee = lineFee * 10000;
                        finalAccessFee = ((accessFee * oldAccessFee) / 100) / 10; // percentage of old access code fee
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalRenewalFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees for geographical number is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Renewal Fee is {}", totalRenewalFee);

                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected

                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        if (selectedNumberCount > 1) {
                            finalAllocationFee = totalRenewalFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalRenewalFee;
                            fee.setAllocationFee(totalRenewalFee);
                        }

                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for special number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "RENEWAL");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

                        if (invoiceResCode >= 1) {
                            int res = selectedNumbersRepo.updateApplicationNotToBePickedRenewal(request.getApplicationId());

                            log.info(request.getNumberSubType() + " " + request.getApplicationId() +
                                    " is updated not to be sent for renewal code {} ", res);

                            // save the renewal fee in selected fee table
                            SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                            selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                            selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " renewal fee");
                            selectedFeeScheduleModel.setInvoiceType("RENEWAL");
                            selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                            int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                            log.info("Allocation fee persist status for standard  "
                                    + request.getNumberSubType() + "number is : {} ", saveSelectedFee);
                        }

                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        log.info("Final Fees for standard number {} ", response);
                    }
                    return response;

                case "ISPC":
                    // No renewal allocation fee/charges for ISPC number

                case "SHORT-CODE":
                    // Short code renewal fee calculation inside FeeCalculatorShortCodeRenewal
            }
        } catch (Exception exe) {
            log.info("Exception occurred during fee calculation {} ", exe.getMessage());
        }
        return null;
    }
}
