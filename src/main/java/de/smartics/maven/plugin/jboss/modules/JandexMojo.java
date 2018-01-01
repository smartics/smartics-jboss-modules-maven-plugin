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
package de.smartics.maven.plugin.jboss.modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexWriter;
import org.jboss.jandex.Indexer;

/**
 * Generates an index of annotations for JBoss modules.
 *
 * @since 1.0
 * @description Generates an index of annotations for JBoss modules.
 */
@Mojo(name = "jandex", threadSafe = true, requiresProject = true,
    defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class JandexMojo extends AbstractMojo
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * Use as default the directory to process all classes. If you want to select
   * files on certain directories, please use <code>fileSets</code>.
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
  private File outputDirectory;

  // CHECKSTYLE:OFF
  /**
   * The set of class files to process. If empty, all class files are processed.
   *
   * <pre>
   * <![CDATA[<fileSets>
   *   <fileSet>
   *     <directory>${project.build.outputDirectory}</directory>
   *     <includes>
   *       <include>some/classes/to</include>
   *       ...
   *     </includes>
   *     <excludes>
   *       <exclude>some/classes/to/exclude/**</exclude>
   *       ...
   *     </excludes>
   *   </fileSet>
   *   ...
   * </fileSets>]]>
   * </pre>
   *
   * @since 1.0
   */
  @Parameter
  private List<FileSet> fileSets;

  // CHECKSTYLE:ON

  /**
   * A simple flag to skip the execution of this MOJO. If set on the command
   * line use <code>-Dsmartics-jandex.skip</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jandex.skip", defaultValue = "false")
  private boolean skip;

  /**
   * The verbose level. If set on the command line use
   * <code>-Dsmartics-jandex.verbose</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jandex.verbose", defaultValue = "false")
  private boolean verbose;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    final Log log = getLog();

    if (skip)
    {
      log.info("Skipping creating Jandex since skip='true'.");
      return;
    }

    runIndexing();
  }

  private void runIndexing() throws MojoExecutionException
  {
    final Indexer indexer = new Indexer();

    if (fileSets == null || fileSets.isEmpty())
    {
      if (outputDirectory.exists())
      {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(outputDirectory);
        runIndexing(indexer, scanner);
      }
      else
      {
        getLog().info(
            String.format("Skipping generation of index since"
                          + " no output directory found: %s",
                outputDirectory.getAbsolutePath()));
        return;
      }
    }
    else
    {
      runIndexing(indexer);
    }

    writeIndex(indexer);
  }

  private void runIndexing(final Indexer indexer)
  {
    for (final FileSet fileSet : fileSets)
    {
      final DirectoryScanner scanner = new DirectoryScanner();
      final File baseDir = calcBasedir(fileSet);
      scanner.setBasedir(baseDir);
      final List<String> includes = fileSet.getIncludes();
      if (includes != null && !includes.isEmpty())
      {
        scanner.setIncludes(includes.toArray(new String[includes.size()]));
      }
      final List<String> excludes = fileSet.getExcludes();
      if (excludes != null && !excludes.isEmpty())
      {
        scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
      }
      runIndexing(indexer, scanner);
    }
  }

  private File calcBasedir(final FileSet fileSet)
  {
    final String directory = fileSet.getDirectory();
    if (StringUtils.isBlank(directory))
    {
      return outputDirectory;
    }
    return new File(directory);
  }

  private void runIndexing(final Indexer indexer, final DirectoryScanner scanner)
  {
    scanner.scan();

    for (final String fileName : scanner.getIncludedFiles())
    {
      if (!fileName.endsWith(".class"))
      {
        continue;
      }

      final File file = new File(scanner.getBasedir(), fileName);
      InputStream input = null;
      try
      {
        input = FileUtils.openInputStream(file);
        final ClassInfo info = indexer.index(input);

        if (verbose)
        {
          getLog().info(
              String.format("Indexed %2d annotations in file %s.", info
                  .annotations().size(), info.name()));
        }
      }
      catch (final IOException e)
      {
        IOUtils.closeQuietly(input);
      }
    }
  }

  private void writeIndex(final Indexer indexer) throws MojoExecutionException
  {
    final File indexFile = new File(outputDirectory, "META-INF/jandex.idx");
    indexFile.getParentFile().mkdirs();

    FileOutputStream indexOutput = null;
    try
    {
      indexOutput = FileUtils.openOutputStream(indexFile);
      final IndexWriter writer = new IndexWriter(indexOutput);
      final Index index = indexer.complete();
      writer.write(index);
    }
    catch (final IOException e)
    {
      throw new MojoExecutionException(String.format(
          "Cannot write index file '%s'.", indexFile.getAbsoluteFile()), e);
    }
    finally
    {
      IOUtils.closeQuietly(indexOutput);
    }
  }

  // --- object basics --------------------------------------------------------

}
