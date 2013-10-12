package de.smartics.maven.plugin.jboss.modules.descriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

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
     * The export attribute for the dependency.
     */
    private Boolean export = Boolean.FALSE;

    /**
     * The services attribute for the dependency.
     */
    private String services;

    /**
     * The optional attribute for the dependency.
     */
    private Boolean optional = Boolean.FALSE;

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
      if (StringUtils.isNotBlank(slot))
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
