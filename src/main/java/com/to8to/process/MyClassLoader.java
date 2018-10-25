package com.to8to.process;

import java.io.File;
import java.io.IOException;
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
        tryLoadClassInDir(dir);
    }

    public void tryLoadClassInDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            this.addURL(dir, true);
        }
    }

    public void tryLoadJarInDir(String dirPath) {
        File dir = new File(dirPath);
        tryLoadClassInDir(dir);
    }

    public void tryLoadJarInDir(File dir) {
        // 自动加载目录下的jar包
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    this.addURL(file, false);
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

}
