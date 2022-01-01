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
package de.smartics.maven.plugin.jboss.modules.util;

/**
 * Utility class to check an argument.
 */
public final class Arg
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Utility class pattern.
   */
  private Arg()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Checks if {@code value} is <code>null</code>.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not {@code null}.
   * @return the passed in {@code value}.
   * @throws NullPointerException if {@code value} is <code>null</code>.
   * @param <T> the type of the argument to check.
   */
  public static <T> T checkNotNull(final String name, final T value)
    throws NullPointerException
  {
    checkNotNull(name, value, null);
    return value;
  }

  /**
   * Checks if {@code value} is <code>null</code> providing an additional
   * message.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not {@code null}.
   * @param message the message to pass to the exception as additional
   *          information to the standard message being generated.
   * @return the passed in {@code value}.
   * @throws NullPointerException if {@code value} is <code>null</code>.
   * @param <T> the type of the argument to check.
   */
  public static <T> T checkNotNull(final String name, final T value,
      final String message) throws NullPointerException
  {
    if (value == null)
    {
      throw new NullArgumentException(name, message);
    }

    return value;
  }

  /**
   * Checks if {@code value} is blank.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not blank.
   * @return the passed in {@code value}.
   * @throws NullArgumentException if {@code value} is <code>null</code>.
   * @throws BlankArgumentException if {@code value} is blank.
   * @param <T> a character sequence like a {@link String}.
   */
  public static <T extends CharSequence> T checkNotBlank(final String name,
      final T value) throws NullArgumentException, BlankArgumentException
  {
    checkNotBlank(name, value, null);
    return value;
  }

  /**
   * Checks if {@code value} is blank providing an additional message.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not blank.
   * @param message the message to pass to the exception as additional
   *          information to the standard message being generated.
   * @return the passed in {@code value}.
   * @throws NullArgumentException if {@code value} is <code>null</code>.
   * @throws BlankArgumentException if {@code value} is blank.
   * @param <T> a character sequence like a {@link String}.
   */
  public static <T extends CharSequence> T checkNotBlank(final String name,
      final T value, final String message) throws NullArgumentException,
    BlankArgumentException
  {
    if (value == null)
    {
      throw new NullArgumentException(name, message);
    }
    if (isBlank(value))
    {
      throw new BlankArgumentException(name, message);
    }

    return value;
  }

  /**
   * Checks if {@code value} is blank except <code>null</code>.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not blank but may be
   *          <code>null</code>.
   * @return the passed in {@code value}.
   * @throws BlankExceptNullArgumentException if {@code value} is blank and not
   *           <code>null</code>.
   * @param <T> a character sequence like a {@link String}.
   */
  public static <T extends CharSequence> T checkNotBlankExceptNull(
      final String name, final T value) throws BlankExceptNullArgumentException
  {
    checkNotBlankExceptNull(name, value, null);
    return value;
  }

  /**
   * Checks if {@code value} is blank except <code>null</code> providing an
   * additional message.
   *
   * @param name the name of the argument of error reporting. Not used if no
   *          exception is thrown. May be <code>null</code>, although not
   *          recommended.
   * @param value the value of the argument to check to be not blank but may be
   *          <code>null</code>.
   * @param message the message to pass to the exception as additional
   *          information to the standard message being generated.
   * @return the passed in {@code value}.
   * @throws BlankExceptNullArgumentException if {@code value} is blank and not
   *           <code>null</code>.
   * @param <T> a character sequence like a {@link String}.
   */
  public static <T extends CharSequence> T checkNotBlankExceptNull(
      final String name, final T value, final String message)
    throws BlankExceptNullArgumentException
  {
    if (value != null && isBlank(value))
    {
      throw new BlankArgumentException(name, message);
    }
    return value;
  }

  // Copied from Apaches StringUtils and modified to cope with CharSequences.
  // http://commons.apache.org/lang/api-2.6/org/apache/commons/lang/StringUtils.html#isBlank%28java.lang.String%29
  private static <T extends CharSequence> boolean isBlank(final T str)
  {
    // CHECKSTYLE:OFF
    int strLen;
    if (str == null || (strLen = str.length()) == 0) // NOPMD
    {
      return true;
    }
    for (int i = 0; i < strLen; i++)
    {
      if ((Character.isWhitespace(str.charAt(i)) == false)) // NOPMD
      {
        return false;
      }
    }
    return true;
    // CHECKSTYLE:ON
  }

  // --- object basics --------------------------------------------------------

}
