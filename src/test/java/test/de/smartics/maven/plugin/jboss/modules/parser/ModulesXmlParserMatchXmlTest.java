package test.de.smartics.maven.plugin.jboss.modules.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.Directives;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Tests {@link de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser}
 * with <code>{@value #ID}</code>.
 */
public class ModulesXmlParserMatchXmlTest extends AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  private static final String ID = "match.xml";

  // --- members --------------------------------------------------------------

  // ****************************** Constructors ******************************

  public ModulesXmlParserMatchXmlTest()
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
    assertThat(descriptor.getName(), is(equalTo("match-module")));
    assertThat(descriptor.getSlot(), is(equalTo("match")));

    final Directives directives = descriptor.getDirectives();
    assertThat(directives.getSkip(), is(equalTo(Boolean.FALSE)));
    assertThat(directives.getInheritSlot(), is(equalTo(Boolean.TRUE)));

    final ArtifactMatcher matcher = descriptor.getMatcher();
    final List<Clusion> includes = matcher.getIncludes();
    assertThat(includes.size(), is(equalTo(2)));
    final Clusion include1 = includes.get(0);
    assertThat(include1.getGroupId(), is(equalTo("de.smartics.test")));
    assertThat(include1.getArtifactId(), is(equalTo("test-lib")));
    final Clusion include2 = includes.get(1);
    assertThat(include2.getGroupId(), is(equalTo("de.smartics.sandbox")));
    assertThat(include2.getArtifactId(), is(nullValue()));

    final List<Clusion> excludes = matcher.getExcludes();
    assertThat(excludes.size(), is(equalTo(1)));
    final Clusion exclude1 = excludes.get(0);
    assertThat(exclude1.getGroupId(), is(nullValue()));
    assertThat(exclude1.getArtifactId(), is(equalTo("sandbox-lib")));
  }
}
