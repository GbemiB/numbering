package com.molcom.nms.allocationLetter.repository;

import com.molcom.nms.allocationLetter.dto.AllocationLetter;
import com.molcom.nms.allocationLetter.dto.AllocationLetterModel;
import com.molcom.nms.allocationLetter.dto.ApplicationInfo;
import com.molcom.nms.general.utils.CurrentTimeStamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class AllocationLetterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Count if allocation letter has been queried
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public int allocationLetterCount(String applicationId) throws SQLException {
        String sql = "SELECT count(*) FROM AllocationLetter WHERE ApplicationId=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, applicationId);
    }


    /**
     * Data layer to get details for allocation letter
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public List<AllocationLetterModel> getDetailsForAllocationLetter(String applicationId) throws SQLException {
        String sql2 = "SELECT SelectedNumberId as Id, NumberType, NumberSubType, SelectedNumberValue, DateAllocated as AllocationDate, Purpose, BearerMedium, AllocationValidityTo as ExpiryDate FROM SelectedNumbers WHERE ApplicationId=?";
        List<AllocationLetterModel> allocationLetterModels = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(AllocationLetterModel.class), applicationId);
        return allocationLetterModels;
    }

    /**
     * Data layer to get allocation letter
     *
     * @param model
     * @return
     * @throws SQLException
     */
    public int saveAllocationLetter(AllocationLetter model) throws SQLException {
        try {
            String sql1 = "SELECT * FROM AllocationLetter WHERE ApplicationId = ?";

            List<AllocationLetter> allocationLetters = jdbcTemplate.query(sql1,
                    BeanPropertyRowMapper.newInstance(AllocationLetter.class), model.getApplicationId());
            log.info("Number of applications {} ", allocationLetters.size());

            if (allocationLetters.size() > 0) {
                String sql2 = "UPDATE AllocationLetter SET AllocationLetterLink=?, GeneratedDate =? WHERE ApplicationId=? ";
                return jdbcTemplate.update(sql2,
                        model.getAllocationLetterLink(),
                        CurrentTimeStamp.getCurrentTimeStamp(),
                        model.getApplicationId());
            } else {
                String sql = "INSERT INTO AllocationLetter (ApplicationId, AllocationLetterLink, GeneratedDate) " +
                        "VALUES(?,?,?)";
                return jdbcTemplate.update(sql,
                        model.getApplicationId(),
                        model.getAllocationLetterLink(),
                        CurrentTimeStamp.getCurrentTimeStamp()
                );
            }
        } catch (Exception exe) {
            log.info("Exception occurred !!!! {}", exe.getMessage());
            return 0;
        }
    }


    /**
     * Data layer to get application per application Id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public AllocationLetter findByApplicationId(String applicationId) throws SQLException {
        String sql = "SELECT * FROM AllocationLetter WHERE ApplicationId = ?";
        try {
            AllocationLetter allocationLetter = jdbcTemplate.queryForObject(sql,
                    BeanPropertyRowMapper.newInstance(AllocationLetter.class), applicationId);
            log.info("AllocationLetter {} ", allocationLetter);
            return allocationLetter;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }


    /**
     * Data layer to get application info
     *
     * @param numberType
     * @param applicationId
     * @return
     * @throws SQLException
     */
    public ApplicationInfo getApplicationInfo(String numberType, String applicationId) throws SQLException {
        List<ApplicationInfo> covAreaAccessList = new ArrayList<>();
        ApplicationInfo covAreaAccess = new ApplicationInfo();

        log.info("Number type {}", numberType.toUpperCase());
        log.info("Application Id {}", applicationId);

        String numType = (numberType != null ? numberType.toUpperCase() : "");

        if (Objects.equals(numType, "STANDARD")) {
            String sqlStd = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyState, CompanyFax, CoverageArea, AreaCode, AccessCode FROM StandardNumber WHERE ApplicationId=?";

            covAreaAccessList = jdbcTemplate.query(sqlStd,
                    BeanPropertyRowMapper.newInstance(ApplicationInfo.class), applicationId);
            log.info("CovAreaAccess 1 {}", covAreaAccessList);

            if (covAreaAccessList != null && covAreaAccessList.size() >= 1) {
                covAreaAccess = covAreaAccessList.get(0);
                log.info("CovAreaAccess 1 {}", covAreaAccess);
                return covAreaAccess;
            }

        } else if (Objects.equals(numType, "SPECIAL")) {
            String sqlSpc = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyState, CompanyFax, AccessCode FROM SpecialNumbers WHERE ApplicationId=?";

            covAreaAccessList = jdbcTemplate.query(sqlSpc,
                    BeanPropertyRowMapper.newInstance(ApplicationInfo.class), applicationId);
            log.info("CovAreaAccess 1 {}", covAreaAccessList);

            if (covAreaAccessList != null && covAreaAccessList.size() >= 1) {
                covAreaAccess = covAreaAccessList.get(0);
                log.info("CovAreaAccess 2 {}", covAreaAccess);
                return covAreaAccess;
            }

        } else if (Objects.equals(numType, "SHORT-CODE")) {
            String sqlSpc = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyState, CompanyFax FROM ShortCodeNumbers WHERE ApplicationId=?";

            covAreaAccessList = jdbcTemplate.query(sqlSpc,
                    BeanPropertyRowMapper.newInstance(ApplicationInfo.class), applicationId);
            log.info("CovAreaAccess 1 {}", covAreaAccessList);

            if (covAreaAccessList != null && covAreaAccessList.size() >= 1) {
                covAreaAccess = covAreaAccessList.get(0);
                log.info("CovAreaAccess 2 {}", covAreaAccess);
                return covAreaAccess;
            }
        } else if (Objects.equals(numType, "ISPC")) {
            String sqlIspc = "SELECT CompanyName, CompanyEmail, CompanyPhone, CompanyState, CompanyFax FROM IspcNumbers WHERE ApplicationId=?";

            covAreaAccessList = jdbcTemplate.query(sqlIspc,
                    BeanPropertyRowMapper.newInstance(ApplicationInfo.class), applicationId);
            log.info("CovAreaAccess 1 {}", covAreaAccessList);

            if (covAreaAccessList != null && covAreaAccessList.size() >= 1) {
                covAreaAccess = covAreaAccessList.get(0);
                log.info("CovAreaAccess 2 {}", covAreaAccess);
                return covAreaAccess;
            }

        } else {
            covAreaAccess = null;
            return covAreaAccess;
        }
        return null;
    }
}
