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

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.repository.RemoteRepository;

/**
 * A repository builder to be used in the context of a Maven Mojo. Mojos get the
 * resources for this builder injected and only pass them on.
 */
public final class MojoRepositoryBuilder implements RepositoryBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The remote repositories of artifacts.
   */
  private List<RemoteRepository> remoteRepositories;

  /**
   * The session to launch requests.
   */
  private RepositorySystemSession session;

  /**
   * The system to run requests against.
   */
  private RepositorySystem repositorySystem;

  /**
   * The filters for dependencies.
   */
  private List<DependencyFilter> dependencyFilters;

  /**
   * The list of managed dependencies to allow to resolve the appropriate
   * versions of artifacts.
   */
  private List<Dependency> managedDependencies;

  /**
   * The flag to set the system offline.
   */
  private boolean offline;

  /**
   * The generator of traversers used to prune dependency branches.
   */
  private DependencyTraverserGenerator traverserGenerator;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public MojoRepositoryBuilder()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  @Override
  public List<RemoteRepository> getRemoteRepositories()
  {
    return remoteRepositories;
  }

  /**
   * Sets the remote repositories of artifacts.
   *
   * @param remoteRepositories the remote repositories of artifacts.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder with(
      final List<RemoteRepository> remoteRepositories)
  {
    this.remoteRepositories = remoteRepositories;
    return this;
  }

  @Override
  public RepositorySystem getRepositorySystem()
  {
    return repositorySystem;
  }

  /**
   * Sets the system to run requests against.
   *
   * @param repositorySystem the system to run requests against.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder with(final RepositorySystem repositorySystem)
  {
    this.repositorySystem = repositorySystem;
    return this;
  }

  @Override
  public RepositorySystemSession getSession()
  {
    return session;
  }

  /**
   * Sets the session to launch requests.
   *
   * @param session the session to launch requests.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder with(final RepositorySystemSession session)
  {
    this.session = session;
    return this;
  }

  @Override
  public List<DependencyFilter> getDependencyFilters()
  {
    return dependencyFilters;
  }

  /**
   * Sets the filters for dependencies.
   *
   * @param dependencyFilters the filters for dependencies.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder withDependencyFilters(
      final List<DependencyFilter> dependencyFilters)
  {
    this.dependencyFilters = dependencyFilters;
    return this;
  }

  @Override
  public List<Dependency> getManagedDependencies()
  {
    return managedDependencies;
  }

  /**
   * Sets the list of managed dependencies to allow to resolve the appropriate
   * versions of artifacts.
   *
   * @param managedDependencies the list of managed dependencies to allow to
   *          resolve the appropriate versions of artifacts.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder withManagedDependencies(
      final List<Dependency> managedDependencies)
  {
    this.managedDependencies = managedDependencies;
    return this;
  }

  @Override
  public boolean isOffline()
  {
    return offline;
  }

  /**
   * Sets the flag to set the system offline.
   *
   * @param offline the flag to set the system offline.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder withOffline(final boolean offline)
  {
    this.offline = offline;
    return this;
  }

  @Override
  public DependencyTraverserGenerator getTraverserGenerator()
  {
    return traverserGenerator;
  }

  /**
   * Sets the generator of traversers used to prune dependency branches.
   *
   * @param traverserGenerator the generator of traversers used to prune
   *          dependency branches.
   * @return a reference to this builder.
   */
  public MojoRepositoryBuilder withTraverserGenerator(
      final DependencyTraverserGenerator traverserGenerator)
  {
    this.traverserGenerator = traverserGenerator;
    return this;
  }

  // --- business -------------------------------------------------------------

  /**
   * Builds the instance.
   *
   * @return the created instance.
   */
  public MavenRepository build()
  {
    return new MavenRepository(this);
  }

  // --- object basics --------------------------------------------------------
}
