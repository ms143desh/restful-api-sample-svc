package com.sample.service;

import com.sample.MainApplicationTests;
import com.sample.model.SampleRequestModel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SampleServiceTest extends MainApplicationTests {

    @Autowired
    SampleService sampleService;

    @Test
    public void saveSampleRequestTest() {
        SampleRequestModel sampleRequestModel = getSampleRequestModelObject();
        sampleService.saveSampleRequest(sampleRequestModel);
        // There is nothing to assert
    }
}
