package com.molcom.nms.number.configurations.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.number.configurations.dto.FrequencyCoverageArea;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class FreqCoverageAreaRepo implements IFreqCoverageAreaRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Data layer to save frequency coverage area
     *
     * @param frequencyCoverageArea
     * @return
     * @throws SQLException
     */
    @Override
    public int saveFrequencyCoverageArea(FrequencyCoverageArea frequencyCoverageArea) throws SQLException {
        String sql = "INSERT INTO FrequencyCoverageArea (ApplicationId, Frequency, CoverageArea, CreatedDate) " +
                "VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql,
                frequencyCoverageArea.getApplicationId(),
                frequencyCoverageArea.getFrequency(),
                frequencyCoverageArea.getCoverageArea(),
                CurrentTimeStamp.getCurrentTimeStamp()
        );
    }

    /**
     * Data layer to delete frequency coverage area
     *
     * @param frequencyCoverageId
     * @return
     * @throws SQLException
     */
    @Override
    public int deleteFrequencyCoverageArea(String frequencyCoverageId) throws SQLException {
        String sql = "DELETE FROM FrequencyCoverageArea WHERE FrequencyCoverageId = ?";
        return jdbcTemplate.update(sql, frequencyCoverageId);
    }

    /**
     * Data layer to get frequency coverage areas by application id
     *
     * @param applicationId
     * @return
     * @throws SQLException
     */
    @Override
    public List<FrequencyCoverageArea> getFrequencyCoverageArea(String applicationId) throws SQLException {
        String sql = "SELECT * FROM FrequencyCoverageArea WHERE ApplicationId = ?";
        log.info("SQL {} ", sql);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(FrequencyCoverageArea.class), applicationId);
    }
}
