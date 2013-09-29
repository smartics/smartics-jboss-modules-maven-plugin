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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base implementation to select modules by their names. Names may be regular
 * expressions.
 */
public abstract class AbstractModuleNameMatcher
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of names to match. If the list is empty, everything is included
   * (and those in the <code>excludes</code> list are excluded. If it is not
   * empty, only those that match are included (as long as they are not also
   * matching a member of the excludes list).
   * <p>
   * If you want to exclude everything, add a member non of your dependencies
   * match.
   * </p>
   */
  private List<String> includes;

  /**
   * The includes as precompiled expressions.
   */
  private List<Expression> includeExpressions;

  /**
   * The list of module names to exclude. If this list is empty, nothing is
   * excluded.
   */
  private List<String> excludes;

  /**
   * The excludes as precompiled expressions.
   */
  private List<Expression> excludeExpressions;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  protected AbstractModuleNameMatcher()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the list of names to match. If the list is empty, everything is
   * included (and those in the <code>excludes</code> list are excluded. If it
   * is not empty, only those that match are included (as long as they are not
   * also matching a member of the excludes list).
   * <p>
   * If you want to exclude everything, add a member non of your dependencies
   * match.
   * </p>
   *
   * @return the list of names to match.
   */
  public List<String> getIncludes()
  {
    return includes;
  }

  /**
   * Sets the list of names to match. If the list is empty, everything is
   * included (and those in the <code>excludes</code> list are excluded. If it
   * is not empty, only those that match are included (as long as they are not
   * also matching a member of the excludes list).
   * <p>
   * If you want to exclude everything, add a member non of your dependencies
   * match.
   * </p>
   *
   * @param includes the list of names to match.
   */
  public void setIncludes(final List<String> includes)
  {
    this.includes = includes;
    this.includeExpressions = new ArrayList<Expression>(includes.size());
    for (final String include : includes)
    {
      final Expression expression = new Expression(include);
      includeExpressions.add(expression);
    }
  }

  /**
   * Returns the list of module names to exclude. If this list is empty, nothing
   * is excluded.
   *
   * @return the list of module names to exclude.
   */
  public List<String> getExcludes()
  {
    return excludes;
  }

  /**
   * Sets the list of module names to exclude. If this list is empty, nothing is
   * excluded.
   *
   * @param excludes the list of module names to exclude.
   */
  public void setExcludes(final List<String> excludes)
  {
    this.excludes = excludes;
    this.excludeExpressions = new ArrayList<Expression>(excludes.size());
    for (final String exclude : excludes)
    {
      final Expression expression = new Expression(exclude);
      excludeExpressions.add(expression);
    }
  }

  // --- business -------------------------------------------------------------

  /**
   * Checks whether the given module should be exported.
   *
   * @param moduleName the name of the module to check for export.
   * @return <code>true</code> if the module is to be exported,
   *         <code>false</code> otherwise.
   */
  public boolean isMatched(final String moduleName)
  {
    if (includeExpressions == null && excludeExpressions == null)
    {
      return false;
    }

    if (includeExpressions == null || includeExpressions.isEmpty())
    {
      final boolean match = match(excludeExpressions, moduleName);
      return !match;
    }

    final boolean includeMatch = match(includeExpressions, moduleName);
    if (includeMatch)
    {
      final boolean excludeMatch = match(excludeExpressions, moduleName);
      return !excludeMatch;
    }

    return includeMatch;
  }

  private boolean match(final List<Expression> expressions,
      final String moduleName)
  {
    if (expressions == null)
    {
      return false;
    }

    for (final Expression expression : expressions)
    {
      final boolean match = expression.matches(moduleName);
      if (match)
      {
        return true;
      }
    }

    return false;
  }

  // CHECKSTYLE:OFF
  /**
   * Merges the includes and excludes of the given matcher with this one.
   *
   * @param matcher the matcher to merge with this instance.
   * @throws NullPointerException if {@code matcher} is <code>null</code>.
   */
  public void merge(final AbstractModuleNameMatcher matcher)
    throws NullPointerException
  {
    if (includes != null && matcher.includes != null)
    {
      includes.addAll(matcher.includes);
    }
    else if (includes == null)
    {
      includes = matcher.includes;
    }

    if (includeExpressions != null && matcher.includeExpressions != null)
    {
      includeExpressions.addAll(matcher.includeExpressions);
    }
    else if (includeExpressions == null)
    {
      includeExpressions = matcher.includeExpressions;
    }

    if (excludes != null && matcher.excludes != null)
    {
      excludes.addAll(matcher.excludes);
    }
    else if (excludes == null)
    {
      excludes = matcher.excludes;
    }

    if (excludeExpressions != null && matcher.excludeExpressions != null)
    {
      excludeExpressions.addAll(matcher.excludeExpressions);
    }
    else if (excludeExpressions == null)
    {
      excludeExpressions = matcher.excludeExpressions;
    }
  }
  // CHECKSTYLE:ON

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    final ToStringBuilder builder = new ToStringBuilder(null);
    builder.append("include", includes).append("excludes", excludes);
    return builder.toString();

  }
}
