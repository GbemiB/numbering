package com.molcom.nms.number.application.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.application.dto.ShortCodeModel;
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
public class ShortCodeRepository implements IShortCodeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Data layer first step short code application
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveShortCodeAppFistStep(ShortCodeModel model) throws SQLException {
        try {
            String sql1 = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId = ?";

            List<ShortCodeModel> shortCodeModels = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(ShortCodeModel.class), model.getApplicationId());
            log.info("ShortCodeModel {} ", shortCodeModels);
            log.info("Number of applications {} ", shortCodeModels.size());
            log.info("Short Code {} ", model.getIsMDA());

            if (shortCodeModels.size() > 0) {
                String sql2 = "UPDATE ShortCodeNumbers SET ApplicationType =?, ApplicationStatus =?," +
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
                String sql = "INSERT INTO ShortCodeNumbers (ApplicationId, ApplicationType, ApplicationStatus, " +
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
            log.info("Exception occurred in first step save of short code number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * Data layer first second short code application
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveShortCodeAppSecondStep(ShortCodeModel model) throws SQLException {
        String sql = "UPDATE ShortCodeNumbers SET ShortCodeCategory=?, ShortCodeService=?,  " +
                "AvailableNumber=?, Quantity=?,  CurrentStep=? WHERE ApplicationId= ? ";
        return jdbcTemplate.update(sql,
                model.getShortCodeCategory(),
                model.getShortCodeService(),
                model.getAvailableNumber(),
                model.getQuantity(),
                "2",
                model.getApplicationId()
        );
    }

    /**
     * Data layer third second short code application
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveShortCodeAppThirdStep(ShortCodeModel model) throws SQLException {
        String sql = "UPDATE ShortCodeNumbers SET UndertakenComment = ?, ApplicationPaymentStatus = ?, " +
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
     * @param currentStep
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int saveDocFourthStep(String currentStep, String applicationId) throws SQLException {
        String sql = "UPDATE ShortCodeNumbers SET CurrentStep=? WHERE ApplicationId= ?";

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
    public List<ShortCodeModel> getShortNoFistStep(String applicationId) throws SQLException {
        String sql = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyFax, CompanyState," +
                "CompanyAddress, CorrespondingEmail, CorrespondingPhone, CorrespondingFax," +
                "CorrespondingState, CorrespondingAddress, InterconnectAgreement, CurrentStep, IsMDA " +
                "FROM ShortCodeNumbers WHERE ApplicationId=?";
        List<ShortCodeModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);
        log.info("ShortCodeModel {} ", models);
        return models;
    }


    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ShortCodeModel> getShortNoSecondStep(String applicationId) throws SQLException {
        String sql = "SELECT ShortCodeCategory, ShortCodeService, ShortCodeCategory, " +
                "CurrentStep, IsMDA FROM ShortCodeNumbers WHERE ApplicationId=?";
        List<ShortCodeModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);
        log.info("ShortCodeModel {} ", models);
        return models;
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<ShortCodeModel> getShortNoFourthStep(String applicationId) throws SQLException {
        String sql = "SELECT UndertakenComment, CurrentStep, IsMDA FROM ShortCodeNumbers WHERE ApplicationId=?";
        List<ShortCodeModel> models = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);
        log.info("ShortCodeModel {} ", models);
        return models;
    }

    /**
     * Data layer to delete existing coverage area
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteApplication(String applicationId) throws SQLException {
        String sql = "DELETE FROM ShortCodeNumbers WHERE ApplicationId=?";
        return jdbcTemplate.update(sql, applicationId);

    }

    /**
     * Data layer to filter short codes
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<ShortCodeModel> filterShortCodes(String companyName,
                                                 String ApplicationId,
                                                 String ShortCodeCategory,
                                                 String ShortCodeService,
                                                 String ApplicationType,
                                                 String ApplicationStatus,
                                                 String ApplicationPaymentStatus,
                                                 String AllocationPaymentStatus,
                                                 String StartDate,
                                                 String EndDate,
                                                 String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ShortCodeNumbers WHERE ");
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
        if (ApplicationStatus != null && !ApplicationStatus.isEmpty() &&
                !Objects.equals(ApplicationStatus, "")
                && !Objects.equals(ApplicationStatus.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ApplicationStatus) = '").append(ApplicationStatus.toUpperCase()).append("' AND ");
        }
        if (ShortCodeCategory != null && !ShortCodeCategory.isEmpty() &&
                !Objects.equals(ShortCodeCategory, "")
                && !Objects.equals(ShortCodeCategory.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeCategory) = '").append(ShortCodeCategory.toUpperCase()).append("' AND ");
        }
        if (ShortCodeService != null && !ShortCodeService.isEmpty() &&
                !Objects.equals(ShortCodeService, "")
                && !Objects.equals(ShortCodeService.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeService)= '").append(ShortCodeService.toUpperCase()).append("' AND ");
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
        if (StartDate != null && EndDate != null &&
                !StartDate.isEmpty() &&
                !EndDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + StartDate + "') AND DATE('" + EndDate + "') AND ");
        }


        sqlBuilder.append("CreatedDate IS NOT NULL AND UPPER(CompanyName)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

//        sqlBuilder.append("CreatedDate IS NOT NULL ");
//        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
//        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<ShortCodeModel> shortCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class));
        log.info("ShortCodeModel {} ", shortCodeModels);
        return shortCodeModels;
    }

    /**
     * @param CompanyName
     * @param ApplicationId
     * @param ShortCodeCategory
     * @param ShortCodeService
     * @param ApplicationType
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
    public List<ShortCodeModel> filterAdminShortCodes(String CompanyName,
                                                      String ApplicationId,
                                                      String ShortCodeCategory,
                                                      String ShortCodeService,
                                                      String ApplicationType,
                                                      String ApplicationStatus,
                                                      String ApplicationPaymentStatus,
                                                      String AllocationPaymentStatus,
                                                      String StartDate, String EndDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from ShortCodeNumbers WHERE ");
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
        if (ShortCodeCategory != null && !ShortCodeCategory.isEmpty() &&
                !Objects.equals(ShortCodeCategory, "")
                && !Objects.equals(ShortCodeCategory.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeCategory) = '").append(ShortCodeCategory.toUpperCase()).append("' AND ");
        }
        if (ShortCodeService != null && !ShortCodeService.isEmpty() &&
                !Objects.equals(ShortCodeService, "")
                && !Objects.equals(ShortCodeService.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(ShortCodeService) = '").append(ShortCodeService.toUpperCase()).append("' AND ");
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
        if (StartDate != null && EndDate != null &&
                !StartDate.isEmpty() &&
                !EndDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + StartDate + "') AND DATE('" + EndDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        log.info("SQL query {} ", sql);
        List<ShortCodeModel> shortCodeModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class));
        log.info("ShortCodeModel {} ", shortCodeModels);
        return shortCodeModels;
    }

    /**
     * Data layer to get short codes by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public ShortCodeModel getShortCodeByApplId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM ShortCodeNumbers WHERE ApplicationId=?";
        try {
            ShortCodeModel shortCodeModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(ShortCodeModel.class), applicationId);
            log.info("ShortCodeModel {} ", shortCodeModel);
            return shortCodeModel;
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
    public List<ShortCodeModel> getAll() throws SQLException {
        String sql = "SELECT * from ShortCodeNumbers ORDER BY CreatedDate DESC";
        List<ShortCodeModel> modelList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(ShortCodeModel.class));
        log.info("ShortCodeModel {} ", modelList);
        return modelList;
    }


}
