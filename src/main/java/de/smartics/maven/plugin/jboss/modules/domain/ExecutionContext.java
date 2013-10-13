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

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.resolution.DependencyResolutionException;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.util.lang.Arg;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * The context and configuration to control the building of the module archive.
 */
public final class ExecutionContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The Maven logger to log.
   */
  private final Log log;

  /**
   * The folder to write the module structure to.
   */
  private final File targetFolder;

  /**
   * The resolver to determine dependencies of an artifact.
   */
  private final TransitiveDependencyResolver resolver;

  /**
   * The slot strategy for modules.
   */
  private final SlotStrategy slotStrategy;

  /**
   * The name of the default slot to write to.
   */
  private final String defaultSlot;

  /**
   * The map of modules encountered so far.
   */
  private final ModuleMap moduleMap;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ExecutionContext(final Builder builder)
  {
    this.log = builder.log;
    this.targetFolder = builder.targetFolder;
    this.resolver = builder.resolver;
    this.slotStrategy = builder.slotStrategy;
    this.defaultSlot = builder.defaultSlot;
    this.moduleMap = builder.moduleMap;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds {@link ExecutionContext}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The Maven logger to log.
     */
    private Log log;

    /**
     * The folder to write the module structure to.
     */
    private File targetFolder;

    /**
     * The resolver to determine dependencies of an artifact.
     */
    private TransitiveDependencyResolver resolver;

    /**
     * The slot strategy for modules.
     */
    private SlotStrategy slotStrategy;

    /**
     * The name of the default slot to write to.
     */
    private String defaultSlot;

    /**
     * The map of modules encountered so far.
     */
    private ModuleMap moduleMap;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Sets the Maven logger to log.
     *
     * @param log the Maven logger to log.
     * @return a reference to this builder.
     */
    public Builder with(final Log log)
    {
      this.log = log;
      return this;
    }

    /**
     * Sets the folder to write the module structure to.
     *
     * @param targetFolder the folder to write the module structure to.
     * @return a reference to this builder.
     */
    public Builder withTargetFolder(final File targetFolder)
    {
      this.targetFolder = targetFolder;
      return this;
    }

    /**
     * Sets the resolver to determine dependencies of an artifact.
     *
     * @param resolver the resolver to determine dependencies of an artifact.
     * @return a reference to this builder.
     */
    public Builder with(final TransitiveDependencyResolver resolver)
    {
      this.resolver = resolver;
      return this;
    }

    /**
     * Sets the slot strategy for modules.
     *
     * @param slotStrategy the slot strategy for modules.
     * @return a reference to this builder.
     */
    public Builder with(final SlotStrategy slotStrategy)
    {
      this.slotStrategy = slotStrategy;
      return this;
    }

    /**
     * Sets the name of the default slot to write to.
     *
     * @param defaultSlot the name of the default slot to write to.
     * @return a reference to this builder.
     */
    public Builder withDefaultSlot(final String defaultSlot)
    {
      this.defaultSlot = defaultSlot;
      return this;
    }

    /**
     * Sets the map of modules encountered so far.
     *
     * @param moduleMap the map of modules encountered so far.
     * @return a reference to this builder.
     */
    public Builder with(final ModuleMap moduleMap)
    {
      this.moduleMap = moduleMap;
      return this;
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds {@link ExecutionContext}.
     *
     * @return the created instance.
     * @throws NullPointerException if any configuration property is
     *           <code>null</code>.
     */
    public ExecutionContext build() throws NullPointerException
    {
      Arg.checkNotNull("targetFolder", targetFolder);
      Arg.checkNotNull("resolver", resolver);
      Arg.checkNotNull("slotStrategy", slotStrategy);
      Arg.checkNotNull("moduleMap", moduleMap);

      return new ExecutionContext(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the Maven logger to log.
   *
   * @return the Maven logger to log.
   */
  public Log getLog()
  {
    return log;
  }

  /**
   * Returns the folder to write the module structure to.
   *
   * @return the folder to write the module structure to.
   */
  public File getTargetFolder()
  {
    return targetFolder;
  }

  /**
   * Returns the resolver for dependencies for an artifact.
   *
   * @return the resolver for dependencies for an artifact.
   */
  public TransitiveDependencyResolver getResolver()
  {
    return resolver;
  }

  /**
   * Returns the slot strategy for modules.
   *
   * @return the slot strategy for modules.
   */
  public SlotStrategy getSlotStrategy()
  {
    return slotStrategy;
  }

  /**
   * Returns the name of the default slot to write to.
   *
   * @return the name of the default slot to write to.
   */
  public String getDefaultSlot()
  {
    return defaultSlot;
  }

  /**
   * Returns the map of modules encountered so far.
   *
   * @return the map of modules encountered so far.
   */
  public ModuleMap getModuleMap()
  {
    return moduleMap;
  }

  // --- business -------------------------------------------------------------

  /**
   * Resolves the direct dependencies for the given dependency.
   *
   * @param dependency the dependency whose calculation of direct dependencies
   *          is requested.
   * @return the set of direct dependencies.
   */
  @SuppressWarnings("unchecked")
  public List<Dependency> resolve(final Dependency dependency)
  {
    try
    {
      return resolver.resolveDirect(dependency);
    }
    catch (final DependencyResolutionException e)
    {
      log.error("Cannot resolve dependency '"
                + dependency.getArtifact().getArtifactId() + "': "
                + e.getMessage());
      return Collections.emptyList();
    }
  }

  /**
   * Returns the module for the given dependency.
   *
   * @param dependency the dependency whose module is requested.
   * @return the module of the dependency.
   * @throws IllegalArgumentException if there is no module registered for the
   *           given dependency.
   */
  public ModuleDescriptor getModule(final Dependency dependency)
    throws IllegalArgumentException
  {
    final ModuleDescriptor module = moduleMap.getModule(dependency);
    if (module == null)
    {
      throw new IllegalArgumentException("Cannot find module for dependency: "
                                         + dependency);
    }
    return module;
  }

  // --- object basics --------------------------------------------------------

}
