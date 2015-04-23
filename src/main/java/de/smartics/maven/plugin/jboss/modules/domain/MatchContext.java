/*
 * Copyright 2013-2015 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.domain;

import java.util.regex.MatchResult;

/**
 * Contains the result of a regular expression match.
 */
public interface MatchContext
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  /**
   * Checks if the match was successful.
   *
   * @return <code>true</code> if the match was successful, <code>false</code>
   *         otherwise.
   */
  boolean isMatched();

  /**
   * Returns the match result to access group information.
   *
   * @return the matcher to access group information.
   */
  MatchResult getMatchResult();

  // --- business -------------------------------------------------------------

  /**
   * Translates the name if it contains placeholders with the matching groups.
   *
   * @param input the input name that may contain placeholders.
   * @return the translated string. It is the input string, if {@code input}
   *         does not contain any placeholders.
   */
  String translateName(String input);

  /**
   * Checks if the match produced at least one group match.
   *
   * @return <code>true</code> if at least one group is matched,
   *         <code>false</code> otherwise.
   */
  boolean hasGroupMatch();

  // --- object basics --------------------------------------------------------
}
