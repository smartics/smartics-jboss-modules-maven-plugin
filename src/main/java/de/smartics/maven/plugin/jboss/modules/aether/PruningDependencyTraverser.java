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
package de.smartics.maven.plugin.jboss.modules.aether;

import java.util.List;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyTraverser;
import org.sonatype.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Prunes the dependency tree upon information on excluded artifacts and skipped
 * modules.
 */
public class PruningDependencyTraverser extends DelegateDependencyTraverser
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The artifacts to exclude.
   */
  private final List<ArtifactClusion> exclusions;

  /**
   * The list of modules to skip.
   */
  private final List<ModuleDescriptor> skipModules;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param delegate the traverse to delegate to after own check is not
   *          rejecting.
   * @param exclusions the artifacts to exclude.
   * @param skipModules the list of modules to skip.
   * @throws NullPointerException if {@code delegate} is <code>null</code>.
   */
  public PruningDependencyTraverser(final DependencyTraverser delegate,
      final List<ArtifactClusion> exclusions,
      final List<ModuleDescriptor> skipModules) throws NullPointerException
  {
    super(delegate);
    this.exclusions = exclusions;
    this.skipModules = skipModules;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  protected boolean doTraverseDependency(final Dependency dependency)
  {
    final Artifact artifact = dependency.getArtifact();
    for (final ArtifactClusion exclusion : exclusions)
    {
      final boolean exclude = exclusion.matches(artifact).isMatched();
      if (exclude)
      {
        return false;
      }
    }

    for (final ModuleDescriptor module : skipModules)
    {
      final boolean exclude = module.match(artifact).isMatched();
      if (exclude)
      {
        return false;
      }
    }

    return true;
  }

  // --- object basics --------------------------------------------------------

}
