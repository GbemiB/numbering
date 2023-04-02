package com.molcom.nms.sendBulkEmail.repository;

import com.molcom.nms.sendBulkEmail.dto.BulkEmailModel;

import java.sql.SQLException;
import java.util.List;

public interface IBulkEmailRepository {
    String sendEmail(BulkEmailModel bulkEmailModel) throws SQLException;

    int updateDispatchStatus(String mailId) throws SQLException;

    List<BulkEmailModel> filter(String recipientEmail, String mailSubject,
                                String startDate, String endDate,
                                String rowNumber) throws Exception;

    List<BulkEmailModel> getAll(String rowNumber) throws Exception;
}
