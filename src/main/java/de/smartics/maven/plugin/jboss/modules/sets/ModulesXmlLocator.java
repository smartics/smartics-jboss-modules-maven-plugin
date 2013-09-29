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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.smartics.maven.plugin.jboss.modules.Module;
import de.smartics.util.lang.classpath.ClassPathContext;
import de.smartics.util.lang.classpath.ClassPathListing;
import de.smartics.util.lang.classpath.JarAndFileClassPathListing;

/**
 * Discovers all module descriptors on the class path.
 */
public final class ModulesXmlLocator
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The parser of external modules XML documents.
   */
  private final ModulesParser parser = new ModulesParser();

  /**
   * The name of the slot to map modules without slot to.
   */
  private final String targetSlot;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param targetSlot the name of the slot to map modules without slot to.
   */
  public ModulesXmlLocator(final String targetSlot)
  {
    this.targetSlot =
        StringUtils.isNotBlank(targetSlot) && !"main".equals(targetSlot)
            ? targetSlot : null;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Discovers all module descriptors on the class path.
   *
   * @return the discovered module descriptors.
   * @throws IOException if resources cannot be loaded from the class path.
   */
  public List<Module> discover() throws IOException
  {
    final List<Module> modules = new ArrayList<Module>();

    final ClassPathListing listing = new JarAndFileClassPathListing();
    final ClassLoader classLoader =
        Thread.currentThread().getContextClassLoader();
    final ClassPathContext context = new ClassPathContext(classLoader, null);
    final Enumeration<URL> urls = classLoader.getResources("jboss-modules");
    while (urls.hasMoreElements())
    {
      final List<String> fileList = listing.list(context, "jboss-modules");
      final URL url = urls.nextElement();
      loadModules(modules, url, fileList);
    }

    if (targetSlot != null)
    {
      for (final Module module : modules)
      {
        if (StringUtils.isBlank(module.getSlot()))
        {
          module.setSlot(targetSlot);
        }
      }
    }

    return modules;
  }

  private void loadModules(final List<Module> modules, final URL url,
      final List<String> fileList) throws IOException
  {
    for (final String file : fileList)
    {
      if (!file.endsWith(".xml"))
      {
        continue;
      }

      final URL fileUrl = new URL(url.toExternalForm() + '/' + file);
      final InputStream input = new BufferedInputStream(fileUrl.openStream());
      try
      {
        final String urlString = fileUrl.toExternalForm();
        parser.parse(modules, input, urlString);
      }
      finally
      {
        IOUtils.closeQuietly(input);
      }
    }
  }
  // --- object basics --------------------------------------------------------

}
