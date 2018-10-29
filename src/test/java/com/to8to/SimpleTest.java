package com.to8to;

import com.to8to.rpc.annotation.API;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by senix.liu on 2018/10/23.
 */
public class SimpleTest {

    @Test
    public void test1() {

        String replace = "t8t-sys-pms".replace("-", ".");

        System.out.println(replace);

    }

    @Test
    public void testFile() throws MalformedURLException {
        URL url = new URL("file:D:/javac/SimpleJava.class");
        String file = url.getFile();
        File file1 = new File(file);

//        File file1=new File("D:\\javac\\SimpleJava.class");
//        String s = file1.toURI().toURL().toString();
        if (file1.exists()) {
            System.out.println("文件存在");
            return;
        }
    }

    @Test
    public void testReflection() {
        File classesDir = new File("E:\\git-workspace\\t8t-scm-ldm\\t8t-scm-ldm-server\\target\\classes");
        File libDir = new File("E:\\git-workspace\\t8t-scm-ldm\\t8t-scm-ldm-server\\target\\lib");
        com.to8to.process.MyClassLoader myClassLoader = new com.to8to.process.MyClassLoader();
        myClassLoader.tryLoadClassInDir(classesDir);
        myClassLoader.tryLoadJarInDir(libDir);

        URL[] urLs = myClassLoader.getURLs();
        Stream.of(urLs).forEach(url -> System.out.println(url.toString()));

        Collection<URL> urls = ClasspathHelper.forPackage("t8t.scm.ldm", myClassLoader);
        urls.forEach(url -> System.out.println(url.toString()));

        ClassLoader pluginClzLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(myClassLoader);
        ConfigurationBuilder configBuilder =
                new ConfigurationBuilder()
                        .setUrls(urls)
                        .filterInputsBy(new FilterBuilder()
                                .exclude("t8t/scm/ldm.*\\.xml")
//                                .excludePackage("t8t.scm.ldm[.|a-z|A-Z]*.mapper[.|a-z|A-Z]*.xml")
                                .excludePackage("META-INF"))
                        .setScanners(
                                new SubTypesScanner(),
                                new TypeAnnotationsScanner(),
                                new MethodAnnotationsScanner());


        Reflections reflections = new Reflections(configBuilder);


        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(API.class);
        System.out.println("----------------------method:------------------");
        methodsAnnotatedWith.forEach(method -> System.out.println(method.getDeclaringClass().getName()));

        Thread.currentThread().setContextClassLoader(pluginClzLoader);

    }

    @Test
    public void testReg() {
        Pattern compile = Pattern.compile("t8t/scm/ldm.*\\.xml");

        boolean matches = compile.matcher("t8t/scm/ldm/conf/mapper/xml/ExpressPriceMapper.xml").matches();

        System.out.println(matches);
    }

    @Test
    public void testArray(){

    }
}
