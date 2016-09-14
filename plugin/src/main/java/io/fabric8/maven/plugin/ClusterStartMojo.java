/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.fabric8.maven.plugin;

import io.fabric8.utils.Strings;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Creates a new local kubernetes or openshift cluster for development.
 *
 * Also ensures that gofabric8 and related tools are installed.
 */
@Mojo(name = "cluster-start", requiresProject = false)
public class ClusterStartMojo extends AbstractInstallMojo {

    /**
     * Defines the kind of cluster such as `minishift`
     */
    @Parameter(property = "fabric8.cluster.kind")
    private String clusterKind;

    /**
     * Which app to startup such as <code>console</code>
     */
    @Parameter(property = "fabric8.cluster.app")
    private String clusterApp;

    /**
     * How many CPUs to give the cluster VM
     */
    @Parameter(property = "fabric8.cluster.cpus")
    private String clusterCPUs;

    /**
     * How many MBs of RAM to give the cluster VM such as <code>4096</code> for 4Gb
     */
    @Parameter(property = "fabric8.cluster.memory")
    private String clusterMemory;


    @Override
    public void executeInternal() throws MojoExecutionException, MojoFailureException {
        File gofabric8 = installBinaries();

        String arguments = "";
        if (Strings.isNotBlank(clusterKind)) {
            String text = clusterKind.toLowerCase().trim();
            if (text.equals("minishift") || text.equals("openshift")) {
                arguments += " --minishift";
            } else {
                // lets assume its valid and let gofabric8 fail
                arguments += " --" + clusterKind;
            }
        }
        if (Strings.isNotBlank(clusterApp)) {
            arguments += " --" + clusterApp;
        }
        if (Strings.isNotBlank(clusterCPUs)) {
            arguments += " --cpus " + clusterCPUs;
        }
        if (Strings.isNotBlank(clusterMemory)) {
            arguments += " --memory " + clusterMemory;
        }
        String message = "gofabric8 start" + arguments;
        log.info("running: " + message);
        runCommand(gofabric8.getAbsolutePath() + " start" + arguments, message);
    }

}
