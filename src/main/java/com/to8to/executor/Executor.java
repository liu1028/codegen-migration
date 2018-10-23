package com.to8to.executor;

import com.to8to.MavenContext;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;

/**
 * Created by senix.liu on 2018/10/22.
 */
public interface Executor {

    void action(Configuration freemarkerConfig,MavenContext ctx);

    void setLog( Log log );
}
