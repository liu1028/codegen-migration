package com.to8to.executor;

import com.to8to.MavenContext;
import com.to8to.MigrationMojo;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;

/**
 * Created by senix.liu on 2018/10/23.
 */
public class FreemarkerConfigCustomizer implements Executor {

    private static final String TEMPLATE_PATH = "/com/to8to/template/";

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) {
        freemarkerConfig.setClassForTemplateLoading(MigrationMojo.class, TEMPLATE_PATH);
        freemarkerConfig.setDefaultEncoding("UTF-8");
//            configuration.setEncoding(Locale.US,"UTF-8");
//            configuration.setClassForTemplateLoading(Mojo.class.getClassLoader().getClass(),TEMPLATE_PATH);
//            configuration.setDirectoryForTemplateLoading(new File(resource.toURI())/*new JarFile(TEMPLATE_PATH)*/);

        return true;
    }

    @Override
    public void setLog(Log log) {
        //do nothing
    }
}
