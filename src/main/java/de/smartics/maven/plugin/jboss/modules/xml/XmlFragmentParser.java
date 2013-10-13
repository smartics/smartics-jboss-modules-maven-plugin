package de.smartics.maven.plugin.jboss.modules.xml;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Helper to parse XML fragments to be added to the main document.
 */
final class XmlFragmentParser
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The document builder.
   */
  private final SAXBuilder builder;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public XmlFragmentParser()
  {
    builder = new SAXBuilder();
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Parses the given XML fragment.
   *
   * @param xmlFragment the fragment to be parsed in UTF-8 encoding.
   * @return the parsed root element.
   * @throws IllegalArgumentException on any parsing problem.
   */
  public Element parse(final String xmlFragment)
    throws IllegalArgumentException
  {
    try
    {
      final InputStream input = IOUtils.toInputStream(xmlFragment, "UTF-8");
      final Document document = builder.build(input);
      final Element root = document.getRootElement();
      return root;
    }
    catch (final IOException e)
    {
      throw new IllegalStateException(
          "UTF-8 encoding not supported on this platform.");
    }
    catch (final JDOMException e)
    {
      throw new IllegalArgumentException("Cannot parse XML fragment: "
                                         + xmlFragment, e);
    }
  }

  // --- object basics --------------------------------------------------------

}
