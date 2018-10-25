package com.to8to;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by senix.liu on 2018/10/24.
 */
public class MyClassLoader extends URLClassLoader {

    public MyClassLoader() {
        super(new URL[]{});
    }

    public MyClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {


        return super.findClass(name);
    }

    public void tryLoadClassesInDir(String dirPath) {
        File dir = new File(dirPath);
        // 自动加载目录下的jar包
        if (dir.exists() && dir.isDirectory()) {
            this.addURL(dir, true);
        }
    }

    public void tryLoadJarInDir(String dirPath) {
        File dir = new File(dirPath);
        // 自动加载目录下的jar包
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    this.addURL(dir, false);
                    continue;
                }
            }
        }
    }

    private void addURL(File file, boolean isDir) {
        try {
            URL url = new URL("file", null, file.getCanonicalPath() + (isDir ? "/" : ""));
            super.addURL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws MalformedURLException {
        String path = "D:\\javac";

        MyClassLoader myClassLoader = new MyClassLoader();
//        MyClassLoader myClassLoader = new MyClassLoader(new URL[]{new URL("file:D:/javac/SimpleJava")});

     /*   ClassLoader classLoader = myClassLoader.getClass().getClassLoader();
        System.out.println("MyClassLoader's classLoader:" + classLoader);

        ClassLoader parent = myClassLoader.getParent();
        System.out.println("MyClassLoader's parent:" + parent);

        if (parent != null) {
            System.out.println("MyClassLoader's parent's parent:" + parent.getParent());

            if(parent.getParent()!=null){
                System.out.println("MyClassLoader's parent's parent's parent:" + parent.getParent().getParent());
            }
        }*/


        myClassLoader.tryLoadClassesInDir("D:\\javac");

        try {

            Class<?> simpleJava = myClassLoader.loadClass("SimpleJava");
            Object o = simpleJava.newInstance();

//            Class<?> math = myClassLoader.loadClass("java.lang.Math");
//            Object o = math.newInstance();
//            Method sayHello = math.getMethod("min",int.class,int.class);
//            System.out.println(sayHello.invoke(null,4,5));

            Method sayHello = simpleJava.getMethod("sayHello");
            sayHello.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
