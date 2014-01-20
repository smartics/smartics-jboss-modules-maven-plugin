/*
 * Copyright 2013-2014 smartics, Kronseder & Reiner GmbH
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

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;

/**
 * Rejects dependencies of scope <tt>test</tt>.
 */
public final class GaExclusionFilter implements DependencyFilter
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The artifacts to exclude.
   */
  private final List<ArtifactClusion> exclusions;

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param exclusions the artifacts to exclude.
   */
  public GaExclusionFilter(final List<ArtifactClusion> exclusions)
  {
    this.exclusions = exclusions;
  }

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

    final Artifact artifact = dependency.getArtifact();
    for (final ArtifactClusion exclusion : exclusions)
    {
      final boolean exclude = exclusion.matches(artifact).isMatched();
      if (exclude)
      {
        DependencyFlagger.INSTANCE.flag(node);
        return false;
      }
    }

    return true;
  }

  // --- object basics --------------------------------------------------------

}
