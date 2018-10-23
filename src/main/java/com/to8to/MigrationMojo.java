package com.to8to;

import com.to8to.executor.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "migrate")
@Execute(lifecycle = "package")
public class MigrationMojo extends AbstractMojo {

    @Component
    protected MavenProject project;

    @Component
    private Settings settings;

    @Override
    public void execute() throws MojoExecutionException {
        Log logger = getLog();

        ExecutorPipeline pipeline = new ExecutorPipeline();
        pipeline.addExecutor(new ProjectValidator(logger));
        pipeline.addExecutor(new FreemarkerConfigCustomizer());
        pipeline.addExecutor(new MvnContextBuilder(logger));
        pipeline.addExecutor(new ProjectStructureRefactor(logger));
        pipeline.addExecutor(new ControllerBuilder(logger));
        pipeline.addExecutor(new FeignBuilder(logger));
        pipeline.addExecutor(new FileMovementExecutor(logger));
        pipeline.addExecutor(new PomRefactor(logger));

        Configuration freeMarkerConfiguration = new Configuration(new Version(2, 3, 28));
        MavenContext mvnCtx = new MavenContext();
        mvnCtx.setProject(project);

        pipeline.invoke(freeMarkerConfiguration, mvnCtx);


      /*  File basePkgFile = new File(javaSrcDir, basePackage);
        if (!basePkgFile.exists()) {
            basePkgFile.mkdirs();
        }

        File controllerFile = new File(basePkgFile, "controller");

        outputController(controllerFile, basePackage);*/

    }

    private void outputController(File controllerFile, String basePkgPath) {

        String TEMPLATE_PATH = "/com/to8to/template/";

        if (!controllerFile.exists()) {
            controllerFile.mkdir();
        }

        Configuration configuration = new Configuration(new Version(2, 3, 28));
        Writer out = null;
        try {


            // step3 创建数据模型
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("classPath", basePkgPath + ";");
            dataMap.put("className", "AutoCodeDemo");
            dataMap.put("helloWorld", "通过简单的 <代码自动生产程序> 演示 FreeMarker的HelloWorld！");
            // step4 加载模版文件
            Template template = configuration.getTemplate("hello.ftl", "UTF-8");
            // step5 生成数据
            File docFile = new File(controllerFile, "AutoCodeDemo.java");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile), Charset.forName("UTF-8")));
            // step6 输出文件
            template.process(dataMap, out);
            System.out.println("AutoCodeDemo.java 文件创建成功 !");
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
