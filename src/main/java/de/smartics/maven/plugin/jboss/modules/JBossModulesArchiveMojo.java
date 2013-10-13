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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencySelector;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyFilter;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.DefaultRepositorySystemSession;
import org.sonatype.aether.util.graph.selector.AndDependencySelector;
import org.sonatype.aether.util.graph.selector.ExclusionDependencySelector;
import org.sonatype.aether.util.graph.selector.OptionalDependencySelector;
import org.sonatype.aether.util.graph.selector.ScopeDependencySelector;

import de.smartics.maven.plugin.jboss.modules.aether.Mapper;
import de.smartics.maven.plugin.jboss.modules.aether.MavenRepository;
import de.smartics.maven.plugin.jboss.modules.aether.MojoRepositoryBuilder;
import de.smartics.maven.plugin.jboss.modules.aether.filter.DefaultTransitiveDependencyResolver;
import de.smartics.maven.plugin.jboss.modules.aether.filter.GaExclusionFilter;
import de.smartics.maven.plugin.jboss.modules.aether.filter.TestScopeFilter;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;
import de.smartics.maven.plugin.jboss.modules.domain.ExecutionContext;
import de.smartics.maven.plugin.jboss.modules.domain.ModuleBuilder;
import de.smartics.maven.plugin.jboss.modules.domain.ModuleMap;
import de.smartics.maven.plugin.jboss.modules.domain.PrunerGenerator;
import de.smartics.maven.plugin.jboss.modules.domain.SlotStrategy;
import de.smartics.maven.plugin.jboss.modules.domain.TransitiveDependencyResolver;
import de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlLocator;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Generates a archive containing modules from a BOM project.
 *
 * @since 1.0
 * @description Generates a archive containing modules from a BOM project.
 */
@Mojo(name = "create-modules-archive", threadSafe = true,
    requiresProject = true,
    requiresDependencyResolution = ResolutionScope.TEST,
    defaultPhase = LifecyclePhase.PACKAGE)
