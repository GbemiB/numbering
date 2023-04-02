package com.molcom.nms.number.application.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.application.dto.SpecialNumberModel;
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
public class SpecialNumberRepository implements ISpecialNumberRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save first step in special number creation
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveSpecialNoAppFistStep(SpecialNumberModel model) throws SQLException {
        log.info("Inside special number {} ", model);
        try {
            String sql1 = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";

            List<SpecialNumberModel> specialNumberModels = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), model.getApplicationId());
            log.info("SpecialNumberModel {} ", specialNumberModels);
            log.info("Number of applications {} ", specialNumberModels.size());

            if (specialNumberModels.size() > 0) {
                String sql2 = "UPDATE SpecialNumbers SET ApplicationType =?, ApplicationStatus =?," +
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
                String sql = "INSERT INTO SpecialNumbers (ApplicationId, ApplicationType, ApplicationStatus, SubType, " +
                        "CompanyName, CompanyEmail, CompanyPhone, CompanyFax, " +
                        "CompanyState, CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                        "CorrespondingState, CompanyRepresentativeIdOne, CompanyRepresentativeIdTwo, CorrespondingAddress, " +
                        "InterconnectAgreement, CreatedBy, CreatedUserEmail,IpAddress, CreatedDate, CurrentStep, IsMDA ) " +
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
            log.info("Exception occurred in first step save of special number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * Data layer to save second step in special number creation
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveSpecialNoAppSecondStep(SpecialNumberModel model) throws SQLException {
        String sql = "UPDATE SpecialNumbers SET AccessCode=?, AvailableNumber=?, NoOfTelcoCompanies=?, " +
                "TelcoCompanies=?, HaveYouReachedAgreement=?, HaveYouMetFinancialReq=?, " +
                "FrequentAssignment=?, Quantity=?, CurrentStep=? WHERE ApplicationId= ? ";
        return jdbcTemplate.update(sql,
                model.getAccessCode(),
                model.getAvailableNumber(),
                model.getNoOfTelcoCompanies(),
                model.getTelcoCompanies(),
                model.getHaveYouReachedAgreement(),
                model.getHaveYouMetFinancialReq(),
                model.getFrequentAssignment(),
                model.getQuantity(),
                "2",
                model.getApplicationId()
        );
    }

    /**
     * Data layer to save third step in special number creation
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveSpecialNoAppThirdStep(SpecialNumberModel model) throws SQLException {
        String sql = "UPDATE SpecialNumbers SET UndertakenComment = ?, ApplicationPaymentStatus = ?, " +
                "AllocationPaymentStatus =?, CurrentStep=? WHERE ApplicationId= ?";

        return jdbcTemplate.update(sql,
                model.getUndertakenComment(),
                "Awaiting Payment",
                "Not Paid",
                "4",
                model.getApplicationId()
        );
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SpecialNumberModel> getSpecialNoFistStep(String applicationId) throws SQLException {
        String sql = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyFax, CompanyState," +
                "CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                "CorrespondingState, CorrespondingAddress, InterconnectAgreement, CurrentStep, IsMDA " +
                "FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);
        log.info("SpecialNumberModel {} ", models);
        return models;
    }


    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SpecialNumberModel> getSpecialNoSecondStep(String applicationId) throws SQLException {
        String sql = "SELECT AccessCode, AvailableNumber, NoOfTelcoCompanies, " +
                "TelcoCompanies, HaveYouReachedAgreement, HaveYouMetFinancialReq, FrequentAssignment, IsMDA FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);
        log.info("SpecialNumberModel {} ", models);
        return models;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<SpecialNumberModel> getSpecialNoFourthStep(String applicationId) throws SQLException {
        String sql = "SELECT UndertakenComment, CurrentStep, IsMDA FROM SpecialNumbers WHERE ApplicationId = ?";
        List<SpecialNumberModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);
        log.info("SpecialNumberModel {} ", models);
        return models;
    }

    /**
     * @param currentStep
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int saveDocFourthStep(String currentStep, String applicationId) throws SQLException {
        String sql = "UPDATE SpecialNumbers SET CurrentStep=? WHERE ApplicationId= ?";

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
    public int deleteApplication(String applicationId) throws SQLException {
        String sql = "DELETE FROM SpecialNumbers WHERE ApplicationId = ?";
        return jdbcTemplate.update(sql, applicationId);
    }

    /**
     * Data layer to filter special numbers
     * Filters are: ApplicationId, SubType,ApplicationType, AccessCode, ApplicationStatus,
     * ApplicationPaymentStatus, AllocationPaymentStatus
     */

    /**
     * @param ApplicationId
     * @param SubType
     * @param ApplicationType
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
    public List<SpecialNumberModel> filterSpecialNo(String companyName,
                                                    String ApplicationId,
                                                    String SubType,
                                                    String ApplicationType,
                                                    String AccessCode,
                                                    String ApplicationStatus,
                                                    String ApplicationPaymentStatus,
                                                    String AllocationPaymentStatus,
                                                    String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from SpecialNumbers WHERE ");
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
        if (AccessCode != null && !AccessCode.isEmpty() &&
                !Objects.equals(AccessCode, "")
                && !Objects.equals(AccessCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("AccessCode = '").append(AccessCode.toUpperCase()).append("' AND ");
        }
        if (ApplicationStatus != null && !ApplicationStatus.isEmpty() &&
                !Objects.equals(ApplicationStatus, "")
                && !Objects.equals(ApplicationStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationStatus) = '").append(ApplicationStatus.toUpperCase()).append("' AND ");
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

//        sqlBuilder.append("CreatedDate IS NOT NULL ");
//        sqlBuilder.append("ORDER BY CreatedDate  DESC LIMIT ").append(rowNumber);
//        String sql = sqlBuilder.toString();

        sqlBuilder.append("CreatedDate IS NOT NULL AND UPPER(CompanyName)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<SpecialNumberModel> specialNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class));
        log.info("SpecialNumberModel {} ", specialNumberModels);
        return specialNumberModels;
    }

    /**
     * Data layer to get special number by application Id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public SpecialNumberModel getSpecialNoByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SpecialNumbers WHERE ApplicationId = ?";
        try {
            SpecialNumberModel specialNumberModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(SpecialNumberModel.class), applicationId);
            log.info("SpecialNumberModel {} ", specialNumberModel);
            return specialNumberModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param SubType
     * @param ApplicationType
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
    public List<SpecialNumberModel> filterAdminSpecialNo(String CompanyName,
                                                         String ApplicationId,
                                                         String SubType,
                                                         String ApplicationType,
                                                         String AccessCode,
                                                         String ApplicationStatus,
                                                         String ApplicationPaymentStatus,
                                                         String AllocationPaymentStatus,
                                                         String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from SpecialNumbers WHERE ");
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

        sqlBuilder.append("CreatedDate != NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<SpecialNumberModel> specialNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class));
        log.info("SpecialNumberModel {} ", specialNumberModels);
        return specialNumberModels;
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<SpecialNumberModel> getAll() throws SQLException {
        String sql = "SELECT * from SpecialNumbers ORDER BY CreatedDate DESC";
        List<SpecialNumberModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SpecialNumberModel.class));
        log.info("SpecialNumberModel {} ", modelList);
        return modelList;
    }
}
