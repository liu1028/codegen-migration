package com.to8to.template.model;

import lombok.Data;

import java.util.List;

/**
 * Created by senix.liu on 2018/10/29.
 */
@Data
public class DtoWrapperModel {

    private String basePackage;

    private List<String> imports;

    private String dtoClassType;

    private List<DtoElement> dtoElements;

    @Data
    public static class DtoElement {

        private List<String> validations;

        private String classType;

        private String classVar;
    }
}
