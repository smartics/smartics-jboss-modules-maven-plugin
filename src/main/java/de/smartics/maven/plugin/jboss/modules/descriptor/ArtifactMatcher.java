package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.util.lang.Arg;

/**
 * Descriptor to define the rules for matching artifacts to be included as
 * resources to a module.
 */
public final class ArtifactMatcher
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of inclusions.
   */
  private final List<Clusion> includes;

  /**
   * The list of exclusions.
   */
  private final List<Clusion> excludes;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ArtifactMatcher(final Builder builder)
  {
    includes = builder.includes;
    excludes = builder.excludes;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ArtifactMatcher}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The list of inclusions.
     */
    private final List<Clusion> includes = new ArrayList<Clusion>();

    /**
     * The list of exclusions.
     */
    private final List<Clusion> excludes = new ArrayList<Clusion>();

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
    public void addInclude(final Clusion include) throws NullPointerException
    {
      includes.add(Arg.checkNotNull("include", include));
    }

    /**
     * Adds an exclude to the list of excludes.
     *
     * @param exclude the exclude to add.
     * @throws NullPointerException if {@code exclude} is <code>null</code>.
     */
    public void addExclude(final Clusion exclude) throws NullPointerException
    {
      excludes.add(Arg.checkNotNull("exclude", exclude));
    }

    /**
     * Builds an instance of {@link ArtifactMatcher}.
     *
     * @return the instance.
     */
    public ArtifactMatcher build()
    {
      return new ArtifactMatcher(this);
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
  public List<Clusion> getIncludes()
  {
    return includes;
  }

  /**
   * Returns the list of exclusions.
   *
   * @return the list of exclusions.
   */
  public List<Clusion> getExcludes()
  {
    return excludes;
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
