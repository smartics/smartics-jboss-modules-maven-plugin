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

import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.collection.DependencySelector;
import org.sonatype.aether.collection.DependencyTraverser;
import org.sonatype.aether.util.FilterRepositorySystemSession;
import org.sonatype.aether.util.graph.selector.AndDependencySelector;
import org.sonatype.aether.util.graph.selector.OptionalDependencySelector;
import org.sonatype.aether.util.graph.selector.ScopeDependencySelector;

import de.smartics.util.lang.Arg;

/**
 * Used for pruning the dependency tree.
 */
public class FilterSession extends FilterRepositorySystemSession
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The traverser to prune.
   */
  private final DependencyTraverser traverser;

  /**
   * The flag that allows to globally ignore exclusions declared in Maven
   * dependencies.
   */
  private final boolean ignoreDependencyExclusions;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Creates a new repository system session that wraps the specified session.
   *
   * @param session the repository system session to wrap.
   * @param traverser the traverser to prune.
   * @param ignoreDependencyExclusions the flag that allows to globally ignore
   *          exclusions declared in Maven dependencies.
   * @throws NullPointerException if {@code session} or {@code traverser} is
   *           <code>null</code>.
   */
  public FilterSession(final RepositorySystemSession session,
      final DependencyTraverser traverser,
      final boolean ignoreDependencyExclusions) throws NullPointerException
  {
    super(session);
    this.traverser = Arg.checkNotNull("traverser", traverser);
    this.ignoreDependencyExclusions = ignoreDependencyExclusions;
  }

  // --- business -------------------------------------------------------------

  @Override
  public DependencyTraverser getDependencyTraverser()
  {
    return traverser;
  }

  @Override
  public DependencySelector getDependencySelector()
  {
    if (ignoreDependencyExclusions)
    {
      // Unfortunately we cannot analyze the super dependency selector ...
      return new AndDependencySelector(new ScopeDependencySelector("test"),
          new OptionalDependencySelector());
    }
    else
    {
      return super.getDependencySelector();
    }
  }

  // --- object basics --------------------------------------------------------

}
