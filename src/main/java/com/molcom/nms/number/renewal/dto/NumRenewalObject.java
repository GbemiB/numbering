package com.molcom.nms.number.renewal.dto;

import com.molcom.nms.invoice.dto.InvoiceModel;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NumRenewalObject {
    private NumRenewalModel numRenewalModel;
    private List<SelectedNumberModel> selectedNumberModelList = new ArrayList<>();
    private List<InvoiceModel> invoiceModelList = new ArrayList<>();

}
