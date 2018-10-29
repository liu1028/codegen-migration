package com.to8to.template.model;

import lombok.Data;

import java.util.List;

/**
 * Created by senix.liu on 2018/10/27.
 */
@Data
public class ControllerModel {

    private String basePackage;

    private String apiDesciption;

    private String className;

    private String serviceClz;

    private String serviceVar;

    private List<MethodModel> methodModels;

    @Data
    class MethodModel{

        private String requestMapping;

        private String apiDesc;

        private String responseClassType;

        private String name;

        private String dtoClassType;

        private String dtoVar;

//        private String paramMethods;
    }

}
