package com.molcom.nms.number.application.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.application.dto.StandardNumberModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class StandardNumberRepository implements IStandardNumberRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveStandardNoFistStep(StandardNumberModel model) throws SQLException {
        try {
            String sql1 = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";

            List<StandardNumberModel> standardNumberModels = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(StandardNumberModel.class), model.getApplicationId());
            log.info("StandardNumberModel {} ", standardNumberModels);
            log.info("Number of applications {} ", standardNumberModels.size());

            if (standardNumberModels.size() > 0) {
                String sql2 = "UPDATE StandardNumber SET ApplicationType =?, ApplicationStatus =?," +
                        "SubType =?, CompanyName =?, CompanyEmail =?, " +
                        "CompanyPhone =?, CompanyFax =?, CompanyState =?, " +
                        "CompanyAddress =?, CorrespondingEmail =?, CorrespondingPhone =?, CorrespondingFax =?, " +
                        "CorrespondingState =?, CompanyRepresentativeIdOne =?, CompanyRepresentativeIdTwo =?, CorrespondingAddress =?," +
                        "InterconnectAgreement =?, CreatedBy =?,CreatedUserEmail =?, IpAddress =?," +
                        "CreatedDate =?, CurrentStep=?, IsMDA=? WHERE ApplicationId=? ";
                return jdbcTemplate.update(sql2,
                        "NEW",
                        "PENDING",
                        model.getSubType(),
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
                String sql = "INSERT INTO StandardNumber (ApplicationId, ApplicationType, ApplicationStatus, SubType, " +
                        "CompanyName, CompanyEmail, CompanyPhone, CompanyFax, " +
                        "CompanyState, CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                        "CorrespondingState, CompanyRepresentativeIdOne, CompanyRepresentativeIdTwo, CorrespondingAddress, " +
                        "InterconnectAgreement, CreatedBy, CreatedUserEmail,IpAddress, CreatedDate, CurrentStep, IsMDA) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                return jdbcTemplate.update(sql,
                        model.getApplicationId(),
                        "NEW",
                        "PENDING",
                        model.getSubType(),
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
            log.info("Exception occurred in first step save of standard number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveStandardNoSecondStep(StandardNumberModel model) throws SQLException {
        String sql = "UPDATE StandardNumber SET CoverageArea=?, AreaCode=?," +
                "AccessCode=?, SelectedNumbers=?, NoOfTelcoCompanies=?, " +
                "TelcoCompanies=?, HaveYouReachedAgreement=?, HaveYouMetFinancialReq=?, " +
                "FrequentAssignment=?, RadioOrCableDeployment=?, Quantity=?, CurrentStep=? WHERE ApplicationId=? ";
        log.info(sql);
        return jdbcTemplate.update(sql,
                model.getCoverageArea(),
                model.getAreaCode(),
                model.getAccessCode(),
                model.getSelectedNumbers(),
                model.getNoOfTelcoCompanies(),
                model.getTelcoCompanies(),
                model.getHaveYouReachedAgreement(),
                model.getHaveYouMetFinancialReq(),
                model.getFrequentAssignment(),
                model.getRadioOrCableDeployment(),
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
    public int saveStandardNoThirdStep(StandardNumberModel model) throws SQLException {
        log.info("Third step {}", model.toString());
        String sql = "UPDATE StandardNumber SET UndertakenComment=?, ApplicationPaymentStatus=?," +
                " AllocationPaymentStatus=?, CurrentStep=? " +
                "WHERE ApplicationId=?";

        return jdbcTemplate.update(sql,
                model.getUndertakenComment(),
                "Awaiting Payment",
                "Not Paid",
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
    public int saveDocFourthStep(String currentStep, String applicationId) throws SQLException {
        String sql = "UPDATE StandardNumber SET CurrentStep=? WHERE ApplicationId= ?";

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
    public List<StandardNumberModel> getStandardNoFistStep(String applicationId) throws SQLException {
        String sql = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyFax, CompanyState," +
                "CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                "CorrespondingState, CorrespondingAddress, InterconnectAgreement, CurrentStep, IsMDA " +
                "FROM StandardNumber WHERE ApplicationId =?";
        List<StandardNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);
        log.info("StandardNumberModel {} ", models);
        return models;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<StandardNumberModel> getStandardNoSecondStep(String applicationId) throws SQLException {
        String sql = "SELECT CoverageArea, AccessCode, SelectedNumbers, NoOfTelcoCompanies, " +
                "TelcoCompanies, HaveYouReachedAgreement, HaveYouMetFinancialReq, FrequentAssignment, " +
                "RadioOrCableDeployment, CurrentStep, IsMDA  FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);
        log.info("StandardNumberModel {} ", models);
        return models;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<StandardNumberModel> getStandardNoFourthStep(String applicationId) throws SQLException {
        String sql = "SELECT UndertakenComment, CurrentStep, IsMDA FROM StandardNumber WHERE ApplicationId = ?";
        List<StandardNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);
        log.info("StandardNumberModel {} ", models);
        return models;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteApplication(String applicationId) throws SQLException {
        String sql = "DELETE FROM StandardNumber WHERE ApplicationId = ?";
        return jdbcTemplate.update(sql, applicationId);
    }

    /**
     * @param ApplicationId
     * @param SubType
     * @param ApplicationType
     * @param CoverageArea
     * @param AreaCode
     * @param AccessCode
     * @param ApplicationStatus
     * @param ApplicationPaymentStatus
     * @param AllocationPaymentStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<StandardNumberModel> filterStandardNo(String companyName,
                                                      String ApplicationId, String SubType, String ApplicationType,
                                                      String CoverageArea, String AreaCode, String AccessCode,
                                                      String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus,
                                                      String StartDate, String EndDate, String rowNumber) throws SQLException {

        log.info("App id {}", ApplicationId);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from StandardNumber WHERE ");
        if (ApplicationId != null && !ApplicationId.isEmpty() &&
                !Objects.equals(ApplicationId, "")
                && !Objects.equals(ApplicationId.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationId) = '").append(ApplicationId.toUpperCase()).append("' AND ");
        }
        if (ApplicationType != null && !ApplicationType.isEmpty() &&
                !Objects.equals(ApplicationType, "")
                && !Objects.equals(ApplicationType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationType) = '").append(ApplicationType.toUpperCase()).append("' AND ");
        }
        if (CoverageArea != null && !CoverageArea.isEmpty() &&
                !Objects.equals(CoverageArea, "")
                && !Objects.equals(CoverageArea.toUpperCase(), "SELECT")) {
            sqlBuilder.append("CoverageArea = '").append(CoverageArea.toUpperCase()).append("' AND ");
        }
        if (AreaCode != null && !AreaCode.isEmpty() &&
                !Objects.equals(AreaCode, "")
                && !Objects.equals(AreaCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("AreaCode = '").append(AreaCode.toUpperCase()).append("' AND ");
        }
        if (AccessCode != null && !AccessCode.isEmpty() &&
                !Objects.equals(AccessCode, "")
                && !Objects.equals(AccessCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("AccessCode = '").append(AccessCode.toUpperCase()).append("' AND ");
        }
        if (ApplicationStatus != null && !ApplicationStatus.isEmpty() &&
                !Objects.equals(ApplicationStatus, "")
                && !Objects.equals(ApplicationStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationStatus)= '").append(ApplicationStatus.toUpperCase()).append("' AND ");
        }
        if (ApplicationPaymentStatus != null && !ApplicationPaymentStatus.isEmpty() &&
                !Objects.equals(ApplicationPaymentStatus, "")
                && !Objects.equals(ApplicationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationPaymentStatus)= '").append(ApplicationPaymentStatus.toUpperCase()).append("' AND ");
        }
        if (AllocationPaymentStatus != null && !AllocationPaymentStatus.isEmpty() &&
                !Objects.equals(AllocationPaymentStatus, "")
                && !Objects.equals(AllocationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AllocationPaymentStatus)= '").append(AllocationPaymentStatus.toUpperCase()).append("' AND ");
        }
        if (SubType != null && !SubType.isEmpty() &&
                !Objects.equals(SubType, "")
                && !Objects.equals(SubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(SubType)= '").append(SubType.toUpperCase()).append("' AND ");
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
        List<StandardNumberModel> standardNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class));
        log.info("StandardNumberModel {} ", standardNumberModels);
        return standardNumberModels;
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param SubType
     * @param ApplicationType
     * @param CoverageArea
     * @param AreaCode
     * @param AccessCode
     * @param ApplicationStatus
     * @param ApplicationPaymentStatus
     * @param AllocationPaymentStatus
     * @param StartDate
     * @param EndDate
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<StandardNumberModel> filterAminStandardNo(String CompanyName, String ApplicationId, String SubType, String ApplicationType, String CoverageArea, String AreaCode, String AccessCode, String ApplicationStatus, String ApplicationPaymentStatus, String AllocationPaymentStatus, String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from StandardNumber WHERE ");
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
        if (CoverageArea != null && !CoverageArea.isEmpty() &&
                !Objects.equals(CoverageArea, "")
                && !Objects.equals(CoverageArea.toUpperCase(), "SELECT")) {
            sqlBuilder.append("CoverageArea = '").append(CoverageArea.toUpperCase()).append("' AND ");
        }
        if (AreaCode != null && !AreaCode.isEmpty() &&
                !Objects.equals(AreaCode, "")
                && !Objects.equals(AreaCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("AreaCode = '").append(AreaCode.toUpperCase()).append("' AND ");
        }
        if (AccessCode != null && !AccessCode.isEmpty() &&
                !Objects.equals(AccessCode, "")
                && !Objects.equals(AccessCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("AccessCode = '").append(AccessCode.toUpperCase()).append("' AND ");
        }
        if (ApplicationPaymentStatus != null && !ApplicationPaymentStatus.isEmpty() &&
                !Objects.equals(ApplicationPaymentStatus, "")
                && !Objects.equals(ApplicationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationPaymentStatus) = '").append(ApplicationPaymentStatus.toUpperCase()).append("' AND ");
        }
        if (AllocationPaymentStatus != null && !AllocationPaymentStatus.isEmpty() &&
                !Objects.equals(AllocationPaymentStatus, "")
                && !Objects.equals(AllocationPaymentStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AllocationPaymentStatus) = '").append(AllocationPaymentStatus.toUpperCase()).append("' AND ");
        }
        if (SubType != null && !SubType.isEmpty() &&
                !Objects.equals(SubType, "")
                && !Objects.equals(SubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(SubType) = '").append(SubType.toUpperCase()).append("' AND ");
        }
        if (StartDate != null && EndDate != null &&
                !StartDate.isEmpty() &&
                !EndDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + StartDate + "') AND DATE('" + EndDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        List<StandardNumberModel> standardNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class));
        log.info("StandardNumberModel {} ", standardNumberModels);
        return standardNumberModels;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public StandardNumberModel getStandardNoByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM StandardNumber WHERE ApplicationId = ?";
        try {
            StandardNumberModel standardNumberModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(StandardNumberModel.class), applicationId);
            log.info("StandardNumberModel {} ", standardNumberModel);
            return standardNumberModel;
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
    public List<StandardNumberModel> getAll() throws SQLException {
        String sql = "SELECT * from StandardNumber ORDER BY CreatedDate DESC";
        List<StandardNumberModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(StandardNumberModel.class));
        log.info("StandardNumberModel {} ", modelList);
        return modelList;
    }
}

