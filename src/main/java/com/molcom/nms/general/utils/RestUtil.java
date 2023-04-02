package com.molcom.nms.general.utils;

import com.molcom.nms.general.exceptions.IntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Objects;


@Service
@Slf4j
public class RestUtil {
    private final RestTemplateBuilder restTemplateBuilder;
    private final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;

    private String url;
    private HttpEntity<Object> requestEntity;
    private int timeout;
    private String password;
    private String certPath;

    public RestUtil(RestTemplateBuilder restTemplateBuilder,
                    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.clientHttpRequestFactory = clientHttpRequestFactory;
        this.timeout = 10;
    }

    public RestUtil setUrl(String url) {
        this.url = url;
        return this;
    }

    public RestUtil setRequest(Object request) {
        requestEntity = new HttpEntity<>(request);
        return this;
    }

    public RestUtil setRequest(HttpHeaders headers) {
        requestEntity = new HttpEntity<>(headers);
        return this;
    }

    public RestUtil setRequest(Object request, HttpHeaders headers) {
        requestEntity = new HttpEntity<>(request, headers);
        return this;
    }

    public RestUtil setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RestUtil setCredentials(String password, String certPath) {
        this.certPath = certPath;
        this.password = password;
        return this;
    }

    public <T> ResponseEntity<T> post(Class<T> entityClass) throws Exception {
        try {
            return getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, entityClass);
        } catch (Exception ex) {
            log.error("Failed AutoFeeResponse=======> {}", ex.getMessage());
            if (ex.getMessage().contains("404")) {
                throw new IntegrationException("Not Found", HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().contains("401")) {
                throw new IntegrationException("Invalid Authorization Credentials", HttpStatus.UNAUTHORIZED);
            }
            ex.printStackTrace();
            throw new IntegrationException(ResponseStatus.EXTERNAL_INTEGRATION_ERROR.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            resetData();
        }
    }

    public <T> ResponseEntity<T> put(Class<T> entityClass) throws Exception {
        try {
            return getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, entityClass);
        } catch (Exception ex) {
            log.error("Failed AutoFeeResponse=======> {}", ex.getMessage());
            if (ex.getMessage().contains("404")) {
                throw new IntegrationException("Not Found", HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().contains("401")) {
                throw new IntegrationException("Invalid Authorization Credentials", HttpStatus.UNAUTHORIZED);
            }

            throw new IntegrationException(
                    ResponseStatus.EXTERNAL_INTEGRATION_ERROR.getMessage() + ": " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            resetData();
        }
    }

    public <T> ResponseEntity<T> get(Class<T> entityClass) throws Exception {
        try {
            return getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, entityClass);
        } catch (Exception ex) {
            log.error("Failed AutoFeeResponse=======> {}", ex.getMessage());
            if (ex.getMessage().contains("404")) {
                throw new IntegrationException("Not Found", HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().contains("401")) {
                throw new IntegrationException("Invalid Authorization Credentials", HttpStatus.UNAUTHORIZED);
            }
            throw new IntegrationException(ResponseStatus.EXTERNAL_INTEGRATION_ERROR.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            resetData();
        }
    }

    public <T> ResponseEntity<T> delete(Class<T> entityClass) throws Exception {
        try {
            return getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, entityClass);
        } catch (Exception ex) {
            log.error("Failed AutoFeeResponse=======> {}", ex.getMessage());
            throw new IntegrationException(ResponseStatus.EXTERNAL_INTEGRATION_ERROR.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            resetData();
        }
    }

    private RestTemplate getRestTemplate() throws Exception {
        if (Objects.nonNull(this.password) && Objects.nonNull(this.certPath)) {
            return getRestTemplateWithCert();
        }
        return getRestTemplateWithoutCert();
    }

    private RestTemplate getRestTemplateWithoutCert()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, (certificate, authType) -> true)
                .build();
        HttpClient client = HttpClients.custom().setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        HttpComponentsClientHttpRequestFactory factory = this.clientHttpRequestFactory;
        factory.setHttpClient(client);

        return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(timeout))
                .setReadTimeout(Duration.ofSeconds(timeout)).requestFactory(() -> factory).build();
    }

    private RestTemplate getRestTemplateWithCert() throws Exception {
        char[] pass = this.password.toCharArray();

        SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore(certPath, pass), pass)
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        HttpComponentsClientHttpRequestFactory factory = this.clientHttpRequestFactory;
        factory.setHttpClient(client);
        return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(timeout))
                .setReadTimeout(Duration.ofSeconds(timeout)).requestFactory(() -> factory).build();
    }

    private KeyStore keyStore(String filePath, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile(filePath);
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, password);
        }
        return keyStore;
    }

    private void resetData() {
        this.url = null;
        this.requestEntity = null;
        this.timeout = 6;
        this.password = null;
        this.certPath = null;
    }

}
