package com.molcom.nms.jobQueue.services;

import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableUpdateRepository;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.number.application.repository.ISPCNumberRepository;
import com.molcom.nms.number.application.repository.ShortCodeRepository;
import com.molcom.nms.number.application.repository.SpecialNumberRepository;
import com.molcom.nms.number.application.repository.StandardNumberRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import com.molcom.nms.numberCreation.standard.dto.CreateStandardNoModel;
import com.molcom.nms.numberCreation.standard.repository.CreateStandardNoRepository;
import com.molcom.nms.numberReport.ispc.repository.ReportIspcRepository;
import com.molcom.nms.numberReport.shortcode.dto.ReportShortCodeNoModel;
import com.molcom.nms.numberReport.shortcode.repository.ReportShortCodeRepository;
import com.molcom.nms.numberReport.special.dto.ReportSpecialNoModel;
import com.molcom.nms.numberReport.special.repository.ReportSpecialRepository;
import com.molcom.nms.numberReport.standard.dto.ReportStandardNoModel;
import com.molcom.nms.numberReport.standard.repository.ReportStandardNoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReportingProcessor {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;

    @Autowired
    private GenericTableUpdateRepository genericTableUpdateRepository;

    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;

    @Autowired
    private ReportStandardNoRepository reportStandardNoRepository;

    @Autowired
    private ReportSpecialRepository reportSpecialRepository;

    @Autowired
    private ReportIspcRepository reportIspcRepository;

    @Autowired
    private ReportShortCodeRepository reportShortCodeRepository;

    @Autowired
    private CreateStandardNoRepository createStandardNoRepository;

    @Autowired
    private StandardNumberRepository standardNumberRepository;
    @Autowired
    private SpecialNumberRepository specialNumberRepository;
    @Autowired
    private ISPCNumberRepository ispcNumberRepository;
    @Autowired
    private ShortCodeRepository shortCodeRepository;

    /**
     * Service processor for reporting
     *
     * @throws Exception
     */
    public boolean sendNumberForReporting() throws Exception {
        // Get list of invoice qualified for reporting
        // When invoice is true, send for reporting is TRUE
        List<InvoiceModel> invoiceModelList = invoiceRepository.getAllInvoiceNeededForReporting();
        log.info("List of invoice to be sent for reporting {} ", invoiceModelList);

        if (invoiceModelList != null && invoiceModelList.size() >= 1) {
            // for each get the selected numbers for the application and calculate quantity and number
            // then persist in number report based on the number type
            // sendForReporting flag is then taken to false
            invoiceModelList.forEach(inv -> {
                String appId = inv.getApplicationId();
                String numberType = inv.getNumberType();
                String numberSubType = inv.getNumberSubType();
                if (appId != null) {
                    log.info(appId);
                    try {
                        List<SelectedNumberModel> selectedNumberModels = selectedNumbersRepo.getSelectedNumber(appId);
                        log.info("List of selected numbers {} ", selectedNumberModels);
                        String accessCode = genericTableCellGetRepository.getAccessCode(appId);
                        boolean parseZero = false;

                        parseZero = accessCode != null && accessCode.startsWith("0");

                        List<BigDecimal> allNumbers = new ArrayList<>();

                        if (selectedNumberModels != null) {
                            selectedNumberModels.forEach(select -> {
                                boolean reportStat = false;
                                // get the selected number value
                                // split and convert to integer
                                // add to array list of integer
                                String number = select.getSelectedNumberValue();

                                // If National, Vanity or Toll-free
                                if ((Objects.equals(numberSubType.toUpperCase(), "NATIONAL")) ||
                                        (Objects.equals(numberSubType.toUpperCase(), "VANITY")) ||
                                        (Objects.equals(numberSubType.toUpperCase(), "TOLL-FREE"))

                                ) {
                                    log.info("List of selected numbers national. vanity and tol free {} ", number);
                                    String[] arrOfStr = number.split("-", 11);
                                    String from = arrOfStr[0];
                                    String to = arrOfStr[1];
                                    log.info("From {} and to {} ", from, to);

                                    if (from != null && to != null) {
                                        allNumbers.add(BigDecimal.valueOf(Long.parseLong(from)));
                                        allNumbers.add(BigDecimal.valueOf(Long.parseLong(to)));
                                    }
                                    log.info("All Integer numbers {} ", allNumbers);
                                }

                                // If Short code or Ispc
                                if ((Objects.equals(numberType.toUpperCase(), "SHORT-CODE")) ||
                                        (Objects.equals(numberType.toUpperCase(), "ISPC"))
                                ) {
                                    // if short code or ispc or short code
                                    String num = select.getSelectedNumberValue();
                                    allNumbers.add(BigDecimal.valueOf(Long.parseLong(num)));
                                    log.info("All Integer numbers {} ", allNumbers);
                                }
                            });

                            // outside each block
                            // National, Vanity and Toll-free
                            if ((Objects.equals(numberSubType.toUpperCase(), "NATIONAL")) ||
                                    (Objects.equals(numberSubType.toUpperCase(), "VANITY")) ||
                                    (Objects.equals(numberSubType.toUpperCase(), "TOLL-FREE"))) {

                                // sort to find the minimum and maximum number
                                BigDecimal minAllocatedNumber = Collections.min(allNumbers);
                                BigDecimal maxAllocatedNumber = Collections.max(allNumbers);

                                log.info("All Integer minimum number {} ", minAllocatedNumber);
                                log.info("All Integer maximum number {} ", maxAllocatedNumber);

                                if (parseZero) {
                                    // Check if first character has start with Zero because it will be
                                    // truncated by big decimal conversions
                                    // min
                                    String aa = String.valueOf(minAllocatedNumber);
                                    String bb = "0" + aa;

                                    // max
                                    String cc = String.valueOf(maxAllocatedNumber);
                                    String dd = "0" + cc;

                                    boolean reportStat = sendForReporting(inv, selectedNumberModels.get(0), bb,
                                            dd, "");
                                    log.info("Report status {} ", reportStat);
                                } else {
                                    boolean reportStat = sendForReporting(inv, selectedNumberModels.get(0), String.valueOf(minAllocatedNumber),
                                            String.valueOf(maxAllocatedNumber), "");
                                    log.info("Report status {} ", reportStat);
                                }
                            }
                            // ISPC and Short code number
                            if ((Objects.equals(numberType.toUpperCase(), "SHORT-CODE")) ||
                                    (Objects.equals(numberType.toUpperCase(), "ISPC"))) {
                                log.info("Entered ISPC/Short codes Block");
                                List<SelectedNumberModel> selectedNumberModelsss = selectedNumbersRepo.getSelectedNumber(appId);
                                if (selectedNumberModelsss != null) {
                                    selectedNumberModelsss.forEach(no -> {
                                        boolean reportStat = sendForReporting(inv, no, "",
                                                "", "");
                                        log.info("Geographical number reporting {} ", reportStat);
                                    });
                                }
                            }


                            // Geographical number
                            if (Objects.equals(numberSubType.toUpperCase(), "GEOGRAPHICAL")) {
                                log.info("Entered Geographical Block");
                                List<SelectedNumberModel> selectedNumberModelsss = selectedNumbersRepo.getSelectedNumber(appId);
                                if (selectedNumberModelsss != null) {
                                    boolean reportStat = sendForReporting(inv, selectedNumberModels.get(0), "",
                                            "", "");
                                    log.info("Geographical number reporting {} ", reportStat);
                                }
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            });
        }
        return false;
    }


    // Method to determine application quantity
    public boolean sendForReporting(InvoiceModel invoiceModel,
                                    SelectedNumberModel selectedNumberModel,
                                    String minBlock,
                                    String maxBlock,
                                    String noVal) {
        int isReportUpdated = 0;
        int isFlagReepdated = 0;
        String numSub = (invoiceModel != null ? invoiceModel.getNumberSubType().toUpperCase() : "");
        String appId = (invoiceModel != null ? invoiceModel.getApplicationId() : "");
        String invoiceType = (invoiceModel != null ? invoiceModel.getApplicationId() : "");
        String quantity = genericTableCellGetRepository.getApplicationQuantity(appId);
        String accessCode = genericTableCellGetRepository.getApplicationQuantity(appId);
        String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(appId);
        Timestamp now = CurrentTimeStamp.getCurrentTimeStamp();
        Timestamp next = CurrentTimeStamp.getNextYearTimeStamp(now, 365);
        String todayDate = now.toString();
        String nextYear = next.toString();


        try {
            switch (numSub) {
                case "NATIONAL":
                case "GEOGRAPHICAL":
                    ReportStandardNoModel reportStandardNoModel = new ReportStandardNoModel();

                    String coverageArea = genericTableCellGetRepository.getCoverageArea(appId);
                    String areaCode = genericTableCellGetRepository.getAreaCode(appId);
                    String accessCodeVal = genericTableCellGetRepository.getAccessCode(appId);

                    reportStandardNoModel.setApplicationId(appId);
                    reportStandardNoModel.setNumberType(invoiceModel.getNumberType());
                    reportStandardNoModel.setNumberSubType(invoiceModel.getNumberSubType());

                    if (Objects.equals(numSub, "NATIONAL")) {
                        reportStandardNoModel.setMinimumNumber(minBlock);
                        reportStandardNoModel.setMaximumNumber(maxBlock);
                    }

                    if (Objects.equals(numSub, "GEOGRAPHICAL")) {
                        // Get details of number using access code and pass as minimum and maximum number
                        List<CreateStandardNoModel> goeNumberList = createStandardNoRepository.getGeoNumberByAccessCode(accessCode);
                        if (goeNumberList != null && goeNumberList.size() >= 1) {
                            CreateStandardNoModel geoNumber = goeNumberList.get(0);
                            reportStandardNoModel.setMinimumNumber(geoNumber.getMinimumNumber());
                            reportStandardNoModel.setMaximumNumber(geoNumber.getMaximumNumber());
                        }
                    }

                    reportStandardNoModel.setCreatedBy("SYSTEM");
                    reportStandardNoModel.setAllotee("");
                    reportStandardNoModel.setPurpose(selectedNumberModel.getPurpose());
                    reportStandardNoModel.setBearerMedium(selectedNumberModel.getBearerMedium());
                    reportStandardNoModel.setCompanyAllocatedTo(companyNameVal);
                    // MDA doesn't require payment thus status will be allocated
                    if (Objects.equals(invoiceType.toUpperCase(), "MDA")) {
                        reportStandardNoModel.setAllocationStatus("ALLOCATED");
                        reportStandardNoModel.setAllocationValidityFrom(todayDate);
                        reportStandardNoModel.setAllocationValidityTo(nextYear);
                        reportStandardNoModel.setDateAllocated(todayDate);
                    } else {
                        reportStandardNoModel.setAllocationStatus("ONGOING ALLOCATION");
                        reportStandardNoModel.setAllocationValidityFrom("");
                        reportStandardNoModel.setAllocationValidityTo("");
                        reportStandardNoModel.setDateAllocated("");
                    }
                    reportStandardNoModel.setCoverageArea(coverageArea);
                    reportStandardNoModel.setAreaCode(areaCode);
                    reportStandardNoModel.setAccessCode(accessCode);
                    reportStandardNoModel.setQuantity(quantity);
                    reportStandardNoModel.setPurpose(selectedNumberModel.getPurpose());
                    reportStandardNoModel.setBearerMedium(selectedNumberModel.getBearerMedium());

                    log.info("!!!!! Standard Number reporting payload !!!!!!!!!!!!!! {}", reportStandardNoModel);
                    isReportUpdated = reportStandardNoRepository.internalReport(reportStandardNoModel);
                    log.info("Number reporting status for application if {} ", selectedNumberModel.getApplicationId());

                    if (isReportUpdated >= 1) {
                        // update sendForReportingFlag to false
                        isFlagReepdated = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "SendForNumberReporting",
                                "FALSE",
                                "ApplicationId",
                                appId);
                        log.info("Is Flag Re-updated status {}", isFlagReepdated);
                    }
                    break;

                case "ISPC":
                    // No invoice for ISPC Number

                case "VANITY":
                case "TOLL-FREE":

                    ReportSpecialNoModel reportSpecialNoModel = new ReportSpecialNoModel();
                    String access = genericTableCellGetRepository.getAccessCode(appId);

                    reportSpecialNoModel.setApplicationId(appId);
                    reportSpecialNoModel.setNumberType(invoiceModel.getNumberType());
                    reportSpecialNoModel.setNumberSubType(invoiceModel.getNumberSubType());
                    reportSpecialNoModel.setMinimumNumber(minBlock);
                    reportSpecialNoModel.setMaximumNumber(maxBlock);
                    reportSpecialNoModel.setCreatedBy("SYSTEM");
                    reportSpecialNoModel.setAllotee("");
                    reportSpecialNoModel.setPurpose(selectedNumberModel.getPurpose());
                    reportSpecialNoModel.setBearerMedium(selectedNumberModel.getBearerMedium());
                    reportSpecialNoModel.setCompanyAllocatedTo(companyNameVal);
                    reportSpecialNoModel.setAccessCode(access);
                    reportSpecialNoModel.setQuantity(quantity);
                    reportSpecialNoModel.setPurpose(selectedNumberModel.getPurpose());
                    reportSpecialNoModel.setBearerMedium(selectedNumberModel.getBearerMedium());
                    // MDA doesn't require payment thus status will be allocated
                    if (Objects.equals(invoiceType.toUpperCase(), "MDA")) {
                        reportSpecialNoModel.setAllocationStatus("ALLOCATED");
                        reportSpecialNoModel.setAllocationValidityFrom(todayDate);
                        reportSpecialNoModel.setAllocationValidityTo(nextYear);
                        reportSpecialNoModel.setDateAllocated(todayDate);
                    } else {
                        reportSpecialNoModel.setAllocationStatus("ONGOING ALLOCATION");
                        reportSpecialNoModel.setAllocationValidityFrom("");
                        reportSpecialNoModel.setAllocationValidityTo("");
                        reportSpecialNoModel.setDateAllocated("");
                    }

                    isReportUpdated = reportSpecialRepository.internalReport(reportSpecialNoModel);
                    log.info("Number reporting status for application if {} ", selectedNumberModel.getApplicationId());

                    if (isReportUpdated >= 1) {
                        // update sendForReportingFlag to false
                        isFlagReepdated = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "SendForNumberReporting",
                                "FALSE",
                                "ApplicationId",
                                appId);
                        log.info("Is Flag Re-updated status {}", isFlagReepdated);
                    }

                    break;

                case "SHORT-CODE":

                    String shortCodeService = genericTableCellGetRepository.getShortCodeService(appId);
                    String shortCodCategory = genericTableCellGetRepository.getShortCodeCat(appId);

                    ReportShortCodeNoModel reportShortCodeNoModel = new ReportShortCodeNoModel();

                    reportShortCodeNoModel.setApplicationId(appId);
                    reportShortCodeNoModel.setNumberType("SHORT-CODE");
                    reportShortCodeNoModel.setShortCodeCategory(shortCodCategory);
                    reportShortCodeNoModel.setShortCodeService(shortCodeService);
                    reportShortCodeNoModel.setShortCodeNumber(selectedNumberModel.getSelectedNumberValue());

                    reportShortCodeNoModel.setCreatedBy("SYSTEM");
                    reportShortCodeNoModel.setAllotee("");
                    reportShortCodeNoModel.setCompanyAllocatedTo(companyNameVal);
                    reportShortCodeNoModel.setQuantity(quantity);
                    reportShortCodeNoModel.setPurpose(selectedNumberModel.getPurpose());
                    reportShortCodeNoModel.setBearerMedium(selectedNumberModel.getBearerMedium());
                    // MDA doesn't require payment thus status will be allocated
                    if (Objects.equals(invoiceType.toUpperCase(), "MDA")) {
                        reportShortCodeNoModel.setAllocationStatus("ALLOCATED");
                        reportShortCodeNoModel.setAllocationValidityFrom(todayDate);
                        reportShortCodeNoModel.setAllocationValidityTo(nextYear);
                        reportShortCodeNoModel.setDateAllocated(todayDate);
                    } else {
                        reportShortCodeNoModel.setAllocationStatus("ONGOING ALLOCATION");
                        reportShortCodeNoModel.setAllocationValidityFrom("");
                        reportShortCodeNoModel.setAllocationValidityTo("");
                        reportShortCodeNoModel.setDateAllocated("");
                    }

                    isReportUpdated = reportShortCodeRepository.internalReport(reportShortCodeNoModel);
                    log.info("Number reporting status for application if {} ", selectedNumberModel.getApplicationId());

                    if (isReportUpdated >= 1) {
                        // update sendForReportingFlag to false
                        isFlagReepdated = genericTableUpdateRepository.updateTableColumn("Invoice",
                                "SendForNumberReporting",
                                "FALSE",
                                "ApplicationId",
                                appId);
                        log.info("Is Flag Re-updated status {}", isFlagReepdated);
                    }

                    break;


                default:
                    break;
            }

        } catch (Exception exe) {
            log.info("Exception occurred during reporting {} ", exe.getMessage());
        }

        return isReportUpdated >= 1;
    }
}
