/*
 * Copyright 2013-2016 smartics, Kronseder & Reiner GmbH
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

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.domain.ModuleMap;
//import de.smartics.testdoc.annotations.Uut;

/**
 * Tests {@link ModuleMap} with an empty map.
 */
public class ModuleMapEmptyTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  //@Uut
  private ModuleMap uut = new ModuleMap();

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  // --- helper ---------------------------------------------------------------

  private static ArtifactBuilder createDefaultArtifactBuilder()
  {
    final ArtifactBuilder builder = ArtifactBuilder.a();
    builder.withGroupId("de.smartics.test");
    builder.withArtifactId("test-artifact");
    builder.withVersion("1.0");
    builder.withType("jar");
    builder.withClassifier("helpers");
    return builder;
  }

  // --- tests ----------------------------------------------------------------

  @Test
  public void ifEmptyReturnsTheEmptyMap()
  {
    final Map<ModuleDescriptor, List<Dependency>> map = uut.toMap();
    assertThat(map.isEmpty(), is(true));
  }

  @Test
  public void allowsToAddArtifactsThatMatchNoGroupAndAreAddedAsNewModules()
  {
    final ArtifactBuilder builder = createDefaultArtifactBuilder();
    final Artifact artifact = builder.build();
    final Dependency dependency = new Dependency(artifact, "compile");
    uut.add(dependency);

    final Map<ModuleDescriptor, List<Dependency>> map = uut.toMap();
    assertThat(map.size(), is(1));

    final ModuleDescriptor module =
        new ModuleDescriptor.Builder().withName(
            "de.smartics.test.test-artifact").build();

    final List<Dependency> dependencies = map.get(module);
    assertThat(dependencies.size(), is(1));
    assertThat(dependencies, hasItem(dependency));
  }

  @Test
  public void allowsToStackArtifactsWithACommonModule()
  {
    final ArtifactBuilder builder = createDefaultArtifactBuilder();
    final Artifact artifact = builder.build();
    final Dependency dependency = new Dependency(artifact, "compile");
    uut.add(dependency);

    builder.withClassifier("sources");
    final Artifact sourceArtifact = builder.build();
    final Dependency sourceDependency =
        new Dependency(sourceArtifact, "compile");
    uut.add(sourceDependency);

    final Map<ModuleDescriptor, List<Dependency>> map = uut.toMap();
    assertThat(map.size(), is(1));

    final ModuleDescriptor module =
        new ModuleDescriptor.Builder().withName(
            "de.smartics.test.test-artifact").build();

    final List<Dependency> dependencies = map.get(module);
    assertThat(dependencies.size(), is(2));
    assertThat(dependencies,
        allOf(hasItem(dependency), hasItem(sourceDependency)));
  }
}
