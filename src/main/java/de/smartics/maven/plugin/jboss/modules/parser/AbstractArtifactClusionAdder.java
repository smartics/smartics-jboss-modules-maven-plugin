package de.smartics.maven.plugin.jboss.modules.parser;

import static de.smartics.maven.plugin.jboss.modules.parser.ModulesDescriptorBuilder.NS;

import java.util.List;

import org.jdom2.Element;

import de.smartics.maven.plugin.jboss.modules.Clusion;

/**
 * Base implementation of an {@link ClusionAdder} to add instances of
 * {@link Clusion}.
 */
abstract class AbstractArtifactClusionAdder extends
    AbstractClusionAdder<Clusion>
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
        final Clusion clusion = new Clusion();
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
