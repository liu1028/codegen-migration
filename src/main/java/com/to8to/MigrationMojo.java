package com.to8to;

import com.to8to.executor.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "migrate")
//@Execute(lifecycle = "package")
public class MigrationMojo extends AbstractMojo {

    @Component
    protected MavenProject project;

    @Component
    private Settings settings;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log logger = getLog();

        ExecutorPipeline pipeline = new ExecutorPipeline();
        pipeline.addExecutor(new ProjectValidator(logger));
        pipeline.addExecutor(new FreemarkerConfigCustomizer());
        pipeline.addExecutor(new MvnContextBuilder(logger));
//        pipeline.addExecutor(new ProjectStructureRefactor(logger));
        pipeline.addExecutor(new ControllerBuilder(logger));
        pipeline.addExecutor(new FeignBuilder(logger));
        pipeline.addExecutor(new FileMovementExecutor(logger));
        pipeline.addExecutor(new PomRefactor(logger));

        Configuration freeMarkerConfiguration = new Configuration(new Version(2, 3, 28));
        MavenContext mvnCtx = new MavenContext();
        mvnCtx.setProject(project);

        pipeline.invoke(freeMarkerConfiguration, mvnCtx);

    }

}
