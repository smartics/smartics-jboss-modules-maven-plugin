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
package de.smartics.maven.plugin.jboss.modules.domain.matching;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;

/**
 * Contains the result of a single regular expression match.
 */
public final class SingleMatchContext implements MatchContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The result of the match.
   */
  private final boolean result;

  /**
   * The match result to access group information.
   */
  private final MatchResult matchResult;

  private final ArtifactClusion clusion;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor with a non-regexp match result.
   *
   * @param result the result of the match.
   */
  public SingleMatchContext(final boolean result, ArtifactClusion clusion)
  {
    this.result = result;
    this.matchResult = null;
    this.clusion = clusion;
  }

  /**
   * Default constructor with a matcher.
   *
   * @param matcher the matcher to access group information.
   */
  public SingleMatchContext(final Matcher matcher, ArtifactClusion clusion)
  {
    this.result = matcher.matches();
    this.matchResult = matcher.toMatchResult();
    this.clusion = clusion;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  @Override
  public boolean isMatched()
  {
    return result;
  }

  @Override
  public MatchResult getMatchResult()
  {
    return matchResult;
  }

  @Override
  public ArtifactClusion getClusion()
  {
    return clusion;
  }

  // --- business -------------------------------------------------------------

  /**
   * Translates the name if it contains placeholders with the matching groups.
   *
   * @param input the input name that may contain placeholders.
   * @return the translated string. It is the input string, if {@code input}
   *         does not contain any placeholders.
   */
  @Override
  public String translateName(final String input)
  {
    if (matchResult != null && isMatched())
    {
      final int groupCount = matchResult.groupCount();
      if (groupCount > 0)
      {
        String translation = input;
        for (int group = 1; group <= groupCount; group++)
        {
          final String replacement = matchResult.group(group);
          translation = translation.replace("$" + group, replacement);
        }
        return translation;
      }
    }
    return input;
  }

  /**
   * Checks if the match produced at least one group match.
   *
   * @return <code>true</code> if at least one group is matched,
   *         <code>false</code> otherwise.
   */
  @Override
  public boolean hasGroupMatch()
  {
    if (matchResult != null && isMatched())
    {
      final int groupCount = matchResult.groupCount();
      return (groupCount > 0);
    }
    return false;
  }

  // --- object basics --------------------------------------------------------

}
