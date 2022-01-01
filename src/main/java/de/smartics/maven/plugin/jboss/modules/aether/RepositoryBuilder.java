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
 * Interface to provide resources to create instances of {@link MavenRepository}
 * .
 */
public interface RepositoryBuilder
{

  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the system to run requests against.
   *
   * @return the system to run requests against.
   */
  RepositorySystem getRepositorySystem();

  /**
   * Returns the filters for dependencies.
   *
   * @return the filters for dependencies.
   */
  List<DependencyFilter> getDependencyFilters();

  /**
   * Returns the flag to set the system offline.
   *
   * @return the flag to set the system offline.
   */
  boolean isOffline();

  /**
   * Returns the session to launch requests.
   *
   * @return the session to launch requests.
   */
  RepositorySystemSession getSession();

  /**
   * Returns the remote repositories of artifacts.
   *
   * @return the remote repositories of artifacts.
   */
  List<RemoteRepository> getRemoteRepositories();

  /**
   * The list of managed dependencies to allow to resolve the appropriate
   * versions of artifacts.
   *
   * @return the list of managed dependencies.
   */
  List<Dependency> getManagedDependencies();

  /**
   * Returns the generator of traversers used to prune dependency branches.
   *
   * @return the generator of traversers used to prune dependency branches.
   */
  DependencyTraverserGenerator getTraverserGenerator();

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
