package com.to8to.executor;

import com.google.common.base.Strings;
import com.to8to.MavenContext;
import com.to8to.rpc.annotation.API;
import com.to8to.template.model.ControllerModel;
import com.to8to.template.model.ControllerModel.MethodModel;
import com.to8to.util.StringUtils;
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
import java.lang.reflect.Parameter;
import java.util.*;

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

        Set<Class> interfaceClzz = new HashSet<>();
        methodWithAPIAnno.forEach(method -> interfaceClzz.add(method.getDeclaringClass()));

        buildController0(ctx, freemarkerConfig, interfaceClzz, reflections);

//        Thread.currentThread().setContextClassLoader(pluginClzLoader);


        return true;
    }

    private void buildController0(MavenContext ctx, Configuration freemarkerConfig,
                                  Set<Class> interfaceClzz, Reflections reflections) {

        for (Class interfaceClz : interfaceClzz) {

            ControllerModel cModel = createControllerModel(ctx, interfaceClz, reflections);

        }


    }

    private ControllerModel createControllerModel(MavenContext ctx, Class interfaceClz, Reflections reflections) {
        ControllerModel cModel = new ControllerModel();
        cModel.setBasePackage(ctx.getBasePackage());

        cModel.setClassName(parseControllerName(interfaceClz));
        cModel.setApiDesciption("XXXXXX");

        Class<?> implementClazz = (Class) reflections.getSubTypesOf(interfaceClz).iterator().next();
        String serviceName = getClassName(implementClazz.getName());
        cModel.setServiceClz(serviceName);
        cModel.setServiceVar(lowerFirst(serviceName));

        cModel.getImports().add(implementClazz.getName());

        cModel.setStoregePath(buildStoregePath(implementClazz.getPackage().getName(), ctx.getBasePkgPath()));

        String commonReqMapping = buildBaseRequestMapping(interfaceClz);
        cModel.setBaseRequestMapping(commonReqMapping);

        for (Method method : interfaceClz.getMethods()) {
            MethodModel methodModel = new MethodModel();
            methodModel.setRequestMapping(StringUtils.stripCommonPrefix(commonReqMapping,getRequestMappingByMethod(method)));
            methodModel.setApiDesc("XXXXX");
            methodModel.setResponseClassType(getClassName(method.getReturnType().getName()));
            methodModel.setName(method.getName());

            Parameter[] parameters = method.getParameters();
            if(parameters.length==1){
                Parameter parameter = parameters[0];

            }


            cModel.getMethodModels().add(methodModel);
        }


        MethodModel methodModel = new MethodModel();
        methodModel.setDtoClassType("ConstantCreateDtoWrapper");
        methodModel.setDtoVar("constantCreateDtoWrapper");
        methodModel.setParamMethods(Arrays.asList("getConstant", "getInteger"));


        return cModel;
    }

    private String buildBaseRequestMapping(Class interfaceClz) {
        Method[] methods = interfaceClz.getMethods();

        if (methods.length == 0) {
            return "";
        }

        if (methods.length == 1) {
            return getRequestMappingByMethod(methods[0]);
        }

        String commonPrefix = getRequestMappingByMethod(methods[0]);
        for (int i = 1; i < methods.length; i++) {
            commonPrefix = Strings.commonPrefix(commonPrefix, getRequestMappingByMethod(methods[i]));
        }

        return commonPrefix;
    }

    private String getRequestMappingByMethod(Method method) {
        API api = method.getAnnotation(API.class);
        return "/" + api.value().replace(".", "/");
    }

    private String buildStoregePath(String packageName, String basePkgPath) {
        try {
            String[] split = packageName.split(".");
            if (split.length > 4) {
                if (!split[3].equalsIgnoreCase("service")) {
                    return basePkgPath + "/" + split[3] + "/controller";
                } else {
                    return basePkgPath + "/" + split[4] + "/controller";
                }
            } else {
                return basePkgPath + "/controller";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return basePkgPath + "/controller";
        }
    }

    private String parseControllerName(Class interfaceClz) {
        String interfaceName = getClassName(interfaceClz.getName());

        String controllClassName = interfaceName.startsWith("I") ? interfaceName.substring(1) : interfaceName;

        controllClassName = controllClassName.endsWith("Service") ?
                controllClassName.substring(0, controllClassName.indexOf("Service")) : controllClassName;

        return controllClassName + "Controller";
    }

    private String getClassName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    private String lowerFirst(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;

        return String.valueOf(chars);
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
