package com.molcom.nms.eservicesintegrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.molcom.nms.GenericDatabaseUpdates.repository.GenericTableCellGetRepository;
import com.molcom.nms.eservicesintegrations.dto.invoice.*;
import com.molcom.nms.fee.schedule.dto.FeeScheduleModel;
import com.molcom.nms.fee.schedule.repository.FeeScheduleRepo;
import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.fee.selectedFeeSchedule.repository.ISelectedFeeScheduleRepository;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.DateFormatter;
import com.molcom.nms.general.utils.GetUniqueId;
import com.molcom.nms.general.utils.RestUtil;
import com.molcom.nms.invoice.dto.InvoiceObject;
import com.molcom.nms.invoice.repository.InvoiceRepository;
import com.molcom.nms.number.renewal.dto.NumRenewalModel;
import com.molcom.nms.number.renewal.repository.INumRenewalRepository;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import com.molcom.nms.number.selectedNumber.repository.ISelectedNumbersRepo;
import com.molcom.nms.organisationprofile.dto.OrganisationProfileModel;
import com.molcom.nms.organisationprofile.repository.IOrganisationProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EInvoiceService {

    @Autowired
    private final RestUtil restUtil;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private GenericTableCellGetRepository genericTableCellGetRepository;
    @Autowired
    private FeeScheduleRepo feeScheduleRepo;
    @Autowired
    private ISelectedFeeScheduleRepository selectedFeeScheduleRepository;
    @Autowired
    private IOrganisationProfileRepo organisationProfileRepo;
    @Autowired
    private ISelectedNumbersRepo selectedNumbersRepo;
    @Autowired
    private INumRenewalRepository numRenewalRepository;

    @Value("${InvoiceUrl}")
    private String invoiceUrl;
    @Value("${InvoiceApiKey}")
    private String invoiceApiKey;
    @Value("${RevenueIdFixed&MobileNew}")
    private String revenueIdFixedMobileNew;
    @Value("${RevenueIdFixed&MobileRenewal}")
    private String revenueIdFixedMobileRenewal;
    @Value("${RevenueIdShortCodeNew}")
    private String revenueIdShortCodeNew;
    @Value("${RevenueIdShortCodeRenewal}")
    private String revenueIdShortCodeRenewal;
    @Value("${CurrencyId}")
    private String currencyId;
    @Value("${RevenueIdFixed&MobileNewInvoiceStructure}")
    private String revenueIdFixedMobileNewInvoiceStructure;
    @Value("${RevenueIdFixed&MobileRenewalInvoiceStructure}")
    private String revenueIdFixedMobileRenewalInvoiceStructure;
    @Value("${RevenueIdShortCodeNewInvoiceStructure}")
    private String revenueIdShortCodeNewInvoiceStructure;
    @Value("${RevenueIdShortCodeRenewalInvoiceStructure}")
    private String revenueIdShortCodeRenewalInvoiceStructure;
    @Value("${InvoiceNotificationUrl}")
    private String invoiceNotificationUrl;
    @Value("${DeleteInvoiceNotificationUrl}")
    private String deleteInvoiceNotificationUrl;
    @Value("${InvoiceTypeIDNew}")
    private String invoiceTypeIDNew;
    @Value("${InvoiceTypeIDRenewal}")
    private String invoiceTypeIDRenewal;
    @Value("${LicenceTypeID}")
    private String licenceTypeID;


    public EInvoiceService(RestUtil restUtil) {
        this.restUtil = restUtil;
    }

    public static void main(String[] args) {
        System.out.println(Year.now().minusYears(1));
    }

    /**
     * Method to get fee schedule for a number application
     *
     * @param feeType
     * @param numberType
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    public Integer getSpecificNewFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        List<FeeScheduleModel> listOfFee = feeScheduleRepo.getSpecificFeeSchedule(feeType, numberType, numberSubType);
        FeeScheduleModel model = listOfFee.get(0);
        log.info("Fee value {}", model.getInitialValue());
        Integer val = Integer.parseInt(model.getInitialValue());
        return val;
    }

    /**
     * Method to get fee schedule for renewal number application
     *
     * @param feeType
     * @param numberType
     * @param numberSubType
     * @return
     * @throws SQLException
     */
    public Integer getSpecificRenewalFeeSchedule(String feeType, String numberType, String numberSubType) throws SQLException {
        List<FeeScheduleModel> listOfFee = feeScheduleRepo.getSpecificFeeSchedule(feeType, numberType, numberSubType);
        FeeScheduleModel model = listOfFee.get(0);
        log.info("Fee value renewal {}", model.getRenewableType());
        Integer val = Integer.parseInt(model.getRenewableType());
        return val;
    }

    /**
     * Method to send invoice to eservies
     * It build invoice payload and send to eservices
     *
     * @param invoiceObject
     * @param type
     * @return
     * @throws Exception
     */
    public Boolean sendInvoiceToEservices(InvoiceObject invoiceObject, String type) throws Exception {
        try {
            log.info("Invoice Object coming in {}", invoiceObject);
            String invoiceId = "";
            List<InvoiceItems> invoiceItems = new ArrayList<>();
            List<String> licenseTypesList = new ArrayList<>();
            String serviceId;
            String email = "";
            String title = "";
            String description = "";
            String numberSubType = "";
            String companyName = "";
            String applicationId = "";
            String nccId = "";
            List<Integer> listOfFees = new ArrayList<>();
            String invoiceStructure = "";
            String invoiceType = "";
            Integer validityPeriod = 0;
            Date serviceStartDate = null;
            Date serviceEndDate = null;
            String description2 = "";
            Year thisYear = Year.now();
            Year previousYear = Year.now().minusYears(1);
            Integer noSelectedCount = 0;
            Integer nationalNumberingBlock = 0;

            CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
            CreateInvoiceResponse response;

            // Note: Invoice types available
            // FIXED_AND_MOBILE_APPLICATION_NEW
            // FIXED_AND_MOBILE_APPLICATION_RENEWAL
            // SHORT_CODE_NEW
            // SHORT_CODE_RENEWAL
            // :::::: Note type can either be NEW or RENEWAL

            if (invoiceObject != null && invoiceObject.getInvoiceModel() != null) {
                email = (invoiceObject.getInvoiceModel().getInitiatorEmail() != null ?
                        invoiceObject.getInvoiceModel().getInitiatorEmail() : "");
                numberSubType = (invoiceObject.getInvoiceModel().getNumberSubType() != null ?
                        invoiceObject.getInvoiceModel().getNumberSubType().toUpperCase() : "");
                applicationId = (invoiceObject.getInvoiceModel().getApplicationId() != null ?
                        invoiceObject.getInvoiceModel().getApplicationId() : "");
                invoiceId = (invoiceObject.getInvoiceModel().getInvoiceId() != null ?
                        invoiceObject.getInvoiceModel().getInvoiceId().toUpperCase() : "");
                noSelectedCount = (invoiceObject.getInvoiceModel().getInvoiceId() != null ?
                        invoiceObject.getSelectedNumbers().size() : 0);
                nationalNumberingBlock = noSelectedCount * 1000000;
                String numType = genericTableCellGetRepository.getNumberType(applicationId);
                log.info("Number Type {} ", numType);

                // If number type is standard or special
                if ((Objects.equals(numType, "STANDARD")) ||
                        (Objects.equals(numType, "SPECIAL"))) {
                    // Build Invoice item per selected number
                    invoiceObject.getSelectedNumbers().forEach(num -> {
                        Integer accessCode = 0;
                        String numberingRangeFrom = "";
                        String numberingRangeTo = "";
                        Integer adminFee = 0;
                        Integer unit = 0;
                        Integer lineFee = 0;
                        Integer accessFee = 0;
                        Integer total = 0;
                        Integer finalAdminFee = 0;
                        Integer finalLineFee = 0;
                        Integer finalAccessFee = 0;
                        Integer oldAccessFee = 0;

                        Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                        Timestamp futureDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, 365);

                        String access = genericTableCellGetRepository.getAccessCode(num.getApplicationId());
                        String numTypeVariable = genericTableCellGetRepository.getNumberType(num.getApplicationId());
                        String numSubTypeVariable = genericTableCellGetRepository.getNumberSubType(num.getApplicationId());
                        String coverageArea = genericTableCellGetRepository.getCoverageArea(num.getApplicationId());
                        String areaCode = genericTableCellGetRepository.getAreaCode(num.getApplicationId());
                        assert num.getDateAllocated() != null;
                        // On persist to renewal table, stamp the billing start and end and retrieve it here
                        String allocationDate = DateFormatter.finalFormattedDate(todayDate.toString());
                        String billingStart = DateFormatter.finalFormattedDate(todayDate.toString());
                        String billingEnd = DateFormatter.finalFormattedDate(futureDate.toString());
                        log.info("Allocated Date {}", allocationDate);
                        log.info("Billing Start {}", billingStart);
                        log.info("Billing End {}", billingEnd);


                        if (access != null) {
                            accessCode = NumberUtils.toInt(access);
                        } else {
                            accessCode = null;
                        }

                        String str = num.getSelectedNumberValue();
                        String[] arrOfStr = str.split("-", 11);
                        String from = arrOfStr[0];
                        String to = arrOfStr[1];
                        log.info("From {} and to {} ", from, to);

                        if (from != null && to != null) {
                            numberingRangeFrom = from;
                            numberingRangeTo = to;
                        }

                        // Get Fees value
                        if (numTypeVariable != null && numSubTypeVariable != null) {
                            try {
                                log.info("Type {}", type);
                                // New allocation: National, Vanity and Toll-Free

                                if (((Objects.equals(type, "NEW")) && (Objects.equals(numSubTypeVariable.toUpperCase(), "NATIONAL"))) ||
                                        ((Objects.equals(type, "NEW")) && (Objects.equals(numSubTypeVariable.toUpperCase(), "VANITY"))) ||
                                        ((Objects.equals(type, "NEW")) && (Objects.equals(numSubTypeVariable.toUpperCase(), "TOLL-FREE")))
                                ) {
                                    adminFee = getSpecificNewFeeSchedule("ADMIN FEE", numTypeVariable, numSubTypeVariable);
                                    lineFee = getSpecificNewFeeSchedule("LINE FEE", numTypeVariable, numSubTypeVariable);
                                    accessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", numTypeVariable, numSubTypeVariable);
                                    log.info("adminFee is {} , line fee is {} and access fee is {}  ", adminFee, lineFee, accessFee);

                                    if (Objects.equals(numSubTypeVariable.toUpperCase(), "NATIONAL")) {
                                        finalLineFee = lineFee * 1000000;
                                        unit = 1000000;
                                        finalAccessFee = accessFee / 10;
                                    } else {
                                        finalLineFee = lineFee * 10000;
                                        unit = 10000;
                                        finalAccessFee = accessFee;
                                    }

                                    finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                                    total = finalAccessFee + finalLineFee + finalAdminFee;
                                    log.info("New allocation fee is {} ", total);

                                    // Access code row
                                    InvoiceItems item = new InvoiceItems();
                                    item.setObjectType("invoice_item");
                                    item.setInvoiceItemId(GetUniqueId.getId());
                                    item.setAmount(finalAccessFee);
                                    JSONObject jsonBreakdown = new JSONObject();
                                    try {
                                        jsonBreakdown.put("Unit", 1);
                                        jsonBreakdown.put("Description", "Access Code Charge for National Destination Code");
                                        jsonBreakdown.put("Numbering Area", "National");
                                        jsonBreakdown.put("Unit Charges", finalAccessFee);
                                        jsonBreakdown.put("Amount", finalAccessFee);
                                        jsonBreakdown.toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    item.setItemData(jsonBreakdown.toString());
                                    invoiceItems.add(item);
                                    listOfFees.add(finalAccessFee);

                                    // Access code row
                                    InvoiceItems lineItem = new InvoiceItems();
                                    lineItem.setObjectType("invoice_item");
                                    lineItem.setInvoiceItemId(GetUniqueId.getId());
                                    lineItem.setAmount(finalLineFee);
                                    JSONObject jsonBreakdownLine = new JSONObject();
                                    try {
                                        jsonBreakdownLine.put("Unit", unit);
                                        jsonBreakdownLine.put("Description", "Lines Charge @ N50 per line");
                                        jsonBreakdownLine.put("Numbering Area", "National");
                                        jsonBreakdownLine.put("Unit Charges", 50);
                                        jsonBreakdownLine.put("Amount", finalLineFee);
                                        jsonBreakdownLine.toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    lineItem.setItemData(jsonBreakdownLine.toString());
                                    invoiceItems.add(lineItem);
                                    listOfFees.add(finalLineFee);

                                }


                                // New allocation: Geographical
                                if ((Objects.equals(type, "NEW")) && (Objects.equals(numSubTypeVariable.toUpperCase(), "GEOGRAPHICAL"))) {
                                    adminFee = getSpecificNewFeeSchedule("ADMIN FEE", numTypeVariable, numSubTypeVariable);
                                    lineFee = getSpecificNewFeeSchedule("LINE FEE", numTypeVariable, numSubTypeVariable);
                                    accessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", numTypeVariable, numSubTypeVariable);
                                    log.info("adminFee is {} , line fee is {} and access fee is {}  ", adminFee, lineFee, accessFee);

                                    finalLineFee = lineFee * 10000;
                                    finalAccessFee = accessFee;
                                    finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                                    total = finalAccessFee + finalLineFee + finalAdminFee;
                                    log.info("New allocation fee is {} ", total);

                                    // Access code row
                                    InvoiceItems item = new InvoiceItems();
                                    item.setObjectType("invoice_item");
                                    item.setInvoiceItemId(GetUniqueId.getId());
                                    item.setAmount(finalAccessFee);
                                    JSONObject jsonBreakdown = new JSONObject();
                                    try {
                                        jsonBreakdown.put("Unit", 1);
                                        jsonBreakdown.put("Description", "Access Code Charge");
                                        jsonBreakdown.put("Numbering Area", coverageArea);
                                        jsonBreakdown.put("Unit Charges", finalAccessFee);
                                        jsonBreakdown.put("Amount", finalAccessFee);
                                        jsonBreakdown.toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    item.setItemData(jsonBreakdown.toString());
                                    invoiceItems.add(item);
                                    listOfFees.add(finalAccessFee);

                                    // Access code row
                                    InvoiceItems lineItem = new InvoiceItems();
                                    lineItem.setObjectType("invoice_item");
                                    lineItem.setInvoiceItemId(GetUniqueId.getId());
                                    lineItem.setAmount(finalLineFee);
                                    JSONObject jsonBreakdownLine = new JSONObject();
                                    try {
                                        jsonBreakdownLine.put("Unit", 10000);
                                        jsonBreakdownLine.put("Description", "Subscriber Lines");
                                        jsonBreakdownLine.put("Numbering Area", coverageArea);
                                        jsonBreakdownLine.put("Unit Charges", 50);
                                        jsonBreakdownLine.put("Amount", finalLineFee);
                                        jsonBreakdownLine.toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    lineItem.setItemData(jsonBreakdownLine.toString());
                                    invoiceItems.add(lineItem);
                                    listOfFees.add(finalLineFee);
                                }

                                // Renewal allocation
                                if (Objects.equals(type, "RENEWAL")) {
                                    adminFee = getSpecificRenewalFeeSchedule("ADMIN FEE", numTypeVariable, numSubTypeVariable);
                                    lineFee = getSpecificRenewalFeeSchedule("LINE FEE", numTypeVariable, numSubTypeVariable);
                                    accessFee = getSpecificRenewalFeeSchedule("ACCESS CODE FEE", numTypeVariable, numSubTypeVariable);
                                    oldAccessFee = getSpecificNewFeeSchedule("ACCESS CODE FEE", numTypeVariable, numSubTypeVariable);
                                    log.info("adminFee is {} , line fee is {} and access fee is {}  ", adminFee, lineFee, accessFee);

                                    NumRenewalModel numRenewalModel = numRenewalRepository.getByApplicationId(num.getApplicationId());
                                    String billStart = DateFormatter.finalFormattedDate(numRenewalModel.getStartBillingPeriod());
                                    String billEnd = DateFormatter.finalFormattedDate(numRenewalModel.getEndBillingPeriod());

                                    String cov = "";
                                    Integer lines = 0;

                                    if (Objects.equals(numSubTypeVariable, "NATIONAL")) {
                                        cov = "National";
                                        finalLineFee = lineFee * 1000000;
                                        finalAccessFee = ((accessFee * oldAccessFee) / 100) / 10; // percentage of old access code fee
                                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                                        total = finalAccessFee + finalLineFee + finalAdminFee;

                                    } else  if (Objects.equals(numSubTypeVariable, "GEOGRAPHICAL")){
                                        cov = coverageArea;
                                        finalLineFee = lineFee * 10000;
                                        finalAccessFee = (accessFee * oldAccessFee) / 100; // percentage of old access code fee
                                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                                        total = finalAccessFee + finalLineFee + finalAdminFee;
                                    } else{
                                        // toll free and vanity
                                        finalLineFee = lineFee * 10000;
                                        finalAccessFee = ((accessFee * oldAccessFee) / 100) / 10; // percentage of old access code fee
                                        finalAdminFee = (adminFee * (finalAccessFee + finalLineFee)) / 100;
                                        total = finalAccessFee + finalLineFee + finalAdminFee;
                                    }
                                    log.info("New allocation fee is {} ", total);


                                    // Invoice item: prepared based on invoice structure
                                    InvoiceItems item = new InvoiceItems();
                                    item.setObjectType("invoice_item");
                                    item.setInvoiceItemId(GetUniqueId.getId());
                                    item.setAmount(total);

                                    Double doubleAmt = Double.valueOf(total);
                                    log.info("Total in Big decimal {} ", doubleAmt);
                                    DecimalFormat df = new DecimalFormat("####0.00");

                                    item.setItemData("{\n" +
                                            "    \"Numbering Area\": \"" + cov + "\",\n" +
                                            "    \"Total Lines\": " + lines + ",\n" +
                                            "    \"Number Of Days\": 365,\n" +
                                            "    \"Access Code Fees (n)\": " + finalAccessFee + ",\n" +
                                            "    \"Admin Fees\": " + finalAdminFee + ",\n" +
                                            "    \"Area Code\": \"" + areaCode + "\",\n" +
                                            "    \"Access Code\": \"" + accessCode + "\",\n" +
                                            "    \"Allocation Date\": \"" + allocationDate + "\",\n" +
                                            "    \"Billing Period From\": \"" + billStart + "\",\n" +
                                            "    \"Billing Period To\": \"" + billEnd + "\",\n" +
                                            "    \"Numbering Range From\": \"" + numberingRangeFrom + "\",\n" +
                                            "    \"Numbering Range To\": \"" + numberingRangeTo + "\",\n" +
                                            "    \"Amount Due\": " + df.format(doubleAmt) + "\n" +
                                            "}");

                                    // Add each invoice item
                                    invoiceItems.add(item);
                                    // add the fee in an array of integer
                                    listOfFees.add(total);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                // If number type is short code
                if (Objects.equals(numType, "SHORT-CODE")) {
                    String appId = (invoiceObject.getSelectedNumbers() != null ?
                            invoiceObject.getSelectedNumbers().get(0).getApplicationId() : "");
                    log.info("Short code application id {} " + appId);

                    List<SelectedFeeScheduleModel> newFee =
                            selectedFeeScheduleRepository.findByApplicationIdNewFee(appId);

                    List<SelectedFeeScheduleModel> renewalFee =
                            selectedFeeScheduleRepository.findByApplicationIdRenewalFee(appId);

                    List<SelectedNumberModel> numbers =
                            selectedNumbersRepo.getSelectedNumber(appId);

                    if (Objects.equals(type, "NEW")) {
                        // Invoice item: prepared based on invoice structure

                        // Index zero
                        if (newFee.size() >= 1 && numbers.size() >= 1) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(0).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(0).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(0).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index one
                        if (newFee.size() >= 2 && numbers.size() >= 2) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(1).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(1).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(1).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index two
                        if (newFee.size() >= 3 && numbers.size() >= 3) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(2).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(2).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(2).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index three
                        if (newFee.size() >= 4 && numbers.size() >= 4) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(3).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(3).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(3).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index four
                        if (newFee.size() >= 5 && numbers.size() >= 5) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(4).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(4).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(4).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index five
                        if (newFee.size() >= 6 && numbers.size() >= 6) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(5).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(5).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(5).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index six
                        if (newFee.size() >= 7 && numbers.size() >= 7) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(6).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(6).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(6).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index seven
                        if (newFee.size() >= 8 && numbers.size() >= 8) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(7).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(7).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(7).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index Eight
                        if (newFee.size() >= 9 && numbers.size() >= 9) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(8).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(8).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(8).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index nine
                        if (newFee.size() >= 10 && numbers.size() >= 10) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(9).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(9).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(9).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index ten
                        if (newFee.size() >= 11 && numbers.size() >= 11) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(newFee.get(10).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(10).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", newFee.get(10).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                    }

                    if (Objects.equals(type, "RENEWAL")) {
                        // Invoice item: prepared based on invoice structure
                        // Index zero
                        if (renewalFee.size() >= 1 && numbers.size() >= 1) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(0).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(0).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(0).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index one
                        if (renewalFee.size() >= 2 && numbers.size() >= 2) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(1).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(1).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(1).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index two
                        if (renewalFee.size() >= 3 && numbers.size() >= 3) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(2).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(2).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(2).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index three
                        if (renewalFee.size() >= 4 && numbers.size() >= 4) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(3).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(3).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(3).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index four
                        if (renewalFee.size() >= 5 && numbers.size() >= 5) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(4).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(4).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(4).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index five
                        if (renewalFee.size() >= 6 && numbers.size() >= 6) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(5).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(5).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(5).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index six
                        if (renewalFee.size() >= 7 && numbers.size() >= 7) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(6).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(6).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(6).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index seven
                        if (renewalFee.size() >= 8 && numbers.size() >= 8) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(7).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(7).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(7).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index Eight
                        if (renewalFee.size() >= 9 && numbers.size() >= 9) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(8).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(8).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(8).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index nine
                        if (renewalFee.size() >= 10 && numbers.size() >= 10) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(9).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(9).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(9).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }

                        // Index ten
                        if (renewalFee.size() >= 11 && numbers.size() >= 11) {
                            InvoiceItems item = new InvoiceItems();
                            Integer aa = Integer.parseInt(renewalFee.get(10).getFeeAmount());
                            item.setObjectType("invoice_item");
                            item.setInvoiceItemId(GetUniqueId.getId());
                            item.setAmount(aa);
                            JSONObject jsonBreakdown = new JSONObject();
                            try {
                                jsonBreakdown.put("Short Code", numbers.get(10).getSelectedNumberValue());
                                jsonBreakdown.put("Service Type", renewalFee.get(10).getFeeName());
                                jsonBreakdown.put("Amount", aa);
                                jsonBreakdown.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            item.setItemData(jsonBreakdown.toString());
                            invoiceItems.add(item);
                            listOfFees.add(aa);
                        }
                    }
                }
            }

            String companyNameVal = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
            log.info("Company Name {} ", companyNameVal);

            // Setting service id
            if ((Objects.equals(numberSubType, "GEOGRAPHICAL")) && (Objects.equals(type, "NEW"))) {
                serviceId = revenueIdFixedMobileNew;
                title = "NATIONAL NUMBERING RESOURCE ALLOCATION OFFER INVOICE";
                description = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                description2 = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                invoiceStructure = revenueIdFixedMobileNewInvoiceStructure;
                invoiceType = invoiceTypeIDNew;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);


            } else if ((Objects.equals(numberSubType, "GEOGRAPHICAL")) && (Objects.equals(type, "RENEWAL"))) {
                serviceId = revenueIdFixedMobileRenewal;
                title = "RENEWAL OF FIXED AND MOBILE APPLICATION Invoice";
                description = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                description2 = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                invoiceStructure = revenueIdFixedMobileRenewalInvoiceStructure;
                invoiceType = invoiceTypeIDRenewal;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "NATIONAL")) && (Objects.equals(type, "NEW"))) {
                serviceId = revenueIdFixedMobileNew;
                title = "NNP INVOICE OFFER FOR " + nationalNumberingBlock + " MOBILE NUMBERS TO " + companyNameVal.toUpperCase();
                description = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                description2 = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                invoiceStructure = revenueIdFixedMobileNewInvoiceStructure;
                invoiceType = invoiceTypeIDNew;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "NATIONAL")) && (Objects.equals(type, "RENEWAL"))) {
                serviceId = revenueIdFixedMobileRenewal;
                title = "RENEWAL OF FIXED AND MOBILE APPLICATION Invoice";
                description = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                description2 = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                invoiceStructure = revenueIdFixedMobileRenewalInvoiceStructure;
                invoiceType = invoiceTypeIDRenewal;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "VANITY")) && (Objects.equals(type, "NEW"))) {
                serviceId = revenueIdFixedMobileNew;
                title = "NNP INVOICE OFFER FOR 10,000 MOBILE NUMBERS TO " + companyNameVal.toUpperCase();
                description = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                description2 = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                invoiceStructure = revenueIdFixedMobileNewInvoiceStructure;
                invoiceType = invoiceTypeIDNew;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "VANITY")) && (Objects.equals(type, "RENEWAL"))) {
                serviceId = revenueIdFixedMobileRenewal;
                title = "RENEWAL OF FIXED AND MOBILE APPLICATION Invoice";
                description = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                description2 = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                invoiceStructure = revenueIdFixedMobileRenewalInvoiceStructure;
                invoiceType = invoiceTypeIDRenewal;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "TOLL-FREE")) && (Objects.equals(type, "NEW"))) {
                serviceId = revenueIdFixedMobileNew;
                title = "NNP INVOICE OFFER FOR 10,000 MOBILE NUMBERS TO " + companyNameVal.toUpperCase();
                description = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                description2 = thisYear + " NEW FIXED AND MOBILE ALLOCATION Invoice";
                invoiceStructure = revenueIdFixedMobileNewInvoiceStructure;
                invoiceType = invoiceTypeIDNew;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "TOLL-FREE")) && (Objects.equals(type, "RENEWAL"))) {
                serviceId = revenueIdFixedMobileRenewal;
                title = "RENEWAL OF FIXED AND MOBILE APPLICATION Invoice";
                description = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                description2 = previousYear + " and " + thisYear + " NEW FIXED AND MOBILE Renewal Invoice";
                invoiceStructure = revenueIdFixedMobileRenewalInvoiceStructure;
                invoiceType = invoiceTypeIDRenewal;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "SHORT-CODE")) && (Objects.equals(type, "NEW"))) {
                serviceId = revenueIdShortCodeNew;
                title = thisYear + " New Short Code Allocation Invoice";
                description = thisYear + " Shortcode Allocation Invoice";
                description2 = thisYear + " Shortcode Allocation Invoice";
                invoiceStructure = revenueIdShortCodeNewInvoiceStructure;
                invoiceType = invoiceTypeIDNew;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else if ((Objects.equals(numberSubType, "SHORT-CODE")) && (Objects.equals(type, "RENEWAL"))) {
                serviceId = revenueIdShortCodeRenewal;
                title = thisYear + " Renewal Short Code Allocation Invoice";
                description = previousYear + " and " + thisYear + " Shortcode Renewal Invoice";
                description2 = previousYear + " and " + thisYear + " Shortcode Renewal Invoice";
                invoiceStructure = revenueIdShortCodeRenewalInvoiceStructure;
                invoiceType = invoiceTypeIDRenewal;

                Timestamp todayDate = CurrentTimeStamp.getCurrentTimeStamp();
                validityPeriod = 60;
                serviceStartDate = todayDate;
                serviceEndDate = CurrentTimeStamp.getNextYearTimeStamp(todayDate, validityPeriod);

            } else {
                serviceId = "";
            }


            if (((Objects.equals(type, "NEW")) && (invoiceObject != null) &&
                    (Objects.equals(invoiceObject.getInvoiceModel().getNumberType().toUpperCase(), "STANDARD"))) ||
                    ((Objects.equals(type, "NEW")) && (invoiceObject != null) &&
                            (Objects.equals(invoiceObject.getInvoiceModel().getNumberType().toUpperCase(), "SPECIAL")))) {
                String numType = (invoiceObject.getInvoiceModel() != null ?
                        invoiceObject.getInvoiceModel().getNumberType().toUpperCase() : "");
                String numSubType = (invoiceObject.getInvoiceModel() != null ?
                        invoiceObject.getInvoiceModel().getNumberSubType().toUpperCase() : "");

                Integer adminFeeVar = getSpecificNewFeeSchedule("ADMIN FEE", numType, numSubType);
                Integer lineFeeVar = getSpecificNewFeeSchedule("LINE FEE", numType, numSubType);
                Integer accessFeeVar = getSpecificNewFeeSchedule("ACCESS CODE FEE", numType, numSubType);
                log.info("adminFee is {} , line fee is {} and access fee is {}  ", adminFeeVar, lineFeeVar, accessFeeVar);

                Integer linexxx = 0;
                Integer accessxxx = 0;
                Integer adminxxx = 0;
                Integer finalAdminxxx = 0;

                if (Objects.equals(numSubType, "NATIONAL")) {
                    linexxx = lineFeeVar * 1000000;
                    accessxxx = accessFeeVar / 10;
                    adminxxx = (adminFeeVar * (accessxxx + linexxx)) / 100;
                    int count = invoiceObject.getSelectedNumbers().size();
                    if (count >= 1) {
                        finalAdminxxx = adminxxx * count;
                    } else {
                        finalAdminxxx = adminxxx;
                    }

                } else if (Objects.equals(numSubType, "GEOGRAPHICAL")) {
                    linexxx = lineFeeVar * 10000;
                    accessxxx = accessFeeVar;
                    adminxxx = (adminFeeVar * (accessxxx + linexxx)) / 100;
                    finalAdminxxx = adminxxx; // Geographical number can only be selected as one
                } else {
                    linexxx = lineFeeVar * 10000;
                    accessxxx = accessFeeVar;
                    adminxxx = (adminFeeVar * (accessxxx + linexxx)) / 100;
                    int count = invoiceObject.getSelectedNumbers().size();
                    if (count >= 1) {
                        finalAdminxxx = adminxxx * count;
                    } else {
                        finalAdminxxx = adminxxx;
                    }
                }

                // Admin Fee
                InvoiceItems adminItem = new InvoiceItems();
                String iid = GetUniqueId.getId();
                adminItem.setObjectType("charge");
                adminItem.setInvoiceItemId(iid);
                adminItem.setAmount(finalAdminxxx); // total admin amount
                adminItem.setItemDescription("Admin Fee");
                JSONObject jsonBreakdownAdmin = new JSONObject();
                try {
                    jsonBreakdownAdmin.put("Type", "Fixed");
                    jsonBreakdownAdmin.put("Value", Double.valueOf(finalAdminxxx));
                    jsonBreakdownAdmin.put("ChargeId", iid);
                    jsonBreakdownAdmin.put("Name", "Admin Fee");
                    jsonBreakdownAdmin.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adminItem.setItemData(jsonBreakdownAdmin.toString());
                invoiceItems.add(adminItem);
                listOfFees.add(finalAdminxxx);
            }

            String id = GetUniqueId.getId();
            Integer sum = listOfFees.stream()
                    .collect(Collectors.summingInt(Integer::intValue));

            licenseTypesList.add(licenceTypeID);

            companyName = genericTableCellGetRepository.getCompanyNameOfApp(applicationId);
            log.info("Company Name {} ", companyName);
            if (companyName != null) {
                OrganisationProfileModel organisationProfileModel = organisationProfileRepo.findByName(companyName);
                log.info("profile {}", organisationProfileModel);
                nccId = organisationProfileModel.getNccId();

            }
            String url = invoiceUrl;
            log.info("Request url :: {} ", url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", invoiceApiKey);

            // Invoice
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(id);
            invoice.setCurrencyId(currencyId);
            invoice.setRevenueServiceId(serviceId);
            invoice.setInvoiceStructureVersionId(invoiceStructure);
            invoice.setAmount(sum);
            invoice.setCustomerEmail(email);
            invoice.setTitle(title);
            invoice.setDescription(description);
            invoice.setInvoiceTypeId(invoiceType);
            invoice.setLicenseTypes(licenseTypesList);
            invoice.setCustomerNccId(nccId);
            invoice.setDescription2(description2);
            invoice.setValidityPeriod(validityPeriod);
            invoice.setServiceStartDate(serviceStartDate);
            invoice.setServiceEndDate(serviceEndDate);

            // add item list
            invoice.setInvoiceItems(invoiceItems);

            createInvoiceRequest.setApiKey(invoiceApiKey);
            createInvoiceRequest.setInvoice(invoice);

            log.info("Built invoice Request :: {} ", createInvoiceRequest);
            log.info("Built invoice Request :: {} ", createInvoiceRequest.getInvoice().getInvoiceItems());

            ResponseEntity<String> res = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(createInvoiceRequest, headers).post(String.class);

            log.info("AutoFeeResponse from invoice eservices status code :: {} ", res.getStatusCode());
            log.info("AutoFeeResponse from invoice eservices  body :: {} ", res.getBody());

            response = objectMapper.readValue(res.getBody(), CreateInvoiceResponse.class);

            log.info("AutoFeeResponse {}", response);
            log.info("AutoFeeResponse {}", response.getResponseMetadata());
            log.info("AutoFeeResponse {}", response.getResponseMetadata().getHttpStatusCode());

            // If call to eservices is successful, update the invoice table with eservices request id
            // and set the flag of sendToEservices to false so the invoice won't be sent to eservices again
            log.info("AutoFeeResponse from eservices is not null {}", response);
            assert response != null;
            invoiceRepository.updateEservicesInvoiceStatus(id, invoiceId);

            if (response != null && Objects.equals(invoiceType, "RENEWAL")) {
                int isResolvedToFalse = selectedNumbersRepo.updateApplicationNotToBePickedRenewal(applicationId);
                log.info("Application renewal set to false {} ", isResolvedToFalse);

            }

            return response != null;

        } catch (Exception exe) {
            log.info("Exception occurred here{}", exe.getMessage());
            exe.printStackTrace();
            return false;
        }
    }

    /**
     * Method to call eservices to get notification queue
     *
     * @return
     * @throws Exception
     */
    public List<InvoiceNotificationPayload> fetchEserviceNotificationQueue() throws Exception {
        try {
            String url = invoiceNotificationUrl;
            log.info("Request url :: {} ", url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", invoiceApiKey);

            ResponseEntity<String> res = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(headers).get(String.class);

            log.info("AutoFeeResponse from get notification queue status code :: {} ", res.getStatusCode());
            log.info("AutoFeeResponse from get notification queue body :: {} ", res.getBody());

            ObjectMapper mapper = new ObjectMapper();

            List<InvoiceNotificationPayload> payloads = mapper.readValue(res.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, InvoiceNotificationPayload.class));

            log.info("AutoFeeResponse from get notification queue body :: {} ", payloads);

            return payloads;

        } catch (Exception exe) {
            return null;
        }
    }

    /**
     * Method to call eservices to get delete from notification queue
     *
     * @return
     * @throws Exception
     */
    public boolean deleteFromNotificationQueue(String receiptHandle) throws Exception {
        try {
            CreateInvoiceResponse response;
            DeleteInvoice deleteInvoiceRequest = new DeleteInvoice();

            String url = deleteInvoiceNotificationUrl;
            log.info("Request url :: {} ", url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", invoiceApiKey);
            headers.add("Content-Type", "application/json");

            deleteInvoiceRequest.setReceiptHandle(receiptHandle);

            ResponseEntity<String> res = restUtil.setUrl(url).setTimeout(60)
                    .setRequest(deleteInvoiceRequest, headers).delete(String.class);

            return res.getStatusCode() == HttpStatus.OK;

        } catch (Exception exe) {
            return false;
        }
    }
}
