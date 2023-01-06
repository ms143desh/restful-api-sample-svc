package com.sample.service;

import com.sample.database.service.SampleDatabaseService;
import com.sample.model.SampleRequestModel;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

    @Autowired private SampleDatabaseService sampleDatabaseService;

    public Document saveSampleRequest(SampleRequestModel sampleRequestModel) {
        logger.info(
                "{} {} {}",
                sampleRequestModel.getName(),
                sampleRequestModel.getAge(),
                sampleRequestModel.getCompany());

        sampleDatabaseService.saveSampleRequest(sampleRequestModel);
        return sampleDatabaseService.getSavedSampleRequest();
    }

    public void saveStaticEncryptedRequest() {
        sampleDatabaseService.saveEncryptedField();
    }
}
