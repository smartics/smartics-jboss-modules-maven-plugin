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

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import de.smartics.maven.plugin.jboss.modules.index.Indexer;

/**
 * Generates an index of packages and folders within a project's classpath.
 *
 * @since 1.0
 * @description Generates an index of annotations for JBoss modules.
 */
@Mojo(name = "index", threadSafe = true, requiresProject = true,
    defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class IndexMojo extends AbstractMojo
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * Use as default the directory to process all packages and folders. If you
   * want to select files on certain directories, please use
   * <code>fileSets</code>.
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
  private File outputDirectory;

  /**
   * The set of packages and folders files to process. If empty, the classpath
   * of the project are processed.
   *
   * @since 1.0
   */
  @Parameter
  private List<FileSet> fileSets;

  /**
   * A simple flag to skip the execution of this MOJO. If set on the command
   * line use <code>-Dsmartics-index.skip</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-index.skip", defaultValue = "false")
  private boolean skip;

  /**
   * The verbose level. If set on the command line use
   * <code>-Dsmartics-index.verbose</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-index.verbose", defaultValue = "false")
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
      log.info("Skipping creating index since skip='true'.");
      return;
    }

    runIndexing();
  }

  private void runIndexing() throws MojoExecutionException
  {
    final Indexer indexer = new Indexer(outputDirectory);

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

    indexer.writeIndex();
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

    for (final String dirName : scanner.getIncludedDirectories())
    {
      indexer.add(dirName);
      if (verbose)
      {
        getLog().info(String.format("Added directory %s.", dirName));
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
