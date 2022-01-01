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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * Clusion information for including and excluding on a module name.
 */
public final class ModuleClusion
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The name to match for this clusion.
   */
  private final String name;

  /**
   * The pattern to match. May be <code>null</code>.
   */
  private final Pattern namePattern;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param name the name to match for this clusion.
   * @throws IllegalArgumentException if {@code name} is blank, but not
   *           <code>null</code>.
   */
  public ModuleClusion(final String name) throws IllegalArgumentException
  {
    this.name = Arg.checkNotBlankExceptNull("name", name);
    this.namePattern = compilePattern(name);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

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

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the name to match for this clusion.
   *
   * @return the name to match for this clusion.
   */
  public String getName()
  {
    return name;
  }

  // --- business -------------------------------------------------------------

  /**
   * Compares the name of the clusion with the given {@code moduleName}.
   *
   * @param moduleName the name to match with.
   * @return <code>true</code> on a match, <code>false</code> otherwise.
   */
  public boolean match(final String moduleName)
  {
    if (name == null)
    {
      return true;
    }

    if (namePattern != null)
    {
      final Matcher matcher = namePattern.matcher(moduleName);
      return matcher.matches();
    }
    else
    {
      return name.equals(moduleName);
    }
  }

  // --- object basics --------------------------------------------------------

  @Override
  public String toString()
  {
    return name;
  }
}
