/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.index;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;

import de.smartics.maven.plugin.jboss.modules.util.Arg;

/**
 * The index builder for packages and folder.
 */
public final class Indexer
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The folder to write the index to (within a META-INF folder that is added).
   */
  private final File outputDirectory;

  /**
   * The set of files.
   */
  private final Set<String> fileNames = new TreeSet<String>();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param outputDirectory the folder to write the index to (within a META-INF
   *          folder that is added).
   */
  public Indexer(final File outputDirectory)
  {
    this.outputDirectory = Arg.checkNotNull("outputDirectory", outputDirectory);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Adds the given file name.
   *
   * @param fileName the file name to add, blank file names are skipped.
   */
  public void add(final String fileName)
  {
    if (StringUtils.isNotBlank(fileName))
    {
      fileNames.add(fileName);
    }
  }

  /**
   * Writes the index.
   *
   * @throws MojoExecutionException on any problem writing the file.
   */
  public void writeIndex() throws MojoExecutionException
  {
    final File indexFile = new File(outputDirectory, "META-INF/INDEX.LIST");
    indexFile.getParentFile().mkdirs();

    PrintWriter writer = null;
    try
    {
      writer =
          new PrintWriter(new OutputStreamWriter(
              FileUtils.openOutputStream(indexFile), "UTF-8"));
      for (final String fileName : fileNames)
      {
        writer.append(fileName).append('\n');
      }
    }
    catch (final IOException e)
    {
      throw new MojoExecutionException(String.format(
          "Cannot write index file '%s'.", indexFile.getAbsoluteFile()), e);
    }
    finally
    {
      IOUtils.closeQuietly(writer);
    }
  }
  // --- object basics --------------------------------------------------------

}
