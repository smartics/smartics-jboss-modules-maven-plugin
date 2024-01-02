/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;

/**
 * Builds test instances of {@link ArtifactClusion}.
 */
public final class ClusionBuilder
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
   * An alternate artifactId to use.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String ALT_ARTIFACT_ID = "alternateArtifact";

  // --- members --------------------------------------------------------------

  /**
   * The groupId to match. May contain wildcards (<code>*</code>).
   */
  private String groupId;

  /**
   * The artifactId to match. May contain wildcards (<code>*</code>).
   */
  private String artifactId;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ClusionBuilder()
  {
  }

  private ClusionBuilder(final ClusionBuilder builder)
  {
    this.groupId = builder.groupId;
    this.artifactId = builder.artifactId;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Sets the groupId to match. May contain wildcards (<code>*</code>).
   *
   * @param groupId the groupId to match. May contain wildcards (<code>*</code>
   *          ).
   * @return a reference to this builder.
   */
  public ClusionBuilder withGroupId(final String groupId)
  {
    this.groupId = groupId;
    return this;
  }

  /**
   * Sets the artifactId to match. May contain wildcards (<code>*</code>).
   *
   * @param artifactId the artifactId to match. May contain wildcards (
   *          <code>*</code>).
   * @return a reference to this builder.
   */
  public ClusionBuilder withArtifactId(final String artifactId)
  {
    this.artifactId = artifactId;
    return this;
  }

  // --- business -------------------------------------------------------------

  /**
   * Builds an empty instance of {@link ClusionBuilder}.
   *
   * @return the created instance.
   */
  public static ClusionBuilder a()
  {
    final ClusionBuilder builder = new ClusionBuilder();
    return builder;
  }

  /**
   * Builds default instance of {@link ClusionBuilder}.
   *
   * @return the created instance.
   */
  public static ClusionBuilder aDefault()
  {
    final ClusionBuilder builder = a();

    builder.withGroupId(DEFAULT_GROUP_ID);
    builder.withArtifactId(DEFAULT_ARTIFACT_ID);

    return builder;
  }

  /**
   * Builds a copy of this instance.
   *
   * @return a copy of this instance.
   */
  public ClusionBuilder but()
  {
    return new ClusionBuilder(this);
  }

  /**
   * Builds an instance of {@link ArtifactClusion}.
   *
   * @return the created instance.
   */
  public ArtifactClusion build()
  {
    return new ArtifactClusion(groupId, artifactId);
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    final StringBuilder buffer = new StringBuilder(64);
    buffer.append("groupId=").append(groupId).append(" / artifactId=")
        .append(artifactId);
    return buffer.toString();
  }
}
