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
package de.smartics.maven.plugin.jboss.modules.parser;

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import org.jdom2.Element;

import java.util.List;

import static de.smartics.maven.plugin.jboss.modules.parser.ModulesDescriptorBuilderV2.NS;

/**
 * Base implementation of an {@link de.smartics.maven.plugin.jboss.modules.parser.ClusionAdder} to add instances of
 * {@link de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion}.
 */
abstract class AbstractArtifactClusionAdderV2 extends
        AbstractArtifactClusionAdder
{

  AbstractArtifactClusionAdderV2(final String collectionElementId,
                                 final String elementId)
  {
    super(collectionElementId, elementId);
  }

  public void addClusions(final Element matchElement)
  {
    if (matchElement != null)
    {
      final List<Element> clusionElements =
          matchElement.getChildren(elementId, NS);
      for (final Element clusionElement : clusionElements)
      {
        final ArtifactClusion clusion = new ArtifactClusion();
        String artifact = clusionElement.getAttributeValue("artifact");
        String[] split = artifact.split(":", 2);
        final String groupId = split.length >= 1 ? split[0] : null;
        final String artifactId = split.length == 2 ? split[1] : null;
        clusion.setGroupId(groupId);
        clusion.setArtifactId(artifactId);
        add(clusion);
      }
    }
  }

}
