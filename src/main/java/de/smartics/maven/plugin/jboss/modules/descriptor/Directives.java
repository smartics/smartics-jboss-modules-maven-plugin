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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Provides directives for a given {@link ModuleDescriptor module descriptor}.
 * Directives control the building process of a single module.
 */
public final class Directives
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The flag to skip the export of the module. This allows to rename a
   * dependency to an existing module in JBoss, but not to create a module in
   * the target folder.
   */
  private Boolean skip;

  /**
   * The signal to control, if the slot is to be inherited to the dependencies
   * if not specified by a dependency otherwise. If set to <code>false</code>,
   * the default slot will be used instead of the module's slot.
   * <p>
   * Useful to create extensions that have to reside in the main slot but have
   * to depend on modules in another slot.
   */
  private Boolean inheritSlot;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private Directives(final Builder builder)
  {
    this.skip = builder.skip;
    this.inheritSlot = builder.inheritSlot;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link Directives}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The flag to skip the export of the module. This allows to rename a
     * dependency to an existing module in JBoss, but not to create a module in
     * the target folder.
     * <p>
     * Defaults to <code>false</code>.
     * </p>
     */
    private Boolean skip = Boolean.FALSE;

    /**
     * The signal to control, if the slot is to be inherited to the dependencies
     * if not specified by a dependency otherwise. If set to <code>false</code>,
     * the default slot will be used instead of the module's slot.
     * <p>
     * Useful to create extensions that have to reside in the main slot but have
     * to depend on modules in another slot.
     * <p>
     * Defaults to <code>true</code>.
     * </p>
     */
    private Boolean inheritSlot = Boolean.TRUE;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Sets the flag to skip the export of the module. This allows to rename a
     * dependency to an existing module in JBoss, but not to create a module in
     * the target folder.
     *
     * @param skip the flag to skip the export of the module.
     * @return a reference to this builder.
     */
    public Builder withSkip(final String skip)
    {
      if (StringUtils.isNotBlank(skip))
      {
        this.skip = Boolean.parseBoolean(skip);
      }
      return this;
    }

    /**
     * Sets the signal to control, if the slot is to be inherited to the
     * dependencies if not specified by a dependency otherwise. If set to
     * <code>false</code>, the default slot will be used instead of the module's
     * slot.
     * <p>
     * Useful to create extensions that have to reside in the main slot but have
     * to depend on modules in another slot.
     *
     * @param inheritSlot the signal to control, if the slot is to be inherited
     *          to the dependencies if not specified by a dependency otherwise.
     * @return a reference to this builder.
     */
    public Builder withInheritSlot(final String inheritSlot)
    {
      if (StringUtils.isNotBlank(inheritSlot))
      {
        this.inheritSlot = Boolean.parseBoolean(inheritSlot);
      }
      return this;
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds an instance of {@link Directives}.
     *
     * @return the instance.
     */
    public Directives build()
    {
      return new Directives(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the flag to skip the export of the module. This allows to rename a
   * dependency to an existing module in JBoss, but not to create a module in
   * the target folder.
   *
   * @return the flag to skip the export of the module.
   */
  public Boolean getSkip()
  {
    return skip;
  }

  /**
   * Returns the signal to control, if the slot is to be inherited to the
   * dependencies if not specified by a dependency otherwise. If set to
   * <code>false</code>, the default slot will be used instead of the module's
   * slot.
   * <p>
   * Useful to create extensions that have to reside in the main slot but have
   * to depend on modules in another slot.
   *
   * @return the signal to control, if the slot is to be inherited to the
   *         dependencies if not specified by a dependency otherwise.
   */
  public Boolean getInheritSlot()
  {
    return inheritSlot;
  }

  // --- business -------------------------------------------------------------

  /**
   * Merges the given directive with this instance.
   *
   * @param directives the instance to merge into this instance.
   */
  public void merge(final Directives directives)
  {
    skip |= directives.skip;
    inheritSlot |= directives.inheritSlot;
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, null);
  }
}
