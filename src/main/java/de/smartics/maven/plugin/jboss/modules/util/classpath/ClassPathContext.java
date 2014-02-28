/*
 * Copyright 2011-2013 smartics, Kronseder & Reiner GmbH
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.lang.ObjectUtils;

import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * A constraint on resources to be loaded from the class path.
 */
public final class ClassPathContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The class loader to load the directory listings.
   */
  private final ClassLoader classLoader;

  /**
   * Selects the archive root to load from.
   */
  private final String archiveRoot;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param classLoader the class loader to load the directory listings.
   * @param archiveRoot the value for archiveRoot.
   * @throws NullPointerException if {@code classLoader} is <code>null</code>.
   */
  public ClassPathContext(final ClassLoader classLoader,
      final String archiveRoot) throws NullPointerException
  {
    Arg.checkNotNull("classLoader", classLoader);

    this.classLoader = classLoader;
    this.archiveRoot = archiveRoot;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Constructs the URL to the resource.
   *
   * @param resource the resource whose URL is requested.
   * @return the URL to the resource or <code>null</code> if the resource cannot
   *         be found on the class path.
   */
  public URL getResource(final String resource)
  {
    if (archiveRoot == null)
    {
      return classLoader.getResource(resource);
    }

    // TODO: maybe we should simply construct the URL?
    try
    {
      for (final Enumeration<URL> en = classLoader.getResources(resource); en
          .hasMoreElements();)
      {
        final URL current = en.nextElement();
        final String urlString = current.toExternalForm();
        if (urlString.startsWith(archiveRoot))
        {
          return current;
        }
      }
    }
    catch (final IOException e)
    {
      // return null
    }
    return null;
  }

  /**
   * Opens the stream to the resource.
   *
   * @param resource the resource whose stream is requested.
   * @return the stream to the resource or <code>null</code> if the resource
   *         cannot be found on the class path. If a stream is returned, the
   *         client is responsible to close that stream.
   */
  public InputStream getResourceAsStream(final String resource)
  {
    final URL url = getResource(resource);
    if (url != null)
    {
      try
      {
        return url.openStream();
      }
      catch (final IOException e)
      {
        // return null;
      }
    }
    return null;
  }

  /**
   * Constructs the URL to the resource.
   *
   * @param resource the resource whose URL is requested.
   * @return the constructed URL.
   * @throws IllegalArgumentException if the URL to the resource cannot be
   *           constructed.
   */
  public URL createUrl(final String resource) throws IllegalArgumentException
  {
    try
    {
      final String urlString =
          (archiveRoot != null ? archiveRoot : "") + resource;
      return new URL(urlString);
    }
    catch (final MalformedURLException e)
    {
      throw new IllegalArgumentException("Cannot construct URL with resource '"
                                         + resource + "'.", e);
    }
  }

  // --- object basics --------------------------------------------------------

  /**
   * Returns the hash code of the object.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode()
  {
    return ObjectUtils.hashCode(archiveRoot);
  }

  /**
   * Returns <code>true</code> if the given object is semantically equal to the
   * given object, <code>false</code> otherwise.
   *
   * @param object the instance to compare to.
   * @return <code>true</code> if the given object is semantically equal to the
   *         given object, <code>false</code> otherwise.
   */
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

    final ClassPathContext other = (ClassPathContext) object;

    return ObjectUtils.equals(archiveRoot, other.archiveRoot);
  }

  @Override
  public String toString()
  {
    return archiveRoot != null ? archiveRoot : "<unspecified archive root>";
  }
}
