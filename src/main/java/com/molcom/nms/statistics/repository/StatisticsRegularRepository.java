package com.molcom.nms.statistics.repository;

import com.molcom.nms.statistics.dto.RecentApplicationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class StatisticsRegularRepository implements IStatisticsRegularRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to count total standard numbers applications
     *
     * @return
     */
    @Override
    public int countTotalStandardNos() {
        String sql = "SELECT COUNT(*) FROM StandardNumber";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * Data layer to count total short number applications
     *
     * @return
     */
    @Override
    public int countTotalShortCodes() {
        String sql = "SELECT COUNT(*) FROM ShortCodeNumbers";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * Data layer to count total ISPC applications
     *
     * @return
     */
    @Override
    public int countTotalISPCs() {
        String sql = "SELECT COUNT(*) FROM IspcNumbers";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * Data layer to count total special number applications
     *
     * @return
     */
    @Override
    public int countTotalSpecialNos() {
        String sql = "SELECT COUNT(*) FROM SpecialNumbers";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * Data layer to count total submitted applications for all number categories
     *
     * @return
     */
    @Override
    public int countTotalSubmittedApplications() {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='SUBMITTED'";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED'";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED'";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED'";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total unsubmitted applications for all number categories -- pending applications
     *
     * @return
     */
    @Override
    public int countTotalUnSubmittedApplications() {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='PENDING'";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='PENDING'";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='PENDING'";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='PENDING'";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total approved applications for all number categories
     * Status changes to approved after approval of application
     *
     * @return
     */
    @Override
    public int countTotalApprovedApplications() {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='APPROVED'";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='APPROVED'";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='APPROVED'";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='APPROVED'";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total rejected applications for all number categories
     * Status changes to approved rejected ejected of application
     *
     * @return
     */
    // Status changes to rejected after reject of application
    @Override
    public int countTotalRejectedApplications() {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='REJECTED'";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='REJECTED'";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='REJECTED'";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='REJECTED'";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to get first 5 recent applications across all number categories
     *
     * @return
     */
    @Override
    public List<RecentApplicationLog> getRecentApplications() {
        String sql = "SELECT * FROM (SELECT 'ShortCode' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from ShortCodeNumbers) ShortCode UNION\n" +
                "SELECT * FROM (SELECT 'Standard' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from StandardNumber ) Standard UNION\n" +
                "SELECT * FROM (SELECT 'ISPC' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from IspcNumbers) Ispc UNION\n" +
                "SELECT * FROM (SELECT 'Special' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from SpecialNumbers) Special\n" +
                "ORDER BY CreatedDate DESC limit 5";

        List<RecentApplicationLog> recentApplicationLogs = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(RecentApplicationLog.class));
        log.info("RecentApplicationLog {} ", recentApplicationLogs);
        return recentApplicationLogs;
    }


    ///////////////// Method overloading //////////////////

    /**
     * Data layer to count total standard numbers applications
     *
     * @return
     */
    @Override
    public int countTotalStandardNos(String companyName) {
        String sql = "SELECT COUNT(*) FROM StandardNumber WHERE CompanyName=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, companyName);
    }

    /**
     * Data layer to count total short number applications
     *
     * @return
     */
    @Override
    public int countTotalShortCodes(String companyName) {
        String sql = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE CompanyName=?";

        return jdbcTemplate.queryForObject(sql, Integer.class, companyName);
    }

    /**
     * Data layer to count total ISPC applications
     *
     * @return
     */
    @Override
    public int countTotalISPCs(String companyName) {
        String sql = "SELECT COUNT(*) FROM IspcNumbers WHERE CompanyName=?";

        return jdbcTemplate.queryForObject(sql, Integer.class, companyName);
    }

    /**
     * Data layer to count total special number applications
     *
     * @return
     */
    @Override
    public int countTotalSpecialNos(String companyName) {
        String sql = "SELECT COUNT(*) FROM SpecialNumbers WHERE CompanyName=?";

        return jdbcTemplate.queryForObject(sql, Integer.class, companyName);
    }

    /**
     * Data layer to count total submitted applications for all number categories
     *
     * @return
     */
    @Override
    public int countTotalSubmittedApplications(String companyName) {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='SUBMITTED' AND CompanyName=?";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED' AND CompanyName=?";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED' AND CompanyName=?";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='SUBMITTED' AND CompanyName=?";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class, companyName);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class, companyName);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class, companyName);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class, companyName);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total unsubmitted applications for all number categories -- pending applications
     *
     * @return
     */
    @Override
    public int countTotalUnSubmittedApplications(String companyName) {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='PENDING' AND CompanyName=?";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='PENDING' AND CompanyName=?";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='PENDING' AND CompanyName=?";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='PENDING' AND CompanyName=?";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class, companyName);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class, companyName);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class, companyName);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class, companyName);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total approved applications for all number categories
     * Status changes to approved after approval of application
     *
     * @return
     */
    @Override
    public int countTotalApprovedApplications(String companyName) {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='APPROVED' AND CompanyName=?";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='APPROVED' AND CompanyName=?";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='APPROVED' AND CompanyName=?";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='APPROVED' AND CompanyName=?";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class, companyName);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class, companyName);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class, companyName);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class, companyName);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to count total rejected applications for all number categories
     * Status changes to approved rejected ejected of application
     *
     * @return
     */
    // Status changes to rejected after reject of application
    @Override
    public int countTotalRejectedApplications(String companyName) {
        int count = 0;
        String sql1 = "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) ='REJECTED' AND CompanyName=?";
        String sql2 = "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) ='REJECTED' AND CompanyName=?";
        String sql3 = "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) ='REJECTED' AND CompanyName=?";
        String sql4 = "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) ='REJECTED' AND CompanyName=?";

        int aa = jdbcTemplate.queryForObject(sql1, Integer.class, companyName);
        int bb = jdbcTemplate.queryForObject(sql2, Integer.class, companyName);
        int cc = jdbcTemplate.queryForObject(sql3, Integer.class, companyName);
        int dd = jdbcTemplate.queryForObject(sql4, Integer.class, companyName);

        count = aa + bb + cc + dd;

        return count;
    }

    /**
     * Data layer to get first 5 recent applications across all number categories
     *
     * @return
     */
    @Override
    public List<RecentApplicationLog> getRecentApplications(String companyName) {
        String sql = "SELECT * FROM (SELECT 'ShortCode' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from ShortCodeNumbers WHERE CompanyName='" + companyName + "') ShortCode UNION\n" +
                "SELECT * FROM (SELECT 'Standard' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from StandardNumber WHERE CompanyName='" + companyName + "') Standard UNION\n" +
                "SELECT * FROM (SELECT 'ISPC' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from IspcNumbers WHERE CompanyName='" + companyName + "') Ispc UNION\n" +
                "SELECT * FROM (SELECT 'Special' as NumberType, ApplicationId, ApplicationStatus, CreatedUserEmail,CreatedBy, CreatedDate from SpecialNumbers WHERE CompanyName='" + companyName + "') Special\n" +
                "ORDER BY CreatedDate DESC limit 5";

        List<RecentApplicationLog> recentApplicationLogs = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(RecentApplicationLog.class));
        log.info("RecentApplicationLog {} ", recentApplicationLogs);
        return recentApplicationLogs;
    }
}
