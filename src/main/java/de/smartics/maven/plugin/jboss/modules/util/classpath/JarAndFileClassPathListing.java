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

import java.util.Collections;
import java.util.List;

/**
 * Supports listing of class path elements for protocols <code>jar</code> and
 * <code>file</code>.
 */
public final class JarAndFileClassPathListing implements ClassPathListing
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public List<String> list(final ClassPathContext context,
      final String resourcePath) throws NullPointerException,
    IllegalArgumentException
  {
    try
    {
      final ClassPathDirectoryListing delegate =
          new ClassPathDirectoryListing(context);
      return delegate.list(resourcePath);
    }
    catch (final IllegalArgumentException e)
    {
      return Collections.emptyList();
    }
  }

  // --- object basics --------------------------------------------------------

}
