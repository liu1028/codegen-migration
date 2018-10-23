package com.to8to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.maven.project.MavenProject;

/**
 * Created by senix.liu on 2018/10/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MavenContext {

    private MavenProject project;

    private String workspacePath;

    private String basePkgPath;

    private String basePackage;

    private String projcetName;
}
