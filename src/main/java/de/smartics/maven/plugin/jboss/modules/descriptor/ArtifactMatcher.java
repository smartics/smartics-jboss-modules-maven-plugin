/*
 * Copyright 2013-2014 smartics, Kronseder & Reiner GmbH
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
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.aether.artifact.Artifact;

import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;
import de.smartics.maven.plugin.jboss.modules.domain.matching.DelegationMatchContext;
import de.smartics.maven.plugin.jboss.modules.domain.matching.SingleMatchContext;
import de.smartics.util.lang.Arg;

/**
 * Descriptor to define the rules for matching artifacts to be included as
 * resources to a module.
 */
public final class ArtifactMatcher
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of inclusions.
   */
  private final List<ArtifactClusion> includes;

  /**
   * The list of exclusions.
   */
  private final List<ArtifactClusion> excludes;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ArtifactMatcher(final Builder builder)
  {
    includes = builder.includes;
    excludes = builder.excludes;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ArtifactMatcher}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The list of inclusions.
     */
    private final List<ArtifactClusion> includes = new ArrayList<ArtifactClusion>();

    /**
     * The list of exclusions.
     */
    private final List<ArtifactClusion> excludes = new ArrayList<ArtifactClusion>();

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    // --- business -----------------------------------------------------------

    /**
     * Adds an include to the list of includes.
     *
     * @param include the include to add.
     * @throws NullPointerException if {@code include} is <code>null</code>.
     */
    public void addInclude(final ArtifactClusion include) throws NullPointerException
    {
      includes.add(Arg.checkNotNull("include", include));
    }

    /**
     * Adds an exclude to the list of excludes.
     *
     * @param exclude the exclude to add.
     * @throws NullPointerException if {@code exclude} is <code>null</code>.
     */
    public void addExclude(final ArtifactClusion exclude) throws NullPointerException
    {
      excludes.add(Arg.checkNotNull("exclude", exclude));
    }

    /**
     * Builds an instance of {@link ArtifactMatcher}.
     *
     * @return the instance.
     */
    public ArtifactMatcher build()
    {
      return new ArtifactMatcher(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the list of inclusions.
   *
   * @return the list of inclusions.
   */
  public List<ArtifactClusion> getIncludes()
  {
    return includes;
  }

  /**
   * Returns the list of exclusions.
   *
   * @return the list of exclusions.
   */
  public List<ArtifactClusion> getExcludes()
  {
    return excludes;
  }

  // --- business -------------------------------------------------------------

  /**
   * Checks if the given artifact matches the module descriptor.
   *
   * @param artifact the artifact to match.
   * @return <code>true</code> if the module descriptor matches the given
   *         artifact, <code>false</code> otherwise.
   */
  public MatchContext match(final Artifact artifact)
  {
    final MatchContext includesContext = cludes(includes, artifact);
    final MatchContext excludesContext = cludes(excludes, artifact);

    final boolean result =
        (includesContext.isMatched() && !excludesContext.isMatched());
    if (includesContext.isMatched())
    {
      return new DelegationMatchContext(result, includesContext);
    }
    else
    {
      return new SingleMatchContext(result);
    }
  }

  private MatchContext cludes(final List<ArtifactClusion> clusions,
      final Artifact artifact)
  {
    if (clusions != null)
    {
      for (final ArtifactClusion clusion : clusions)
      {
        final MatchContext matchContext = clusion.matches(artifact);
        if (matchContext.isMatched())
        {
          return matchContext;
        }
      }
    }
    return new SingleMatchContext(false);
  }

  // --- object basics --------------------------------------------------------

  /**
   * {@inheritDoc}
   * <p>
   * Provides the properties via reflection for displaying debug information.
   * </p>
   */
  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
