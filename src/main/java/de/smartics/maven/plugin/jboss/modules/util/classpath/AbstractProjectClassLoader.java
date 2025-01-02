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
package de.smartics.maven.plugin.jboss.modules.util.classpath;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

/**
 * Base implementation of class loaders that serve classes from the given
 * directories.
 */
public abstract class AbstractProjectClassLoader extends ClassLoader
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * Reference to the logger for this class.
   */
  private static final Logger LOG = Logger
      .getLogger(ClassPathListingFactory.class.getName());

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param parent the parent class loader.
   */
  protected AbstractProjectClassLoader(final ClassLoader parent)
  {
    super(parent);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Ensures that the package of a class specified by the given name is already
   * defined. If it is not defined the loaded class instance will not have a
   * package reference.
   *
   * @param className the name of the class whose package is to check to be
   *          already defined.
   */
  protected void ensurePackageProvided(final String className)
  {
    final int lastDotIndex = className.lastIndexOf('.');
    if (lastDotIndex != -1)
    {
      final String packageName = className.substring(0, lastDotIndex);
      final Package paccage = getPackage(packageName);
      if (paccage == null)
      {
        definePackage(packageName, null, null, null, null, null, null, null);
      }
    }
  }

  /**
   * Loads the class. Ensures that the package of the class is defined.
   *
   * @param className the name of the class.
   * @param classFile the file with the binary data.
   * @return the loaded class definition.
   * @throws ClassNotFoundException if the class cannot be loaded.
   */
  protected Class<?> loadClassFile(final String className, final File classFile)
    throws ClassNotFoundException
  {
    ensurePackageProvided(className);
    InputStream in = null;
    try
    {
      in = new BufferedInputStream(new FileInputStream(classFile));
      final byte[] data = IOUtils.toByteArray(in);
      final Class<?> clazz = defineClass(className, data, 0, data.length);
      return clazz;
    }
    catch (final IOException e)
    {
      final String message =
          "Cannot load class '" + className + "' from file '" + classFile
              + "'.";
      LOG.fine(message);

      throw new ClassNotFoundException(message, e);
    }
    finally
    {
      IOUtils.closeQuietly(in);
    }
  }

  /**
   * Opens a reader to the class file within the archive.
   *
   * @param className the name of the class to load.
   * @param fileName the name of the class file to load.
   * @param dirName the name of the directory the archive file is located.
   * @return the reader to the source file for the given class in the archive.
   * @throws ClassNotFoundException if the class cannot be found.
   */
  protected Class<?> loadClassFromLibrary(final String className,
      final String fileName, final File dirName) throws ClassNotFoundException
  {
    ensurePackageProvided(className);
    try
    {
      final JarFile jarFile = new JarFile(dirName); // NOPMD
      final JarEntry entry = jarFile.getJarEntry(fileName);
      if (entry != null)
      {
        InputStream in = null;
        try
        {
          in = new BufferedInputStream(jarFile.getInputStream(entry));
          final byte[] data = IOUtils.toByteArray(in);
          final Class<?> clazz = defineClass(className, data, 0, data.length);
          return clazz;
        }
        finally
        {
          IOUtils.closeQuietly(in);
        }
      }
    }
    catch (final IOException e)
    {
      final String message =
          "Cannot load class '" + className + "' from file '" + dirName + "'.";
      LOG.fine(message);

      throw new ClassNotFoundException(message, e);
    }

    final String message =
        "Cannot load class '" + className + "' from file '" + dirName + "'.";
    LOG.fine(message);

    throw new ClassNotFoundException(message);
  }

  /**
   * Opens a reader to the resource file within the archive.
   *
   * @param resourceName the name of the resource to load.
   * @param fileName the name of the resource file to load.
   * @param dirName the name of the directory the archive file is located.
   * @return the reader to the source file for the given class in the archive.
   * @throws IOException if the class cannot be found.
   */
  protected URL loadResourceFromLibrary(final String resourceName,
      final String fileName, final File dirName) throws IOException
  {
    ensurePackageProvided(resourceName);
    try
    {
      return new URL("jar:file:" + dirName.getName() + "!/" + fileName);
    }
    catch (final IOException e)
    {
      final String message =
          "Cannot load class '" + resourceName + "' from file '" + dirName
              + "'.";
      LOG.fine(message);
      final IOException ioe = new IOException(message);
      ioe.initCause(e);
      throw ioe;
    }
  }

  // --- object basics --------------------------------------------------------

}
