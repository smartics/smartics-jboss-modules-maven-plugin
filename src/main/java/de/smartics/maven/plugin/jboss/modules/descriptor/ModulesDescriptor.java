package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.smartics.util.lang.Arg;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Descriptor containing information on modules configurations. This information
 * is parsed from module XML documents.
 * <p>
 * It basically contains module descriptors, but may also contain any meta data.
 * </p>
 */
public final class ModulesDescriptor
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The identifier of the set of modules. Used for error reporting mainly.
   * Typically this is the system identifier of the XML file this descriptor is
   * based on.
   */
  private final String modulesId;

  /**
   * The list of descriptors for modules.
   */
  private final List<ModuleDescriptor> descriptors =
      new ArrayList<ModuleDescriptor>();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param modulesId the identifier of the set of modules. Used for error
   *          reporting mainly. Typically this is the system identifier of the
   *          XML file this descriptor is based on.
   */
  public ModulesDescriptor(final String modulesId)
  {
    this.modulesId = Arg.checkNotBlank("modulesId", modulesId);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the identifier of the set of modules. Used for error reporting
   * mainly. Typically this is the system identifier of the XML file this
   * descriptor is based on.
   *
   * @return the identifier of the set of modules.
   */
  public String getModulesId()
  {
    return modulesId;
  }

  // --- business -------------------------------------------------------------

  /**
   * Adds the given descriptor to the list of module descriptors.
   *
   * @param descriptor the descriptor to add.
   * @throws NullPointerException if {@code descriptor} is <code>null</code>.
   */
  public void addDescriptor(final ModuleDescriptor descriptor)
    throws NullPointerException
  {
    descriptors.add(Arg.checkNotNull("descriptor", descriptor));
  }

  /**
   * Returns the list of descriptors for modules.
   *
   * @return the list of descriptors for modules.
   */
  @SuppressWarnings("unchecked")
  public List<ModuleDescriptor> getDescriptors()
  {
    return Collections.unmodifiableList(descriptors);
  }

  /**
   * Adds the given slot to all module descriptors that have not set their slots
   * explicitly.
   *
   * @param slot the slot to set.
   */
  public void applyDefaultSlot(final String slot)
  {
    for (final ModuleDescriptor descriptor : descriptors)
    {
      descriptor.applySlot(slot);
    }
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
    return ToStringBuilder.reflectionToString(this);
  }
}
