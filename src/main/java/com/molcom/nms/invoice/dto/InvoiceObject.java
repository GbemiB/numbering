package com.molcom.nms.invoice.dto;

import com.molcom.nms.fee.selectedFeeSchedule.dto.SelectedFeeScheduleModel;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvoiceObject {
    private InvoiceModel invoiceModel;
    private List<SelectedFeeScheduleModel> selectedFees = new ArrayList<>();
    private List<SelectedNumberModel> selectedNumbers = new ArrayList<>();
}
