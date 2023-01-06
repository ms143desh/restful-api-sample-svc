package com.sample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class ApplicationPropertiesConfig {

    @Value("${name.regex.pattern}")
    private String nameRegexPattern;

    @Value("${minAge}")
    private int minAge;

    @Value("${company.regex.pattern}")
    private String companyRegexPattern;

    @Value("${svcErrorPrefix}")
    private String svcErrorPrefix;
}
