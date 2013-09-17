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
package de.smartics.maven.plugin.jboss.modules.domain;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.Module;
import de.smartics.maven.plugin.jboss.modules.xml.ModuleXmlBuilder;

/**
 * Creates a single module within the archive of modules.
 */
public final class ModuleBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The configuration to control the building of the modules archive.
   */
  private final ExecutionContext context;

  /**
   * The descriptor of the module to build.
   */
  private final Module module;

  /**
   * The artifacts that are part of this module.
   */
  private final List<Dependency> dependencies;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param context the configuration to control the building of the modules
   *          archive.
   * @param module the descriptor of the module to build.
   * @param dependencies the dependencies that are part of this module.
   */
  public ModuleBuilder(final ExecutionContext context, final Module module,
      final Collection<Dependency> dependencies)
  {
    this.context = context;
    this.module = module;
    this.dependencies = new ArrayList<Dependency>(dependencies);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Creates the module.
   *
   * @throws IOException on any problem creating the module in the given base
   *           directory.
   */
  public void create() throws IOException
  {
    if (!module.isSkip())
    {
      final File moduleFolder = createModuleFolder();
      createModuleXml(moduleFolder);
      copyResources(moduleFolder);
    }
  }

  private File createModuleFolder() throws IOException
  {
    String path = module.getBasePath();
    if (path == null)
    {
      path = module.getName().replace('.', '/');
    }

    final File folder = new File(context.getTargetFolder(), path);
    final File slotFolder = new File(folder, calcSlot());
    final boolean created = slotFolder.mkdirs();
    if (!created)
    {
      throw new IOException(String.format(
          "Cannot created folder '%s' for module '%s'.",
          slotFolder.getAbsolutePath(), module.getName()));
    }
    return slotFolder;
  }

  private String calcSlot()
  {
    String slot = module.getSlot();
    if (StringUtils.isBlank(slot))
    {
      final SlotStrategy strategy = context.getSlotStrategy();

      if (!dependencies.isEmpty())
      {
        final Artifact artifact = dependencies.get(0).getArtifact();
        slot = strategy.calcSlot(artifact, context.getDefaultSlot());
      }
      else
      {
        slot = SlotStrategy.MAIN_SLOT;
      }
    }
    return slot;
  }

  private void createModuleXml(final File moduleFolder) throws IOException
  {
    final ModuleXmlBuilder xml =
        new ModuleXmlBuilder(context, module, dependencies);
    final XMLOutputter outputter = new XMLOutputter();
    outputter.setFormat(Format.getPrettyFormat());
    final File file = new File(moduleFolder, "module.xml");
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(file));
      final Document document = xml.build();
      outputter.output(document, out);
    }
    finally
    {
      IOUtils.closeQuietly(out);
    }
  }

  private void copyResources(final File moduleFolder) throws IOException
  {
    for (final Dependency dependency : dependencies)
    {
      final Artifact artifact = dependency.getArtifact();
      final File remoteFile = artifact.getFile();
      if (remoteFile != null)
      {
        final File localFile = new File(moduleFolder, remoteFile.getName());
        FileUtils.copyFile(remoteFile, localFile);
      }
      else
      {
        context.getLog().warn(
            String.format(
                "Cannot copy non-existing remote file for dependency '%s'.",
                dependency.getArtifact()));
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
