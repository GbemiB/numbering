package com.molcom.nms.signatory.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.signatory.dto.SignatoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SignatoryRepo implements ISignatoryRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(SignatoryModel model) throws SQLException {
        String sql = "INSERT INTO Signatory (SignatoryName, SignatoryDesignation, SignatorySignature, " +
                "Organisation, CreatedDate, CreatedBy) VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getSignatoryName(),
                model.getSignatoryDesignation(),
                model.getSignatorySignature(),
                model.getOrganisation(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getCreatedBy()
        );
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveSignatoryToApplication(SignatoryModel model) throws SQLException {
        String sql = "INSERT INTO Signatory (SignatoryName, SignatoryDesignation, SignatorySignature, " +
                "Organisation, CreatedDate, CreatedBy) VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getSignatoryName(),
                model.getSignatoryDesignation(),
                model.getSignatorySignature(),
                model.getOrganisation(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getCreatedBy()
        );
    }

    /**
     * @param model
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public int edit(SignatoryModel model, int signatoryId) throws SQLException {
        String sql = "UPDATE Signatory set SignatoryName=?, SignatoryDesignation=?, SignatorySignature=?," +
                " Organisation=?, UpdatedDate=?, UpdatedBy=? WHERE SignatoryId=?";

        return jdbcTemplate.update(sql,
                model.getSignatoryName(),
                model.getSignatoryDesignation(),
                model.getSignatorySignature(),
                model.getOrganisation(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getUpdatedBy(),
                signatoryId
        );
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public int checkActiveCount() throws SQLException {
        String sql = "SELECT count(*) FROM Signatory WHERE IsActive='TRUE'";
        return jdbcTemplate.queryForObject(
                sql, Integer.class);
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public int setAsActive(int signatoryId) throws SQLException {
        String sql = "UPDATE Signatory set IsActive='TRUE' WHERE SignatoryId=?";
        return jdbcTemplate.update(sql,
                signatoryId
        );
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public int removeAsActive(int signatoryId) throws SQLException {
        String sql = "UPDATE Signatory set IsActive='FALSE' WHERE SignatoryId=?";
        return jdbcTemplate.update(sql,
                signatoryId
        );
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int signatoryId) throws SQLException {
        String sql = "DELETE FROM Signatory WHERE SignatoryId = ?";
        return jdbcTemplate.update(sql, signatoryId);
    }

    /**
     * @param signatoryId
     * @return
     * @throws SQLException
     */
    @Override
    public SignatoryModel findByID(int signatoryId) throws SQLException {
        String sql = "SELECT * FROM Signatory WHERE SignatoryId = ?";
        try {
            SignatoryModel signatoryModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(SignatoryModel.class), signatoryId);
            log.info("SignatoryModel {} ", signatoryModel);
            return signatoryModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while getting signatory by id");
            return null;
        }
    }

    /**
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<SignatoryModel> getAll(String rowNumber) throws SQLException {
        String sql = "SELECT * from Signatory ORDER BY CreatedDate DESC LIMIT " + rowNumber + "";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SignatoryModel.class));
    }


    /**
     * Method to get active signatory
     *
     * @return
     * @throws SQLException
     */

    @Override
    public SignatoryModel getActiveSignatory() throws SQLException {
        String sql = "SELECT * FROM Signatory WHERE IsActive='TRUE'";
        try {
            SignatoryModel signatoryModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(SignatoryModel.class));
            log.info("SignatoryModel {} ", signatoryModel);
            return signatoryModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while getting signatory by id");
            return null;
        }
    }


    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SignatoryModel> findSavedSignatory(String applicationId) throws SQLException {
        String sql = "SELECT * FROM Signatory WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SignatoryModel.class), applicationId);
    }

    /**
     * @param signatoryName
     * @param signatoryDesignation
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<SignatoryModel> filter(String signatoryName, String signatoryDesignation, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from Signatory WHERE ");

        if (signatoryName != null
                && !signatoryName.isEmpty()
                && !Objects.equals(signatoryName, "")
                && !Objects.equals(signatoryName.toUpperCase(), "SELECT")
                && !Objects.equals(signatoryName.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("SignatoryName = '").append("" + signatoryName + "").append("' AND ");
        }

        if (signatoryDesignation != null
                && !signatoryDesignation.isEmpty()
                && !Objects.equals(signatoryDesignation, "")
                && !Objects.equals(signatoryDesignation.toUpperCase(), "SELECT")
                && !Objects.equals(signatoryName.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("SignatoryDesignation = '").append("" + signatoryDesignation + "").append("' AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("Signatory {} ", sql);

        List<SignatoryModel> signatoryModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SignatoryModel.class));
        return signatoryModels;
    }
}
