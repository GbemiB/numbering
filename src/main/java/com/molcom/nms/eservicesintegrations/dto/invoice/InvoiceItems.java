package com.molcom.nms.eservicesintegrations.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceItems {
    @JsonProperty("objectType")
    private String objectType;
    @JsonProperty("amount")
    private int amount;
    @JsonProperty("itemData")
    private String itemData;
    @JsonProperty("invoiceItemId")
    private String invoiceItemId;
    @Nullable
    @JsonProperty("itemDescription")
    private String itemDescription;

}




