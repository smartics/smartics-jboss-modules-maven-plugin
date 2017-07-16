/*
 * Copyright 2013-2017 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.test.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Utilities to make unit tests with streams to resources from the class path
 * easier.
 *
 * @author <a href="mailto:robert.reiner@smartics.de">Robert Reiner</a>
 * @version $Revision:591 $
 */
public final class IoTestUtils
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Utility class pattern.
   */
  private IoTestUtils()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Returns a stream to a resource on the class path loaded by the class loader
   * of this class.
   *
   * @param resourceName name of the resource on the class path.
   * @return the opened buffered stream. The client is responsible to close it.
   * @throws IllegalArgumentException if {@code resourceName} cannot be found.
   */
  public static InputStream openStreamFromResource(final String resourceName)
    throws IllegalArgumentException
  {
    final ClassLoader classLoader = IoTestUtils.class.getClassLoader(); // NOPMD
    return openStreamFromResource(classLoader, resourceName);
  }

  /**
   * Returns a stream to a resource on the class path loaded by the class loader
   * that has loaded the given {@code type}.
   *
   * @param type the type to refer to the class loader to load the resource
   *          from. The resource is relative to this type.
   * @param resourceName name of the resource on the class path.
   * @return the opened buffered stream. The client is responsible to close it.
   * @throws IllegalArgumentException if {@code resourceName} cannot be found.
   */
  public static InputStream openStreamFromResourceByTypeClassLoader(
      final Class<?> type, final String resourceName)
    throws IllegalArgumentException
  {
    try
    {
      return openStreamFromResource(type.getClassLoader(), resourceName);
    }
    catch (final IllegalArgumentException e)
    {
      throw new IllegalArgumentException(
          "Cannot find resource '" // NOPMD
              + resourceName
              + "' on the class path using the class loader of type '"
              + type.getClass().getName() + "'.", e);
    }
  }

  /**
   * Returns a stream to a resource on the class path.
   *
   * @param classLoader the class loader to load the resource from.
   * @param resourceName name of the resource on the class path.
   * @return the opened buffered stream. The client is responsible to close it.
   * @throws IllegalArgumentException if {@code resourceName} cannot be found.
   */
  public static InputStream openStreamFromResource(
      final ClassLoader classLoader, final String resourceName)
    throws IllegalArgumentException
  {
    final InputStream input = classLoader.getResourceAsStream(resourceName);
    if (input == null)
    {
      throw new IllegalArgumentException(
          "Cannot find resource '" + resourceName
              + "' on the class path using the given class loader.");
    }

    final BufferedInputStream bin = new BufferedInputStream(input);
    return bin;
  }

  /**
   * Returns the specified file from a resource relative to the given type.
   *
   * @param type the type to refer to the class loader to load the resource
   *          from. The resource is relative to this type.
   * @param relativeResourceName name of the resource on the class path,
   *          relative to the passed in {@code type}.
   * @return the opened buffered stream. The client is responsible to close it.
   * @throws IllegalArgumentException if {@code relativeResourceName} cannot be
   *           found.
   */
  public static InputStream openStreamFromResource(final Class<?> type,
      final String relativeResourceName) throws IllegalArgumentException
  {
    final InputStream input = type.getResourceAsStream(relativeResourceName);
    if (input == null)
    {
      throw new IllegalArgumentException(
          "Cannot find resource '" + relativeResourceName
              + "' on the class path relative to type '"
              + type.getClass().getName() + "'.");
    }

    final BufferedInputStream bin = new BufferedInputStream(input);
    return bin;
  }

  // --- object basics --------------------------------------------------------

}
