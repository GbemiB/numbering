package com.molcom.nms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtSecurityService {

    @Value("${nms.app.jwtKey}")
    private String nmsJwtKey;
    @Value("${nms.app.jwtSecret}")
    private String nmsJwtSecret;
    @Value("${nms.app.jwtExpirationMs}")
    private long nmsJwtExpirationMs;

    public Token create512JwtToken(String username) {
        log.info("Creating Json Web Token for client: {}", username);
        String token = JWT.create()
                .withIssuer(nmsJwtKey)
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + nmsJwtExpirationMs))
                .sign(Algorithm.HMAC512(nmsJwtSecret));

        log.info("Json Web Token created successfully for client: {}", username);
        return new Token(token, "bearer", nmsJwtExpirationMs);
    }

    public boolean validateToken(String jwt) throws JsonProcessingException {
        try {
            String[] chunks = jwt.split("\\.");

            Base64.Decoder decoder = Base64.getUrlDecoder();

            try {
                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));

                log.info("Header {} ", header);
                log.info("Payload {} ", payload);

                if (header != null && payload != null) {

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> data = mapper.readValue(payload, Map.class);
                    log.info("Jwt payload mapped to data !: {}", data);

                    String nmsKey = data.get("iss").toString();
                    String expiration = data.get("exp").toString();

                    long expireAt = Long.parseLong(expiration);

                    boolean isJwtExpired = expireAt >= System.currentTimeMillis();

                    boolean isKeyValid = nmsKey.equals(nmsJwtKey);

                    log.info("Expires At {} ", expireAt);
                    log.info("Is Token Expired {} ", isJwtExpired);
                    log.info("Is Key valid {} ", isKeyValid);

                    if (!isJwtExpired && isKeyValid) {
                        log.info("success block ");
                        return true;
                    } else {
                        log.info("failed block ");
                        return false;
                    }
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }

        } catch (IllegalArgumentException exception) {
            log.info("Exception occurred while validating token {} ", exception.getMessage());
            return false;
        }
    }

}