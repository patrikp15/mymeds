package com.mymeds.authservice.client;

import com.mymeds.sharedutilities.exception.GeneralErrorCode;
import com.mymeds.sharedutilities.exception.MyMedsGeneralException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseClient {

  private static final Logger LOG = LoggerFactory.getLogger(BaseClient.class);

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public BaseClient(RestTemplate restTemplate, String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  public <T, R> R requestPost(T request, String path, Class<R> responseType) {
    return request(request, HttpMethod.POST, path, responseType);
  }

  public <T, R> R requestGet(String path, Class<R> responseType) {
    return request(null, HttpMethod.GET, path, responseType);
  }

  public <T, R> R requestPut(T request, String path, Class<R> responseType) {
    return request(request, HttpMethod.PUT, path, responseType);
  }

  private <T, R> R request(T request, HttpMethod httpMethod, String path, Class<R> responseType) {
    LOG.info("action=CLIENT.request, status=started, method={}, baseUrl={}, path={}", httpMethod, baseUrl, path);
    RequestEntity<T> requestEntity;
    if (request != null) {
      requestEntity = new RequestEntity<>(
          request,
          httpMethod,
          URI.create(baseUrl + path)
      );
    } else {
      requestEntity = new RequestEntity<>(
          httpMethod,
          URI.create(baseUrl + path)
      );
    }
    final ResponseEntity<R> response = restTemplate.exchange(
        requestEntity,
        responseType
    );

    LOG.info("action=CLIENT.request, status=finished");
    return handleResponse(response);
  }

  private <T> T handleResponse(ResponseEntity<T> response) {
    if (response.getStatusCode().isError()) {
      LOG.error("action=handleResponse, status=failed, errorCode={}", response.getStatusCode().value());
      throw new MyMedsGeneralException(GeneralErrorCode.REQUEST_TO_CLIENT_SERVICE_FAILED);
    }
    return response.getBody();
  }
}
