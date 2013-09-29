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
package de.smartics.maven.plugin.jboss.modules.sets;

import java.util.ArrayList;
import java.util.List;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.Module;

/**
 * The representation of a modules XML document.
 */
public class ModuleDescritors
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The list of modules.
   */
  private final List<Module> modules = new ArrayList<Module>();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public ModuleDescritors()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Returns the list of modules.
   *
   * @return the list of modules.
   */
  public List<Module> getModules()
  {
    for (final Module module : modules)
    {
      final List<Clusion> includes = module.getIncludes();
      if (includes != null)
      {
        for (final Clusion clusion : includes)
        {
          clusion.setGroupId(clusion.getGroupId());
          clusion.setArtifactId(clusion.getArtifactId());
        }
      }
      final List<Clusion> excludes = module.getExcludes();
      if (excludes != null)
      {
        for (final Clusion clusion : excludes)
        {
          clusion.setGroupId(clusion.getGroupId());
          clusion.setArtifactId(clusion.getArtifactId());
        }
      }
    }
    return modules;
  }

  // --- object basics --------------------------------------------------------

}
