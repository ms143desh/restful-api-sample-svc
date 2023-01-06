package com.sample.controller;

import com.sample.model.SampleRequestModel;
import com.sample.service.SampleService;
import com.sample.validation.SampleRequestValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired private SampleRequestValidator sampleRequestValidator;

    @Autowired private SampleService sampleService;

    @InitBinder("sampleRequestModel")
    public void initSubscriptionBinder(WebDataBinder binder) {
        binder.addValidators(sampleRequestValidator);
    }

    @GetMapping(
            value = "/",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    // @PreAuthorize("#oauth2.hasScope('lc.svc.r')")
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<>("HelloWorld Sample Project!", HttpStatus.OK);
    }

    @GetMapping(
            value = "/sample/{name}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    // @PreAuthorize("#oauth2.hasScope('lc.svc.r')")
    public ResponseEntity<String> helloName(@PathVariable String name, HttpServletRequest request) {

        return new ResponseEntity<>("Hello ".concat(name), HttpStatus.OK);
    }

    @PostMapping(
            value = "/sample",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    // @PreAuthorize("#oauth2.hasScope('lc.svc.w')")
    public ResponseEntity<String> postSampleRequest(
            @Valid @RequestBody SampleRequestModel sampleRequestModel, HttpServletRequest request) {

        Document document = sampleService.saveSampleRequest(sampleRequestModel);
        return new ResponseEntity<>(document.toJson(), HttpStatus.OK);
    }

    @PostMapping(
            value = "/sample/static-encrypted",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    // @PreAuthorize("#oauth2.hasScope('lc.svc.w')")
    public ResponseEntity<SampleRequestModel> postStaticEncryptedRequest(
            HttpServletRequest request) {

        sampleService.saveStaticEncryptedRequest();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
