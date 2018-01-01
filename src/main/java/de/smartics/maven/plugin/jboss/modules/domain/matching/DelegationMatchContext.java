/*
 * Copyright 2013-2018 smartics, Kronseder & Reiner GmbH
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

import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.domain.MatchContext;

/**
 * Contains the delegated result of a regular expression match.
 */
public final class DelegationMatchContext implements MatchContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The result of the match.
   */
  private final boolean result;

  /**
   * The delegate of the match result to access group information.
   */
  private final MatchContext delegateMatchResult;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Constructor with a context.
   *
   * @param result the result of the match.
   * @param delegateMatchResult the delegate of the match result to access group
   *          information.
   */
  public DelegationMatchContext(final boolean result,
      final MatchContext delegateMatchResult)
  {
    this.result = result;
    this.delegateMatchResult = delegateMatchResult;
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
    return delegateMatchResult.getMatchResult();
  }

  @Override
  public ArtifactClusion getClusion()
  {
    return delegateMatchResult.getClusion();
  }

  // --- business -------------------------------------------------------------

  @Override
  public String translateName(final String input)
  {
    final String translation = delegateMatchResult.translateName(input);
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
    return delegateMatchResult.hasGroupMatch();
  }

  // --- object basics --------------------------------------------------------

}
