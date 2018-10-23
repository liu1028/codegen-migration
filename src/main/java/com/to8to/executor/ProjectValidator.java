package com.to8to.executor;

import com.to8to.MavenContext;
import com.to8to.executor.Executor;
import freemarker.template.Configuration;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

/**
 * 检查是否在maven多项目的根路径（有.git文件夹存在）
 *
 * @author senix.liu
 * @date 2018/10/23
 */
public class ProjectValidator implements Executor {

    private Log log;

    public ProjectValidator(Log logger) {
        this.log=logger;
    }


    @Override
    public boolean action(Configuration freemarkerConfig, MavenContext ctx) throws MojoFailureException {
        MavenProject project = ctx.getProject();

        String artifactId = project.getArtifactId();
        if(artifactId.endsWith("-server") || artifactId.endsWith("-task")
                || artifactId.endsWith("-service") || artifactId.endsWith("-service-api")){

            log.info("如果你执行该命令或脚本不在RPC项目根目录，请更正！如果是，请忽略。。。");

            return false;
        }

        String packaging = project.getPackaging();
        if(!"pom".equalsIgnoreCase(packaging)){
            throw new MojoFailureException("该项目不是pom类型，请切换到RPC项目或者该项目的根路径执行命令！");
        }

        String serverName = project.getArtifactId()+"-server";
        List<String> modules = project.getModules();
        boolean serverExist = modules.stream().anyMatch(module -> module.endsWith("-server"));
        boolean serverStandard = modules.stream().anyMatch(module -> module.equalsIgnoreCase(serverName));
        if(!serverExist){
            throw new MojoFailureException("该项目没有server模块，断定不是RPC项目，该脚本不支持此项目迁移！");
        }
        if(!serverStandard){
            throw new MojoFailureException("该项目的server模块不与格式：[项目名-server]匹配，不是标准RPC项目！");
        }


        File serverTargetDir=new File(serverName,"target");
        if(!serverTargetDir.exists()){
            throw new MojoFailureException("该项目没有预先构建，请在根目录执行 mvn clean package -DskipTests 进行构建");
        }

        return true;
    }

    @Override
    public void setLog(Log log) {
        this.log=log;
    }
}
