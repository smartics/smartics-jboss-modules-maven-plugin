package de.smartics.maven.plugin.jboss.modules.parser;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;
import de.smartics.util.lang.Arg;

/**
 * Parses a modules XML document.
 */
public final class ModulesXmlParser
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * Builder of JDOM documents.
   */
  private final SAXBuilder builder;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public ModulesXmlParser()
  {
    this.builder = new SAXBuilder();
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Parses the given document from the stream.
   *
   * @param systemId the identifier of the XML document for error handling and
   *          link resolution.
   * @param input the stream to parse the modules XML document.
   * @return the descriptors found in a given document.
   * @throws NullPointerException if {@code input} or {@code systemId} is
   *           <code>null</code>.
   * @throws IllegalArgumentException if {@code systemId} is blank.
   * @throws JDOMException when errors occur in parsing
   * @throws IOException when an I/O error prevents a document from being fully
   *           parsed
   */
  public ModulesDescriptor parse(final String systemId, final InputStream input)
    throws NullPointerException, IllegalArgumentException, JDOMException,
    IOException
  {
    final Document document =
        builder.build(Arg.checkNotNull("input", input),
            Arg.checkNotBlank("systemId", systemId));
    final ModulesDescriptorBuilder builder =
        new ModulesDescriptorBuilder(systemId, document);
    final ModulesDescriptor descriptor = builder.build();
    return descriptor;
  }

  // --- object basics --------------------------------------------------------

}
