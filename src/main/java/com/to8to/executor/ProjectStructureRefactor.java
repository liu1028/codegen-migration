package com.to8to.executor;

import com.to8to.MavenContext;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author senix.liu
 * @date 2018/10/22
 */
public class ProjectStructureRefactor implements Executor {

    private static final String MAVEN_MAIN_JAVA_PATH = "/src/main/java";

    private static final String MAVEN_MAIN_RESOURCE_PATH = "/src/main/resources";

    private static final HashMap<String, List<String>> srcStructure = new HashMap<String, List<String>>() {{
        put("common", null);
        put("configuration", null);
        put("controller", null);
        put("entity", Arrays.asList("dto", "dtowrapper", "vo"));
        put("mapper", Arrays.asList("xml"));
        put("service", Arrays.asList("component", "impl", "external", "task"));
        put("utils", null);
    }};

    private HashMap<String, String> resourceStructure = new HashMap<String,String>(){{
       put("META-INF","app.properties");
    }};

    private Log log;

    public ProjectStructureRefactor(Log log) {
        this.log = log;
    }

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) {

        String workspacePath = ctx.getWorkspacePath();

        buildJavaStructure(mkdirs(workspacePath, MAVEN_MAIN_JAVA_PATH), ctx);

        buildJavaResource(mkdirs(workspacePath, MAVEN_MAIN_RESOURCE_PATH), ctx);

        return true;
    }

    private void buildJavaStructure(File javaSrcDirFile, MavenContext ctx) {
        String basePkgPath = ctx.getBasePkgPath();

        File basePkgFile = mkdirs(javaSrcDirFile, basePkgPath);

        srcStructure.forEach((pDir, childDirs) -> {

            File pFile = mkdirs(basePkgFile, pDir);

            if (childDirs != null) {
                childDirs.stream().forEach(childPath -> mkdirs(pFile, childPath));
            }

        });
    }

    private void buildJavaResource(File javaResourceDirFile, MavenContext ctx) {

        resourceStructure.forEach((pDir,childFile)->{
            File pFile = mkdirs(javaResourceDirFile, pDir);

            mkFile(pFile,childFile);
        });

        mkFile(javaResourceDirFile,"application.yml");
    }

    private File mkdirs(String prefixPath, String path) {
        return mkdirs(new File(prefixPath), path);
    }

    private File mkdirs(File prefixFile, String path) {
        File pDirFile = new File(prefixFile, path);
        if (!pDirFile.exists()) {
            pDirFile.mkdirs();
        }

        return pDirFile;
    }

    private File mkFile(File prefixFile, String path) {
        File pDirFile = new File(prefixFile, path);
        if (!pDirFile.exists()) {
            try {
                pDirFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pDirFile;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
