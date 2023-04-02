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

@Slf4j
@Service
public class FeeCalculationNew {
    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private IFeeScheduleRepo feeScheduleRepo;

    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    public Integer getSpecificNewFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        List<FeeScheduleModel> listOfFee = feeScheduleRepo.getSpecificFeeSchedule(feeType, numberType, numberSubType);
        FeeScheduleModel model = listOfFee.get(0);
        // Get initial value for the fee
        log.info("Fee value {}", model.getInitialValue());
        Integer val = Integer.parseInt(model.getInitialValue());
        return val;
    }

    /**
     * Fee calculator new allocation
     *
     * @param request
     * @return
     * @throws Exception
     */
    public GenericResponse<FeeCalculationResponse> feeCalculator(FeeCalculationRequest request) throws Exception {
        GenericResponse<FeeCalculationResponse> response = new GenericResponse<>();
        FeeCalculationResponse fee = new FeeCalculationResponse();
        Integer accessFee = 0;
        Integer lineFee = 0;
        Integer adminFee = 0;
        Integer finalAccessFee = 0;
        Integer finalLineFee = 0;
        Integer finalAdminFee = 0;
        String numberSubType = (request.getNumberSubType() != null ? request.getNumberSubType().toUpperCase() : "");
        Integer adminCharge;
        Integer totalAllocationFee = 0;
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

                    adminFee = getSpecificNewFeeSchedule("ADMIN FEE", "STANDARD", numberSubType);
                    lineFee = getSpecificNewFeeSchedule("LINE FEE", "STANDARD", numberSubType);
                    accessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0) {
                        finalLineFee = lineFee * 1000000;
                        finalAccessFee = accessFee / 10;
//                        finalAccessFee = accessFee;
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalAllocationFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees for geographical number is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Allocation Fee is {}", totalAllocationFee);

                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected
                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        if (selectedNumberCount > 1) {
                            log.info("selected number count is {} ", selectedNumberCount);

                            finalAllocationFee = totalAllocationFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalAllocationFee;
                            fee.setAllocationFee(totalAllocationFee);
                        }

                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "NEW");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

                        // save the fee in selected fee table
                        SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                        selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                        selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setInvoiceType("NEW");
                        selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                        int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                        log.info("Allocation fee persist status for standard  "
                                + request.getNumberSubType() + "number is : {} ", saveSelectedFee);


                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);
                    }

                    return response;

                case "GEOGRAPHICAL":
                    // Admin fee (e.g 5 as per 5%) must be configured on portal as passed as request
                    // Line fee must be configured on portal as passed as request
                    // Access fee must be configured on portal as passed as request
                    // Processing fee must be configured on portal as passed as request
                    // Application fee must be configured on portal as passed as request

                    adminFee = getSpecificNewFeeSchedule("ADMIN FEE", "STANDARD", numberSubType);
                    lineFee = getSpecificNewFeeSchedule("LINE FEE", "STANDARD", numberSubType);
                    accessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "STANDARD", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0) {
                        finalLineFee = lineFee * 10000;
                        finalAccessFee = accessFee;
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalAllocationFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees for geographical number is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Allocation Fee is {}", totalAllocationFee);

                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected

                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        if (selectedNumberCount > 1) {
                            log.info("selected number count is {} ", selectedNumberCount);

                            finalAllocationFee = totalAllocationFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalAllocationFee;
                            fee.setAllocationFee(totalAllocationFee);
                        }

                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "NEW");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

                        // save the fee in selected fee table
                        SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                        selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                        selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setInvoiceType("NEW");
                        selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                        int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                        log.info("Allocation fee persist status for standard  "
                                + request.getNumberSubType() + "number is : {} ", saveSelectedFee);


                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        response.setOutputPayload(fee);
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

                    adminFee = getSpecificNewFeeSchedule("ADMIN FEE", "SPECIAL", numberSubType);
                    lineFee = getSpecificNewFeeSchedule("LINE FEE", "SPECIAL", numberSubType);
                    accessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", "SPECIAL", numberSubType);

                    if (adminFee != 0 && lineFee != 0 && accessFee != 0) {
                        finalLineFee = lineFee * 10000;
                        finalAccessFee = accessFee;
//                        finalAccessFee = accessFee;
                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                        totalAllocationFee = finalAccessFee + finalLineFee + finalAdminFee;

                        log.info("Access, Line and Admin Fees for geographical number is {} {} {}", finalAccessFee, finalLineFee, finalAdminFee);
                        log.info("Total Allocation Fee is {}", totalAllocationFee);

                        // Multiply by total number selected if number selected is greater than one
                        // else calculate for just number selected

                        int selectedNumberCount = selectedNumbersRepo.countPerApplicationId(request.getApplicationId());
                        log.info("selected number count is {} ", selectedNumberCount);

                        if (selectedNumberCount > 1) {
                            finalAllocationFee = totalAllocationFee * selectedNumberCount;
                            fee.setAllocationFee(finalAllocationFee);
                        } else {
                            finalAllocationFee = totalAllocationFee;
                            fee.setAllocationFee(totalAllocationFee);
                        }

                        response.setResponseCode("000");
                        response.setResponseMessage("SUCCESS");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for vanity number {} ", response);

                        // Persist allocation fee to invoice table
                        invoiceResCode = invoiceService.persistInvoice(request.getApplicationId(),
                                request.getNumberSubType(), finalAllocationFee, "UNPAID", true, "", "NEW");
                        log.info("AutoFeeResponse from invoice persist {}", invoiceResCode);

                        // save the fee in selected fee table
                        SelectedFeeScheduleModel selectedFeeScheduleModel = new SelectedFeeScheduleModel();
                        selectedFeeScheduleModel.setFeeName(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setFeeAmount(String.valueOf(finalAllocationFee));
                        selectedFeeScheduleModel.setFeeDescription(request.getNumberSubType() + " allocation fee");
                        selectedFeeScheduleModel.setInvoiceType("NEW");
                        selectedFeeScheduleModel.setApplicationId(request.getApplicationId());
                        int saveSelectedFee = selectedFeeScheduleRepository.save(selectedFeeScheduleModel);
                        log.info("Allocation fee persist status for standard  "
                                + request.getNumberSubType() + "number is : {} ", saveSelectedFee);

                    } else {
                        response.setResponseCode("000");
                        response.setResponseMessage("Necessary fee not configured");
                        response.setOutputPayload(fee);
                        log.info("Final Fees for standard number {} ", response);
                    }
                    return response;

                case "ISPC":
                    // No Allocation fee/charges for ISPC number

                case "SHORT-CODE":
                    // No auto generation of allocation fee and invoice persist for short code, it manually
            }
        } catch (Exception exe) {
            log.info("Exception occurred during fee calculation {} ", exe.getMessage());
        }
        return null;
    }
}