public final class JBossModulesArchiveMojo extends AbstractMojo
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ... Mojo infrastructure ..................................................

  /**
   * The Maven project.
   *
   * @since 1.0
   */
  @Component
  private MavenProject project;

  /**
   * The Maven session.
   */
  @Component
  private MavenSession session;

  /**
   * Resolver for artifact repositories.
   *
   * @since 1.0
   */
  @Component
  private RepositorySystem repositorySystem;

  /**
   * The current repository/network configuration of Maven.
   */
  @Parameter(defaultValue = "${repositorySystemSession}")
  private RepositorySystemSession repositorySession;

  /**
   * The project's remote repositories to use for the resolution of
   * dependencies.
   */
  @Parameter(defaultValue = "${project.remoteProjectRepositories}")
  private List<RemoteRepository> remoteRepos;

  /**
   * Helper to add attachments to the build.
   *
   * @since 1.0
   */
  @Component
  private MavenProjectHelper projectHelper;

  /**
   * Helper to create an archive.
   *
   * @since 1.0
   */
  @Component(role = Archiver.class, hint = "jar")
  private JarArchiver jarArchiver;

  /**
   * The archive configuration to use. See <a
   * href="http://maven.apache.org/shared/maven-archiver/index.html">Maven
   * Archiver Reference</a>.
   *
   * @since 1.0
   */
  @Parameter
  private final MavenArchiveConfiguration archive =
      new MavenArchiveConfiguration();

  /**
   * A simple flag to skip the execution of this MOJO. If set on the command
   * line use <code>-Dsmartics-jboss-modules.skip</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jboss-modules.skip", defaultValue = "false")
  private boolean skip;

  /**
   * The verbose level. If set on the command line use
   * <code>-Dsmartics-jboss-modules.verbose</code>.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jboss-modules.verbose",
      defaultValue = "false")
  private boolean verbose;

  /**
   * Allows to attach the generated modules as a ZIP archive to the build.
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "true")
  private boolean attach;

  /**
   * Controls the system to act as being offline (<code>true</code>) or not (
   * <code>false</code>).
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "${offline}")
  private boolean offline;

  /**
   * Controls the update policy according the access of the remote repositories.
   * <p>
   * Allowed values are <code>never</code>, <code>always</code>, and
   * <code>daily</code>.
   * </p>
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jboss-modules.update", defaultValue = "never")
  private String updatePolicy;

  /**
   * Controls whether or not optional dependencies should be followed.
   *
   * @since 1.0
   */
  @Parameter(property = "smartics-jboss-modules.followOptionalDependencies",
      defaultValue = "false")
  private boolean followOptionalDependencies;

  /**
   * The name of the default slot to write to. See <code>slotStrategy</code>.
   *
   * @since 1.0
   * @see #slotStrategy
   */
  @Parameter(defaultValue = "main")
  private String defaultSlot;

  /**
   * The name of the slot strategy to us. If not specified, the major version of
   * the dependency will be used as slot value.
   * <p>
   * Possible values are:
   * </p>
   * <table>
   * <tr>
   * <th>value</th>
   * <th>description</th>
   * </tr>
   * <tr>
   * <td>version-major</td>
   * <td>The slot has the major number of the version. The
   * <code>defaultSlot</code> is prepended, if not set to <code>main</code>
   * (e.g. <code>defaultSlot</code>=prodx and version 1.2.3 then the slot will
   * be named prodx1.</td>
   * </tr>
   * <tr>
   * <td>main</td>
   * <td>The slot has the name as given with <code>defaultSlot</code>.</td>
   * </tr>
   * </table>
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "version-major")
  private String slotStrategy;

  /**
   * A list of dependencies to be excluded from the transitive dependency
   * collection process.
   *
   * <pre>
   *  &lt;dependencyExcludes&gt;
   *    &lt;exclude&gt;
   *      &lt;groupId&gt;com.sun&lt;/groupId&gt;
   *      &lt;artifactId&gt;tools&lt;/artifactId&gt;
   *    &lt;/exclude&gt;
   *  &lt;/dependencyExcludes&gt;
   * </pre>
   */
  @Parameter
  private List<ArtifactClusion> dependencyExcludes;

  /**
   * The root directories to search for modules XML files that contain module
   * descriptors.
   * <p>
   * If not specified, the default location <code>src/etc/modules</code> is
   * probed and - if exists - is appended.
   * </p>
   *
   * <pre>
   * &lt;modules&gt;
   *   &lt;dir&gt;src/etc/modules&lt;/dir&gt;
   * &lt;/modules&gt;
   * </pre>
   *
   * @since 1.0
   */
  @Parameter
  private List<String> modules;

  /**
   * The folder to write the module structure to.
   *
   * @since 1.0
   */
  @Parameter(defaultValue = "${project.build.directory}/jboss-modules")
  private File targetFolder;

  /**
   * The file to attach, containing the JBoss modules.
   *
   * @since 1.0
   */
  @Parameter(
      defaultValue = "${project.build.directory}/${project.artifactId}-${project.version}-jboss-modules.jar")
  private File modulesArchive;

  /**
   * The modules declared in the POM and the modules declared on the classpath
   * (in that order).
   */
  private List<ModulesDescriptor> modulesDescriptors;

  /**
   * The linear list of all modules from {@link #modulesDescriptors}.
   */
  private List<ModuleDescriptor> allModules;

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
      log.info("Skipping creating archive for JBoss modules since skip='true'.");
      return;
    }

    this.modulesDescriptors = initModulesDescriptors();
    this.allModules = initModules();
    this.repositorySession = adjustSession();

    final List<Dependency> rootDependencies = calcRootDependencies();
    final List<Dependency> dependencies = resolve(rootDependencies);

    logDependencies(rootDependencies, dependencies);
    runModuleCreation(dependencies);
    attach();
  }

  private List<ModuleDescriptor> initModules()
  {
    final List<ModuleDescriptor> modules = new ArrayList<ModuleDescriptor>();

    for (final ModulesDescriptor descriptor : modulesDescriptors)
    {
      modules.addAll(descriptor.getDescriptors());
    }
    return modules;
  }

  private List<ModulesDescriptor> initModulesDescriptors()
    throws MojoExecutionException
  {
    try
    {
      final ModulesXmlLocator locator = new ModulesXmlLocator(defaultSlot);
      final ClassLoader parentClassLoader =
          Thread.currentThread().getContextClassLoader();
      final List<File> rootDirectories = calcModulesRootDirectories();
      final List<ModulesDescriptor> descriptors =
          locator.discover(parentClassLoader, rootDirectories);
      return descriptors;
    }
    catch (final IOException e)
    {
      throw new MojoExecutionException("Cannot read modules from class path.",
          e);
    }
  }

  @SuppressWarnings("unchecked")
  private List<File> calcModulesRootDirectories()
  {
    if (modules == null)
    {
      final String defaultDir = "src/etc/modules";
      final File rootDirectory = new File(project.getBasedir(), defaultDir);
      if (rootDirectory.isDirectory())
      {
        modules = Collections.singletonList(defaultDir);
      }
    }

    if (modules != null)
    {
      final List<File> rootDirectories = new ArrayList<File>(modules.size());
      for (final String dir : modules)
      {
        final File rootDirectory = new File(project.getBasedir(), dir);
        if (rootDirectory.isDirectory())
        {
          rootDirectories.add(rootDirectory);
        }
        else
        {
          getLog().warn(
              String.format(
                  "Modules directory '%s' does not exist. Skipping ...",
                  rootDirectory.getAbsolutePath()));
        }
      }

      return rootDirectories;
    }

    return new ArrayList<File>(0);
  }

  private void runModuleCreation(final List<Dependency> dependencies)
    throws MojoExecutionException
  {
    final boolean isPomProject = "pom".equals(project.getPackaging());
    if (!isPomProject)
    {
      final Mapper mapper = new Mapper();
      final Artifact projectArtifact = mapper.map(project.getArtifact());
      final Dependency projectAsDependency =
          new Dependency(projectArtifact, "compile");
      dependencies.add(0, projectAsDependency);
    }

    final ExecutionContext context = createContext(dependencies);
    for (final Entry<ModuleDescriptor, List<Dependency>> entry : context
        .getModuleMap().toMap().entrySet())
    {
      final ModuleDescriptor module = entry.getKey();
      final Collection<Dependency> moduleDependencies =
          new HashSet<Dependency>(entry.getValue());
      final ModuleBuilder builder =
          new ModuleBuilder(context, module, moduleDependencies);
      try
      {
        builder.create();
      }
      catch (final IOException e)
      {
        throw new MojoExecutionException("Cannot write module '"
                                         + entry.getKey().getName() + "'.", e);
      }
    }
  }

  private void logDependencies(final Collection<Dependency> rootDependencies,
      final Collection<Dependency> dependencies) throws MojoExecutionException
  {
    if (verbose)
    {
      try
      {
        FileUtils.writeStringToFile(new File(project.getBasedir(),
            "target/root-dependencies.txt"), rootDependencies.toString());
        FileUtils.writeStringToFile(new File(project.getBasedir(),
            "target/resolved-dependencies.txt"), dependencies.toString());
      }
      catch (final IOException e)
      {
        throw new MojoExecutionException("dependencies", e);
      }
    }
  }

  private DefaultRepositorySystemSession adjustSession()
  {
    final DefaultRepositorySystemSession session =
        new DefaultRepositorySystemSession(repositorySession);
    final Set<DependencySelector> selectors = new HashSet<DependencySelector>();
    selectors.add(new ScopeDependencySelector("test"));
    if (!followOptionalDependencies)
    {
      selectors.add(new OptionalDependencySelector());
    }
    selectors.add(new ExclusionDependencySelector());
    final AndDependencySelector selector = new AndDependencySelector(selectors);
    session.setDependencySelector(selector);
    session.setUpdatePolicy(updatePolicy);
    session.setOffline(offline);
    return session;
  }

  private void attach() throws MojoExecutionException
  {
    if (!attach)
    {
      return;
    }
    if (!targetFolder.isDirectory())
    {
      getLog().info("Nothing to attach.");
      return;
    }

    try
    {
      jarArchiver.addDirectory(targetFolder);

      final MavenArchiver archiver = new MavenArchiver();
      archiver.setArchiver(jarArchiver);
      archiver.setOutputFile(modulesArchive);
      archiver.createArchive(session, project, archive);
      projectHelper.attachArtifact(project, "jar", "jboss-modules",
          modulesArchive);
    }
    catch (final Exception e)
    {
      final String message =
          String.format("Cannot create archive '%s'.: %s",
              modulesArchive.getAbsolutePath(), e.getMessage());
      throw new MojoExecutionException(message, e);
    }
  }

  private ExecutionContext createContext(final List<Dependency> dependencies)
  {
    final ExecutionContext.Builder builder = new ExecutionContext.Builder();
    builder.with(getLog());
    builder.withTargetFolder(targetFolder);

    final TransitiveDependencyResolver resolver = createResolver(dependencies);
    builder.with(resolver);

    final SlotStrategy slotStrategy =
        SlotStrategy.fromString(this.slotStrategy);
    builder.with(slotStrategy);
    builder.withDefaultSlot(defaultSlot);

    final ModuleMap moduleMap = new ModuleMap(allModules, dependencies);
    builder.with(moduleMap);

    if (verbose)
    {
      getLog().info("Modules:\n" + moduleMap.toString());
    }

    return builder.build();
  }

  @SuppressWarnings("unchecked")
  private List<Dependency> calcRootDependencies() throws MojoExecutionException
  {
    final List<Dependency> rootDependencies = new ArrayList<Dependency>();

    final List<org.apache.maven.model.Dependency> projectDependencies =
        project.getDependencies();
    addMappedDependencies(rootDependencies, projectDependencies);

    final boolean isPomProject = "pom".equals(project.getPackaging());
    if (isPomProject)
    {
      final DependencyManagement management = project.getDependencyManagement();
      if (management != null)
      {
        final List<org.apache.maven.model.Dependency> managedDependencies =
            management.getDependencies();
        addMappedDependencies(rootDependencies, managedDependencies);
      }
    }

    return rootDependencies;
  }

  private static void addMappedDependencies(
      final List<Dependency> rootDependencies,
      final List<org.apache.maven.model.Dependency> newDependencies)
  {
    if (newDependencies != null && !newDependencies.isEmpty())
    {
      final Mapper mapper = new Mapper();
      for (final org.apache.maven.model.Dependency mavenDependency : newDependencies)
      {
        final Dependency dependency = mapper.map(mavenDependency);
        rootDependencies.add(dependency);
      }
    }
  }

  private List<Dependency> resolve(final List<Dependency> rootDependencies)
    throws MojoExecutionException
  {
    final TransitiveDependencyResolver resolver = createResolver(null);
    try
    {
      final List<Dependency> dependencies = resolver.resolve(rootDependencies);
      return dependencies;
    }
    catch (final DependencyResolutionException e)
    {
      final String message =
          "Cannot resolve dependency: "
              + e.getMessage()
              + "\nYou may use 'dependencyExcludes' to exclude broken dependencies from examination.";
      throw new MojoExecutionException(message, e);
    }
  }

  private TransitiveDependencyResolver createResolver(
      final List<Dependency> managedDependencies)
  {
    final PrunerGenerator prunerGenerator =
        new PrunerGenerator(dependencyExcludes, allModules);
    final List<DependencyFilter> dependencyFilters = createDependencyFilters();
    final MojoRepositoryBuilder builder = new MojoRepositoryBuilder();
    builder.with(repositorySystem).with(repositorySession).with(remoteRepos)
        .withDependencyFilters(dependencyFilters)
        .withManagedDependencies(managedDependencies).withOffline(offline)
        .withTraverserGenerator(prunerGenerator).build();
    final MavenRepository repository = builder.build();

    return new DefaultTransitiveDependencyResolver(repository);
  }

  private List<DependencyFilter> createDependencyFilters()
  {
    final List<DependencyFilter> dependencyFilters =
        new ArrayList<DependencyFilter>();
    dependencyFilters.add(new TestScopeFilter());
    if (dependencyExcludes != null && !dependencyExcludes.isEmpty())
    {
      final GaExclusionFilter filter =
          new GaExclusionFilter(dependencyExcludes);
      dependencyFilters.add(filter);
    }
    return dependencyFilters;
  }

  // --- object basics --------------------------------------------------------
}
