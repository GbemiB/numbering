package com.molcom.nms.number.renewal.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class NumRenewalModel {
    private String applicationId;
    private String organization;
    private String startBillingPeriod;
    private String endBillingPeriod;
    private String renewalStatus;
    private String paymentStatus;
    private String applicationExpiryDate;
    private String dateAllocatedFrom;
    private String dateAllocatedTo;
    private String numberType;
    private String numberSubType;
    @Nullable
    private String renewalId;

    // when an application expires: it should be dumped in renewal table
    // set renewal status as APPROVED and AWAITING REVIEW
    // of course it should be sent to eservices i.e the invoice should be created
    // if the invoice is paid for, update the invoice as paid
    // for get by renewal fee for the application
    // get the selected number for the application
//    ApplicationExpiryDate is EndBillingPeriod ++ 1 day
}
