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
package de.smartics.maven.plugin.jboss.modules;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A module dependency within a {@link Module module}.
 *
 * @see <a
 *      href="https://docs.jboss.org/author/display/MODULES/Module+descriptors">Module
 *      descriptors</a>
 * @impl Currently imports and exports are not supported, since the syntax and
 *       semantics are not quite clear.
 */
public class Dependency
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The name of the module upon which this module depends. The value is
   * mandatory.
   */
  private String name;

  /**
   * The version slot of the module upon which this module depends. Defaults to
   * "main".
   */
  private String slot;

  /**
   * The flag to specify whether this dependency is re-exported by default. If
   * not specified, defaults to <code>false</code>.
   */
  private boolean export;

  /**
   * The modules services.
   * <p>
   * Specify whether this dependency's services are imported and/or exported.
   * Possible values are <code>none</code>, <code>import</code>, or
   * <code>export</code>. Defaults to <code>none</code>.
   * </p>
   */
  private String services;

  /**
   * The flag to the module to specify whether this dependency is optional.
   * Defaults to <code>false</code>.
   */
  private boolean optional;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public Dependency()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the name of the module upon which this module depends. The value is
   * mandatory.
   *
   * @return the name of the module upon which this module depends.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the module upon which this module depends. The value is
   * mandatory.
   *
   * @param name the name of the module upon which this module depends.
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * Returns the version slot of the module upon which this module depends.
   * Defaults to "main".
   *
   * @return the version slot of the module upon which this module depends.
   */
  public String getSlot()
  {
    return slot;
  }

  /**
   * Sets the version slot of the module upon which this module depends.
   * Defaults to "main".
   *
   * @param slot the version slot of the module upon which this module depends.
   */
  public void setSlot(final String slot)
  {
    this.slot = slot;
  }

  /**
   * Returns the flag to specify whether this dependency is re-exported by
   * default. If not specified, defaults to <code>false</code>.
   *
   * @return the flag to specify whether this dependency is re-exported by
   *         default.
   */
  public boolean isExport()
  {
    return export;
  }

  /**
   * Sets the flag to specify whether this dependency is re-exported by default.
   * If not specified, defaults to <code>false</code>.
   *
   * @param export the flag to specify whether this dependency is re-exported by
   *          default.
   */
  public void setExport(final boolean export)
  {
    this.export = export;
  }

  /**
   * Returns the modules services.
   * <p>
   * Specify whether this dependency's services are imported and/or exported.
   * Possible values are <code>none</code>, <code>import</code>, or
   * <code>export</code>. Defaults to <code>none</code>.
   * </p>
   *
   * @return the modules services.
   */
  public String getServices()
  {
    return services;
  }

  /**
   * Sets the modules services.
   * <p>
   * Specify whether this dependency's services are imported and/or exported.
   * Possible values are <code>none</code>, <code>import</code>, or
   * <code>export</code>. Defaults to <code>none</code>.
   * </p>
   *
   * @param services the modules services.
   */
  public void setServices(final String services)
  {
    this.services = services;
  }

  /**
   * Returns the flag to the module to specify whether this dependency is
   * optional. Defaults to <code>false</code>.
   *
   * @return the flag to the module to specify whether this dependency is
   *         optional.
   */
  public boolean isOptional()
  {
    return optional;
  }

  /**
   * Sets the flag to the module to specify whether this dependency is optional.
   * Defaults to <code>false</code>.
   *
   * @param optional the flag to the module to specify whether this dependency
   *          is optional.
   */
  public void setOptional(final boolean optional)
  {
    this.optional = optional;
  }

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(name);
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

    final Dependency other = (Dependency) object;

    return (ObjectUtils.equals(name, other.name) && export == other.export
            && optional == other.optional
            && ObjectUtils.equals(services, other.services) && ObjectUtils
        .equals(slot, other.slot));
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
