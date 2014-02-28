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
package de.smartics.maven.plugin.jboss.modules.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.JDOMException;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;
import de.smartics.maven.plugin.jboss.modules.util.classpath.ClassPathContext;
import de.smartics.maven.plugin.jboss.modules.util.classpath.ClassPathListing;
import de.smartics.maven.plugin.jboss.modules.util.classpath.JarAndFileClassPathListing;

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
  private final ModulesXmlParser parser = new ModulesXmlParser();

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
   * @param classLoader the class loader whose class path is searched.
   * @param rootDirectories additional root directories to check first.
   * @return the discovered module descriptors.
   * @throws IOException if resources cannot be loaded from the class path.
   */
  public List<ModulesDescriptor> discover(final ClassLoader classLoader,
      final List<File> rootDirectories) throws IOException
  {
    final List<ModulesDescriptor> modules = new ArrayList<ModulesDescriptor>();

    for (final File rootDirectory : rootDirectories)
    {
      loadModules(modules, rootDirectory);
    }

    final ClassPathListing listing = new JarAndFileClassPathListing();
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
      for (final ModulesDescriptor module : modules)
      {
        module.applyDefaultSlot(targetSlot);
      }
    }

    return modules;
  }

  private void loadModules(final List<ModulesDescriptor> modules,
      final URL url, final List<String> fileList) throws IOException
  {
    for (final String file : fileList)
    {
      if (!file.endsWith(".xml"))
      {
        continue;
      }

      final URL fileUrl = new URL(url.toExternalForm() + '/' + file);
      final InputStream input = new BufferedInputStream(fileUrl.openStream());
      final String urlString = fileUrl.toExternalForm();
      try
      {
        final ModulesDescriptor descriptor = parser.parse(urlString, input);
        modules.add(descriptor);
      }
      catch (final JDOMException e)
      {
        throw new IOException("Cannot parse XML file: " + urlString, e);
      }
      finally
      {
        IOUtils.closeQuietly(input);
      }
    }
  }

  private void loadModules(final List<ModulesDescriptor> modules,
      final File rootDirectory) throws IOException
  {
    final File[] fileList = rootDirectory.listFiles();
    for (final File file : fileList)
    {
      if (!file.getName().endsWith(".xml"))
      {
        continue;
      }

      final InputStream input =
          new BufferedInputStream(FileUtils.openInputStream(file));
      final String fileId = file.getAbsolutePath();
      try
      {
        final ModulesDescriptor descriptor = parser.parse(fileId, input);
        modules.add(descriptor);
      }
      catch (final JDOMException e)
      {
        throw new IOException("Cannot parse XML file: " + fileId, e);
      }
      finally
      {
        IOUtils.closeQuietly(input);
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
