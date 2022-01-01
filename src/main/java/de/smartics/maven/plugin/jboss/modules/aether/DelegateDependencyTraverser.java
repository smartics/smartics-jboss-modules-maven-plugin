/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
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

import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencyTraverser;
import org.eclipse.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * Delegates to another traverser in case itself does not prune a requested
 * dependency.
 */
public abstract class DelegateDependencyTraverser implements
    DependencyTraverser
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The traverse to delegate to after own check is not rejecting.
   */
  private final DependencyTraverser delegate;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param delegate the traverse to delegate to after own check is not
   *          rejecting.
   * @throws NullPointerException if {@code delegate} is <code>null</code>.
   */
  protected DelegateDependencyTraverser(final DependencyTraverser delegate)
    throws NullPointerException
  {
    this.delegate = Arg.checkNotNull("delegate", delegate);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public final boolean traverseDependency(final Dependency dependency)
  {
    if (!doTraverseDependency(dependency))
    {
      return false;
    }
    return delegate.traverseDependency(dependency);
  }

  /**
   * Override this to prune.
   *
   * @param dependency the dependency to check.
   * @return true of the dependency should be traversed, <code>false</code>
   *         otherwise.
   */
  protected abstract boolean doTraverseDependency(final Dependency dependency);

  @Override
  public DependencyTraverser deriveChildTraverser(
      final DependencyCollectionContext context)
  {
    return this;
  }

  // --- object basics --------------------------------------------------------

}
