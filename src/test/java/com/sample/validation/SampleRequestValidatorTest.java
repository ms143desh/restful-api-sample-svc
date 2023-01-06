package com.sample.validation;

import static com.sample.constants.Constants.INVALID_AGE;
import static com.sample.constants.Constants.INVALID_COMPANY;
import static com.sample.constants.Constants.INVALID_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sample.MainApplicationTests;
import com.sample.config.ApplicationPropertiesConfig;
import com.sample.exception.SampleException;
import com.sample.model.SampleRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class SampleRequestValidatorTest extends MainApplicationTests {

    @Autowired ApplicationPropertiesConfig applicationPropertiesConfig;
    @SpyBean
    SampleRequestValidator sampleRequestValidator;

    private Errors errors;

    @Test
    public void supportClassTest() {

        assert (sampleRequestValidator.supports(SampleRequestModel.class));
    }

    @Test
    public void nameValidationFailureTest() {
        SampleRequestModel sampleRequestModel = getSampleRequestModelObject();
        sampleRequestModel.setName("$#");

        errors = new BeanPropertyBindingResult(sampleRequestModel, "sampleRequestModel");

        SampleException sampleException =
                assertThrows(
                        SampleException.class,
                        () -> {
                            sampleRequestValidator.validate(sampleRequestModel, errors);
                        },
                        "Name invalid exception");

        assertEquals(INVALID_NAME, sampleException.getMessage());
    }

    @Test
    public void ageValidationFailureTest() {
        SampleRequestModel sampleRequestModel = getSampleRequestModelObject();
        sampleRequestModel.setAge(10);

        errors = new BeanPropertyBindingResult(sampleRequestModel, "sampleRequestModel");

        SampleException sampleException =
                assertThrows(
                        SampleException.class,
                        () -> {
                            sampleRequestValidator.validate(sampleRequestModel, errors);
                        },
                        "Age invalid exception");

        assertEquals(INVALID_AGE, sampleException.getMessage());
    }

    @Test
    public void companyValidationFailureTest() {
        SampleRequestModel sampleRequestModel = getSampleRequestModelObject();
        sampleRequestModel.setCompany("testCompany");

        errors = new BeanPropertyBindingResult(sampleRequestModel, "sampleRequestModel");

        SampleException sampleException =
                assertThrows(
                        SampleException.class,
                        () -> {
                            sampleRequestValidator.validate(sampleRequestModel, errors);
                        },
                        "Company invalid exception");

        assertEquals(INVALID_COMPANY, sampleException.getMessage());
    }
}
