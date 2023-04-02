package com.molcom.nms.additionofservice.repository;

import com.molcom.nms.additionofservice.dto.AdditionOfServiceModel;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.selectedNumber.dto.SelectedNumberModel;
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
public class AdditionOfServiceRepo implements IAdditionOfServiceRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Datalayer to check if addition of service application already exist
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int doesExist(AdditionOfServiceModel model) throws SQLException {
        String sql = "SELECT count(*) FROM AdditionOfService WHERE ShortCode=? AND AdditionOfServicePurpose=? AND ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, model.getShortCode(), model.getAdditionOfServicePurpose(), model.getApplicationId());
    }

    /**
     * Data layer to save new addition of service application
     *
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int save(AdditionOfServiceModel model) throws SQLException {
        String sql = "INSERT INTO AdditionOfService (ApplicationId,ParentApplicationId, ShortCode, ServiceCategory, ApplicationStatus," +
                "CurrentPurpose, AdditionOfServicePurpose, AdditionOfServiceReason, CreatedBy, CreatedDate) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                model.getApplicationId(),
                model.getParentApplicationId(),
                model.getShortCode(),
                model.getServiceCategory(),
                model.getApplicationStatus(),
                model.getCurrentPurpose(),
                model.getAdditionOfServicePurpose(),
                model.getAdditionOfServiceReason(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to get short code allocated to a particular company
     *
     * @param companyName
     * @return
     * @throws SQLException
     */
    @Override
    public List<SelectedNumberModel> getAllocatedNos(String companyName) throws SQLException {
        String sql = "SELECT * FROM SelectedNumbers WHERE UPPER(NumberType) = 'SHORT-CODE' AND UPPER(AllocationStatus)='ALLOCATED' AND CompanyAllocatedTo=?";
        List<SelectedNumberModel> selectedNumberModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SelectedNumberModel.class), companyName);
        log.info("SelectedNumberModel {} ", selectedNumberModels);
        return selectedNumberModels;
    }

    /**
     * Data layer to delete addition of service application by id
     *
     * @param additionOfServiceId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int additionOfServiceId) throws SQLException {
        String sql = "DELETE FROM AdditionOfService WHERE AdditionOfServiceId = ?";
        return jdbcTemplate.update(sql, additionOfServiceId);
    }


    /**
     * Data layer to find get addition of service application by id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public AdditionOfServiceModel findById(String applicationId) throws SQLException {
        String sql = "SELECT * FROM AdditionOfService WHERE AdditionOfServiceId = ?";
        try {
            AdditionOfServiceModel serviceModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(AdditionOfServiceModel.class), applicationId);
            log.info("Addition of service {} ", serviceModel);
            return serviceModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }

    }

    /**
     * Data layer to get all addition of service application
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<AdditionOfServiceModel> getAll() throws SQLException {
        String sql = "SELECT * from AdditionOfService";
        List<AdditionOfServiceModel> serviceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdditionOfServiceModel.class));
        log.info("Addition of service {} ", serviceModels);
        return serviceModels;
    }

    @Override
    public List<AdditionOfServiceModel> getAllByOrganisation(String organisation) throws SQLException {
        String org = (organisation != null ? organisation.trim().toUpperCase() : "");
        String sql = "SELECT * from AdditionOfService WHERE CreatedBy=?";
        List<AdditionOfServiceModel> serviceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdditionOfServiceModel.class), org);
        log.info("Addition of service {} ", serviceModels);
        return serviceModels;
    }


    /**
     * Data layer to filter addition of service application
     *
     * @param queryParam1
     * @param queryValue1
     * @param queryParam2
     * @param queryValue2
     * @param queryValue3
     * @param queryValue4
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<AdditionOfServiceModel> filter(String queryParam1, String queryValue1, String queryParam2,
                                               String queryValue2,
                                               String queryValue3,
                                               String queryValue4, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from AdditionOfService WHERE ");

        if (queryValue1 != null
                && !queryValue1.isEmpty()
                && !Objects.equals(queryValue1, "")
                && !queryValue1.equalsIgnoreCase("select")
                && !queryValue1.equalsIgnoreCase("undefined")) {
            sqlBuilder.append("UPPER(" + queryParam1 + ") = '").append("" + queryValue1.toUpperCase() + "").append("' AND ");
        }

        if (queryValue2 != null && !queryValue2.isEmpty()
                && !Objects.equals(queryValue2, "")
                && !queryValue2.equalsIgnoreCase("select")
                && !queryValue2.equalsIgnoreCase("undefined")) {
            sqlBuilder.append("UPPER(" + queryParam2 + ") = '").append("" + queryValue2.toUpperCase() + "").append("' AND ");
        }

        if (queryValue3 != null && queryValue4 != null &&
                !queryValue3.isEmpty() &&
                !queryValue4.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + queryValue3 + "') AND DATE('" + queryValue4 + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("SQL {} ", sql);

        List<AdditionOfServiceModel> serviceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdditionOfServiceModel.class));
        log.info("Addition of service {} ", serviceModels);
        return serviceModels;
    }

    @Override
    public List<AdditionOfServiceModel> filterForRegularUser(String companyName, String queryParam1, String queryValue1, String queryParam2, String queryValue2, String queryValue3, String queryValue4, String rowNumber) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from AdditionOfService WHERE ");

        if (queryValue1 != null
                && !queryValue1.isEmpty()
                && !Objects.equals(queryValue1, "")
                && !queryValue1.equalsIgnoreCase("select")
                && !queryValue1.equalsIgnoreCase("undefined")) {
            sqlBuilder.append("UPPER(" + queryParam1 + ") = '").append("" + queryValue1.toUpperCase() + "").append("' AND ");
        }

        if (queryValue2 != null && !queryValue2.isEmpty()
                && !Objects.equals(queryValue2, "")
                && !queryValue2.equalsIgnoreCase("select")
                && !queryValue2.equalsIgnoreCase("undefined")) {
            sqlBuilder.append("UPPER(" + queryParam2 + ") = '").append("" + queryValue2.toUpperCase() + "").append("' AND ");
        }

        if (queryValue3 != null && queryValue4 != null &&
                !queryValue3.isEmpty() &&
                !queryValue4.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + queryValue3 + "') AND DATE('" + queryValue4 + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL AND UPPER(CreatedBy)='");
        sqlBuilder.append("" + companyName.toUpperCase() + "").append("' ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();

        List<AdditionOfServiceModel> serviceModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(AdditionOfServiceModel.class));
        log.info("Addition of service {} ", serviceModels);
        return serviceModels;
    }

}
