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
package de.smartics.maven.plugin.jboss.modules.aether;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.aether.graph.Dependency;

/**
 * The Maven response with calculated dependencies.
 */
public final class MavenResponse
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The calculated dependencies.
   */
  private final List<Dependency> dependencies = new ArrayList<Dependency>();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public MavenResponse()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Adds a dependency.
   *
   * @param dependency the dependency to add.
   */
  public void add(final Dependency dependency)
  {
    dependencies.add(dependency);
  }

  /**
   * Returns the calculated dependencies.
   *
   * @return the calculated dependencies.
   */
  public List<Dependency> getDependencies()
  {
    return dependencies;
  }

  // --- object basics --------------------------------------------------------

}
