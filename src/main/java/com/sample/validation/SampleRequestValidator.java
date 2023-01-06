package com.sample.validation;

import com.sample.config.ApplicationPropertiesConfig;
import com.sample.constants.Constants;
import com.sample.exception.SampleException;
import com.sample.model.SampleRequestModel;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SampleRequestValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(SampleRequestValidator.class);

    @Autowired private ApplicationPropertiesConfig applicationPropertiesConfig;

    @Override
    public boolean supports(Class<?> clazz) {
        return SampleRequestModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SampleRequestModel sampleRequetModel = (SampleRequestModel) target;
        logger.info("Basic Validation started");
        basicValidation(sampleRequetModel);
        if (errors.hasErrors()) {
            logger.info("Basic validation failed with {} errors", errors.getErrorCount());
            return;
        }
    }

    private void basicValidation(SampleRequestModel sampleRequetModel) {

        validateName(sampleRequetModel.getName());
        validateAge(sampleRequetModel.getAge());
        validateCompany(sampleRequetModel.getCompany());
    }

    private void validateName(String name) {
        final Pattern pattern = Pattern.compile(applicationPropertiesConfig.getNameRegexPattern());
        if (!pattern.matcher(name).matches()) {
            throw new SampleException(Constants.INVALID_NAME, name);
        }
    }

    private void validateAge(int age) {
        if (age < applicationPropertiesConfig.getMinAge()) {
            throw new SampleException(Constants.INVALID_AGE);
        }
    }

    private void validateCompany(String company) {
        final Pattern pattern =
                Pattern.compile(applicationPropertiesConfig.getCompanyRegexPattern());
        if (!pattern.matcher(company).matches()) {
            throw new SampleException(Constants.INVALID_COMPANY, company);
        }
    }
}
