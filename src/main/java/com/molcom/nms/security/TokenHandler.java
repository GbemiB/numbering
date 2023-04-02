package com.molcom.nms.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.molcom.nms.general.dto.GenericResponse;
import com.molcom.nms.general.utils.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenHandler {
    @Autowired
    private JwtSecurityService jwtSecurityService;

    public GenericResponse<?> tokenHandler(String tokenVal) throws JsonProcessingException {
        GenericResponse<String> genericResponse = new GenericResponse<>();

        if (tokenVal != null) {
            boolean isValid = jwtSecurityService.validateToken(tokenVal);
            log.info("Is token valid {}", isValid);

            if (!isValid) {
                genericResponse.setResponseCode(ResponseStatus.UNAUTHORIZED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.UNAUTHORIZED.getCode());
            } else {
                genericResponse.setResponseCode(ResponseStatus.AUTHORIZED.getCode());
                genericResponse.setResponseMessage(ResponseStatus.AUTHORIZED.getCode());
            }
        }
        return genericResponse;
    }
}
