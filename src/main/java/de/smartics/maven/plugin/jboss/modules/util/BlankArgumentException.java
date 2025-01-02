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
package de.smartics.maven.plugin.jboss.modules.util;

/**
 * Signals that a given {@link String} argument is blank where is must no be
 * blank.
 */
public class BlankArgumentException extends IllegalArgumentException
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The class version identifier.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  private static final long serialVersionUID = 1L;

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Convenience constructor if no argument name needs to be provided. This
   * constructor is not recommended since the exception's message is less
   * verbose.
   */
  public BlankArgumentException()
  {
    this(null);
  }

  /**
   * Convenience constructor.
   *
   * @param argName the name of the {@link String} argument that was blank.
   */
  public BlankArgumentException(final String argName)
  {
    this(argName, null);
  }

  /**
   * Default constructor.
   *
   * @param argName the name of the {@link String} argument that was blank.
   * @param message an optional additional message to provide information about
   *          the context of the argument.
   * @see java.lang.IllegalArgumentException#IllegalArgumentException(java.lang.String)
   */
  public BlankArgumentException(final String argName, final String message)
  {
    super((argName == null ? "Argument" : argName) + " must not be blank"
          + (message != null ? ": " + message : "."));
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
