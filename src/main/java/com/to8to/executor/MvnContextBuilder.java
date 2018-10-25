package com.to8to.executor;

import com.google.common.base.Strings;
import com.to8to.MavenContext;
import com.to8to.process.MyClassLoader;
import freemarker.template.Configuration;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 *
 * @author senix.liu
 * @date 2018/10/22
 */
public class MvnContextBuilder implements Executor {

    private Log log;

    public MvnContextBuilder(Log log) {
        this.log = log;
    }

    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) {
        MavenProject project = ctx.getProject();

        File basedir = project.getBasedir();
        String workspacePath = basedir.getAbsolutePath();
        ctx.setWorkspacePath(workspacePath);

        String artifactId = project.getArtifactId();
        String changedArtId = artifactId.replace("-", ".");

        int pos = changedArtId.indexOf(".");
        String suffixPkg = pos != -1 ? changedArtId.substring(pos + 1) : "";
        String basePackage = "com.to8to" + (Strings.isNullOrEmpty(suffixPkg) ? "" : "." + suffixPkg);
        ctx.setBasePackage(basePackage);
        ctx.setBasePkgPath("/" + basePackage.replace(".", "/"));

        ctx.setProjcetName(project.getArtifactId());

        File serverModuleDir = new File(project.getArtifactId() + "-server");
        File classesDir = new File(serverModuleDir, "target/classes/");
        File libDir = new File(serverModuleDir, "lib/");
        MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.tryLoadClassInDir(classesDir);
        myClassLoader.tryLoadJarInDir(libDir);
        ctx.setClzLoader(myClassLoader);

        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}
