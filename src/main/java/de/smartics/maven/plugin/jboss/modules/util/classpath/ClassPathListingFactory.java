/*
 * Copyright 2013-2015 smartics, Kronseder & Reiner GmbH
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

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * A factory to create instances of {@link ClassPathListing}.
 */
public final class ClassPathListingFactory
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * Reference to the logger for this class.
   */
  private static final Logger LOG = Logger
      .getLogger(ClassPathListingFactory.class.getName());

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Creates an instance of {@link ClassPathListing} using the configuration of
   * the service in <code>META-INF/services</code>.
   *
   * @return instance of {@link ClassPathListing}.
   */
  public ClassPathListing create()
  {
    final Iterator<ClassPathListing> iterator =
        ServiceLoader.load(ClassPathListing.class).iterator();

    final StringBuilder buffer = new StringBuilder(64);
    ClassPathListing instance = null;
    while (iterator.hasNext())
    {
      try
      {
        if (instance != null)
        {
          final String implementation = iterator.next().getClass().getName();
          buffer.append("\nDuplicated implementation rejected: "
                        + implementation);
          continue;
        }

        instance = iterator.next();
      }
      catch (final ServiceConfigurationError e)
      {
        buffer.append("\nError encountered: ").append(e.getMessage());
      }
    }

    if (buffer.length() > 0)
    {
      LOG.warning("Problems encountered while fetching implementations of '"
                  + ClassPathListing.class.getName() + "':" + buffer);
    }

    if (instance == null)
    {
      throw new IllegalStateException(
          "Cannot create instance of implementation of '"
              + ClassPathListing.class.getName() + "'.");
    }

    return instance;
  }

  // --- object basics --------------------------------------------------------

}
