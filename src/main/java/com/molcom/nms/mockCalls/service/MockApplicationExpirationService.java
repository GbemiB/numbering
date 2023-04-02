package com.molcom.nms.mockCalls.service;

import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import com.molcom.nms.mockCalls.dto.MockApplicationExpiration;
import com.molcom.nms.number.selectedNumber.repository.SelectedNumbersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@Service
public class MockApplicationExpirationService {

    @Autowired
    private SelectedNumbersRepo selectedNumbersRepo;

    /**
     * @param mockApplicationExpiration
     * @return
     * @throws SQLException
     */
    public GenericResponse<?> mockApplicationExpiration(MockApplicationExpiration mockApplicationExpiration) throws SQLException {
        GenericResponse<?> genericResponse = new GenericResponse<>();

        try {
            int isApplicationExpired = selectedNumbersRepo.manuallyExpireApplication(mockApplicationExpiration.getApplicationId());

            log.info("Manually expiration of application status {} ", isApplicationExpired);

            if (isApplicationExpired >= 1) {
                genericResponse.setResponseCode(ResponseStatus.SUCCESS.getCode());
                genericResponse.setResponseMessage(ResponseStatus.SUCCESS.getMessage());
            } else {
                genericResponse.setResponseMessage(ResponseStatus.EXPIRATION_MOCK_FAILED.getCode());
                genericResponse.setResponseCode(ResponseStatus.EXPIRATION_MOCK_FAILED.getMessage());
            }
        } catch (Exception e) {
            genericResponse.setResponseMessage(ResponseStatus.ERROR_OCCURRED.getCode());
            genericResponse.setResponseCode(ResponseStatus.ERROR_OCCURRED.getMessage());
        }
        return genericResponse;
    }
}
