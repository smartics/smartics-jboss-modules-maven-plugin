package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.smartics.util.lang.Arg;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Stores information that has to be applied to dependencies if the matcher
 * matches the name of a module.
 */
public final class ApplyToDependencies
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The descriptors of dependencies to match modules names to have their
   * information applied.
   */
  private final List<DependenciesDescriptor> descriptors;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ApplyToDependencies(final Builder builder)
  {
    descriptors = builder.descriptors;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ApplyToDependencies}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The descriptors of dependencies to match modules names to have their
     * information applied.
     */
    private final List<DependenciesDescriptor> descriptors =
        new ArrayList<DependenciesDescriptor>();

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Adds the given descriptor to the list of descriptors.
     *
     * @param descriptor the descriptor to add.
     * @throws NullPointerException if {@code descriptor} is <code>null</code>.
     */
    public void add(final DependenciesDescriptor descriptor)
      throws NullPointerException
    {
      descriptors.add(Arg.checkNotNull("descriptor", descriptor));
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds an instance of {@link ApplyToDependencies}.
     *
     * @return the instance.
     */
    public ApplyToDependencies build()
    {
      return new ApplyToDependencies(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the descriptors of dependencies to match modules names to have
   * their information applied.
   *
   * @return the descriptors of dependencies to match modules names to have
   *         their information applied.
   */
  @SuppressWarnings("unchecked")
  public List<DependenciesDescriptor> getDescriptors()
  {
    return Collections.unmodifiableList(descriptors);
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
