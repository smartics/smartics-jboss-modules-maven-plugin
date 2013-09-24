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
package de.smartics.maven.plugin.jboss.modules.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;

import de.smartics.maven.plugin.jboss.modules.Module;
import de.smartics.maven.plugin.jboss.modules.Services;
import de.smartics.maven.plugin.jboss.modules.domain.ExecutionContext;
import de.smartics.maven.plugin.jboss.modules.domain.SlotStrategy;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Creates <code>module.xml</code> descriptors for JBoss modules.
 */
public final class ModuleXmlBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The default namespace for the <code>module.xml</code> descriptor.
   */
  public static final Namespace NS = Namespace
      .getNamespace("urn:jboss:module:1.1");

  // --- members --------------------------------------------------------------

  /**
   * The context and configuration to control the building of XML files.
   */
  private final ExecutionContext context;

  /**
   * The module to build.
   */
  private final Module module;

  /**
   * The dependencies to reference.
   */
  private final Collection<Dependency> dependencies;

  /**
   * The XML document.
   */
  private final Document document;

  /**
   * The root element of the document.
   */
  private final Element root;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param context the context and configuration to control the building of XML
   *          files.
   * @param module the module to build.
   * @param dependencies the dependencies to reference.
   */
  public ModuleXmlBuilder(final ExecutionContext context, final Module module,
      final Collection<Dependency> dependencies)
  {
    this.context = context;
    this.module = module;
    this.dependencies = dependencies;

    root = new Element("module", NS);
    root.setAttribute("name", module.getName());
    final String slot = calcSlot(context, module, dependencies);
    if (!SlotStrategy.MAIN_SLOT.equals(slot))
    {
      root.setAttribute("slot", slot);
    }
    document = new Document(root);
  }

  // ****************************** Inner Classes *****************************

  /**
   * Helper to sort artifact lists.
   */
  private static final class SortElement implements Comparable<SortElement>
  {
    /**
     * The key used for sorting.
     */
    private final String key;

    /**
     * The dependency to be sorted.
     */
    private final Dependency dependency;

    private SortElement(final String key, final Dependency dependency)
    {
      this.key = key;
      this.dependency = dependency;
    }

    @Override
    public int compareTo(final SortElement o)
    {
      return key.compareTo(o.key);
    }

  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  private static String calcSlot(final ExecutionContext context,
      final Module module, final Collection<Dependency> dependencies)
  {
    final String specifiedSlot = module.getSlot();
    if (StringUtils.isNotBlank(specifiedSlot))
    {
      return specifiedSlot;
    }

    final String defaultSlot = context.getDefaultSlot();
    final SlotStrategy strategy = context.getSlotStrategy();
    if (SlotStrategy.VERSION_MAJOR == strategy)
    {
      final Artifact artifact = calcArtifact(dependencies);
      final String slot = strategy.calcSlot(artifact, defaultSlot);
      return slot;
    }
    else
    {
      return defaultSlot;
    }
  }

  private static Artifact calcArtifact(final Collection<Dependency> dependencies)
  {
    if (dependencies != null && !dependencies.isEmpty())
    {
      final Dependency dependency = dependencies.iterator().next();
      final Artifact artifact = dependency.getArtifact();
      return artifact;
    }
    return null;
  }

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Builds the document.
   *
   * @return the XML document.
   */
  public Document build()
  {
    addProperties(module);
    addResources(dependencies);
    addDependencies(module, dependencies);

    return document;
  }

  private void addProperties(final Module module)
  {
    final Map<String, String> properties = module.getProperties();
    if (properties != null && !properties.isEmpty())
    {
      final Element propertiesElement = new Element("properties", NS);
      for (final Entry<String, String> entry : properties.entrySet())
      {
        final Element property = new Element("property", NS);
        property.setAttribute("name", entry.getKey());
        property.setAttribute("value", entry.getValue());
        propertiesElement.addContent(property);
      }

      root.addContent(propertiesElement);
    }
  }

  private void addResources(final Collection<Dependency> dependencies)
  {
    if (!dependencies.isEmpty())
    {
      final Element resources = new Element("resources", NS);

      final List<SortElement> sorted = createSortedResources(dependencies);
      for (final SortElement element : sorted)
      {
        final Element resource = new Element("resource-root", NS);
        final String fileName = element.key;
        resource.setAttribute("path", fileName);
        resources.addContent(resource);
      }

      root.addContent(resources);
    }
  }

  private List<SortElement> createSortedResources(
      final Collection<Dependency> dependencies)
  {
    final List<SortElement> sorted =
        new ArrayList<SortElement>(dependencies.size());
    for (final Dependency dependency : dependencies)
    {
      final Artifact artifact = dependency.getArtifact();
      final File file = artifact.getFile();
      if (file != null)
      {
        final String fileName = file.getName();
        sorted.add(new SortElement(fileName, dependency));
      }
    }
    Collections.sort(sorted);
    return sorted;
  }

  private void addDependencies(final Module module,
      final Collection<Dependency> dependencies)
  {
    if (!dependencies.isEmpty() || module.hasDependencies())
    {
      final Element dependenciesElement = new Element("dependencies", NS);

      addResolvedDependencies(module, dependencies, dependenciesElement);
      addStaticDependencies(module, dependenciesElement);

      root.addContent(dependenciesElement);
    }
  }

  private void addResolvedDependencies(final Module module,
      final Collection<Dependency> dependencies,
      final Element dependenciesElement)
  {
    final Set<SortElement> sorted =
        createSortedDependencies(module, dependencies);

    for (final SortElement element : sorted)
    {
      final String name = element.key;
      final Element moduleElement = new Element("module", NS);
      moduleElement.setAttribute("name", name);
      if (element.dependency.isOptional())
      {
        moduleElement.setAttribute("optional", "true");
      }
      if (module.isExport(name))
      {
        moduleElement.setAttribute("export", "true");
      }
      final String services = module.getService(name);
      if (!Services.NONE.equals(services))
      {
        moduleElement.setAttribute("services", services);
      }

      final SlotStrategy slotStrategy = context.getSlotStrategy();
      final Dependency dependency = element.dependency;
      final String defaultSlot = calcDefaultSlot(module, dependency);
      final String slot =
          slotStrategy.calcSlot(dependency.getArtifact(), defaultSlot);
      if (!SlotStrategy.MAIN_SLOT.equals(slot)
          || (StringUtils.isNotBlank(module.getSlot()) && !slot.equals(module
              .getSlot())))
      {
        moduleElement.setAttribute("slot", slot);
      }
      dependenciesElement.addContent(moduleElement);
    }
  }

  private Set<SortElement> createSortedDependencies(final Module module,
      final Collection<Dependency> dependencies)
  {
    final Set<SortElement> sorted = new TreeSet<SortElement>();
    for (final Dependency dependency : dependencies)
    {
      final List<Dependency> resolvedDependencies = context.resolve(dependency);
      addSortedDependencies(sorted, module, resolvedDependencies);
    }
    return sorted;
  }

  private String calcDefaultSlot(final Module module,
      final Dependency dependency)
  {
    final Module depModule = context.getModule(dependency);
    final String depModuleSlot = depModule.getSlot();
    if (StringUtils.isNotBlank(depModuleSlot))
    {
      return depModuleSlot;
    }

    final boolean inheritSlot = module.isInheritSlot();
    if (inheritSlot)
    {
      final String moduleSlot = module.getSlot();
      if (StringUtils.isNotBlank(moduleSlot))
      {
        return moduleSlot;
      }
    }

    final String defaultSlot = context.getDefaultSlot();
    return defaultSlot;
  }

  // CHECKSTYLE:OFF
  private void addStaticDependencies(final Module module,
      final Element dependenciesElement)
  {
    if (module.hasDependencies())
    {
      final List<de.smartics.maven.plugin.jboss.modules.Dependency> dependencies =
          module.getDependencies();
      for (final de.smartics.maven.plugin.jboss.modules.Dependency dependency : dependencies)
      {
        final Element moduleElement = new Element("module", NS);
        final String name = dependency.getName();
        if (StringUtils.isBlank(name))
        {
          continue;
        }
        moduleElement.setAttribute("name", name);

        final String slot = dependency.getSlot();
        if (StringUtils.isNotBlank(slot))
        {
          moduleElement.setAttribute("slot", slot);
        }
        if (context.isExportAll() || dependency.isExport())
        {
          moduleElement.setAttribute("export", "true");
        }
        final String services = dependency.getServices();
        if (StringUtils.isNotBlank(services))
        {
          moduleElement.setAttribute("services", services);
        }
        if (dependency.isOptional())
        {
          moduleElement.setAttribute("optional", "true");
        }
        dependenciesElement.addContent(moduleElement);
      }
    }
  }

  // CHECKSTYLE:ON

  private void addSortedDependencies(final Set<SortElement> sorted,
      final Module owningModule, final List<Dependency> dependencies)
  {
    for (final Dependency dependency : dependencies)
    {
      try
      {
        final Module module = context.getModule(dependency);
        final String name = module.getName();
        if (!name.equals(owningModule.getName()))
        {
          sorted.add(new SortElement(name, dependency));
        }
      }
      catch (final IllegalArgumentException e)
      {
        context.getLog().error(
            String.format("Skipping '%s' referenced from module '%s'.",
                dependency.getArtifact().getArtifactId(),
                owningModule.getName()));
      }
    }
  }

  // --- object basics --------------------------------------------------------

}
