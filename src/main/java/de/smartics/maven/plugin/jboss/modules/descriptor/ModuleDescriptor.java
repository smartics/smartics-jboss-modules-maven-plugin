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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.smartics.util.lang.Arg;

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
  private final String slot;

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
   * The XML fragment to be applied to the generated module.
   */
  private final String applyToModuleXml;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ModuleDescriptor(final Builder builder)
  {
    name = builder.name;
    slot = builder.slot;
    directives = builder.directives;
    matcher = builder.matcher;
    applyToDependencies = builder.applyToDependencies;
    applyToModuleXml = builder.applyToModuleXml;
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
     * The XML fragment to be applied to the generated module.
     */
    private String applyToModuleXml;

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
     * Sets the XML fragment to be applied to the generated module.
     *
     * @param applyToModuleXml the XML fragment to be applied to the generated
     *          module.
     * @return a reference on this builder.
     */
    public Builder withApplyToModuleXml(final String applyToModuleXml)
    {
      this.applyToModuleXml = applyToModuleXml;
      return this;
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

      return new ModuleDescriptor(this);
    }

    // --- object basics ------------------------------------------------------

  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

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
   * Returns the XML fragment to be applied to the generated module.
   *
   * @return the XML fragment to be applied to the generated module.
   */
  public String getApplyToModuleXml()
  {
    return applyToModuleXml;
  }

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

  /**
   * {@inheritDoc}
   * <p>
   * Provides the properties via reflection for displaying debug information.
   * </p>
   */
  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
