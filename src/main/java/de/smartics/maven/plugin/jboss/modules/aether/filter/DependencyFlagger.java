/*
 * Copyright 2013-2016 smartics, Kronseder & Reiner GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.smartics.maven.plugin.jboss.modules.aether.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

/**
 * Adds properties to artifacts of a dependency.
 */
public final class DependencyFlagger
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The name of the reject flag.
   */
  private static final String REJECT_FLAG = "smartics.collect.reject";

  // --- members --------------------------------------------------------------

  /**
   * A singleton to reuse.
   */
  public static final DependencyFlagger INSTANCE = new DependencyFlagger();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public DependencyFlagger()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Flags the artifact as being rejects.
   *
   * @param node the node of the dependency to flag.
   */
  public void flag(final DependencyNode node)
  {
    final Dependency dependency = node.getDependency();
    final Artifact artifact = dependency.getArtifact();
    node.setArtifact(flagRejection(artifact));
  }

  /**
   * Checks if the given dependency is flagged as its artifact being rejected.
   *
   * @param dependency the dependency to check.
   * @return <code>true</code> of the artifact of this dependency is rejected,
   *         <code>false</code> otherwise.
   */
  public boolean isFlagged(final Dependency dependency)
  {
    final Artifact artifact = dependency.getArtifact();
    final boolean flagged =
        "true".equals(artifact.getProperty(REJECT_FLAG, null));
    return flagged;
  }

  private Artifact flagRejection(final Artifact artifact)
  {
    final Map<String, String> properties =
        new HashMap<String, String>(artifact.getProperties());
    properties.put(REJECT_FLAG, "true");
    return artifact.setProperties(properties);
  }

  // --- object basics --------------------------------------------------------

}
