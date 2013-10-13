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
package de.smartics.maven.plugin.jboss.modules.domain;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.aether.collection.DependencyTraverser;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.aether.DependencyTraverserGenerator;
import de.smartics.maven.plugin.jboss.modules.aether.PruningDependencyTraverser;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Implements pruning on dependency excludes an modules tagged as 'skip'.
 */
public class PrunerGenerator implements DependencyTraverserGenerator
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * A list of dependencies to be excluded from the transitive dependency
   * collection process.
   */
  private final List<Clusion> dependencyExcludes;

  /**
   * The module descriptors that skip dependency resolution.
   */
  private final List<ModuleDescriptor> skipModules;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param dependencyExcludes a list of dependencies to be excluded from the
   *          transitive dependency collection process.
   * @param modules lost of modules to calculate the skip modules.
   */
  public PrunerGenerator(final List<Clusion> dependencyExcludes,
      final List<ModuleDescriptor> modules)
  {
    this.dependencyExcludes =
        dependencyExcludes != null ? dependencyExcludes
            : new ArrayList<Clusion>();
    this.skipModules = calcSkipModules(modules);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  private List<ModuleDescriptor> calcSkipModules(final List<ModuleDescriptor> modules)
  {
    if (modules == null)
    {
      return new ArrayList<ModuleDescriptor>();
    }
    final List<ModuleDescriptor> skipModules = new ArrayList<ModuleDescriptor>(modules.size());
    for (final ModuleDescriptor module : modules)
    {
      if (module.getDirectives().getSkip())
      {
        skipModules.add(module);
      }
    }
    return skipModules;
  }

  // --- business -------------------------------------------------------------

  /**
   * Creates a dependency traverser to prune dependency branches that are
   * excluded as dependencies or skipped as modules.
   *
   * @param delegate the traverse to delegate to after own check is not
   *          rejecting.
   * @return the traverser.
   * @throws NullPointerException if {@code delegate} is <code>null</code>.
   */
  public DependencyTraverser createDependencyTraverser(
      final DependencyTraverser delegate) throws NullPointerException
  {
    return new PruningDependencyTraverser(delegate, dependencyExcludes,
        skipModules);
  }

  // --- object basics --------------------------------------------------------

}
