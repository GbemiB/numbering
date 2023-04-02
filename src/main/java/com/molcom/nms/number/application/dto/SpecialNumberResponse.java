package com.molcom.nms.number.application.dto;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.dto.EquipmentDetailModel;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpecialNumberResponse {
    SpecialNumberObject specialNumberObject = new SpecialNumberObject();
    List<ConnectedTelcoCompany> connectedTelcoCompanyList = new ArrayList<>();
    List<EquipmentDetailModel> equipmentDetailModelList = new ArrayList<>();
    List<SelectedNumberModel> selectedNumberModelList = new ArrayList<>();
    List<FrequencyCoverageArea> frequencyCoverageAreaList = new ArrayList<>();
    List<SupportingDocument> supportingDocumentList = new ArrayList<>();
    List<CompRepModel> compRepModelList = new ArrayList<>();
}
