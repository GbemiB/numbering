package com.molcom.nms.number.application.dto;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.number.configurations.dto.ConnectedTelcoCompany;
import com.molcom.nms.number.configurations.dto.ISPCDetailModel;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ISPCNumberResponse {
    ISPCNumberObject ispcObject = new ISPCNumberObject();
    List<ConnectedTelcoCompany> connectedTelcoCompanyList = new ArrayList<>();
    List<SupportingDocument> supportingDocumentList = new ArrayList<>();
    List<ISPCDetailModel> ispcDetailModels = new ArrayList<>();
    List<CompRepModel> compRepModelList = new ArrayList<>();
}
