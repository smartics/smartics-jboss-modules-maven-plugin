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
package help.de.smartics.maven.plugin.jboss.modules;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.util.artifact.DefaultArtifact;

/**
 * Builds test instances of {@link Artifact}.
 */
public final class ArtifactBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The default groupId found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_GROUP_ID = "de.smartics.test";

  /**
   * The default artifactId found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_ARTIFACT_ID = "testArtifact";

  /**
   * The default version found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_VERSION = "1.0";

  /**
   * The default type found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_TYPE = "jar";

  /**
   * The default classifier found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_CLASSIFIER = "sources";

  // --- members --------------------------------------------------------------

  /**
   * The group identifier of the artifact.
   */
  private String groupId;

  /**
   * The identifier of the artifact.
   */
  private String artifactId;

  /**
   * The version of the artifact.
   */
  private String version;

  /**
   * The type of the artifact.
   */
  private String type;

  /**
   * The classifier of the artifact.
   */
  private String classifier;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ArtifactBuilder()
  {
  }

  private ArtifactBuilder(final ArtifactBuilder builder)
  {
    this.groupId = builder.groupId;
    this.artifactId = builder.artifactId;
    this.version = builder.version;
    this.type = builder.type;
    this.classifier = builder.classifier;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Sets the group identifier of the artifact.
   *
   * @param groupId the group identifier of the artifact.
   * @return a reference to this builder.
   */
  public ArtifactBuilder withGroupId(final String groupId)
  {
    this.groupId = groupId;
    return this;
  }

  /**
   * Sets the identifier of the artifact.
   *
   * @param artifactId the identifier of the artifact.
   * @return a reference to this builder.
   */
  public ArtifactBuilder withArtifactId(final String artifactId)
  {
    this.artifactId = artifactId;
    return this;
  }

  /**
   * Sets the version of the artifact to the specified version.
   *
   * @param version the version of the artifact.
   * @return a reference to this builder.
   */
  public ArtifactBuilder withVersion(final String version)
  {
    this.version = version;
    return this;
  }

  /**
   * Sets the type of the artifact.
   *
   * @param type the type of the artifact.
   * @return a reference to this builder.
   */
  public ArtifactBuilder withType(final String type)
  {
    this.type = type;
    return this;
  }

  /**
   * Sets the classifier of the artifact.
   *
   * @param classifier the classifier of the artifact.
   * @return a reference to this builder.
   */
  public ArtifactBuilder withClassifier(final String classifier)
  {
    this.classifier = classifier;
    return this;
  }

  // --- business -------------------------------------------------------------

  /**
   * Builds an empty instance of {@link ArtifactBuilder}.
   *
   * @return the created instance.
   */
  public static ArtifactBuilder a()
  {
    final ArtifactBuilder builder = new ArtifactBuilder();
    builder.withType(DEFAULT_TYPE);
    return builder;
  }

  /**
   * Builds default instance of {@link ArtifactBuilder}.
   *
   * @return the created instance.
   */
  public static ArtifactBuilder aDefault()
  {
    final ArtifactBuilder builder = a();

    builder.withGroupId(DEFAULT_GROUP_ID);
    builder.withArtifactId(DEFAULT_ARTIFACT_ID);
    builder.withVersion(DEFAULT_VERSION);
    builder.withClassifier(DEFAULT_CLASSIFIER);

    return builder;
  }

  /**
   * Builds a copy of this instance.
   *
   * @return a copy of this instance.
   */
  public ArtifactBuilder but()
  {
    return new ArtifactBuilder(this);
  }

  /**
   * Builds an instance of {@link Artifact}.
   *
   * @return the created instance.
   */
  public Artifact build()
  {
    return new DefaultArtifact(groupId, artifactId, classifier, type, version);
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    final StringBuilder buffer = new StringBuilder(64);
    buffer.append("groupId=").append(groupId);
    buffer.append(" / artifactId=").append(artifactId);
    buffer.append(" / version=").append(version);
    buffer.append(" / type=").append(type);
    buffer.append(" / classifier=").append(classifier);
    return buffer.toString();
  }

}
