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
package test.de.smartics.maven.plugin.jboss.modules.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToDependencies;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.DependenciesDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleMatcher;

/**
 * Tests {@link de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser}
 * with <code>{@value #ID}</code>.
 */
public class ModulesXmlParserDependenciesFullXmlTest extends
    AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  private static final String ID = "dependencies-full.xml";

  // --- members --------------------------------------------------------------

  // ****************************** Constructors ******************************

  public ModulesXmlParserDependenciesFullXmlTest()
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
    assertThat(descriptor.getName(), is(equalTo("$g1.services")));
    assertThat(descriptor.getSlot(), is(equalTo("services")));

    final ArtifactMatcher matcher = descriptor.getMatcher();
    final List<Clusion> includes = matcher.getIncludes();
    assertThat(includes.size(), is(equalTo(1)));
    final Clusion include1 = includes.get(0);
    assertThat(include1.getGroupId(), is(equalTo("(de.smartics.test)")));
    assertThat(include1.getArtifactId(), is(equalTo("service-.*")));

    assertApplyToDependencies(descriptor);
  }

  private void assertApplyToDependencies(final ModuleDescriptor descriptor)
  {
    final ApplyToDependencies applyToDependencies =
        descriptor.getApplyToDependencies();
    final List<DependenciesDescriptor> dependenciesDescriptors =
        applyToDependencies.getDescriptors();
    assertThat(dependenciesDescriptors.size(), is(equalTo(1)));
    final DependenciesDescriptor dependencies = dependenciesDescriptors.get(0);
    final ModuleMatcher moduleMatcher = dependencies.getMatcher();
    final List<ModuleClusion> moduleIncludes = moduleMatcher.getIncludes();
    assertThat(moduleIncludes.size(), is(equalTo(1)));
    final ModuleClusion moduleInclude = moduleIncludes.get(0);
    assertThat(moduleInclude.getName(),
        is(equalTo("org\\.apache\\.commons\\..*")));
    assertThat(moduleMatcher.getExcludes().size(), is(equalTo(0)));

    assertThat(dependencies.getSlot(), is(equalTo("other")));
    assertThat(dependencies.getExport(), is(equalTo(Boolean.TRUE)));
    assertThat(dependencies.getServices(), is(equalTo("import")));
    assertThat(dependencies.getOptional(), is(equalTo(Boolean.TRUE)));

    assertThat(dependencies.getImportsXml(),
        is(equalTo("<imports xmlns=\"urn:jboss:module:1.1\"><include path=\"de/smartics/test/one\" />"
                   + "<include-set><path name=\"de/smartics/sandbox/one\" />"
                   + "<path name=\"de/smartics/sandbox/two\" /></include-set>"
                   + "<exclude-set><path name=\"org/jboss/example/tests1\" />"
                   + "<path name=\"org/jboss/example/tests2\" />"
                   + "</exclude-set></imports>")));
    assertThat(dependencies.getExportsXml(),
        is(equalTo("<exports xmlns=\"urn:jboss:module:1.1\"><include path=\"de/smartics/test/one\" />"
                   + "<include-set><path name=\"de/smartics/api\" />"
                   + "<path name=\"de/smartics/spi\" /></include-set>"
                   + "<exclude path=\"de/smartics/impl\" /><exclude-set>"
                   + "<path name=\"org/jboss/example/tests\" />"
                   + "<path name=\"org/jboss/example/impl\" />"
                   + "</exclude-set></exports>")));
  }
}
