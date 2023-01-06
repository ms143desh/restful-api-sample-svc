package com.sample;

import com.sample.model.SampleRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MainApplicationTests {

    public static String TESTING_1 = "testing1";

    @Test
    void contextLoads() {}

    public SampleRequestModel getSampleRequestModelObject() {
        SampleRequestModel sampleRequestModel = new SampleRequestModel();
        sampleRequestModel.setAge(25);
        sampleRequestModel.setCompany("example.com");
        sampleRequestModel.setName(TESTING_1);

        return sampleRequestModel;
    }
}
