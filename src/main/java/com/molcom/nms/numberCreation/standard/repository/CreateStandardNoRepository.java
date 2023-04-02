package com.molcom.nms.numberCreation.standard.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.numberCreation.standard.dto.CreateStandardNoModel;
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
public class CreateStandardNoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int countIfNoExist(String minNumber, String maxNumber, String numberSubType, String accessCode) throws SQLException {
        String subType = (numberSubType != null ? numberSubType.toUpperCase() : "");
        String numberRange = minNumber + "-" + maxNumber;
        String sql = "SELECT count(*) FROM CreatedStandardNumber WHERE MinimumNumber =? AND MaximumNumber=? AND UPPER(NumberSubType)=? AND AccessCode=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber, subType, accessCode);
    }

    public int createSingleNumber(CreateStandardNoModel model) throws SQLException {
        String numberSubType = model.getNumberSubType() != null ? model.getNumberSubType() : "";
        String qty = "";

        qty = numberSubType.equalsIgnoreCase("NATIONAL") ? "1000000" : "10000";

        String sql = "INSERT INTO CreatedStandardNumber (NumberType, NumberSubType,CoverageArea, AreaCode, AccessCode,MinimumNumber,MaximumNumber," +
                "CreatedBy, CreatedDate,Quantity, AvailableCount ) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                "Standard",
                model.getNumberSubType(),
                model.getCoverageArea(),
                model.getAreaCode(),
                model.getAccessCode(),
                model.getMinimumNumber(),
                model.getMaximumNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                qty,
                10
        );
    }

    public int saveNumberBlock(String numberType,
                               String numberSubType, String numberRange,
                               String numberBlock) throws SQLException {
        String sql = "INSERT INTO NumberBlock (NumberType,NumberSubType,NumberRange,NumberBlock," +
                "IsSelected ) " +
                "VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                numberType,
                numberSubType,
                numberRange,
                numberBlock,
                "FALSE"
        );
    }

    public List<CreateStandardNoModel> getNumberByAccessCode(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedStandardNumber WHERE AccessCode = ?";
        try {
            List<CreateStandardNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class), accessCode);
            log.info("ReportStandardNoModel {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public List<CreateStandardNoModel> getAvailableNationalNo(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedStandardNumber WHERE AccessCode = ? AND UPPER(NumberSubType) ='NATIONAL' AND AvailableCount != '0'";
        try {
            List<CreateStandardNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class), accessCode);
            log.info("ReportStandardNoModel {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public List<CreateStandardNoModel> getAvailableGeographicalNo(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedStandardNumber WHERE AccessCode = ? AND UPPER(NumberSubType) ='Geographical' AND AvailableCount != '0'";
        try {
            List<CreateStandardNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class), accessCode);
            log.info("ReportStandardNoModel {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public int deleteNumberById(String id) throws SQLException {
        String min = "";
        String max = "";
        String number = "";
        String subType = "";
        String accessCode = "";

        String sql1 = "SELECT * from CreatedStandardNumber WHERE CreatedStandardNumberId=?";
        List<CreateStandardNoModel> createStandardNoModels = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class), id);
        log.info("createStandardNoModels {} ", createStandardNoModels);
        if (createStandardNoModels.size() > 0) {
            CreateStandardNoModel data = createStandardNoModels.get(0);
            accessCode = data.getAccessCode();
            min = accessCode + data.getMinimumNumber();
            max = accessCode + data.getMaximumNumber();
            number = min + "-" + max;
            subType = data.getNumberSubType();
            log.info("Number {}", number);

        }
        String sql2 = "DELETE FROM CreatedStandardNumber WHERE CreatedStandardNumberId = ?";
        int deleteNumberRange = jdbcTemplate.update(sql2, id);
        log.info("Number range delete {}", deleteNumberRange);

        String sql3 = "DELETE FROM NumberBlock WHERE NumberRange=? AND NumberSubType=?";
        int deleteNumberBlock = jdbcTemplate.update(sql3, number, subType);
        log.info("Number block delete {}", deleteNumberBlock);

        return deleteNumberRange;
    }

    public List<CreateStandardNoModel> getAllNumber() throws SQLException {
        String sql = "SELECT * from CreatedStandardNumber";
        List<CreateStandardNoModel> createStandardNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class));
        log.info("createStandardNoModels {} ", createStandardNoModels);
        return createStandardNoModels;
    }

    public int getAvailableNumberCount(String minNumber, String maxNumber) throws SQLException {
        String number = minNumber + "-" + maxNumber;
        log.info("Number {}", number);
        String sql = "SELECT count(*) FROM SelectedNumbers WHERE SelectedNumberValue=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, number);
    }

    public List<CreateStandardNoModel> filterNumber(String numberSubType, String coverageArea, String areaCode, String accessCode, String startDate, String endDate, String rowNumber) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CreatedStandardNumber WHERE ");

        if (numberSubType != null && !numberSubType.isEmpty() &&
                !Objects.equals(numberSubType, "")
                && !Objects.equals(numberSubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(NumberSubType) = '").append("" + numberSubType.toUpperCase() + "").append("' AND ");
        }

        if (coverageArea != null && !coverageArea.isEmpty() &&
                !Objects.equals(coverageArea, "")
                && !Objects.equals(coverageArea.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(CoverageArea) = '").append("" + coverageArea.toUpperCase() + "").append("' AND ");
        }

        if (areaCode != null && !areaCode.isEmpty() &&
                !Objects.equals(areaCode, "")
                && !Objects.equals(areaCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AreaCode) = '").append("" + areaCode.toUpperCase() + "").append("' AND ");
        }

        if (accessCode != null && !accessCode.isEmpty() &&
                !Objects.equals(accessCode, "")
                && !Objects.equals(accessCode.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(AccessCode) = '").append("" + accessCode.toUpperCase() + "").append("' AND ");
        }

        if (startDate != null && endDate != null &&
                !startDate.isEmpty() &&
                !endDate.isEmpty()) {
            sqlBuilder.append("DATE(CreatedDate) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') AND ");
        }

        sqlBuilder.append("CreatedDate IS NOT NULL ");
        sqlBuilder.append("ORDER BY CreatedDate DESC LIMIT ").append(rowNumber);
        String sql = sqlBuilder.toString();
        log.info("sql {} ", sql);


        List<CreateStandardNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }


    public List<CreateStandardNoModel> getGeoNumberByAccessCode(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedStandardNumber WHERE UPPER(NumberSubType) = 'GEOGRAPHICAL'";
        try {
            List<CreateStandardNoModel> createStandardNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateStandardNoModel.class));
            return createStandardNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }
}
