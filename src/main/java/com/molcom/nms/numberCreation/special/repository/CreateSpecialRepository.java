package com.molcom.nms.numberCreation.special.repository;

import com.molcom.nms.general.utils.CurrentTimeStamp;
import com.molcom.nms.numberCreation.special.dto.CreateSpecialNoModel;
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
public class CreateSpecialRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int countIfNoExist(String minNumber, String maxNumber, String numberSubType, String accessCode) throws SQLException {
        String subType = (numberSubType != null ? numberSubType.toUpperCase() : "");
//        String numberRange = accessCode + minNumber + "-" + accessCode + maxNumber;
        String numberRange = minNumber + "-" + maxNumber;
        String sql = "SELECT count(*) FROM CreatedSpecialNumber WHERE MinimumNumber =? AND MaximumNumber=? AND UPPER(NumberSubType)=? AND AccessCode=?";
        return jdbcTemplate.queryForObject(
                sql, Integer.class, minNumber, maxNumber, subType, accessCode);
    }

    public int createSingleNumber(CreateSpecialNoModel model) throws SQLException {
        String sql = "INSERT INTO CreatedSpecialNumber (NumberType, NumberSubType,AccessCode,MinimumNumber,MaximumNumber," +
                "CreatedBy, CreatedDate, Quantity ) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                "Special",
                model.getNumberSubType(),
                model.getAccessCode(),
                model.getMinimumNumber(),
                model.getMaximumNumber(),
                model.getCreatedBy(),
                CurrentTimeStamp.getCurrentTimeStamp(),
                "10000"
        );
    }

    public int saveNumberBlock(String numberType,
                               String numberSubType,
                               String numberRange,
                               String accessCode,
                               String numberBlock) throws SQLException {
        String sql = "INSERT INTO NumberBlock (NumberType,NumberSubType,NumberRange,AccessCode,NumberBlock,IsSelected ) " +
                "VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                numberType,
                numberSubType,
                numberRange,
                accessCode,
                numberBlock,
                "FALSE"
        );
    }

    public List<CreateSpecialNoModel> getNumberByAccessCode(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedSpecialNumber WHERE AccessCode = ?";
        try {
            List<CreateSpecialNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class), accessCode);
            log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public List<CreateSpecialNoModel> getAvailableVanityNumbers(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedSpecialNumber WHERE AccessCode = ? AND UPPER(NumberSubType) ='Vanity'";
        try {
            List<CreateSpecialNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class), accessCode);
            log.info("Available Vanity Number {} ", CreateSpecialNoModels);
            return CreateSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    public List<CreateSpecialNoModel> getAvailableTollFreeNumbers(String accessCode) throws SQLException {
        String sql = "SELECT * from CreatedSpecialNumber WHERE AccessCode = ? AND UPPER(NumberSubType) ='TOLL-FREE'";
        try {
            List<CreateSpecialNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class), accessCode);
            log.info("Available Toll-free Number {} ", CreateSpecialNoModels);
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

        String sql1 = "SELECT * from CreatedSpecialNumber WHERE CreatedSpecialNumberId=?";
        List<CreateSpecialNoModel> createSpecialNoModels = jdbcTemplate.query(sql1,
                BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class), id);
        log.info("CreateSpecialNoModel {} ", createSpecialNoModels);
        if (createSpecialNoModels.size() > 0) {
            CreateSpecialNoModel data = createSpecialNoModels.get(0);
            min = data.getMinimumNumber();
            max = data.getMaximumNumber();
            number = min + "-" + max;
            subType = data.getNumberSubType();
            log.info("Number {}", number);

        }
        String sql2 = "DELETE FROM CreatedSpecialNumber WHERE CreatedSpecialNumberId = ?";
        int deleteNumberRange = jdbcTemplate.update(sql2, id);
        log.info("Number range delete {}", deleteNumberRange);

        String sql3 = "DELETE FROM NumberBlock WHERE NumberRange=? AND NumberSubType=?";
        int deleteNumberBlock = jdbcTemplate.update(sql3, number, subType);
        log.info("Number block delete {}", deleteNumberBlock);

        if (deleteNumberBlock >= 1 && deleteNumberRange >= 1) {
            return 1;
        } else {
            return -1;
        }
    }


    public List<CreateSpecialNoModel> getAllNumber() throws SQLException {
        String sql = "SELECT * from CreatedSpecialNumber";
        List<CreateSpecialNoModel> CreateSpecialNoModels = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModels);
        return CreateSpecialNoModels;
    }

    public int getAvailableNumberCount(String minNumber, String maxNumber) throws SQLException {
        String number = minNumber + "-" + maxNumber;
        log.info("Number {}", number);
        String sql = "SELECT count(*) FROM SelectedNumbers WHERE SelectedNumberValue=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, number);
    }

    public List<CreateSpecialNoModel> filterNumber(String numberSubType, String accessCode, String startDate, String endDate, String rowNumber) throws SQLException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * from CreatedSpecialNumber WHERE ");

        if (numberSubType != null && !numberSubType.isEmpty() &&
                !Objects.equals(numberSubType, "")
                && !Objects.equals(numberSubType.toUpperCase(), "SELECT")) {
            sqlBuilder.append("UPPER(NumberSubType) = '").append("" + numberSubType.toUpperCase() + "").append("' AND ");
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


        List<CreateSpecialNoModel> CreateSpecialNoModelsList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class));
        log.info("ReportShortCodeNoModel {} ", CreateSpecialNoModelsList);
        return CreateSpecialNoModelsList;
    }

    public List<CreateSpecialNoModel> getUnallocatedNumbers() throws SQLException {
        String sql = "SELECT * from CreatedSpecialNumber WHERE UPPER(AllocationStatus) != 'ALLOCATED'";
        try {
            List<CreateSpecialNoModel> createSpecialNoModels = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(CreateSpecialNoModel.class));
            return createSpecialNoModels;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }
}
