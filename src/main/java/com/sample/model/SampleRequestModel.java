package com.sample.model;

import com.sample.constants.UserTypes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SampleRequestModel {

    private String name;
    private int age;
    private String company;
    private String mobile;
    private UserTypes userType;
}
