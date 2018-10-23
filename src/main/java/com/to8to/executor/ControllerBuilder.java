package com.to8to.executor;

import com.to8to.MavenContext;
import freemarker.template.Configuration;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

/**
 * Created by senix.liu on 2018/10/22.
 */
public class ControllerBuilder implements Executor{

    private Log log;

    public ControllerBuilder(Log log) {
        this.log = log;
    }

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) throws MojoFailureException {

        String projcetName = ctx.getProjcetName();

        File serverClassPath=new File(projcetName+"-server","target/classes");


        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
