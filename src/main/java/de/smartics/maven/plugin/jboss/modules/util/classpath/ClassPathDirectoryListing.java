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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * Lists the content of a directory on the class path.
 * <p>
 * Currently only the protocols
 * </p>
 * <ol>
 * <li>file system (<code>file</code>) and</li>
 * <li>JAR files (<code>jar</code>)</li>
 * </ol>
 * <p>
 * are supported.
 * </p>
 */
public class ClassPathDirectoryListing
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The context to load the directory listings.
   */
  private final ClassPathContext context;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param classLoader the class loader to load the directory listings.
   * @throws NullPointerException if {@code classLoader} is <code>null</code>.
   */
  public ClassPathDirectoryListing(final ClassLoader classLoader)
    throws NullPointerException
  {
    this(new ClassPathContext(classLoader, null));
  }

  /**
   * Default constructor.
   *
   * @param context the context to load the directory listings.
   * @throws NullPointerException if {@code classLoader} is <code>null</code>.
   */
  public ClassPathDirectoryListing(final ClassPathContext context)
    throws NullPointerException
  {
    Arg.checkNotNull("context", context);
    this.context = context;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the context to load the directory listings.
   *
   * @return the context to load the directory listings.
   */
  public final ClassPathContext getClassPathContext()
  {
    return context;
  }

  // --- business -------------------------------------------------------------

  /**
   * Lists the contents of the resource path.
   *
   * @param resourcePath the path to the resource whose contents is to be
   *          listed. The empty string returns the contents of the class
   *          loader's root directory (which is usually the parent class
   *          loader's root).
   * @return the contents of the resource as names. The list may be empty, but
   *         is never <code>null</code>.
   * @throws NullPointerException if {@code resourcePath} is <code>null</code>.
   * @throws IllegalArgumentException if resource path cannot be resolved to
   *           determine the contents. Either the protocol is unknown or there
   *           is a problem to access the resource's content physically.
   * @impl Sub classes may override this method to add additional protocol
   *       handlers. Call this method, if the derived handler does not handle
   *       the protocol.
   */
  public List<String> list(final String resourcePath)
    throws NullPointerException, IllegalArgumentException
  {
    Arg.checkNotNull("resourcePath", resourcePath);

    final URL resourcePathUrl = context.getResource(resourcePath);
    if (resourcePathUrl == null)
    {
      throw new IllegalArgumentException("Cannot find resource '"
                                         + resourcePath + "' on class path.");
    }

    final String protocol = resourcePathUrl.getProtocol();
    if ("file".equals(protocol))
    {
      return handleFile(resourcePath, resourcePathUrl);
    }
    else if ("jar".equals(protocol))
    {
      return handleJar(resourcePath, resourcePathUrl);
    }

    throw new IllegalArgumentException(
        "Protocol '" + protocol + "' is not supported to resolve resource '"
            + resourcePath + "'.");
  }

  private List<String> handleFile(final String resourcePath,
      final URL resourcePathUrl) throws IllegalArgumentException
  {
    try
    {
      final String[] contentsArray = new File(resourcePathUrl.toURI()).list();
      return Arrays.asList(contentsArray);
    }
    catch (final URISyntaxException e)
    {
      throw new IllegalArgumentException(
          "Cannot read URL derived from resource '" + resourcePath
              + "' on the class path.", e);
    }
  }

  private List<String> handleJar(final String resourcePath,
      final URL resourcePathUrl) throws IllegalArgumentException
  {
    try
    {
      final List<String> contents = new ArrayList<String>();

      final int separatorIndex = resourcePathUrl.getPath().indexOf('!');
      final String jarPath =
          resourcePathUrl.getPath().substring(5, separatorIndex);
      final JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
      traverseJarEntries(resourcePath, contents, jar);

      return contents;
    }
    catch (final IOException e)
    {
      throw new IllegalArgumentException("Read from JAR '" + resourcePath
                                         + "'.", e);
    }
  }

  private void traverseJarEntries(final String resourcePath,
      final List<String> contents, final JarFile jar)
  {
    final int resourcePathLength = resourcePath.length();

    final Enumeration<JarEntry> entries = jar.entries();
    while (entries.hasMoreElements())
    {
      final String name = entries.nextElement().getName();
      if (name.startsWith(resourcePath))
      {
        final String entry = name.substring(resourcePathLength);
        final String normalized = normalize(entry);

        if (normalized != null && !contents.contains(normalized))
        {
          contents.add(normalized);
        }
      }
    }
  }

  private static String normalize(final String entry)
  {
    if (StringUtils.isBlank(entry))
    {
      return null;
    }

    String normalized = entry;
    if (normalized.charAt(0) == '/')
    {
      if (entry.length() == 1)
      {
        return null;
      }
      normalized = normalized.substring(1);
    }

    final int subDirIndex = normalized.indexOf('/');
    if (subDirIndex != -1)
    {
      normalized = normalized.substring(0, subDirIndex);
    }

    return normalized;
  }

  // --- object basics --------------------------------------------------------

}
