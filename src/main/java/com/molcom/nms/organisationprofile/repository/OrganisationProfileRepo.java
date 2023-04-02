package com.molcom.nms.organisationprofile.repository;

import com.molcom.nms.organisationprofile.dto.*;
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
public class OrganisationProfileRepo implements IOrganisationProfileRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationRepresentatives> getOrganisationReps(String organisationName) throws SQLException {
        String sql = "SELECT FirstName, LastName, Email, Designation  FROM CompanyRep WHERE UPPER(Organisation)=?";
        List<OrganisationRepresentatives> repsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(OrganisationRepresentatives.class), organisationName);
        log.info("OrganisationRepresentatives {} ", repsList);
        return repsList;
    }

    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationUsers> getOrganisationUsers(String organisationName) throws SQLException {
        String sql = "SELECT Name, Email FROM AdminManagement WHERE UPPER(Organisation)=?";
        List<OrganisationUsers> userList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(OrganisationUsers.class), organisationName);
        log.info("OrganisationUsers {} ", userList);
        return userList;
    }

    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationApplications> getOrganisationApplications(String organisationName) throws SQLException {
        String sql = "SELECT * FROM (SELECT 'ShortCode' as NumberType, 'ShortCode' as SubType, ApplicationId, ApplicationStatus, '1' as Quantity from ShortCodeNumbers WHERE UPPER(CompanyName)=?) ShortCode UNION " +
                "SELECT * FROM (SELECT 'Standard' as NumberType, SubType, ApplicationId, ApplicationStatus, '1000000'as Quantity from  StandardNumber WHERE UPPER(CompanyName)=?) Standard UNION " +
                "SELECT * FROM (SELECT 'Ispc' as NumberType, 'Ispc' as SubType, ApplicationId, ApplicationStatus, '1'as Quantity from IspcNumbers WHERE UPPER(CompanyName)=?) Ispc UNION " +
                "SELECT * FROM (SELECT 'Special' as NumberType, SubType, ApplicationId, ApplicationStatus, '10000000'as Quantity  from SpecialNumbers WHERE UPPER(CompanyName)=?) Special " +
                "ORDER BY ApplicationId DESC";
        List<OrganisationApplications> applicationsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(OrganisationApplications.class), organisationName, organisationName, organisationName, organisationName);
        log.info("OrganisationApplications {} ", applicationsList);
        return applicationsList;
    }

    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationAllocatedNos> getOrganisationAllocatedNos(String organisationName) throws SQLException {
        String sql = "SELECT * FROM SelectedNumbers WHERE CompanyAllocatedTo=? ";
        List<OrganisationAllocatedNos> allocatedList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(OrganisationAllocatedNos.class), organisationName);
        log.info("OrganisationAllocatedNos {} ", allocatedList);
        return allocatedList;
    }

    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public int countIfOrganisationExist(String organisationName) throws SQLException {
        String sql = "SELECT count(*) FROM OrganisationProfile WHERE OrganisationName=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, organisationName);
    }

    /**
     * @param model
     * @return
     * @throws SQLException
     */
    @Override
    public int saveOrganisationOnFistLogin(OrganisationProfileModel model) throws SQLException {
        String sql = "INSERT INTO OrganisationProfile (OrganisationName, NccId) " +
                "VALUES(?,?)";
        return jdbcTemplate.update(sql,
                model.getOrganisationName(),
                model.getNccId()
        );
    }

    /**
     * @param organisationProfileId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteById(int organisationProfileId) throws SQLException {
        String sql = "DELETE FROM OrganisationProfile WHERE OrganisationProfileId=?";
        return jdbcTemplate.update(sql, organisationProfileId);
    }

    /**
     * @param organisationName
     * @return
     * @throws SQLException
     */
    @Override
    public OrganisationProfileModel findByName(String organisationName) throws SQLException {
        String org = (organisationName != null ? organisationName.trim().toUpperCase() : "");
        String sql = "SELECT * FROM OrganisationProfile WHERE UPPER(OrganisationName)=?";
        try {
            OrganisationProfileModel organisationProfileModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(OrganisationProfileModel.class), org);
            log.info("OrganisationProfileModel {} ", organisationProfileModel);
            return organisationProfileModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    /**
     * @param organisationProfileId
     * @return
     * @throws SQLException
     */
    @Override
    public OrganisationProfileModel getByOrganisationProileId(int organisationProfileId) throws SQLException {
        String sql = "SELECT * FROM OrganisationProfile WHERE OrganisationProfileId = ?";
        try {
            OrganisationProfileModel organisationProfileModel = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(OrganisationProfileModel.class), organisationProfileId);
            log.info("OrganisationProfileModel {} ", organisationProfileModel);
            return organisationProfileModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred while number getting renewal details by billing id ");
            return null;
        }

    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationProfileModel> getAll() throws SQLException {
        String sql = "SELECT * from OrganisationProfile ORDER BY OrganisationName ASC";
        List<OrganisationProfileModel> organisationProfileModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(OrganisationProfileModel.class));
        log.info("OrganisationProfileModel {} ", organisationProfileModels);
        return organisationProfileModels;
    }

    /**
     * @param queryParam1
     * @param queryValue1
     * @param rowNumber
     * @return
     * @throws SQLException
     */
    @Override
    public List<OrganisationProfileModel> filter(String queryParam1, String queryValue1, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        if (queryValue1 != null
                && !queryValue1.isEmpty()
                && !Objects.equals(queryValue1, "")
                && !Objects.equals(queryValue1.toUpperCase(), "SELECT")
                && !Objects.equals(queryValue1.toUpperCase(), "UNDEFINED")) {
            sqlBuilder.append("SELECT * from OrganisationProfile WHERE UPPER(OrganisationName) LIKE");
            sqlBuilder.append("'%").append(queryValue1).append("%' ");
            sqlBuilder.append("ORDER BY OrganisationName ASC LIMIT ").append(rowNumber);
        } else {
            sqlBuilder.append("SELECT * from OrganisationProfile ORDER BY OrganisationName ASC LIMIT ").append(rowNumber);
        }

        String sql = sqlBuilder.toString();
        log.info("SQL query {} ", sql);
        List<OrganisationProfileModel> organisationProfileModels = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(OrganisationProfileModel.class));
        log.info("OrganisationProfileModel {} ", organisationProfileModels);
        return organisationProfileModels;
    }

    @Override
    public List<OrganisationProfileModel> findByNameList(String organisationName) throws SQLException {
        String org = (organisationName != null ? organisationName.toUpperCase() : "");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from OrganisationProfile WHERE UPPER(OrganisationName) LIKE");

        if (organisationName != null && !organisationName.isEmpty() &&
                !Objects.equals(organisationName, "")
                && !Objects.equals(organisationName.toUpperCase(), "SELECT")) {
            sqlBuilder.append("'%").append(org).append("%'");
        }
        String sql = sqlBuilder.toString();
        log.info("sql {} ", sql);
        try {
            List<OrganisationProfileModel> organisationProfileModel = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(OrganisationProfileModel.class));
            log.info("List {} ", organisationProfileModel);
            return organisationProfileModel;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! {} ", e.getMessage());
            return null;
        }
    }
}
