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
package de.smartics.maven.plugin.jboss.modules.domain.matching;

import java.util.regex.MatchResult;

import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;

/**
 * Contains the result of two a regular expression matches. One groupId and one
 * artifactId match.
 */
public final class DoubleMatchContext implements MatchContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The result of the match.
   */
  private final boolean result;

  /**
   * The match result to access group information of the groupId.
   */
  private final MatchResult groupMatchResult;

  /**
   * The match result to access group information of the artifactId.
   */
  private final MatchResult artifactMatchResult;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Constructor with a context.
   *
   * @param result the result of the match.
   * @param groupIdContext the context of a match to derive from for groupIds.
   * @param artifactIdContext the context of a match to derive from for
   *          artifactIds.
   */
  public DoubleMatchContext(final boolean result,
      final MatchContext groupIdContext, final MatchContext artifactIdContext)
  {
    this.result = result;
    this.groupMatchResult =
        groupIdContext != null && groupIdContext.isMatched() ? groupIdContext
            .getMatchResult() : null;
    this.artifactMatchResult =
        artifactIdContext != null && artifactIdContext.isMatched()
            ? artifactIdContext.getMatchResult() : null;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Checks if the match was successful.
   *
   * @return <code>true</code> if the match was successful, <code>false</code>
   *         otherwise.
   */
  public boolean isMatched()
  {
    return result;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Returns the artifactId match result.
   * </p>
   */
  @Override
  public MatchResult getMatchResult()
  {
    return artifactMatchResult;
  }

  // --- business -------------------------------------------------------------

  @Override
  public String translateName(final String input)
  {
    String translation = input;

    if (isMatched())
    {
      if (artifactMatchResult != null)
      {
        translation = translate(artifactMatchResult, "$", translation);
      }

      if (groupMatchResult != null)
      {
        translation = translate(groupMatchResult, "$g", translation);
      }
    }

    return translation;
  }

  private static String translate(final MatchResult matchResult,
      final String delimiter, final String input)
  {
    String translation = input;
    final int groupCount = matchResult.groupCount();
    if (groupCount > 0)
    {
      for (int group = 1; group <= groupCount; group++)
      {
        final String replacement = matchResult.group(group);
        translation = translation.replace(delimiter + group, replacement);
      }
    }
    return translation;
  }

  /**
   * Checks if any match produced at least one group match.
   *
   * @return <code>true</code> if at least one group is matched,
   *         <code>false</code> otherwise.
   */
  public boolean hasGroupMatch()
  {
    if (artifactMatchResult != null && isMatched())
    {
      final int groupCount = artifactMatchResult.groupCount();
      if (groupCount > 0)
      {
        return true;
      }
    }

    if (groupMatchResult != null && isMatched())
    {
      final int groupCount = groupMatchResult.groupCount();
      if (groupCount > 0)
      {
        return true;
      }
    }

    return false;
  }

  // --- object basics --------------------------------------------------------

}
