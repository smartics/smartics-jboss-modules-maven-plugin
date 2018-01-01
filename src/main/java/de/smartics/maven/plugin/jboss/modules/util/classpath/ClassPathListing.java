/*
 * Copyright 2013-2018 smartics, Kronseder & Reiner GmbH
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

import java.util.List;

import de.smartics.maven.plugin.jboss.modules.util.marker.ServiceInterface;

/**
 * Service to list the contents of a folder on the class path.
 */
@ServiceInterface
public interface ClassPathListing
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Lists the contents of the resource path.
   *
   * @param context the context to load the resource from the class path.
   * @param resourcePath the path to the resource whose contents is to be
   *          listed. The empty string returns the contents of the class
   *          loader's root directory (which is usually the parent class
   *          loader's root).
   * @return the contents of the resource as names. The list may be empty, but
   *         is never <code>null</code>.
   * @throws NullPointerException if {@code context} or {@code resourcePath} is
   *           <code>null</code>.
   * @throws IllegalArgumentException if resource path cannot be resolved to
   *           determine the contents. Either the protocol is unknown or there
   *           is a problem to access the resource's content physically.
   */
  List<String> list(ClassPathContext context, String resourcePath)
    throws NullPointerException, IllegalArgumentException;

  // --- object basics --------------------------------------------------------

}
