package com.molcom.nms.number.application.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.application.dto.ISPCNumberModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ISPCNumberRepository implements IISPCNumberRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveISPCFistStep(ISPCNumberModel model) throws SQLException {
        try {
            String sql1 = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";

            List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), model.getApplicationId());
            log.info("ISPCNumberModel {} ", ispcNumberModels);
            log.info("Number of applications {} ", ispcNumberModels.size());

            if (ispcNumberModels.size() > 0) {
                String sql2 = "UPDATE IspcNumbers SET ApplicationType =?, ApplicationStatus =?," +
                        "CompanyName =?, CompanyEmail =?, " +
                        "CompanyPhone =?, CompanyFax =?, CompanyState =?, " +
                        "CompanyAddress =?, CorrespondingEmail =?, CorrespondingPhone =?, CorrespondingFax =?, " +
                        "CorrespondingState =?, CompanyRepresentativeIdOne =?, CompanyRepresentativeIdTwo =?, CorrespondingAddress =?," +
                        "InterconnectAgreement =?, CreatedBy =?,CreatedUserEmail =?, IpAddress =?," +
                        "CreatedDate =?, CurrentStep=?, IsMDA=? WHERE ApplicationId=? ";
                return jdbcTemplate.update(sql2,
                        "NEW",
                        "PENDING",
                        model.getCompanyName(),
                        model.getCompanyEmail(),
                        model.getCompanyPhone(),
                        model.getCompanyFax(),
                        model.getCompanyState(),
                        model.getCompanyAddress(),
                        model.getCorrespondingEmail(),
                        model.getCorrespondingPhone(),
                        model.getCompanyFax(),
                        model.getCorrespondingState(),
                        model.getCompanyRepresentativeIdOne(),
                        model.getCompanyRepresentativeIdTwo(),
                        model.getCompanyAddress(),
                        model.getInterconnectAgreement(),
                        model.getCreatedBy(),
                        model.getCreatedUserEmail(),
                        model.getIpAddress(),
                        CurrentTimeStamp.getCurrentTimeStamp(),
                        "1",
                        model.getIsMDA(),
                        model.getApplicationId());
            } else {
                String sql = "INSERT INTO IspcNumbers (ApplicationId, ApplicationType, ApplicationStatus, " +
                        "CompanyName, CompanyEmail, CompanyPhone, CompanyFax, " +
                        "CompanyState, CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                        "CorrespondingState, CompanyRepresentativeIdOne, CompanyRepresentativeIdTwo, CorrespondingAddress, " +
                        "InterconnectAgreement, CreatedBy, CreatedUserEmail,IpAddress, CreatedDate, CurrentStep, IsMDA) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                return jdbcTemplate.update(sql,
                        model.getApplicationId(),
                        "NEW",
                        "PENDING",
                        model.getCompanyName(),
                        model.getCompanyEmail(),
                        model.getCompanyPhone(),
                        model.getCompanyFax(),
                        model.getCompanyState(),
                        model.getCompanyAddress(),
                        model.getCorrespondingEmail(),
                        model.getCorrespondingPhone(),
                        model.getCompanyFax(),
                        model.getCorrespondingState(),
                        model.getCompanyRepresentativeIdOne(),
                        model.getCompanyRepresentativeIdTwo(),
                        model.getCompanyAddress(),
                        model.getInterconnectAgreement(),
                        model.getCreatedBy(),
                        model.getCreatedUserEmail(),
                        model.getIpAddress(),
                        CurrentTimeStamp.getCurrentTimeStamp(),
                        "1",
                        model.getIsMDA()
                );
            }
        } catch (Exception exe) {
            log.info("Exception occurred in first step save of ispc number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveISPCSecondStep(ISPCNumberModel model) throws SQLException {
        String sql = "UPDATE IspcNumbers SET ExistingIspcNumber=?, ExistingIspcNumberId=?, IntendingIspcNumber=?, Quantity=?, CurrentStep=? WHERE ApplicationId=? ";
        return jdbcTemplate.update(sql,
                model.getExistingIspcNumber(),
                model.getExistingIspcNumberId(),
                model.getIntendingIspcNumber(),
                model.getQuantity(),
                "2",
                model.getApplicationId()
        );
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveISPCThirdStep(ISPCNumberModel model) throws SQLException {
        String sql = "UPDATE IspcNumbers SET UndertakenComment=?, CurrentStep=? WHERE ApplicationId=?";

        return jdbcTemplate.update(sql,
                model.getUndertakenComment(),
                "4",
                model.getApplicationId()
        );
    }

    /**
     * @param currentStep
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int saveISPCDocStep(String currentStep, String applicationId) throws SQLException {
        String sql = "UPDATE IspcNumbers SET CurrentStep=? WHERE ApplicationId= ?";

        return jdbcTemplate.update(sql,
                "3",
                applicationId
        );
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> getIspcNoFistStep(String applicationId) throws SQLException {
        String sql = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyFax, CompanyState," +
                "CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                "CorrespondingState, CorrespondingAddress, InterconnectAgreement, CurrentStep, IsMDA " +
                "FROM IspcNumbers WHERE ApplicationId=?";
        List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);
        log.info("ISPCNumberModel {} ", ispcNumberModels);
        return ispcNumberModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> getIspcNoSecondStep(String applicationId) throws SQLException {
        String sql = "SELECT ExistingIspcNumber, ExistingIspcNumberId, IntendingIspcNumber, " +
                "CurrentStep, IsMDA FROM IspcNumbers WHERE ApplicationId=?";
        List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);
        log.info("ISPCNumberModel {} ", ispcNumberModels);
        return ispcNumberModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> getIspcNoFourthStep(String applicationId) throws SQLException {
        String sql = "SELECT UndertakenComment, CurrentStep, IsMDA FROM IspcNumbers WHERE ApplicationId=?";
        List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);
        log.info("ISPCNumberModel {} ", ispcNumberModels);
        return ispcNumberModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteApplication(String applicationId) throws SQLException {
        String sql = "DELETE FROM IspcNumbers WHERE ApplicationId=?";
        return jdbcTemplate.update(sql, applicationId);
    }

    /**
     * @param ApplicationId
     * @param ApplicationType
     * @param ApplicationStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> filterISPC(String companyName, String ApplicationId, String ApplicationType, String ApplicationStatus, String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from IspcNumbers WHERE ");
        if (ApplicationId != null && !ApplicationId.isEmpty() &&
                !Objects.equals(ApplicationId, "")
                && !Objects.equals(ApplicationId.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationId) = '").append(ApplicationId.toUpperCase()).append("' AND ");
        }
        if (ApplicationStatus != null && !ApplicationStatus.isEmpty() &&
                !Objects.equals(ApplicationStatus, "")
                && !Objects.equals(ApplicationStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationStatus) = '").append(ApplicationStatus.toUpperCase()).append("' AND ");
        }

        if (ApplicationType != null && !ApplicationType.isEmpty() &&
                !Objects.equals(ApplicationType, "")
                && !Objects.equals(ApplicationType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationType) = '").append(ApplicationType.toUpperCase()).append("' AND ");
        }
        if (StartDate != null && EndDate != null &&
                !StartDate.isEmpty() &&
                !EndDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + StartDate + "') AND DATE('" + EndDate + "') AND ");
        }

//        sqlBuilder.append("CreatedDate IS NOT NULL ");
//        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
//        String sql = sqlBuilder.toString();

        sqlBuilder.append("CreatedDate IS NOT NULL AND UPPER(CompanyName)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class));
        log.info("ISPCNumberModel {} ", ispcNumberModels);
        return ispcNumberModels;
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param ApplicationType
     * @param ApplicationStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> filterAdminISPC(String CompanyName, String ApplicationId, String ApplicationType, String ApplicationStatus, String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from IspcNumbers WHERE ");
        if (ApplicationStatus != null && !ApplicationStatus.isEmpty() &&
                !Objects.equals(ApplicationStatus, "")
                && !Objects.equals(ApplicationStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationStatus) = '").append(ApplicationStatus.toUpperCase()).append("' AND ");
        }

        if (ApplicationId != null && !ApplicationId.isEmpty() &&
                !Objects.equals(ApplicationId, "")
                && !Objects.equals(ApplicationId.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationId) = '").append(ApplicationId.toUpperCase()).append("' AND ");
        }
        if (CompanyName != null && !CompanyName.isEmpty() &&
                !Objects.equals(CompanyName, "")
                && !Objects.equals(CompanyName.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CompanyName) = '").append(CompanyName.toUpperCase()).append("' AND ");
        }
        if (ApplicationType != null && !ApplicationType.isEmpty() &&
                !Objects.equals(ApplicationType, "")
                && !Objects.equals(ApplicationType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationType) = '").append(ApplicationType.toUpperCase()).append("' AND ");
        }
        if (StartDate != null && EndDate != null &&
                !StartDate.isEmpty() &&
                !EndDate.isEmpty()
        ) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + StartDate + "') AND DATE('" + EndDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<ISPCNumberModel> ispcNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class));
        log.info("ISPCNumberModel {} ", ispcNumberModels);
        return ispcNumberModels;
    }


    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public ISPCNumberModel getISPCByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM IspcNumbers WHERE ApplicationId = ?";
        try {
            ISPCNumberModel ispcNumberModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(ISPCNumberModel.class), applicationId);
            log.info("ISPCNumberModel {} ", ispcNumberModel);
            return ispcNumberModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<ISPCNumberModel> getAll() throws SQLException {
        String sql = "SELECT * from IspcNumbers ORDER BY CreatedDate DESC";
        List<ISPCNumberModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ISPCNumberModel.class));
        log.info("ISPCNumberModel {} ", modelList);
        return modelList;
    }


}

