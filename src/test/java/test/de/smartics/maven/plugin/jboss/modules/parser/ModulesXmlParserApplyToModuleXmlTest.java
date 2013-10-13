package test.de.smartics.maven.plugin.jboss.modules.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToModule;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Tests {@link de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser}
 * with <code>{@value #ID}</code>.
 */
public class ModulesXmlParserApplyToModuleXmlTest extends
    AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  private static final String ID = "apply-to-module.xml";

  // --- members --------------------------------------------------------------

  // ****************************** Constructors ******************************

  public ModulesXmlParserApplyToModuleXmlTest()
  {
    super(ID);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  // --- helper ---------------------------------------------------------------

  // --- tests ----------------------------------------------------------------

  @Test
  public void parsesXml() throws Exception
  {
    assertThat(result.getModulesId(), is(equalTo(ID)));

    final List<ModuleDescriptor> descriptors = result.getDescriptors();
    assertThat(descriptors.size(), is(equalTo(1)));
    final ModuleDescriptor descriptor = descriptors.get(0);
    assertThat(descriptor.getName(), is(equalTo("apply-to-modules")));
    assertThat(descriptor.getSlot(), is(nullValue()));

    final ArtifactMatcher matcher = descriptor.getMatcher();
    final List<Clusion> includes = matcher.getIncludes();
    assertThat(includes.size(), is(equalTo(1)));
    final Clusion include1 = includes.get(0);
    assertThat(include1.getGroupId(), is(equalTo("de.smartics.test")));
    assertThat(include1.getArtifactId(), is(equalTo("service-test")));

    assertApplyToModule(descriptor);
  }

  private void assertApplyToModule(final ModuleDescriptor descriptor)
  {
    final ApplyToModule apply = descriptor.getApplyToModule();
    final String exportsXml = apply.getExportsXml();
    assertThat(exportsXml,
        is(equalTo("<exports xmlns=\"urn:jboss:module:1.1\"><exclude path=\"**/impl/*\" /></exports>")));
    final String mainClassXml = apply.getMainClassXml();
    assertThat(mainClassXml,
        is(equalTo("<main-class xmlns=\"urn:jboss:module:1.1\" name=\"de.smartics.test.Main\" />")));
  }
}
