package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.SupportingDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class SupportingDocumentRepo implements ISupportingDocumentRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save supporting document
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveSupportingDocument(SupportingDocument model) throws SQLException {

        String sql1 = "SELECT * FROM SupportingDocuments WHERE ApplicationId =? AND DocumentName =?";

        List<SupportingDocument> supportingDocuments = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(SupportingDocument.class), model.getApplicationId(), model.getDocumentName());
        log.info("SupportingDocument {} ", supportingDocuments);
        log.info("Number of supporting document with name {} are : {} ", model.getDocumentName(), supportingDocuments.size());

        if (supportingDocuments.size() > 0) {
            String sql2 = "UPDATE SupportingDocuments set DocumentBase64String=?, DocumentFileName=?," +
                    "IsRequired=? WHERE DocumentName = ?";

            return jdbcTemplate.update(sql2,
                    model.getDocumentBase64String(),
                    model.getDocumentFileName(),
                    model.getIsRequired(),
                    model.getDocumentName()
            );
        } else {

            String sql = "INSERT INTO SupportingDocuments (ApplicationId, DocumentName," +
                    " DocumentBase64String, DocumentFileName, IsRequired, CreatedDate) " +
                    "VALUES(?,?,?,?,?,?)";
            return jdbcTemplate.update(sql,
                    model.getApplicationId(),
                    model.getDocumentName(),
                    model.getDocumentBase64String(),
                    model.getDocumentFileName(),
                    model.getIsRequired(),
                    CurrentTimeStamp.getCurrentTimeStamp()
            );
        }
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int updateSupportingDocument(SupportingDocument model) throws SQLException {
        String sql = "UPDATE SupportingDocuments set DocumentBase64String=?, DocumentFileName=?," +
                "IsRequired=? WHERE DocumentName = ?";

        return jdbcTemplate.update(sql,
                model.getDocumentBase64String(),
                model.getDocumentFileName(),
                model.getIsRequired(),
                model.getDocumentName()
        );
    }

    /**
     * Data layer to delete supporting document
     *
     * @param documentId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteSupportingDocumentById(String documentId) throws SQLException {
        String sql = "DELETE FROM SupportingDocuments WHERE DocumentId = ?";
        return jdbcTemplate.update(sql, documentId);
    }

    /**
     * @param documentName
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteSupportingDocumentByName(String documentName) throws SQLException {
        String sql = "DELETE FROM SupportingDocuments WHERE DocumentName = ?";
        return jdbcTemplate.update(sql, documentName);
    }

    /**
     * Data layer to get supporting documents by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SupportingDocument> getSupportingRequiredDocument(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SupportingDocuments WHERE ApplicationId=? AND IsRequired='Y'";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SupportingDocument.class), applicationId);
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SupportingDocument> getSupportingAdditionalDocument(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SupportingDocuments WHERE ApplicationId=? AND IsRequired='N'";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SupportingDocument.class), applicationId);
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SupportingDocument> getAllDocumentByApplication(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SupportingDocuments WHERE ApplicationId=? ";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SupportingDocument.class), applicationId);
    }
}
