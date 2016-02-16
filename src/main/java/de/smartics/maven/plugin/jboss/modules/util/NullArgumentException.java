/*
 * Copyright 2013-2016 smartics, Kronseder & Reiner GmbH
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
 * Signals that a given argument is <code>null</code> where is must no be
 * <code>null</code>.
 * <p>
 * Due to Java conventions the exception does not derive from
 * {@link IllegalArgumentException} as one might expect.
 * </p>
 * <blockquote>If a caller passes <code>null</code> in some parameter for which
 * null values are prohibited, convention dictates that
 * {@link NullPointerException} be thrown rather than
 * {@link IllegalArgumentException}.</blockquote>
 * <p>
 * Effective Java - Second Edition, p 248
 * </p>
 */
public class NullArgumentException extends NullPointerException
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
  public NullArgumentException()
  {
    this(null);
  }

  /**
   * Convenience constructor.
   *
   * @param argName the name of the argument that was <code>null</code>.
   */
  public NullArgumentException(final String argName)
  {
    this(argName, null);
  }

  /**
   * Default constructor.
   *
   * @param argName the name of the argument that was <code>null</code>.
   * @param message an optional additional message to provide information about
   *          the context of the argument.
   * @see java.lang.IllegalArgumentException#IllegalArgumentException(java.lang.String)
   */
  public NullArgumentException(final String argName, final String message)
  {
    super((argName == null ? "Argument" : argName) + " must not be 'null'"
          + (message != null ? ": " + message : "."));
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
