package com.to8to.template.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Created by senix.liu on 2018/10/27.
 */
@Data
public class ControllerModel {

    private String storegePath;

    private String basePackage;

    private String baseRequestMapping;

    private String apiDesciption;

    private String className;

    private String serviceClz;

    private String serviceVar;

    private List<String> imports = Lists.newArrayList();

    private List<MethodModel> methodModels = Lists.newArrayList();

    @Data
    public static class MethodModel {

        private String requestMapping;

        private String apiDesc;

        private String responseClassType;

        private String name;

        private String dtoClassType;

        private String dtoVar;

        private List<String> paramMethods= Lists.newArrayList();


    }


}
