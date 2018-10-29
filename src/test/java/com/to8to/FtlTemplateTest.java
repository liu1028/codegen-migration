package com.to8to;

import com.google.common.collect.Lists;
import com.to8to.template.model.ControllerModel;
import com.to8to.template.model.ControllerModel.MethodModel;
import com.to8to.template.model.DtoWrapperModel;
import com.to8to.template.model.DtoWrapperModel.DtoElement;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by senix.liu on 2018/10/29.
 */
public class FtlTemplateTest {

    private final String TEMPLATE_PATH = "/com/to8to/template/";

    @Test
    public void testWrapperModel() throws IOException {
        DtoWrapperModel dtoWrapperModel = new DtoWrapperModel();
        dtoWrapperModel.setBasePackage("com.to8to.sys.exp");
        dtoWrapperModel.setImports(
                Arrays.asList("com.to8to.sys.exp.entity.dto.ConstantCreateDTO",
                        "javax.validation.constraints.NotNull")
        );
        dtoWrapperModel.setDtoClassType("ConstantCreateDtoWrapper");

        List<DtoElement> elements=new ArrayList<>();
        DtoElement e1=new DtoElement();
        e1.setClassType("ConstantCreateDTO");
        e1.setClassVar("constantCreateDTO");
        e1.setValidations(Arrays.asList("NotNull","Min(1)","Max(100)"));

        DtoElement e2=new DtoElement();
        e2.setClassType("Double");
        e2.setClassVar("constantValue");
        e2.setValidations(Arrays.asList("Length(max = 100)","NotNull(message = \"适用范围不能为空\")"));
//        e2.setValidations(Lists.newArrayList());
        elements.add(e1);
        elements.add(e2);

        dtoWrapperModel.setDtoElements(elements);

        renderAndMakeFile("dtowrapper.ftl",
                new File("E:\\local-workspace\\migration-maven-plugin\\src\\test\\resources\\output\\model.java"),
                dtoWrapperModel);
    }

    @Test
    public void testControllerRenderResult() throws IOException {
        ControllerModel cModel = new ControllerModel();
        cModel.setBasePackage("com.to8to.sys.exp");
        cModel.setBaseRequestMapping("/constant/");
        cModel.setApiDesciption("常量操作接口相关");
        cModel.setClassName("ConstantController");
        cModel.setServiceClz("ConstantService");
        cModel.setServiceVar("constantService");

        List<MethodModel> methodModels = new ArrayList<>();
        MethodModel methodModel = new MethodModel();
        methodModel.setRequestMapping("create");
        methodModel.setApiDesc("新建常量");
        methodModel.setResponseClassType("Integer");
        methodModel.setName("create");
        methodModel.setDtoClassType("ConstantCreateDtoWrapper");
        methodModel.setDtoVar("constantCreateDtoWrapper");
//        methodModel.setParamMethods(Arrays.asList("getConstant", "getInteger"));
        methodModel.setParamMethods(Lists.newArrayList());
        methodModels.add(methodModel);

        cModel.setMethodModels(methodModels);

        renderAndMakeFile("controller.ftl",
                new File("E:\\local-workspace\\migration-maven-plugin\\src\\test\\resources\\output\\controller.java"),
                cModel);
    }

    private void renderAndMakeFile(String templateName,File outputFile, Object modelData) throws IOException {
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        Configuration configuration = new Configuration(new Version(2, 3, 28));
        Writer out = null;

        try {

            // step1 获取模版路径
            configuration.setClassForTemplateLoading(FtlTemplateTest.class, TEMPLATE_PATH);
            configuration.setDefaultEncoding("UTF-8");
//            configuration.setEncoding(Locale.US,"UTF-8");
//            configuration.setClassForTemplateLoading(Mojo.class.getClassLoader().getClass(),TEMPLATE_PATH);
//            configuration.setDirectoryForTemplateLoading(new File(resource.toURI())/*new JarFile(TEMPLATE_PATH)*/);

            // step2 加载模版文件
            Template template = configuration.getTemplate(templateName, "UTF-8");

            // step3 生成数据
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), Charset.forName("UTF-8")
            ));

            // step4 输出文件
            template.process(modelData, out);

            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^AutoCodeDemo.java 文件创建成功 !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                    out.close();

                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
