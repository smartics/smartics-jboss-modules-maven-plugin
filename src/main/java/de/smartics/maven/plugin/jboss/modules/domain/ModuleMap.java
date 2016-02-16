/*
 * Copyright 2013-2015 smartics, Kronseder & Reiner GmbH
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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * Maps artifacts to their modules.
 */
@NotThreadSafe
public final class ModuleMap
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The modules encountered or declared.
   */
  private final List<ModuleDescriptor> modules;

  /**
   * Maps modules to their dependencies.
   */
  private final Multimap<ModuleDescriptor, Dependency> module2Dependencies =
      HashMultimap.create();

  /**
   * Maps a dependency to its module.
   */
  private final Map<DependencyKey, ModuleDescriptor> dependency2Module =
      new HashMap<DependencyKey, ModuleDescriptor>();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Constructor to create an empty map.
   */
  public ModuleMap()
  {
    this(new ArrayList<ModuleDescriptor>());
  }

  /**
   * Default constructor.
   *
   * @param modules the configured modules.
   */
  public ModuleMap(final List<ModuleDescriptor> modules)
  {
    this.modules = modules;
  }

  /**
   * Convenience constructor to initialize with values.
   *
   * @param modulesDescriptors the configured modules.
   * @param dependencies the dependencies to add.
   */
  public ModuleMap(final List<ModuleDescriptor> modulesDescriptors,
      final Collection<Dependency> dependencies)
  {
    this.modules =
        new ArrayList<ModuleDescriptor>(modulesDescriptors != null
            ? modulesDescriptors : new ArrayList<ModuleDescriptor>());

    initDependencies(dependencies);
  }

  // ****************************** Inner Classes *****************************

  /**
   * Makes dependencies equal according to the referenced artifact.
   */
  private static final class DependencyKey
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The wrapped dependency.
     */
    private final Dependency dependency;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    private DependencyKey(final Dependency dependency)
    {
      this.dependency = dependency;
    }

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    // --- business -----------------------------------------------------------

    // --- object basics ------------------------------------------------------

    @Override
    public int hashCode()
    {
      return dependency.getArtifact().hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
      if (this == object)
      {
        return true;
      }
      else if (object == null || getClass() != object.getClass())
      {
        return false;
      }

      final ModuleMap.DependencyKey other = (ModuleMap.DependencyKey) object;

      final Artifact artifact = dependency.getArtifact();
      final Artifact otherArtifact = other.dependency.getArtifact();

      return checkArtifactsWithoutProperties(artifact, otherArtifact);
    }

    private boolean checkArtifactsWithoutProperties(final Artifact artifact,
        final Artifact otherArtifact)
    {
      return (artifact.getArtifactId().equals(otherArtifact.getArtifactId())
              && artifact.getGroupId().equals(otherArtifact.getGroupId())
              && artifact.getVersion().equals(otherArtifact.getVersion())
              && artifact.getExtension().equals(otherArtifact.getExtension())
              && artifact.getClassifier().equals(otherArtifact.getClassifier()) && ObjectUtils
          .equals(artifact.getFile(), otherArtifact.getFile()));
    }

    @Override
    public String toString()
    {
      return dependency.toString();
    }
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  private void initDependencies(final Collection<Dependency> dependencies)
  {
    for (final Dependency dependency : dependencies)
    {
      add(dependency);
    }
  }

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the map of modules.
   *
   * @return the map of modules.
   */
  public synchronized Map<ModuleDescriptor, List<Dependency>> toMap()
  {
    final Map<ModuleDescriptor, List<Dependency>> map =
        new LinkedHashMap<ModuleDescriptor, List<Dependency>>();
    for (final Entry<ModuleDescriptor, Collection<Dependency>> entry : module2Dependencies
        .asMap().entrySet())
    {
      final List<Dependency> list = new ArrayList<Dependency>(entry.getValue());
      map.put(entry.getKey(), list);
    }
    return map;
  }

  // --- business -------------------------------------------------------------

  /**
   * Adds the given dependency to its module. If the dependency has already been
   * added, the already associated module is returned without adding the
   * dependency a second time.
   *
   * @param dependency the dependency to add.
   * @return the module the dependency is associated with.
   */
  public synchronized ModuleDescriptor add(final Dependency dependency)
  {
    final DependencyKey key = new DependencyKey(dependency);
    final ModuleDescriptor alreadyStoredModule = dependency2Module.get(key);
    if (alreadyStoredModule != null)
    {
      return alreadyStoredModule;
    }

    final ModuleDescriptor module = calcModule(key);
    storeArtifact(module, dependency);
    return module;
  }

  private ModuleDescriptor calcModule(final DependencyKey key)
  {
    final ModuleDescriptor alreadyStoredModule = dependency2Module.get(key);
    if (alreadyStoredModule != null)
    {
      return alreadyStoredModule;
    }

    for (final ModuleDescriptor module : modules)
    {
      final MatchContext matchContext =
          module.match(key.dependency.getArtifact());
      if (matchContext.isMatched())
      {
        if (matchContext.hasGroupMatch())
        {
          final String name = matchContext.translateName(module.getName());
          final ModuleDescriptor matchingModule = findMatchingModule(name);
          if (matchingModule != null)
          {
            return matchingModule;
          }
          else
          {
            final ModuleDescriptor newModule = createModule(matchContext, module);
            return newModule;
          }
        }
        else
        {
          return module;
        }
      }
    }

    final ModuleDescriptor module = createModule(key.dependency);
    return module;
  }

  private ModuleDescriptor findMatchingModule(String name)
  {
    for (ModuleDescriptor moduleDescriptor : modules)
    {
      if (moduleDescriptor.getName().equals(name) && !moduleDescriptor.getDirectives().getSkip())
      {
        return moduleDescriptor;
      }
    }
    return null;
  }

  private void storeArtifact(final ModuleDescriptor module,
      final Dependency dependency)
  {
    if (!module.getDirectives().getSkip())
    {
      if (module2Dependencies.containsKey(module))
      {
        for (final ModuleDescriptor current : module2Dependencies.keySet())
        {
          if (module.equals(current))
          {
            current.merge(module);
          }
        }
      }
      module2Dependencies.put(module, dependency);
      dependency2Module.put(new DependencyKey(dependency), module);
    }
  }

  private ModuleDescriptor createModule(final MatchContext matchContext,
      final ModuleDescriptor originalModule)
  {
    final String name = matchContext.translateName(originalModule.getName());
    final ModuleDescriptor module = ModuleDescriptor.copy(name, originalModule);

    return module;
  }

  private ModuleDescriptor createModule(final Dependency dependency)
  {
    final Artifact artifact = dependency.getArtifact();
    final String groupId = artifact.getGroupId();
    final String artifactId = artifact.getArtifactId();

    final String name = createName(groupId, artifactId);

    final ModuleDescriptor module = ModuleDescriptor.create(name);
    return module;
  }

  /**
   * Constructs a default module name for a given group ID and artifact ID.
   *
   * @param groupId the group ID of the artifact to create a module name for.
   * @param artifactId the artifact ID of the artifact to create a module name
   *          for.
   * @return a default module name.
   * @throws NullPointerException if any of {@code groupId} or
   *           {@code artifactId} is <code>null</code>.
   * @throws IllegalArgumentException if any of {@code groupId} or
   *           {@code artifactId} is blank.
   */
  static String createName(final String groupId, final String artifactId)
    throws NullPointerException, IllegalArgumentException
  {
    Arg.checkNotBlank("groupId", groupId);
    Arg.checkNotBlank("artifactId", artifactId);

    final String name;
    if (groupId.equals(artifactId) || groupId.endsWith('.' + artifactId))
    {
      name = groupId;
    }
    else
    {
      name = groupId + '.' + artifactId;
    }
    return name;
  }

  /**
   * Returns the module for the given dependency.
   *
   * @param dependency the artifact whose module is requested.
   * @return the module of the dependency.
   */
  public synchronized ModuleDescriptor getModule(final Dependency dependency)
  {
    final DependencyKey key = new DependencyKey(dependency);
    ModuleDescriptor module = dependency2Module.get(key);

    if (module == null)
    {
      module = calcModule(key);
    }
    return module;
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    final StringBuilder buffer = new StringBuilder(2048);
    for (final Entry<ModuleDescriptor, Collection<Dependency>> entry : module2Dependencies
        .asMap().entrySet())
    {
      final ModuleDescriptor module = entry.getKey();
      buffer.append('\n').append(module.getName()).append(':');
      for (final Dependency dependency : entry.getValue())
      {
        buffer.append("\n  ").append(dependency.getArtifact().getArtifactId());
      }
    }

    return buffer.toString();
  }
}
