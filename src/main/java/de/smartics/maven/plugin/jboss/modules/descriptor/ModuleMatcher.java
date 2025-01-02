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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.smartics.maven.plugin.jboss.modules.util.Arg;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Descriptor to define the rules for matching modules to be matched to have
 * additional information applied.
 */
public final class ModuleMatcher
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of inclusions.
   */
  private final List<ModuleClusion> includes;

  /**
   * The list of exclusions.
   */
  private final List<ModuleClusion> excludes;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ModuleMatcher(final Builder builder)
  {
    includes = builder.includes;
    excludes = builder.excludes;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ModuleMatcher}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The list of inclusions.
     */
    private final List<ModuleClusion> includes = new ArrayList<ModuleClusion>();

    /**
     * The list of exclusions.
     */
    private final List<ModuleClusion> excludes = new ArrayList<ModuleClusion>();

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    // --- business -----------------------------------------------------------

    /**
     * Adds an include to the list of includes.
     *
     * @param include the include to add.
     * @throws NullPointerException if {@code include} is <code>null</code>.
     */
    public void addInclude(final ModuleClusion include)
      throws NullPointerException
    {
      includes.add(Arg.checkNotNull("include", include));
    }

    /**
     * Adds an exclude to the list of excludes.
     *
     * @param exclude the exclude to add.
     * @throws NullPointerException if {@code exclude} is <code>null</code>.
     */
    public void addExclude(final ModuleClusion exclude)
      throws NullPointerException
    {
      excludes.add(Arg.checkNotNull("exclude", exclude));
    }

    /**
     * Builds an instance of {@link ModuleMatcher}.
     *
     * @return the instance.
     */
    public ModuleMatcher build()
    {
      return new ModuleMatcher(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the list of inclusions.
   *
   * @return the list of inclusions.
   */
  public List<ModuleClusion> getIncludes()
  {
    return includes;
  }

  /**
   * Returns the list of exclusions.
   *
   * @return the list of exclusions.
   */
  public List<ModuleClusion> getExcludes()
  {
    return excludes;
  }

  // --- business -------------------------------------------------------------

  /**
   * Checks if the matcher matches with the given module name.
   *
   * @param name the module name to match.
   * @return <code>true</code> on a match, <code>false</code> otherwise.
   */
  public boolean matches(final String name)
  {
    final boolean included = matches(includes, name, true);
    if (included)
    {
      final boolean excluded = matches(excludes, name, false);
      return !excluded;
    }

    return included;
  }

  private boolean matches(final List<ModuleClusion> cludes, final String name,
      final boolean defaultValue)
  {
    if (cludes.isEmpty())
    {
      return defaultValue;
    }
    for (final ModuleClusion clusion : cludes)
    {
      if (clusion.match(name))
      {
        return true;
      }
    }
    return false;
  }

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
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, null);
  }
}
