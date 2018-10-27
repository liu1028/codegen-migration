package com.to8to.executor;

import com.to8to.MavenContext;
import com.to8to.rpc.annotation.API;
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

import java.lang.reflect.Method;
import java.util.Set;

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

        System.out.println("oldBasePkg: " + oldBasePkg);

        ClassLoader pluginClzLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(ctx.getClzLoader());
        ConfigurationBuilder configBuilder =
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(oldBasePkg, ctx.getClzLoader()))
                        .filterInputsBy(new FilterBuilder()
                                .excludePackage(oldBasePkg + ".mapper.xml")
                                .excludePackage("META-INF"))
                        .setScanners(
                                new SubTypesScanner(),
                                new TypeAnnotationsScanner(),
                                new MethodAnnotationsScanner());


        Reflections reflections = new Reflections(configBuilder);

        Set<Method> methodWithAPIAnno = reflections.getMethodsAnnotatedWith(API.class);

//        System.out.println("the set of methodAnnotated: " + methodsAnnotatedWith);
//        methodsAnnotatedWith.forEach(method -> System.out.println(method.getDeclaringClass().getName()));

        Thread.currentThread().setContextClassLoader(pluginClzLoader);

        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
