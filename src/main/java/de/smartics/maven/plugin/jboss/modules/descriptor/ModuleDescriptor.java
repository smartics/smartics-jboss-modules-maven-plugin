/*
 * Copyright 2013-2017 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.eclipse.aether.artifact.Artifact;

import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;
import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * Holds the information of one module descriptor. The descriptor controls the
 * process of generating JBoss modules in a folder.
 */
public final class ModuleDescriptor
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The name of the module.
   */
  private final String name;

  /**
   * The name of the slot this module is part of. May be <code>null</code>.
   */
  private String slot;

  /**
   * The directives for this module. Directives control the building process of
   * this module.
   */
  private final Directives directives;

  /**
   * The artifact matcher to define the rules for matching artifacts to be
   * included as resources to a module.
   */
  private final ArtifactMatcher matcher;

  /**
   * The information that has to be applied to dependencies if the matcher
   * matches the name of a module.
   */
  private final ApplyToDependencies applyToDependencies;

  /**
   * The module information to be applied to the generated module.
   */
  private final ApplyToModule applyToModule;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ModuleDescriptor(final String name,
      final ModuleDescriptor originalModule)
  {
    this.name = name;
    this.slot = originalModule.slot;
    this.directives = originalModule.directives;
    this.matcher = originalModule.matcher;
    this.applyToDependencies = originalModule.applyToDependencies;
    this.applyToModule = originalModule.applyToModule;
  }

  private ModuleDescriptor(final Builder builder)
  {
    name = builder.name;
    slot = builder.slot;
    directives = builder.directives;
    matcher = builder.matcher;
    applyToDependencies = builder.applyToDependencies;
    applyToModule = builder.applyToModule;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ModuleDescriptor}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The name of the module.
     */
    private String name;

    /**
     * The name of the slot this module is part of. May be <code>null</code>.
     */
    private String slot;

    /**
     * The directives for this module. Directives control the building process
     * of this module.
     */
    private Directives directives;

    /**
     * The artifact matcher to define the rules for matching artifacts to be
     * included as resources to a module.
     */
    private ArtifactMatcher matcher;

    /**
     * The information that has to be applied to dependencies if the matcher
     * matches the name of a module.
     */
    private ApplyToDependencies applyToDependencies;

    /**
     * The module information to be applied to the generated module.
     */
    private ApplyToModule applyToModule;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Sets the name of the module.
     *
     * @param name the name of the module.
     * @return a reference on this builder.
     */
    public Builder withName(final String name)
    {
      this.name = name;
      return this;
    }

    /**
     * Sets the name of the slot this module is part of. May be
     * <code>null</code>.
     *
     * @param slot the name of the slot this module is part of.
     * @return a reference on this builder.
     */
    public Builder withSlot(final String slot)
    {
      this.slot = slot;
      return this;
    }

    /**
     * Sets the directives for this module. Directives control the building
     * process of this module.
     *
     * @param directives the directives for this module.
     * @return a reference on this builder.
     */
    public Builder with(final Directives directives)
    {
      this.directives = directives;
      return this;
    }

    /**
     * Sets the artifact matcher to define the rules for matching artifacts to
     * be included as resources to a module.
     *
     * @param matcher the dependency matcher for resources.
     * @return a reference on this builder.
     */
    public Builder with(final ArtifactMatcher matcher)
    {
      this.matcher = matcher;
      return this;
    }

    /**
     * Sets the information that has to be applied to dependencies if the
     * matcher matches the name of a module.
     *
     * @param applyToDependencies the information that has to be applied to
     *          dependencies if the matcher matches the name of a module.
     * @return a reference on this builder.
     */
    public Builder with(final ApplyToDependencies applyToDependencies)
    {
      this.applyToDependencies = applyToDependencies;
      return this;
    }

    /**
     * Sets the module information to be applied to the generated module.
     *
     * @param applyToModule the module information to be applied to the
     *          generated module.
     * @return a reference on this builder.
     */
    public Builder with(final ApplyToModule applyToModule)
    {
      this.applyToModule = applyToModule;
      return this;
    }

    /**
     * Returns the name of the module.
     *
     * @return the name of the module.
     */
    public String getName()
    {
      return name;
    }

    /**
     * Returns the name of the slot this module is part of. May be
     * <code>null</code>.
     *
     * @return the name of the slot this module is part of.
     */
    public String getSlot()
    {
      return slot;
    }

    /**
     * Returns the directives for this module. Directives control the building
     * process of this module.
     *
     * @return the directives for this module.
     */
    public Directives getDirectives()
    {
      return directives;
    }

    /**
     * Returns the artifact matcher to define the rules for matching artifacts
     * to be included as resources to a module.
     *
     * @return the artifact matcher to define the rules for matching artifacts
     *         to be included as resources to a module.
     */
    public ArtifactMatcher getMatcher()
    {
      return matcher;
    }

    /**
     * Returns the information that has to be applied to dependencies if the
     * matcher matches the name of a module.
     *
     * @return the information that has to be applied to dependencies if the
     *         matcher matches the name of a module.
     */
    public ApplyToDependencies getApplyToDependencies()
    {
      return applyToDependencies;
    }

    /**
     * Returns the module information to be applied to the generated module.
     *
     * @return the module information to be applied to the generated module.
     */
    public ApplyToModule getApplyToModule()
    {
      return applyToModule;
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds instances of {@link ModuleDescriptor}.
     *
     * @return the new instance.
     * @throws NullPointerException if any required builder property is
     *           <code>null</code>.
     * @throws IllegalArgumentException if any builder property is not validated
     *           successfully.
     */
    public ModuleDescriptor build() throws NullPointerException,
      IllegalArgumentException
    {
      Arg.checkNotBlank("name", name);

      if (directives == null)
      {
        directives = new Directives.Builder().build();
      }
      if (matcher == null)
      {
        matcher = new ArtifactMatcher.Builder().build();
      }
      if (applyToDependencies == null)
      {
        applyToDependencies = new ApplyToDependencies.Builder().build();
      }
      if (applyToModule == null)
      {
        applyToModule = new ApplyToModule.Builder().build();
      }

      return new ModuleDescriptor(this);
    }

    // --- object basics ------------------------------------------------------

  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- factory --------------------------------------------------------------

  /**
   * Creates a shallow copy of the module descriptor.
   *
   * @param name the name of the module.
   * @param originalModule the descriptor to shallow copy.
   * @return the shallow copy.
   */
  public static ModuleDescriptor copy(final String name,
      final ModuleDescriptor originalModule)
  {
    return new ModuleDescriptor(name, originalModule);
  }

  /**
   * Creates a module with a given name.
   *
   * @param name the name of the module.
   * @return the new instance.
   */
  public static ModuleDescriptor create(final String name)
  {
    return new Builder().withName(name).build();
  }

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the name of the module.
   *
   * @return the name of the module.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the name of the slot this module is part of. May be
   * <code>null</code>.
   *
   * @return the name of the slot this module is part of.
   */
  public String getSlot()
  {
    return slot;
  }

  /**
   * Returns the directives for this module. Directives control the building
   * process of this module.
   *
   * @return the directives for this module.
   */
  public Directives getDirectives()
  {
    return directives;
  }

  /**
   * Returns the artifact matcher to define the rules for matching artifacts to
   * be included as resources to a module.
   *
   * @return the artifact matcher to define the rules for matching artifacts to
   *         be included as resources to a module.
   */
  public ArtifactMatcher getMatcher()
  {
    return matcher;
  }

  /**
   * Returns the information that has to be applied to dependencies if the
   * matcher matches the name of a module.
   *
   * @return the information that has to be applied to dependencies if the
   *         matcher matches the name of a module.
   */
  public ApplyToDependencies getApplyToDependencies()
  {
    return applyToDependencies;
  }

  /**
   * Returns the module information to be applied to the generated module.
   *
   * @return the module information to be applied to the generated module.
   */
  public ApplyToModule getApplyToModule()
  {
    return applyToModule;
  }

  // --- business -------------------------------------------------------------

  /**
   * Sets the slot to the given value, if the slot is not set.
   *
   * @param slot the slot to set.
   */
  public void applySlot(final String slot)
  {
    if (this.slot == null)
    {
      this.slot = slot;
    }
  }

  /**
   * Checks if the given artifact matches the module descriptor.
   *
   * @param artifact the artifact to match.
   * @return <code>true</code> if the module descriptor matches the given
   *         artifact, <code>false</code> otherwise.
   */
  public MatchContext match(final Artifact artifact)
  {
    return matcher.match(artifact);
  }

  /**
   * Merges the dependencies, properties, port and export of the given module
   * descriptor with this one.
   *
   * @param moduleDescriptor the module descriptor to merge with this instance.
   * @throws NullPointerException if {@code moduleDescriptor} is
   *           <code>null</code>.
   * @throws IllegalArgumentException if the module descriptor is illegal to be
   *           merged.
   */
  public void merge(final ModuleDescriptor moduleDescriptor)
    throws NullPointerException, IllegalArgumentException
  {
    if (!ObjectUtils.equals(name, moduleDescriptor.name)
        || !ObjectUtils.equals(slot, moduleDescriptor.slot))
    {
      throw new IllegalArgumentException(String.format(
          "Cannot merge different modules: %s:%s vs %s:%s.", name, slot,
          moduleDescriptor.name, moduleDescriptor.slot));
    }

    directives.merge(moduleDescriptor.directives);
    // matcher is not merged
    applyToDependencies.merge(moduleDescriptor.applyToDependencies);
    applyToModule.merge(moduleDescriptor.applyToModule);
  }

  // --- object basics --------------------------------------------------------

  @Override
  public int hashCode()
  {
    int result = 17;
    result = 37 * result + ObjectUtils.hashCode(name);
    result = 37 * result + ObjectUtils.hashCode(slot);

    return result;
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

    final ModuleDescriptor other = (ModuleDescriptor) object;

    return (ObjectUtils.equals(name, other.name) && ObjectUtils.equals(slot,
        other.slot));
  }

  /**
   * {@inheritDoc}
   * <p>
   * Provides the properties via reflection for displaying debug information.
   * </p>
   */
  @Override
  public String toString()
  {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, null);
  }
}
