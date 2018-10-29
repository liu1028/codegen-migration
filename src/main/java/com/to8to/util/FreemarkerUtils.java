package com.to8to.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by senix.liu on 2018/10/29.
 */
public class FreemarkerUtils {

    public static void render(Configuration ftlConfig, String templateName, File outputFile, Object modelData){

        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Writer out = null;
        try {

            Template template = ftlConfig.getTemplate(templateName, "UTF-8");

            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), Charset.forName("UTF-8")
            ));

            template.process(modelData, out);

            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^"+outputFile.getName()+".java 创建成功 ^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                    out.close();

                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
}
