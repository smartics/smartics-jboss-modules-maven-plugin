/*
 * Copyright 2013-2018 smartics, Kronseder & Reiner GmbH
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

import java.util.List;

import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.DependencyResolutionException;

/**
 * Resolves the transitive dependencies for a given artifact.
 */
public interface TransitiveDependencyResolver
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Resolves the transitive dependencies for the given dependency.
   *
   * @param dependency the dependency whose calculation of transitive
   *          dependencies is requested.
   * @return the set of transitive dependencies.
   * @throws DependencyResolutionException if the dependency cannot be resolved.
   */
  List<Dependency> resolve(Dependency dependency)
    throws DependencyResolutionException;

  /**
   * Resolves the direct dependencies for the given dependency.
   *
   * @param dependency the dependency whose calculation of direct dependencies
   *          is requested.
   * @return the set of direct dependencies.
   * @throws DependencyResolutionException if the dependency cannot be resolved.
   */
  List<Dependency> resolveDirect(Dependency dependency)
    throws DependencyResolutionException;

  /**
   * Resolves the transitive dependencies for the given dependencies.
   *
   * @param dependencies the dependencies whose calculation of transitive
   *          dependencies is requested.
   * @return the set of transitive dependencies.
   * @throws DependencyResolutionException if any dependency cannot be resolved.
   */
  List<Dependency> resolve(List<Dependency> dependencies)
    throws DependencyResolutionException;

  // --- object basics --------------------------------------------------------

}
