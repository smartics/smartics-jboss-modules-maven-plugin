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
package de.smartics.maven.plugin.jboss.modules.util.classpath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * A class loader that serves classes from the given directories.
 */
public class ProjectClassLoader extends AbstractProjectClassLoader
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of root directories and Java archive files to search for classes.
   */
  private final List<File> rootDirectories;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Convenience constructor using the {@code Thread.currentThread().getContextClassLoader()}.
   *
   * @param rootDirectories the list of root directories to search for classes.
   */
  public ProjectClassLoader(final List<File> rootDirectories)
  {
    this(Thread.currentThread().getContextClassLoader(), rootDirectories);
  }

  /**
   * Convenience constructor to specify the directories as {@link String}s and
   * using the {@code Thread.currentThread().getContextClassLoader()}.
   *
   * @param rootDirectoryNames the list of root directory names to search for
   *          classes.
   */
  public ProjectClassLoader(final Collection<String> rootDirectoryNames)
  {
    this(initDirectories(Logger.getLogger(ProjectClassLoader.class.getName()),
        rootDirectoryNames));
  }

  @Override
  protected URL findResource(final String name)
  {
    for (File directoryOrArchive : rootDirectories)
    {
      try
      {
        if (directoryOrArchive.isDirectory())
        {
          final File resourceFile = new File(directoryOrArchive, name);
          if (resourceFile.canRead())
          {
            return resourceFile.toURI().toURL();
          }
        }
        else
        {
          String resourceName = name;
          final int index = name.lastIndexOf('.');
          if (index != -1)
          {
            resourceName = name.substring(0, index);
          }
          resourceName = resourceName.replace('/', '.');
          return loadResourceFromLibrary(resourceName, name, directoryOrArchive);
        }
      }
      catch (final IOException e)
      {
        // Ignore this and try the next location...
      }
    }
    return super.findResource(name);
  }

  @Override
  protected Enumeration<URL> findResources(final String name)
  {
    return new Enumeration<URL>()
    {
      private URL element = findResource(name);

      public boolean hasMoreElements()
      {
        return this.element != null;
      }

      public URL nextElement()
      {
        if (this.element != null)
        {
          final URL element = this.element;
          this.element = null;
          return element;
        }
        throw new NoSuchElementException();
      }
    };
  }

  /**
   * Convenience constructor to specify the directories as {@link String}s.
   *
   * @param parent the parent class loader.
   * @param rootDirectoryNames the list of root directory names to search for
   *          classes.
   */
  public ProjectClassLoader(final ClassLoader parent,
      final Collection<String> rootDirectoryNames)
  {
    this(parent, initDirectories(
        Logger.getLogger(ProjectClassLoader.class.getName()),
        rootDirectoryNames));
  }

  /**
   * Default constructor.
   *
   * @param parent the parent class loader.
   * @param rootDirectories the list of root directories to search for classes.
   */
  public ProjectClassLoader(final ClassLoader parent,
      final List<File> rootDirectories)
  {
    super(parent);
    this.rootDirectories = Collections.unmodifiableList(rootDirectories);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  /**
   * Checks the directories for accessibility as root directories. Any directory
   * that is not accessible is dismissed and a debug message is logged.
   *
   * @param log the logger to use for logging debug messages.
   * @param directoryNames the collection of directory names to analyze.
   * @return the valid directories.
   */
  private static List<File> initDirectories(final Logger log,
      final Collection<String> directoryNames)
  {
    final List<File> directories = new ArrayList<File>(directoryNames.size());
    for (final String directoryName : directoryNames)
    {
      final File directory = new File(directoryName);
      if (directory.canRead()
          && (directory.isDirectory() || directory.isFile()
                                         && isArchive(directoryName)))
      {
        directories.add(directory);
      }
      else
      {
        final String message =
            "Cannot access '" + directoryName
                + "' as directory or Java archive."
                + " Ignoring as classpath root.";
        log.fine(message);

      }
    }
    return directories;
  }

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException
  {
    for (File directoryOrArchive : rootDirectories)
    {
      try
      {
        final String fileName = name.replace('.', '/') + ".class";
        if (directoryOrArchive.isDirectory())
        {
          final File classFile = new File(directoryOrArchive, fileName);
          if (classFile.canRead())
          {
            return loadClassFile(name, classFile);
          }
        }
        else
        {
          return loadClassFromLibrary(name, fileName, directoryOrArchive);
        }
      }
      catch (final ClassNotFoundException e)
      {
        // Ignore this and try the next location...
      }
    }
    return super.findClass(name);
  }

  /**
   * Checks if the given root refers to an archive or not. Simply the file
   * extension is checked. Accessibility et al. is not considered.
   *
   * @param root the root to check.
   * @return <code>true</code> if root refers to an archive file,
   *         <code>false</code> otherwise.
   * @throws NullPointerException if <code>root</code> is <code>null</code>.
   */
  private static boolean isArchive(final String root)
    throws NullPointerException
  {
    return root.endsWith(".jar") || root.endsWith(".zip");
  }
  // --- object basics --------------------------------------------------------

}
