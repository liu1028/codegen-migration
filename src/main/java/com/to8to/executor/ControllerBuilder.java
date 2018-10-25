package com.to8to.executor;

import com.to8to.MavenContext;
import com.to8to.process.MyClassLoader;
import freemarker.template.Configuration;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * Created by senix.liu on 2018/10/22.
 */
public class ControllerBuilder implements Executor {

    private Log log;

    public ControllerBuilder(Log log) {
        this.log = log;
    }

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) throws MojoFailureException {

        String projcetName = ctx.getProjcetName();
        String oldBasePkg = projcetName.replace("-", ".");

        ConfigurationBuilder configBuilder =
                new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(oldBasePkg, ctx.getClzLoader()))
                        .filterInputsBy(new FilterBuilder().excludePackage(oldBasePkg + ".mapper.xml"))
                        .setScanners(
                                new SubTypesScanner(),
                                new TypeAnnotationsScanner(),
                                new MethodAnnotationsScanner());


        Reflections reflections = new Reflections(configBuilder);

        MyClassLoader clzLoader = ctx.getClzLoader();
        try {
            Class<?> apiAnnotateClz = clzLoader.loadClass("com.to8to.rpc.annotation.Api");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        reflections.getMethodsAnnotatedWith();

        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
