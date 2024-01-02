/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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

import org.eclipse.aether.collection.DependencyTraverser;

/**
 * Generates dependency traversers.
 */
public interface DependencyTraverserGenerator
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the flag that allows to globally ignore exclusions declared in
   * Maven dependencies.
   *
   * @return the flag that allows to globally ignore exclusions declared in
   *         Maven dependencies.
   */
  boolean isIgnoreDependencyExclusions();

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
  DependencyTraverser createDependencyTraverser(DependencyTraverser delegate)
    throws NullPointerException;

  // --- object basics --------------------------------------------------------

}
