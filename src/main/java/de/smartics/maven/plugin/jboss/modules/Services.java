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

/**
 * Handles matching module names for tagging its services attribute.
 */
public class Services extends AbstractModuleNameMatcher
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The default value for the services flag.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String NONE = "none";

  // --- members --------------------------------------------------------------

  /**
   * The value for the matching modules.
   */
  private String value = NONE;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public Services()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the value for the matching modules.
   *
   * @return the value for the matching modules.
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Sets the value for the matching modules.
   *
   * @param value the value for the matching modules.
   */
  public void setValue(final String value)
  {
    this.value = value;
  }
  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
