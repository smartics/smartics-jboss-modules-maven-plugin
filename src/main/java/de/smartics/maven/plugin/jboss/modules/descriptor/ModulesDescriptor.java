/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
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
import java.util.Collections;
import org.apache.commons.lang.builder.ToStringStyle;

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
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, null);
  }
}
