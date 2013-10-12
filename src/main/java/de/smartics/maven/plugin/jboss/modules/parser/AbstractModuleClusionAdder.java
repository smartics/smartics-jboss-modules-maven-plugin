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
