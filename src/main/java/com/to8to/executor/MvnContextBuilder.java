package com.to8to.executor;

import com.google.common.base.Strings;
import com.to8to.MavenContext;
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
    public void action(Configuration freemarkerConfig, MavenContext ctx) {
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
    }

    @Override
    public void setLog(Log log) {
        this.log = log;
    }
}