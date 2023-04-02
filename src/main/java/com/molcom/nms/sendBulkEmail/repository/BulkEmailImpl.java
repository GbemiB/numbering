package com.molcom.nms.sendBulkEmail.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.general.utils.RefGenerator;
import com.molcom.nms.sendBulkEmail.dto.BulkEmailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class BulkEmailImpl implements IBulkEmailRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public String sendEmail(BulkEmailModel model) throws SQLException {
        try {
            String mailId = "MOLCOM" + RefGenerator.getRefNo(10);
            log.info("mail id {}", mailId);
            String sql = "INSERT INTO BulkEmail (RecipientEmail, MailSubject, MailBody, ExternalMailId, isDispatched, CreatedDate) " +
                    "VALUES(?,?,?,?,?,?)";
            int response = jdbcTemplate.update(sql,
                    model.getRecipientEmail(),
                    model.getMailSubject(),
                    model.getMailBody(),
                    mailId,
                    "FALSE",
                    CurrentTimeStamp.getCurrentTimeStamp());
            log.info("AutoFeeResponse {}", response);
            if (response == 1) {
                return mailId;
            } else {
                return "error";
            }
        } catch (Exception exception) {
            log.info("Exception occured here");
            return null;

        }
    }

    /**
     * @param mailId
     * @return
     * @throws SQLException
     */
    @Override
    public int updateDispatchStatus(String mailId) throws SQLException {
        String sql = "UPDATE BulkEmail set isDispatched=? WHERE ExternalMailId=?";

        return jdbcTemplate.update(sql, "TRUE", mailId);
    }


    /**
     * @param recipientEmail
     * @param mailSubject
     * @param startDate
     * @param endDate
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<BulkEmailModel> filter(String recipientEmail, String mailSubject, String startDate, String endDate, String rowNumber) throws Exception {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from BulkEmail WHERE ");

        if (recipientEmail != null
                && !recipientEmail.isEmpty()
                && !Objects.equals(recipientEmail, "")
                && !Objects.equals(recipientEmail.toUpperCase(), "SELECT")
                && !Objects.equals(recipientEmail.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("UPPER(RecipientEmail) = '").append("" + recipientEmail.toUpperCase() + "").append("' AND ");
        }

        if (mailSubject != null
                && !mailSubject.isEmpty()
                && !Objects.equals(mailSubject, "")
                && !Objects.equals(mailSubject.toUpperCase(), "SELECT")
                && !Objects.equals(recipientEmail.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("UPPER(MailSubject) = '").append("" + mailSubject.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("sql {} ", sql);

        List<BulkEmailModel> bulkEmailModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(BulkEmailModel.class));
        log.info("BulkEmailModel {} ", bulkEmailModels);
        return bulkEmailModels;
    }

    /**
     * @param rowNumber
     * @return
     * @throws Exception
     */
    @Override
    public List<BulkEmailModel> getAll(String rowNumber) throws Exception {
        String sql = "SELECT * from BulkEmail  ORDER BY CreatedDate  DESC LIMIT " + rowNumber + "";
        List<BulkEmailModel> bulkEmailModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(BulkEmailModel.class));
        log.info("BulkEmailModel {} ", bulkEmailModels);
        return bulkEmailModels;
    }
}
