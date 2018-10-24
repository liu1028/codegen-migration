package com.to8to;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by senix.liu on 2018/10/23.
 */
public class SimpleTest {

    @Test
    public void test1(){

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
        if(file1.exists()){
            System.out.println("文件存在");
            return;
        }
    }
}
