/*
 * Copyright 2013-2025 smartics, Kronseder & Reiner GmbH
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

import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.DependencyResolutionException;

import de.smartics.maven.plugin.jboss.modules.aether.MavenRepository;
import de.smartics.maven.plugin.jboss.modules.aether.MavenResponse;
import de.smartics.maven.plugin.jboss.modules.domain.TransitiveDependencyResolver;

/**
 * Accesses a {@link MavenRepository} for resolving.
 */
public class DefaultTransitiveDependencyResolver implements
    TransitiveDependencyResolver
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The repository to access for resolving dependencies.
   */
  private final MavenRepository repository;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param repository the repository to access for resolving dependencies.
   */
  public DefaultTransitiveDependencyResolver(final MavenRepository repository)
  {
    this.repository = repository;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public List<Dependency> resolve(final Dependency dependency)
    throws DependencyResolutionException
  {
    final MavenResponse response = repository.resolve(dependency);
    return response.getDependencies();
  }

  @Override
  public List<Dependency> resolve(final List<Dependency> rootDependencies)
    throws DependencyResolutionException
  {
    final MavenResponse response = repository.resolve(rootDependencies);
    return response.getDependencies();
  }

  @Override
  public List<Dependency> resolveDirect(final Dependency dependency)
    throws DependencyResolutionException
  {
    final MavenResponse response = repository.resolveDirect(dependency);
    return response.getDependencies();
  }

  // --- object basics --------------------------------------------------------

}
