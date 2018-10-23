package com.to8to.executor;

import com.to8to.MavenContext;
import freemarker.template.Configuration;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * Created by senix.liu on 2018/10/22.
 */
public class FeignBuilder implements Executor{

    private Log log;

    public FeignBuilder(Log log) {
        this.log = log;
    }

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) throws MojoFailureException {
        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
