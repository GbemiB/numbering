package com.molcom.nms.numberCreation.availableNumbers.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.numberCreation.availableNumbers.dto.AvailableNumberBlockModel;
import com.molcom.nms.numberCreation.availableNumbers.dto.AvailableNumberCount;
import com.molcom.nms.numberCreation.availableNumbers.dto.GetAvailableNumberCount;
import com.molcom.nms.numberCreation.ispc.repository.CreateIspcRepository;
import com.molcom.nms.numberCreation.special.repository.CreateSpecialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AvailableNoCountService {
    @Autowired
    private CreateSpecialRepository specialRepository;

    @Autowired
    private CreateIspcRepository ispcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public GenericResponse<AvailableNumberCount> getAvailableNumbers(GetAvailableNumberCount request) {
        log.info("Get available numbers model {}", request);
        GenericResponse<AvailableNumberCount> genericResponse = new GenericResponse<>();
        AvailableNumberCount availableNumberCount = new AvailableNumberCount();
        String numberSubType = "";

        if (request.getNumberType() != null) {
            if (Objects.equals(request.getNumberType().toUpperCase(), "SHORT-CODE")) {
                numberSubType = "SHORT-CODE";
            } else {
                numberSubType = (request.getSubType() != null ? request.getSubType().toUpperCase() : "");
            }

        }
        String minNumber = request.getMinimumNumber();
        String maxNumber = request.getMaximumNumber();
        String shortCodeNumber = request.getShortCodeNumber();
        String shortCodeService = request.getShortCodeService();
        String accessCode = request.getAccessCode();
        int highestNumber = 10;


        log.info("NumberSubtype {}", numberSubType);
        switch (numberSubType) {
            case "NATIONAL":
                try {
                    List<AvailableNumberBlockModel> numberBlocks = getNumberBlockForStandardNo(minNumber, maxNumber, numberSubType);
                    log.info("Number blocks {}", numberBlocks);
                    int countStd = 0;
                    if (numberBlocks != null) {
                        countStd = numberBlocks.size();
                    }
                    log.info("Result set from repository {} ====> ", countStd);
                    if (countStd <= highestNumber) {
                        availableNumberCount.setCountAvailable(countStd);
                        availableNumberCount.setNumberBlock(numberBlocks);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    } else {
                        availableNumberCount.setCountAvailable(0);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            case "GEOGRAPHICAL":
                try {
                    List<AvailableNumberBlockModel> numberBlocks = getNumberBlockForStandardNo(minNumber, maxNumber, numberSubType);
                    log.info("Number blocks {}", numberBlocks);
//                    int countStd = 0;
//                    if (numberBlocks != null) {
//                        countStd = numberBlocks.size();
//                    }
//                    log.info("Result set from repository ::{} ====> ", countStd);
                    if (numberBlocks != null) {
                        availableNumberCount.setCountAvailable(1);
                        availableNumberCount.setNumberBlock(numberBlocks);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    } else {
                        availableNumberCount.setCountAvailable(0);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;


            case "VANITY":
            case "TOLL-FREE":
                try {
                    List<AvailableNumberBlockModel> numberBlocks = getNumberBlockOnlyForSpecialNo(minNumber, maxNumber, numberSubType, accessCode);
                    log.info("Number blocks {}", numberBlocks);
                    int countSpecial = 0;
                    if (numberBlocks != null) {
                        countSpecial = numberBlocks.size();
                    }
                    log.info("Result count {} ====> ", countSpecial);
                    if (countSpecial <= highestNumber) {
                        availableNumberCount.setCountAvailable(countSpecial);
                        availableNumberCount.setNumberBlock(numberBlocks);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    } else {
                        availableNumberCount.setCountAvailable(0);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred !!!!! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;
            case "SHORT-CODE":
                try {
                    // check short code number count
                    List<AvailableNumberBlockModel> numberBlock = getNumberBlockForShortCodes(minNumber, maxNumber, shortCodeService);
                    log.info("Available number blocks {}", numberBlock);

                    assert numberBlock != null;
                    int countShortCode = numberBlock.size();

                    if (countShortCode > 0) {
                        availableNumberCount.setCountAvailable(countShortCode);
                        availableNumberCount.setNumberBlock(numberBlock);

                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);

                    } else {
                        availableNumberCount.setCountAvailable(0);
                        genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                        genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
                        genericResponse.setOutputPayload(availableNumberCount);
                    }
                } catch (Exception exception) {
                    log.info("Exception occurred ! {} ", exception.getMessage());
                    genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getCode());
                    genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getMessage());
                }
                return genericResponse;

            default:
                break;
        }
        return null;
    }

    // Getting number block for ISPC number
    public int getNumberBlockOnlyForIspcNo(String number) throws SQLException {
        String sql = "SELECT count(*) FROM SelectedNumbers WHERE SelectedNumberValue=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, number);
    }

    private List<AvailableNumberBlockModel> getNumberBlockOnlyForSpecialNo(String minimum, String maximum, String numberSubType, String accessCode) throws SQLException {
        String numberRange = minimum + "-" + maximum;
        String sql = "SELECT * from NumberBlock WHERE NumberRange =? AND NumberSubType=? AND AccessCode=? AND IsSelected='FALSE'";
        try {
            List<AvailableNumberBlockModel> numberBlockModelList = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(AvailableNumberBlockModel.class), numberRange, numberSubType, accessCode);
            log.info("AvailableNumberBlockModel for special number {} ", numberBlockModelList);
            return numberBlockModelList;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    // Getting number block for Standard number
    private List<AvailableNumberBlockModel> getNumberBlockForStandardNo(String minimum, String maximum, String numberSubType) throws SQLException {
        String numberRange = minimum + "-" + maximum;
        String sql = "SELECT * from NumberBlock WHERE NumberRange =? AND NumberSubType=? AND IsSelected='FALSE'";
        try {
            List<AvailableNumberBlockModel> numberBlockModelList = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(AvailableNumberBlockModel.class), numberRange, numberSubType);
            log.info("AvailableNumberBlockModel for short code {} ", numberBlockModelList);
            return numberBlockModelList;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

    // Getting number block for ShortCode number
    private List<AvailableNumberBlockModel> getNumberBlockForShortCodes(String minimum, String maximum, String numberSubType) throws SQLException {
        String numberRange = minimum + "-" + maximum;
        log.info("number range {}", numberRange);
        String sql = "select * from NumberBlock where UPPER(NumberSubType)=? AND NumberRange=? AND IsSelected='FALSE'";
        log.info("AvailableNumberBlockModel for short code {} ", sql);
        try {
            List<AvailableNumberBlockModel> numberBlockModelList = jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(AvailableNumberBlockModel.class), numberSubType, numberRange);
            log.info("AvailableNumberBlockModel for short code {} ", numberBlockModelList);
            return numberBlockModelList;

        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("Exception occurred !!!! ");
            return null;
        }
    }

}
