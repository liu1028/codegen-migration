package com.to8to.executor;

import com.to8to.MavenContext;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;

/**
 * Created by senix.liu on 2018/10/22.
 */
public class PomRefactor implements Executor{
    private Log log;

    public PomRefactor(Log log){
        this.log=log;
    }

    @Override
    public void action(Configuration freemarkerConfig, MavenContext ctx) {

    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
