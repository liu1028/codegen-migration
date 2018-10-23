package com.to8to.executor;

import com.to8to.MavenContext;
import com.to8to.executor.Executor;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;

/**
 * Created by senix.liu on 2018/10/23.
 */
public class ProjectValidator implements Executor {

    private Log log;

    public ProjectValidator(Log logger) {
        this.log=logger;
    }


    @Override
    public void action(Configuration freemarkerConfig, MavenContext ctx) {

    }

    @Override
    public void setLog(Log log) {
        this.log=log;
    }
}
