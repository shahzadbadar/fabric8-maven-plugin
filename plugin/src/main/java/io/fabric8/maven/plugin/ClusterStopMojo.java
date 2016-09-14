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

import static org.eclipse.jgit.transport.TransportProtocol.URIishField.PATH;

/**
 * Stops the local kubernetes or openshift cluster.
 *
 * All data and images in the cluster are preserved and will be available
 * after you run <code>mvn fabric8:cluster-start</code>
 */
@Mojo(name = "cluster-stop", requiresProject = false)
public class ClusterStopMojo extends AbstractInstallMojo {

    @Override
    public void executeInternal() throws MojoExecutionException, MojoFailureException {
        File gofabric8 = findExecutable(GOFABRIC8);
        if (gofabric8 == null) {
            throw new MojoFailureException("No binary `" + GOFABRIC8 + "` found on the PATH. Did you create the cluster via `mvn fabric8:cluster-start`?");
        }
        if (!gofabric8.isFile() || !gofabric8.exists() || !gofabric8.canExecute()) {
            throw new MojoFailureException("File " + gofabric8.getAbsolutePath() + " is not an executable file. Did you create the cluster via `mvn fabric8:cluster-start`?");
        }
        String arguments = "";
        // TODO have an option to stop and delete?
        String cli = " stop";
        String message = "gofabric8" + cli;
        log.info("running: " + message);
        runCommand(gofabric8.getAbsolutePath() + cli, message);
    }

}
