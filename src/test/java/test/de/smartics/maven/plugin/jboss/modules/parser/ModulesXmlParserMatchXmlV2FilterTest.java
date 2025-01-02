/*
 * Copyright 2013-2025 smartics, Kronseder & Reiner GmbH
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
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.Directives;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;

/**
 * Tests {@link de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser}
 * with <code>{@value #ID}</code>.
 */
public class ModulesXmlParserMatchXmlV2FilterTest extends AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  private static final String ID = "match-v2-filter.xml";

  // --- members --------------------------------------------------------------

  // ****************************** Constructors ******************************

  public ModulesXmlParserMatchXmlV2FilterTest()
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
    final List<ArtifactClusion> includes = matcher.getIncludes();
    assertThat(includes.size(), is(equalTo(2)));
    final ArtifactClusion include1 = includes.get(0);
    assertThat(include1.getGroupId(), is(equalTo("de.smartics.test")));
    assertThat(include1.getArtifactId(), is(equalTo("test-lib")));
    assertThat(include1.getFilter(), is(equalTo("<filter xmlns=\"http://smartics.de/ns/jboss-modules-descriptor/2\"><include path=\"de/smartics/folder1/**\" /><exclude path=\"de/smartics/folder2\" /><include-set><path name=\"de/smartics/folder3\" /><path name=\"de/smartics/folder4\" /></include-set><exclude-set><path name=\"de/smartics/folder5\" /><path name=\"de/smartics/folder6\" /></exclude-set></filter>")));

    final ArtifactClusion include2 = includes.get(1);
    assertThat(include2.getGroupId(), is(equalTo("de.smartics.sandbox")));
    assertThat(include2.getArtifactId(), is(nullValue()));
    assertThat(include2.getFilter(), is(nullValue()));

    final List<ArtifactClusion> excludes = matcher.getExcludes();
    assertThat(excludes.size(), is(equalTo(1)));
    final ArtifactClusion exclude1 = excludes.get(0);
    assertThat(exclude1.getGroupId(), is(nullValue()));
    assertThat(exclude1.getArtifactId(), is(equalTo("sandbox-lib")));
    assertThat(exclude1.getFilter(), is(nullValue()));
  }
}
