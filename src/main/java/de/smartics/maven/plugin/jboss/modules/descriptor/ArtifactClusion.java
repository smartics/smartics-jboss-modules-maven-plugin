/*
 * Copyright 2013-2017 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.aether.artifact.Artifact;

import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;
import de.smartics.maven.plugin.jboss.modules.domain.matching.DoubleMatchContext;
import de.smartics.maven.plugin.jboss.modules.domain.matching.SingleMatchContext;

/**
 * Models an inclusion or exclusion. An include/exclude matches if all given
 * information matches (that is: <code>and</code>ed).
 */
public class ArtifactClusion
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The groupId to match. May be a regular expression.
   */
  private String groupId;

  /**
   * The groupId pattern to match. May be <code>null</code>, if groupId does not
   * specify a pattern.
   */
  private Pattern groupIdPattern;

  /**
   * The artifactId to match. May be a regular expression.
   */
  private String artifactId;

  /**
   * The artifactId pattern to match. May be <code>null</code>, if artifactId
   * does not specify a pattern.
   */
  private Pattern artifactIdPattern;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public ArtifactClusion()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- factory --------------------------------------------------------------

  /**
   * Helper to create an instance.
   *
   * @param groupId the groupId to match. May be a regular expression.
   * @param artifactId the artifactId to match. May be a regular expression.
   * @return the new instance.
   */
  public static ArtifactClusion create(final String groupId, final String artifactId)
  {
    final ArtifactClusion clusion = new ArtifactClusion();
    clusion.setGroupId(groupId);
    clusion.setArtifactId(artifactId);
    return clusion;
  }

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the groupId to match. May be a regular expression.
   *
   * @return the groupId to match.
   */
  public String getGroupId()
  {
    return groupId;
  }

  /**
   * Sets the groupId to match.May be a regular expression.
   *
   * @param groupId the groupId to match.
   */
  public void setGroupId(final String groupId)
  {
    if (StringUtils.isNotBlank(groupId))
    {
      this.groupId = groupId;
      groupIdPattern = compilePattern(groupId);
    }
  }

  private static Pattern compilePattern(final String pattern)
  {
    if (StringUtils.isNotBlank(pattern))
    {
      try
      {
        return Pattern.compile(pattern);
      }
      catch (final PatternSyntaxException e)
      {
        // ignore
      }
    }
    return null;
  }

  /**
   * Returns the artifactId to match. May be a regular expression.
   *
   * @return the artifactId to match.
   */
  public String getArtifactId()
  {
    return artifactId;
  }

  /**
   * Sets the artifactId to match. May be a regular expression.
   *
   * @param artifactId the artifactId to match.
   */
  public void setArtifactId(final String artifactId)
  {
    if (StringUtils.isNotBlank(artifactId))
    {
      this.artifactId = artifactId;
      artifactIdPattern = compilePattern(artifactId);
    }
  }

  // --- business -------------------------------------------------------------

  /**
   * Checks if the clusion matches the artifact.
   *
   * @param artifact the artifact to match.
   * @return a context to access the match result, with <code>true</code> if the
   *         artifact matches groupId and artifactId, <code>false</code>
   *         otherwise.
   */
  public MatchContext matches(final Artifact artifact)
  {
    final SingleMatchContext matchesGroupId =
        matches(groupIdPattern, groupId, artifact.getGroupId());
    if (matchesGroupId != null && !matchesGroupId.isMatched())
    {
      return new SingleMatchContext(false);
    }
    final SingleMatchContext matchesArtifactId =
        matches(artifactIdPattern, artifactId, artifact.getArtifactId());

    final boolean result =
        (matchesGroupId != null && matchesGroupId.isMatched() && (matchesArtifactId == null || matchesArtifactId
            .isMatched()))
            || (matchesArtifactId != null && matchesArtifactId.isMatched());

    final MatchContext context =
        new DoubleMatchContext(result, matchesGroupId, matchesArtifactId);
    return context;
  }

  private static SingleMatchContext matches(final Pattern pattern,
      final String id, final String inputId)
  {
    if (pattern != null)
    {
      final Matcher matcher = pattern.matcher(inputId);
      return new SingleMatchContext(matcher);
    }

    if (StringUtils.isNotBlank(id))
    {
      return new SingleMatchContext(id.equals(inputId));
    }

    return null;
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    return ObjectUtils.toString(groupId) + ':'
           + ObjectUtils.toString(artifactId);
  }
}
