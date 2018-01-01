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

import static de.smartics.maven.plugin.jboss.modules.parser.ModulesDescriptorBuilder.NS;

import java.util.List;

import org.jdom2.Element;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleClusion;

/**
 * Base implementation of an {@link ClusionAdder} to add instances of
 * {@link ModuleClusion}.
 */
abstract class AbstractModuleClusionAdder extends
    AbstractClusionAdder<ModuleClusion>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  AbstractModuleClusionAdder(final String collectionElementId,
      final String elementId)
  {
    super(collectionElementId, elementId);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  public void addClusions(final Element matchElement)
  {
    final Element clusionsElement =
        matchElement.getChild(collectionElementId, NS);
    if (clusionsElement != null)
    {
      final List<Element> clusionElements =
          clusionsElement.getChildren(elementId, NS);
      for (final Element clusionElement : clusionElements)
      {
        final String name = clusionElement.getTextNormalize();
        final ModuleClusion clusion = new ModuleClusion(name);
        add(clusion);
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
