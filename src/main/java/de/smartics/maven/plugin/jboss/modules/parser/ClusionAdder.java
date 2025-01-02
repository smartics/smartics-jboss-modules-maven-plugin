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
package de.smartics.maven.plugin.jboss.modules.parser;

import org.jdom2.Element;

/**
 * Adds clusions.
 *
 * @param <T> the type of clusion to be added.
 */
interface ClusionAdder<T>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the identifier of the element that encloses the collection of
   * clusions.
   *
   * @return the identifier of the element that encloses the collection of
   *         clusions.
   */
  String getCollectionElementId();

  /**
   * Returns the identifier of the element that encloses the clusion.
   *
   * @return the identifier of the element that encloses the clusion.
   */
  String getElementId();

  // --- business -------------------------------------------------------------

  /**
   * Adds the clusion.
   *
   * @param clusion the clusion to be added.
   */
  void add(T clusion);

  /**
   * Adds all clusions of the given {@link #getCollectionElementId()
   * collectionElementId} with in the {@code rootElement}.
   *
   * @param rootElement the element within the {@link #getCollectionElementId()
   *          collectionElement}s are found.
   */
  void addClusions(Element rootElement);

  // --- object basics --------------------------------------------------------

}
