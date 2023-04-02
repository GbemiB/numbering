package com.molcom.nms.sendBulkEmail.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.sendBulkEmail.dto.BulkEmailModel;
import com.molcom.nms.sendBulkEmail.dto.EservicesSendMailRequest;

import java.util.List;

public interface IBulkEmailService {

    GenericResponse<BulkEmailModel> sendEmail(BulkEmailModel bulkEmailModel) throws Exception;

    GenericResponse<List<BulkEmailModel>> getAll(String rowNumber) throws Exception;

    GenericResponse<List<BulkEmailModel>> filter(String recipientEmail, String mailSubject,
                                                 String startDate, String endDate,
                                                 String rowNumber) throws Exception;

    Boolean genericMailFunction(EservicesSendMailRequest mailRequest) throws Exception;
}
