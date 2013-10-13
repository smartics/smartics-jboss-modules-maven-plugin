/*
 * Copyright 2013 smartics, Kronseder & Reiner GmbH
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

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;

/**
 * Base implementation of an {@link ClusionAdder} to add instances of
 * {@link ArtifactClusion}.
 */
abstract class AbstractArtifactClusionAdder extends
    AbstractClusionAdder<ArtifactClusion>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  AbstractArtifactClusionAdder(final String collectionElementId,
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
        final ArtifactClusion clusion = new ArtifactClusion();
        final String groupId = clusionElement.getChildText("groupId", NS);
        final String artifactId = clusionElement.getChildText("artifactId", NS);
        clusion.setGroupId(groupId);
        clusion.setArtifactId(artifactId);
        add(clusion);
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
