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
package de.smartics.maven.plugin.jboss.modules.parser;

/**
 * Base implementation of an {@link ClusionAdder}.
 *
 * @param <T> the type of clusion to be added.
 */
abstract class AbstractClusionAdder<T> implements ClusionAdder<T>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The identifier of the element that encloses the collection of clusions.
   */
  protected final String collectionElementId;

  /**
   * The identifier of the element that encloses the clusion.
   */
  protected final String elementId;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  AbstractClusionAdder(final String collectionElementId, final String elementId)
  {
    assert collectionElementId != null : "The collectionElementId must not be 'null'.";
    assert elementId != null : "The elementId must not be 'null'.";

    this.collectionElementId = collectionElementId;
    this.elementId = elementId;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  @Override
  public String getCollectionElementId()
  {
    return collectionElementId;
  }

  @Override
  public String getElementId()
  {
    return elementId;
  }

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
