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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
  /**
   * A mutable {@link ArtifactClusion} so that Plexus can instantiate it and set the fields.
   */
  public static class Builder
  {
    public String groupId;

    public String artifactId;

    public String filter;

    public ArtifactClusion build()
    {
      return new ArtifactClusion(groupId, artifactId, filter);
    }

  }

  /**
   * Transforms the given {@link List} of mutable {@code builders} to a {@link List} of immutable {@link ArtifactClusion}s.
   *
   * @param builders a list of mutable {@link Builder}s
   * @return a new {@link List} of immutable {@link ArtifactClusion}s
   */
  public static List<ArtifactClusion> buildList(List<Builder> builders) {
    if (builders == null) {
      return Collections.emptyList();
    } else {
      List<ArtifactClusion> result = new ArrayList<ArtifactClusion>(builders.size());
      for (Builder b : builders)
      {
        result.add(b.build());
      }
      return Collections.unmodifiableList(result);
    }
  }

    // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The groupId to match. May be a regular expression.
   */
  private final String groupId;

  /**
   * The groupId pattern to match. May be <code>null</code>, if groupId does not
   * specify a pattern.
   */
  private final Pattern groupIdPattern;

  /**
   * The artifactId to match. May be a regular expression.
   */
  private final String artifactId;

  /**
   * The artifactId pattern to match. May be <code>null</code>, if artifactId
   * does not specify a pattern.
   */
  private final Pattern artifactIdPattern;

  /**
   * An XML fragment that represents the {@code filter} sub-element
   */
  private final String filter;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * A shorthand for {@code new ArtifactClusion(groupId, artifactId, null)}
   *
   * @param groupId the groupId to match. May be a regular expression.
   * @param artifactId the artifactId to match. May be a regular expression.
   */
  public ArtifactClusion(String groupId, String artifactId)
  {
    this(groupId, artifactId, null);
  }

  /**
   * @param groupId the groupId to match. May be a regular expression.
   * @param artifactId the artifactId to match. May be a regular expression.
   * @param filter a filter sub-element or {@code null} in case there is no filter set
   */
  public ArtifactClusion(String groupId, String artifactId, String filter)
  {
    super();
    if (StringUtils.isNotBlank(groupId)) {
      this.groupId = groupId;
      this.groupIdPattern = compilePattern(groupId);
    } else {
      this.groupId = null;
      this.groupIdPattern = null;
    }
    if (StringUtils.isNotBlank(artifactId)) {
      this.artifactId = artifactId;
      this.artifactIdPattern = compilePattern(artifactId);
    } else {
      this.artifactId = null;
      this.artifactIdPattern = null;
    }
    this.filter = filter;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- factory --------------------------------------------------------------

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
   * Returns an XML fragment that represents the {@code filter} sub-element.
   *
   * @return an XML fragment or {@code null}.
   */
  public String getFilter()
  {
    return filter;
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
      return new SingleMatchContext(false, this);
    }
    final SingleMatchContext matchesArtifactId =
        matches(artifactIdPattern, artifactId, artifact.getArtifactId());

    final boolean result =
        (matchesGroupId != null && matchesGroupId.isMatched() && (matchesArtifactId == null || matchesArtifactId
            .isMatched()))
            || (matchesArtifactId != null && matchesArtifactId.isMatched());

    final MatchContext context =
        new DoubleMatchContext(result, matchesGroupId, matchesArtifactId, this);
    return context;
  }

  private SingleMatchContext matches(final Pattern pattern,
      final String id, final String inputId)
  {
    if (pattern != null)
    {
      final Matcher matcher = pattern.matcher(inputId);
      return new SingleMatchContext(matcher, this);
    }

    if (StringUtils.isNotBlank(id))
    {
      return new SingleMatchContext(id.equals(inputId), this);
    }

    return null;
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    return ObjectUtils.toString(groupId) + ':'
           + ObjectUtils.toString(artifactId) + ':'
           + ObjectUtils.toString(filter);
  }
}
