package com.molcom.nms.companyrep.repository;

import com.molcom.nms.companyrep.dto.CompRepModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class CompRepRepository implements ICompRepRepository {
    private final JdbcTemplate jdbcTemplate;

    public CompRepRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Data layer to save new company representative
     *
     * @param model Company representative detail
     * @return 1 for successful save
     * @throws SQLException
     */
    @Override
    public int save(CompRepModel model) throws SQLException {

        String sql = "INSERT INTO CompanyRep (Title, FirstName, LastName, Email, PhoneNumber, " +
                "Designation, Passport, Signature, CreatedDate, UserId, IpAddress, Organisation) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getTitle(),
                model.getFirstname(),
                model.getLastname(),
                model.getEmail(),
                model.getPhoneNumber(),
                model.getDesignation(),
                model.getPassport(),
                model.getSignature(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getUserId(),
                model.getIpAddress(),
                model.getOrganisation()
        );
    }

    @Override
    public int saveSelectedCompRep(CompRepModel model) throws SQLException {
        try {
            String sql1 = "SELECT * FROM SelectedCompanyRep WHERE ApplicationId =? AND LastName =?";

            List<CompRepModel> compRepModelList = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(CompRepModel.class), model.getApplicationId(), model.getLastname());
//            log.info("CompRepModel {} ", compRepModelList);
//            log.info("Number of company representative with name {} are : {} ", model.getLastname(), compRepModelList.size());

            if (compRepModelList.size() > 0) {
                String sql2 = "UPDATE SelectedCompanyRep SET Title =?, FirstName =?," +
                        "LastName =?, Email =?, PhoneNumber =?, Designation =?, Passport =?, " +
                        "Signature =?, CreatedDate =?, UserId =?, IpAddress =?";
                return jdbcTemplate.update(sql2,
                        model.getTitle(),
                        model.getFirstname(),
                        model.getLastname(),
                        model.getEmail(),
                        model.getPhoneNumber(),
                        model.getDesignation(),
                        model.getPassport(),
                        model.getSignature(),
                        CurrentTimeStamp.getCurrentTimeStamp(),
                        model.getUserId(),
                        model.getIpAddress());
            } else {
                String sql2 = "INSERT INTO SelectedCompanyRep (ApplicationId, Title, FirstName, LastName, Email, PhoneNumber, " +
                        "Designation, Passport, Signature, CreatedDate, UserId, IpAddress, Organisation) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                return jdbcTemplate.update(sql2,
                        model.getApplicationId(),
                        model.getTitle(),
                        model.getFirstname(),
                        model.getLastname(),
                        model.getEmail(),
                        model.getPhoneNumber(),
                        model.getDesignation(),
                        model.getPassport(),
                        model.getSignature(),
                        CurrentTimeStamp.getCurrentTimeStamp(),
                        model.getUserId(),
                        model.getIpAddress(),
                        model.getOrganisation()
                );
            }
        } catch (Exception exe) {
            log.info("Exception occurred in first step save of ispc number {}", exe.getMessage());
            return 0;
        }
    }

    /**
     * Data layer to edit existing company representative
     *
     * @param model     Company representative detail
     * @param compRepId Unique field of company representative
     * @return 1 for successful edit
     * @throws SQLException
     */
    @Override
    public int edit(CompRepModel model, int compRepId) throws SQLException {

        String sql = "UPDATE CompanyRep set Title = ?, FirstName  = ?, LastName  = ?, Email  = ?, PhoneNumber  = ?, " +
                "Designation  = ?, Passport  = ?, Signature  = ?, ModifiedDate  = ?" +
                ", UserId  = ?, IpAddress  = ?, UpdatedBy = ? WHERE CompanyRepId = ?";

        return jdbcTemplate.update(sql,
                model.getTitle(),
                model.getFirstname(),
                model.getLastname(),
                model.getEmail(),
                model.getPhoneNumber(),
                model.getDesignation(),
                model.getPassport(),
                model.getSignature(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                model.getUserId(),
                model.getIpAddress(),
                model.getUpdatedBy(),
                compRepId
        );
    }

    /**
     * Data layer to delete existing company representative
     *
     * @param compRepId Unique field of company representative
     * @return 1 for successful delete
     * @throws SQLException
     */
    @Override
    public int deleteById(int compRepId) throws SQLException {
        String sql = "DELETE FROM CompanyRep WHERE CompanyRepId = ?";
        return jdbcTemplate.update(sql, compRepId);
    }

    /**
     * @param selectedCompReId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteSelectedCompRep(int selectedCompReId) throws SQLException {
        String sql = "DELETE FROM SelectedCompanyRep WHERE SelectedCompanyRepId = ?";
        return jdbcTemplate.update(sql, selectedCompReId);
    }

    /**
     * Data layer to find by existing company representative
     *
     * @param compRepId Unique field of company representative
     * @return CompRepModel
     * @throws SQLException
     */
    @Override
    public CompRepModel findByCompRepId(int compRepId) throws SQLException {
        String sql = "SELECT * FROM CompanyRep WHERE CompanyRepId = ?";
        try {
            CompRepModel compRepModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(CompRepModel.class), compRepId);
            return compRepModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while getting company representative by id");
            return null;
        }
    }

    @Override
    public List<CompRepModel> getAll() throws SQLException {
        String sql = "SELECT * FROM CompanyRep ORDER BY CreatedDate DESC";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CompRepModel.class));
    }

    /**
     * Data layer to find existing company representatives by user id
     *
     * @param userId Unique field of a user
     * @return Result set of List<CompRepModel>
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> findByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM CompanyRep WHERE UserId = ? ORDER BY CreatedDate DESC";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CompRepModel.class), userId);
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> findSelectedCompRep(String applicationId) throws SQLException {
        String sql = "SELECT * FROM SelectedCompanyRep WHERE ApplicationId = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CompRepModel.class), applicationId);
    }

    /**
     * @param userId
     * @return
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> findByUserIdWithImage(String userId) throws SQLException {
        String sql = "SELECT Title, FirstName, LastName FROM CompanyRep WHERE UserId = ? ORDER BY CreatedDate DESC";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CompRepModel.class), userId);
    }

    /**
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> findSelectedCompRepWithoutImage(String applicationId) throws SQLException {
        String sql = "SELECT Title, FirstName, LastName FROM SelectedCompanyRep WHERE ApplicationId = ? ORDER BY CreatedDate DESC";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CompRepModel.class), applicationId);
    }

    /**
     * Data layer to filterForRegularUser For regular user existing company representatives.
     *
     * @param queryParam1 Represent query param which can be FirstName or LastName
     * @param queryValue1 Represent query value
     * @param queryParam2
     * @param queryValue2
     * @param rowNumber   Count of data per query
     * @return Result set of List<CompRepModel>
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> filterForRegularUser(String queryParam1, String queryValue1,
                                                   String queryParam2, String queryValue2,
                                                   String userId, String rowNumber) throws SQLException {
        String user = (userId != null ? userId.toUpperCase() : "");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CompanyRep WHERE UPPER(UserId) ");
        sqlBuilder.append("= '" + user + "' AND ");

        if (queryValue1 != null && !queryValue1.isEmpty() &&
                !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
        }

        if (queryValue2 != null && !queryValue2.isEmpty() &&
                !Objects.equals(queryValue2, "")
                && !Objects.equals(queryValue2.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("sql {} ", sql);


        List<CompRepModel> compRepModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CompRepModel.class));
        log.info("Company Reps {} ", compRepModels);
        return compRepModels;
    }

    /**
     * Data layer to filterForRegularUser For admin user existing company representatives.
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryParam3
     * @param queryValue3
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<CompRepModel> filterForAdminUser(String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryParam3, String queryValue3, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CompanyRep WHERE ");

        if (queryValue1 != null && !queryValue1.isEmpty() &&
                !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam1 + " = '").append("" + queryValue1 + "").append("' AND ");
        }

        if (queryValue2 != null && !queryValue2.isEmpty() &&
                !Objects.equals(queryValue2, "")
                && !Objects.equals(queryValue2.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam2 + " = '").append("" + queryValue2 + "").append("' AND ");
        }

        if (queryValue3 != null && !queryValue3.isEmpty() &&
                !Objects.equals(queryValue3, "")
                && !Objects.equals(queryValue3.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("" + queryParam3 + " = '").append("" + queryValue3 + "").append("' AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("sql {} ", sql);


        List<CompRepModel> compRepModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CompRepModel.class));
        return compRepModels;
    }
}