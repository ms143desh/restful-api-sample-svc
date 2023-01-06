package com.sample.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.sample.MainApplicationTests;
import com.sample.model.SampleRequestModel;
import com.sample.service.SampleService;
import com.sample.validation.SampleRequestValidator;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

public class SampleControllerTest extends MainApplicationTests {

    @Autowired private TestRestTemplate testRestTemplate;

    MockMvc mockMvc;
    @MockBean private SampleService sampleService;
    @MockBean private SampleRequestValidator sampleRequestValidator;

    @Test
    void testGetRoot() {
        URI targetUrl = UriComponentsBuilder.fromUriString("/").build().encode().toUri();
        ResponseEntity<String> responseEntity =
                this.testRestTemplate.getForEntity(targetUrl, String.class);

        String responseBody = responseEntity.getBody();
        assertEquals("HelloWorld Sample Project!", responseBody);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetSampleName() {
        URI targetUrl =
                UriComponentsBuilder.fromUriString("/sample/".concat(TESTING_1))
                        .build()
                        .encode()
                        .toUri();
        ResponseEntity<String> responseEntity =
                this.testRestTemplate.getForEntity(targetUrl, String.class);

        String responseBody = responseEntity.getBody();
        assertEquals("Hello ".concat(TESTING_1), responseBody);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetSampleJson() {
        URI targetUrl = UriComponentsBuilder.fromUriString("/sample").build().encode().toUri();

        when(sampleRequestValidator.supports(SampleRequestModel.class)).thenReturn(true);

        SampleRequestModel sampleRequestModel = getSampleRequestModelObject();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SampleRequestModel> requestEntity =
                new HttpEntity<>(sampleRequestModel, headers);
        ResponseEntity<SampleRequestModel> responseEntity =
                this.testRestTemplate.postForEntity(
                        targetUrl, requestEntity, SampleRequestModel.class);

        SampleRequestModel sampleRequestModelResponse = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(sampleRequestModel.getCompany(), sampleRequestModelResponse.getCompany());
    }
}
