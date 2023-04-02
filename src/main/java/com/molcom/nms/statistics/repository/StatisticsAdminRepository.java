package com.molcom.nms.statistics.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class StatisticsAdminRepository implements IStatisticsAdminRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to count national coverage area
     *
     * @return
     */
    @Override
    public int countNationalCoverageArea() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM CoverageArea WHERE UPPER(CoverageType) = 'NATIONAL'", Integer.class);
    }

    /**
     * Data layer to count geographical coverage area
     *
     * @return
     */
    @Override
    public int countGeographicalCoverageArea() {

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM CoverageArea WHERE UPPER(CoverageType) = 'GEOGRAPHICAL'", Integer.class);

    }

    /**
     * Data layer to count total coverage area
     *
     * @return
     */
    @Override
    public int countTotalCoverageArea() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM CoverageArea", Integer.class);
    }


    /**
     * Data layer to count approved standard numbers
     *
     * @return
     */
    @Override
    public int countApprovedStandardNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) = 'APPROVED'", Integer.class);
    }

    /**
     * Data layer to count approved short codes
     *
     * @return
     */
    @Override
    public int countApprovedShortCodes() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) = 'APPROVED'", Integer.class);
    }


    /**
     * Data layer to count approved ISPCs
     *
     * @return
     */
    @Override
    public int countApprovedISPCs() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM IspcNumbers WHERE UPPER(ApplicationStatus) = 'APPROVED'", Integer.class);
    }


    /**
     * Data layer to count approved special numbers
     *
     * @return
     */
    @Override
    public int countApprovedSpecialNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) = 'APPROVED'", Integer.class);
    }


    /**
     * Data layer to count pending standard numbers
     *
     * @return
     */
    @Override
    public int countPendingStandardNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) = 'PENDING'", Integer.class);
    }


    /**
     * Data layer to count pending short codes
     *
     * @return
     */
    @Override
    public int countPendingShortCodes() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) = 'PENDING'", Integer.class);
    }


    /**
     * Data layer to count pending ISPC
     *
     * @return
     */
    @Override
    public int countPendingISPCs() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) = 'PENDING'", Integer.class);
    }


    /**
     * Data layer to count pending special numbers
     *
     * @return
     */
    @Override
    public int countPendingSpecialNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) = 'PENDING'", Integer.class);
    }


    /**
     * Data layer to count rejected standard numbers
     *
     * @return
     */
    @Override
    public int countRejectedStandardNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM StandardNumber WHERE UPPER(ApplicationStatus) = 'REJECTED'", Integer.class);
    }

    /**
     * Data layer to count rejected short codes
     *
     * @return
     */
    @Override
    public int countRejectedShortCodes() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ShortCodeNumbers WHERE UPPER(ApplicationStatus) = 'REJECTED'", Integer.class);
    }

    /**
     * Data layer to count rejected ISPC
     *
     * @return
     */
    @Override
    public int countRejectedISPCs() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) = 'REJECTED'", Integer.class);
    }

    /**
     * Data layer to count rejected special numbers
     *
     * @return
     */
    @Override
    public int countRejectedSpecialNos() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SpecialNumbers WHERE UPPER(ApplicationStatus) = 'REJECTED'", Integer.class);
    }

    /**
     * Data layer to count approved addition of services
     *
     * @return
     */
    @Override
    public int countApprovedServices() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM AdditionOfServices WHERE UPPER(ApplicationStatus) = 'APPROVED'", Integer.class);
    }


    /**
     * Data layer to count pending addition of services
     *
     * @return
     */
    @Override
    public int countPendingServices() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM AdditionOfServices WHERE UPPER(ApplicationStatus) = 'PENDING'", Integer.class);
    }

    /**
     * Data layer to count submitted addition of services
     *
     * @return
     */
    @Override
    public int countSubmittedServices() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM AdditionOfServices WHERE UPPER(ApplicationStatus) = 'SUBMITTED'", Integer.class);
    }


    /**
     * Data layer to count rejected addition of services
     *
     * @return
     */
    @Override
    public int countRejectedServices() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM AdditionOfServices WHERE UPPER(ApplicationStatus) = 'REJECTED'", Integer.class);
    }

}
