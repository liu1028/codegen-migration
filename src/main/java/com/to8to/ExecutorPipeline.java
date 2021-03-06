package com.to8to;

import com.to8to.executor.Executor;
import freemarker.template.Configuration;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senix.liu on 2018/10/22.
 */
public class ExecutorPipeline {

    private List<Executor> executors = new LinkedList<>();

    public void invoke(Configuration freemarkerConfig, MavenContext ctx) throws MojoFailureException{
        Iterator<Executor> iterator = executors.iterator();

        while (iterator.hasNext()) {
            Executor next = iterator.next();

            if(!next.action(freemarkerConfig, ctx)){
                return ;
            }
        }
    }

    public void addExecutor(Executor executor) {
        this.executors.add(executor);
    }

    public void removeExecutor(Executor executor) {
        this.executors.remove(executor);
    }
}
