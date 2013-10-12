package test.de.smartics.maven.plugin.jboss.modules.parser;

import java.io.InputStream;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Before;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;
import de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser;
import de.smartics.util.test.io.IoTestUtils;

/**
 * Tests {@link ModulesXmlParser}.
 */
public abstract class AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  private final String resourceId;

  private ModulesXmlParser uut;

  protected ModulesDescriptor result;

  // ****************************** Inner Classes *****************************

  protected AbstractModulesXmlParserTest(final String resourceId)
  {
    this.resourceId = resourceId;
  }

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  @Before
  public void setUp() throws Exception
  {
    uut = new ModulesXmlParser();
    final InputStream input =
        IoTestUtils
            .openStreamFromResource(AbstractModulesXmlParserTest.class, resourceId);
    try
    {
      this.result = uut.parse(resourceId, input);
    }
    finally
    {
      IOUtil.close(input);
    }

  }

  // --- helper ---------------------------------------------------------------

  // --- tests ----------------------------------------------------------------

}
