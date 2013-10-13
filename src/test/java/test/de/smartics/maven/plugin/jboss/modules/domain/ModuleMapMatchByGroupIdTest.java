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
package test.de.smartics.maven.plugin.jboss.modules.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import help.de.smartics.maven.plugin.jboss.modules.ArtifactBuilder;
import help.de.smartics.maven.plugin.jboss.modules.ClusionBuilder;
import help.de.smartics.maven.plugin.jboss.modules.ModuleDescriptorBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.domain.ModuleMap;
import de.smartics.testdoc.annotations.Uut;

/**
 * Tests {@link ModuleMap} by stacking up modules by an regular expression match
 * on the artifact identifier.
 */
public class ModuleMapMatchByGroupIdTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The module name used in this test.
   */
  private static final String MODULE_NAME = "de.smartics.test.commons";

  // --- members --------------------------------------------------------------

  @Uut
  private ModuleMap uut;

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  @Before
  public void setUp()
  {
    final ModuleDescriptorBuilder builder = ModuleDescriptorBuilder.a();
    builder.withName(MODULE_NAME);
    final ClusionBuilder clusionBuilder = ClusionBuilder.a();
    clusionBuilder.withGroupId("commons-.*");
    builder.withInclude(clusionBuilder.build());

    final List<ModuleDescriptor> modules = Arrays.asList(builder.build());
    uut = new ModuleMap(modules);
  }

  // --- helper ---------------------------------------------------------------

  private static ArtifactBuilder createArtifactBuilder(final String id)
  {
    final ArtifactBuilder builder = ArtifactBuilder.a();
    builder.withGroupId(id);
    builder.withArtifactId(id);
    builder.withVersion("1.0");
    return builder;
  }

  // --- tests ----------------------------------------------------------------

  @Test
  public void allowsToStackArtifactsByArtifactIdRegExp()
  {
    final ArtifactBuilder builder = createArtifactBuilder("commons-one");
    final Artifact artifactOne = builder.build();
    final Dependency one = new Dependency(artifactOne, "compile");
    uut.add(one);

    builder.withArtifactId("commons-two");
    final Artifact artifactTwo = builder.build();
    final Dependency two = new Dependency(artifactTwo, "compile");
    uut.add(two);

    builder.withArtifactId("commons-three");
    final Artifact artifactThree = builder.build();
    final Dependency three = new Dependency(artifactThree, "compile");
    uut.add(three);

    final Map<ModuleDescriptor, List<Dependency>> map = uut.toMap();
    assertThat(map.size(), is(1));

    final ModuleDescriptor module =
        new ModuleDescriptor.Builder().withName(MODULE_NAME).build();

    final List<Dependency> artifacts = map.get(module);
    assertThat(artifacts.size(), is(3));
    assertThat(artifacts, allOf(hasItem(one), hasItem(two), hasItem(three)));
  }
}
