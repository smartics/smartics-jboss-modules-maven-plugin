/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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
 * Stores information about a matcher on modules to apply dependency information
 * to. A module descriptor stores dependencies for its resources. The
 * dependencies are matched with Maven artifacts and mapped to module names.
 * These module names are matched to check if the information of a dependencies
 * descriptor is to be applied.
 */
public final class DependenciesDescriptor
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The matcher to match modules by their name.
   */
  private final ModuleMatcher matcher;

  /**
   * The slot of the dependency.
   */
  private final String slot;

  /**
   * Should the dependency be skipped?
   */
  private final Boolean skip;

  /**
   * The export attribute for the dependency.
   */
  private final Boolean export;

  /**
   * The services attribute for the dependency.
   */
  private final String services;

  /**
   * The optional attribute for the dependency.
   */
  private final Boolean optional;

  /**
   * The XML fragment specifying imports for the dependency.
   */
  private final String importsXml;

  /**
   * The XML fragment specifying exports for the dependency.
   */
  private final String exportsXml;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private DependenciesDescriptor(final Builder builder)
  {
    matcher = builder.matcher;
    slot = builder.slot;
    skip = builder.skip;
    export = builder.export;
    services = builder.services;
    optional = builder.optional;
    importsXml = builder.importsXml;
    exportsXml = builder.exportsXml;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link DependenciesDescriptor}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The matcher to match modules by their name.
     */
    private ModuleMatcher matcher;

    /**
     * The slot of the dependency.
     */
    private String slot;

    /**
     * Should the dependency be skipped?
     */
    private Boolean skip;

    /**
     * The export attribute for the dependency.
     */
    private Boolean export;

    /**
     * The services attribute for the dependency.
     */
    private String services;

    /**
     * The optional attribute for the dependency.
     */
    private Boolean optional;

    /**
     * The XML fragment specifying imports for the dependency.
     */
    private String importsXml;

    /**
     * The XML fragment specifying exports for the dependency.
     */
    private String exportsXml;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Sets the matcher to match modules by their name.
     *
     * @param matcher the matcher to match modules by their name.
     * @return a reference to this builder.
     */
    public Builder with(final ModuleMatcher matcher)
    {
      this.matcher = matcher;
      return this;
    }

    /**
     * Sets the slot of the dependency.
     *
     * @param slot the slot of the dependency.
     * @return a reference to this builder.
     */
    public Builder withSlot(final String slot)
    {
      if (StringUtils.isNotBlank(slot))
      {
        this.slot = slot;
      }
      return this;
    }

      /**
       * Sets if the dependency should be skipped
       *
       * @param skip a boolean value in its string representation.
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
     * Sets the export attribute for the dependency.
     *
     * @param export the export attribute for the dependency.
     * @return a reference to this builder.
     */
    public Builder withExport(final String export)
    {
      if (StringUtils.isNotBlank(export))
      {
        this.export = Boolean.parseBoolean(export);
      }
      return this;
    }

    /**
     * Sets the services attribute for the dependency.
     *
     * @param services the services attribute for the dependency.
     * @return a reference to this builder.
     */
    public Builder withServices(final String services)
    {
      if (StringUtils.isNotBlank(services))
      {
        this.services = services;
      }
      return this;
    }

    /**
     * Sets the optional attribute for the dependency.
     *
     * @param optional the optional attribute for the dependency.
     * @return a reference to this builder.
     */
    public Builder withOptional(final String optional)
    {
      if (StringUtils.isNotBlank(optional))
      {
        this.optional = Boolean.parseBoolean(optional);
      }
      return this;
    }

    /**
     * Sets the XML fragment specifying imports for the dependency.
     *
     * @param importsXml the XML fragment specifying imports for the dependency.
     * @return a reference to this builder.
     */
    public Builder withImportsXml(final String importsXml)
    {
      this.importsXml = importsXml;
      return this;
    }

    /**
     * Sets the XML fragment specifying exports for the dependency.
     *
     * @param exportsXml the XML fragment specifying exports for the dependency.
     * @return a reference to this builder.
     */
    public Builder withExportsXml(final String exportsXml)
    {
      this.exportsXml = exportsXml;
      return this;
    }

    // --- business -----------------------------------------------------------

    /**
     * Merges the content of the given descriptor into the builder instance.
     * <p>
     * Not that the matcher information is not merged.
     * </p>
     *
     * @param moduleName the name of the module to merge the descriptor.
     * @param descriptor the descriptor information to merge into this instance.
     */
    public void merge(final String moduleName,
        final DependenciesDescriptor descriptor)
    {
      slot = merge("slot", moduleName, slot, descriptor.slot);
      skip = merge("skip", moduleName, skip, descriptor.skip);
      export = merge("export", moduleName, export, descriptor.export);
      services = merge("services", moduleName, services, descriptor.services);
      optional = merge("optional", moduleName, optional, descriptor.optional);
      importsXml =
          merge("imports", moduleName, importsXml, descriptor.importsXml);
      exportsXml =
          merge("exports", moduleName, exportsXml, descriptor.exportsXml);
    }

    private <T> T merge(final String property, final String moduleName,
        final T value1, final T value2)
    {
      if (value1 != null && value2 != null && !value1.equals(value2))
      {
        throw new IllegalArgumentException(String.format(
            "Module %s: Cannot merge %s: '%s' differs from '%s'.", moduleName,
            property, value1, value2));
      }

      return value1 == null ? value2 : value1;
    }

    /**
     * Builds an instance of {@link DependenciesDescriptor}.
     *
     * @return the instance.
     */
    public DependenciesDescriptor build()
    {
      if (matcher == null)
      {
        matcher = new ModuleMatcher.Builder().build();
      }

      if (export == null)
      {
        export = Boolean.FALSE;
      }
      if (optional == null)
      {
        optional = Boolean.FALSE;
      }

      return new DependenciesDescriptor(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the matcher to match modules by their name.
   *
   * @return the matcher to match modules by their name.
   */
  public ModuleMatcher getMatcher()
  {
    return matcher;
  }

  /**
   * Returns the slot of the dependency.
   *
   * @return the slot of the dependency.
   */
  public String getSlot()
  {
    return slot;
  }

  /**
   * Returns if the dependency should be skipped.
   *
   * @return if the dependency should be skipped.
   */
  public Boolean getSkip()
    {
      return skip;
    }

  /**
   * Returns the export attribute for the dependency.
   *
   * @return the export attribute for the dependency.
   */
  public Boolean getExport()
  {
    return export;
  }

  /**
   * Returns the services attribute for the dependency.
   *
   * @return the services attribute for the dependency.
   */
  public String getServices()
  {
    return services;
  }

  /**
   * Returns the optional attribute for the dependency.
   *
   * @return the optional attribute for the dependency.
   */
  public Boolean getOptional()
  {
    return optional;
  }

  /**
   * Returns the XML fragment specifying imports for the dependency.
   *
   * @return the XML fragment specifying imports for the dependency.
   */
  public String getImportsXml()
  {
    return importsXml;
  }

  /**
   * Returns the XML fragment specifying exports for the dependency.
   *
   * @return the XML fragment specifying exports for the dependency.
   */
  public String getExportsXml()
  {
    return exportsXml;
  }

  // --- business -------------------------------------------------------------

  /**
   * Checks if the matcher of this descriptor matches with the given module
   * name.
   *
   * @param name the module name to match.
   * @return <code>true</code> on a match, <code>false</code> otherwise.
   */
  public boolean matches(final String name)
  {
    return matcher.matches(name);
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final DependenciesDescriptor that = (DependenciesDescriptor) o;

    if (slot != null ? !slot.equals(that.slot) : that.slot != null) return false;
    if (skip != null ? !skip.equals(that.skip) : that.skip != null) return false;
    if (export != null ? !export.equals(that.export) : that.export != null) return false;
    if (services != null ? !services.equals(that.services) : that.services != null) return false;
    if (optional != null ? !optional.equals(that.optional) : that.optional != null) return false;
    if (importsXml != null ? !importsXml.equals(that.importsXml) : that.importsXml != null) return false;
    return !(exportsXml != null ? !exportsXml.equals(that.exportsXml) : that.exportsXml != null);

  }

  @Override
  public int hashCode() {
    int result = slot != null ? slot.hashCode() : 0;
    result = 31 * result + (skip != null ? skip.hashCode() : 0);
    result = 31 * result + (export != null ? export.hashCode() : 0);
    result = 31 * result + (services != null ? services.hashCode() : 0);
    result = 31 * result + (optional != null ? optional.hashCode() : 0);
    result = 31 * result + (importsXml != null ? importsXml.hashCode() : 0);
    result = 31 * result + (exportsXml != null ? exportsXml.hashCode() : 0);
    return result;
  }
}
