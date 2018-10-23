package com.to8to;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Settings;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "hello")
public class MyMojoTest extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir")
    private File outputDirectory;

    @Component
    protected MavenProject project;

    @Component
    private Settings settings;

    @Override
    public void execute() throws MojoExecutionException {

        File basedir = project.getBasedir();
        String userDir = basedir.getAbsolutePath();

        String javaSrcDir=userDir+"/src/main/java";

        String groupId=project.getGroupId();
        String basePackage="com.to8to."+groupId;

        File basePkgFile=new File(javaSrcDir,basePackage);
        if(!basePkgFile.exists()){
            basePkgFile.mkdirs();
        }

        File controllerFile=new File(basePkgFile,"controller");

        outputController(controllerFile,basePackage);

    }

    private void outputController(File controllerFile,String basePkgPath) {

        String TEMPLATE_PATH = "/com/to8to/template/";

        if(!controllerFile.exists()){
            controllerFile.mkdir();
        }

        Configuration configuration=new Configuration(new Version(2,3,28));
        Writer out = null;
        try {

            // step2 获取模版路径
            configuration.setClassForTemplateLoading(MigrationMojo.class,TEMPLATE_PATH);
            configuration.setDefaultEncoding("UTF-8");
//            configuration.setEncoding(Locale.US,"UTF-8");
//            configuration.setClassForTemplateLoading(Mojo.class.getClassLoader().getClass(),TEMPLATE_PATH);
//            configuration.setDirectoryForTemplateLoading(new File(resource.toURI())/*new JarFile(TEMPLATE_PATH)*/);

            // step3 创建数据模型
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("classPath", basePkgPath+";");
            dataMap.put("className", "AutoCodeDemo");
            dataMap.put("helloWorld", "通过简单的 <代码自动生产程序> 演示 FreeMarker的HelloWorld！");
            // step4 加载模版文件
            Template template = configuration.getTemplate("hello.ftl","UTF-8");
            // step5 生成数据
            File docFile = new File(controllerFile, "AutoCodeDemo.java");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile), Charset.forName("UTF-8")));
            // step6 输出文件
            template.process(dataMap, out);
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

    private void printSomeInfos(){
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        String version = project.getVersion();
        getLog().info(String.format("groupId:%s, artifactId:%s, version:%s",groupId,artifactId,version));

        String spring_version = project.getProperties().getProperty("spring_version");
        getLog().info("spring-version: "+spring_version);

        try {
            String canonicalPath = project.getBasedir().getCanonicalPath();
            getLog().info("workspace path: "+canonicalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dependency dependency = project.getDependencyManagement().getDependencies().get(2);
        String dArtId = dependency.getArtifactId();
        String dVersion = dependency.getVersion();
        getLog().info("the third dependency of management info, artId:"+dArtId+", version: "+dVersion);

        Mirror mirror = settings.getMirrors().get(0);
        getLog().info("user's setting for  mirror el, url: "+mirror.getUrl()+" ,mirrorOf: "+mirror.getMirrorOf());

    }


    private void doSomeWithOutputDir() throws MojoExecutionException {
        File f = outputDirectory;

        getLog().info("outputDir: "+outputDirectory);

        if (!f.exists()) {
            f.mkdirs();
            getLog().info("create targert success...");
        }

        File touch = new File(f, "touch.txt");

        FileWriter w = null;
        try {
            w = new FileWriter(touch);

            w.write("touch.txt");

            getLog().info("create touch.txt into targert success...");

        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + touch, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
