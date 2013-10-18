/*
 * Copyright 2013 smartics, Kronseder & Reiner GmbH
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

import java.util.List;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyFilter;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.Exclusion;

/**
 * Rejects dependencies that are excluded by their parents.
 */
public final class ExclusionFilter implements DependencyFilter
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The singleton instance since the filter has no state.
   */
  public static final ExclusionFilter INSTANCE = new ExclusionFilter();

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public boolean accept(final DependencyNode node,
      final List<DependencyNode> parents)
  {
    final Dependency dependency = node.getDependency();
    if (dependency == null)
    {
      return false;
    }

    if (parents.isEmpty())
    {
      return true;
    }

    final Artifact artifact = dependency.getArtifact();
    final String groupId = artifact.getGroupId();
    final String artifactId = artifact.getArtifactId();
    final DependencyNode parent = parents.get(0);

    final Dependency parentDependency = parent.getDependency();
    if (parentDependency != null)
    {
      for (final Exclusion exclusion : parentDependency.getExclusions())
      {
        if (artifactId.equals(exclusion.getArtifactId())
            && groupId.equals(exclusion.getGroupId()))
        {
          return false;
        }
      }
    }
    return true;
  }

  // --- object basics --------------------------------------------------------

}
