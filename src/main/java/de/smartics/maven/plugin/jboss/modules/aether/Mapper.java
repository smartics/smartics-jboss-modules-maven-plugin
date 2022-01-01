/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.aether;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Maps instances from the Maven to the Aether world.
 */
public final class Mapper
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public Mapper()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Maps a Maven dependency to an Aether dependency.
   *
   * @param mavenDependency the Maven dependency to map.
   * @return the mapped Aether dependency.
   */
  public Dependency map(final org.apache.maven.model.Dependency mavenDependency)
  {
    final Artifact artifact = extractArtifact(mavenDependency);
    final String scope = mavenDependency.getScope();
    final boolean optional = mavenDependency.isOptional();
    final List<Exclusion> exclusions = map(mavenDependency.getExclusions());

    final Dependency aetherDependency =
        new Dependency(artifact, scope, optional, exclusions);
    return aetherDependency;
  }

  private List<Exclusion> map(
      final List<org.apache.maven.model.Exclusion> mavenExclusions)
  {
    final List<Exclusion> aetherExclusions =
        new ArrayList<Exclusion>(mavenExclusions.size());

    for (final org.apache.maven.model.Exclusion mavenExclusion : mavenExclusions)
    {
      final String groupId = mavenExclusion.getGroupId();
      final String artifactId = mavenExclusion.getArtifactId();
      final Exclusion aetherExclusion =
          new Exclusion(groupId, artifactId, null, null);
      aetherExclusions.add(aetherExclusion);
    }

    return aetherExclusions;
  }

  private Artifact extractArtifact(
      final org.apache.maven.model.Dependency mavenDependency)
  {
    final String groupId = mavenDependency.getGroupId();
    final String artifactId = mavenDependency.getArtifactId();
    final String classifier = mavenDependency.getClassifier();
    final String extension = mavenDependency.getType();
    final String version = mavenDependency.getVersion();

    final Artifact aetherArtifact =
        new DefaultArtifact(groupId, artifactId, classifier, extension, version);
    return aetherArtifact;
  }

  /**
   * Maps a Maven artifact to an Aether artifact.
   *
   * @param mavenArtifact the Maven artifact to map.
   * @return the mapped Aether artifact.
   */
  public Artifact map(final org.apache.maven.artifact.Artifact mavenArtifact)
  {
    final Artifact aetherArtifact =
        new DefaultArtifact(mavenArtifact.getGroupId(),
            mavenArtifact.getArtifactId(), mavenArtifact.getClassifier(),
            mavenArtifact.getType(), mavenArtifact.getVersion());
    return aetherArtifact.setFile(mavenArtifact.getFile());
  }

  // --- object basics --------------------------------------------------------

}
