package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    private String invoiceId;
    private String currencyId;
    private String revenueServiceId;
    private String invoiceStructureVersionId;
    private Integer amount;
    private String customerEmail;
    private String title;
    private String description;
    private String description2;
    private String invoiceTypeId;
    private String customerNccId;
    private Integer validityPeriod;
    private Date serviceStartDate;
    private Date serviceEndDate;
    private List<String> licenseTypes = new ArrayList<>();
    private List<InvoiceItems> invoiceItems = new ArrayList<>();

}
