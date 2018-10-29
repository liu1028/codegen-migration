package com.to8to;

import com.to8to.template.model.ControllerModel;
import com.to8to.template.model.ControllerModel.MethodModel;
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
    public void testWrapperModel(){


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
        methodModel.setParamMethods(Arrays.asList("getConstant", "getInteger"));
        methodModels.add(methodModel);

        cModel.setMethodModels(methodModels);

        File file = new File("E:\\local-workspace\\migration-maven-plugin\\src\\test\\resources\\output\\controller.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        renderAndMakeFile(file, cModel);
    }

    private void renderAndMakeFile(File outputFile, Object modelData) {

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
            Template template = configuration.getTemplate("controller.ftl", "UTF-8");

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
