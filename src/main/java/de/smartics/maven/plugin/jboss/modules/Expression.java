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
package de.smartics.maven.plugin.jboss.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

/**
 * An expression to match.
 */
public class Expression
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The identifier to match. May be a regular expression.
   */
  private String id;

  /**
   * The pattern to match. May be <code>null</code>, if {@link id} does not
   * specify a pattern.
   */
  private Pattern pattern;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public Expression()
  {
  }

  /**
   * Convenience constructor.
   *
   * @param id he identifier to match. May be a regular expression.
   */
  public Expression(final String id)
  {
    setId(id);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the identifier to match. May be a regular expression.
   *
   * @return the identifier to match.
   */
  public String getId()
  {
    return id;
  }

  /**
   * Sets the identifier to match. May be a regular expression.
   *
   * @param id the identifier to match.
   */
  public final void setId(final String id)
  {
    this.id = id;
    pattern = compilePattern(id);
  }

  // --- business -------------------------------------------------------------

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
   * Checks if the clusion matches the input.
   *
   * @param input the input to match.
   * @return <code>true</code> on a match, <code>false</code> otherwise.
   */
  public boolean matches(final String input)
  {
    if (pattern != null)
    {
      final Matcher matcher = pattern.matcher(input);
      return matcher.matches();
    }

    if (StringUtils.isNotBlank(id))
    {
      return id.equals(input);
    }

    return false;
  }

  // --- object basics --------------------------------------------------------

}
