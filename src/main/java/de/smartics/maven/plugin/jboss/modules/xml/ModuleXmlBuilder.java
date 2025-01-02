/*
 * Copyright 2013-2025 smartics, Kronseder & Reiner GmbH
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToDependencies;
import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToModule;
import de.smartics.maven.plugin.jboss.modules.descriptor.DependenciesDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.domain.ExecutionContext;
import de.smartics.maven.plugin.jboss.modules.domain.SlotStrategy;
import de.smartics.maven.plugin.jboss.modules.util.XmlUtils;
import java.util.Collections;

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
  public static final Namespace MODULE_NS_1_1;
  public static final String MODULE_NS_1_1_URI = "urn:jboss:module:1.1";

  static {
    MODULE_NS_1_1 = Namespace.getNamespace(MODULE_NS_1_1_URI);
  }

  // --- members --------------------------------------------------------------

  /**
   * The context and configuration to control the building of XML files.
   */
  private final ExecutionContext context;

  /**
   * The module to build.
   */
  private final ModuleDescriptor module;

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

  /**
   * A helper class to parse XML fragments.
   */
  private final XmlFragmentParser xmlFragmentParser = new XmlFragmentParser();

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
  public ModuleXmlBuilder(final ExecutionContext context,
      final ModuleDescriptor module, final Collection<Dependency> dependencies)
  {
    this.context = context;
    this.module = module;
    this.dependencies = dependencies;

    root = new Element("module", context.getTargetNamespace());
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

    /**
     * Returns the hash code of the object.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode()
    {
      return ObjectUtils.hashCode(key);
    }

    /**
     * Returns <code>true</code> if the given object is semantically equal to
     * the given object, <code>false</code> otherwise.
     *
     * @param object the instance to compare to.
     * @return <code>true</code> if the given object is semantically equal to
     *         the given object, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object object)
    {
      if (this == object)
      {
        return true;
      }
      else if (object == null || getClass() != object.getClass())
      {
        return false;
      }

      final ModuleXmlBuilder.SortElement other =
          (ModuleXmlBuilder.SortElement) object;

      return ObjectUtils.equals(key, other.key);
    }

    @Override
    public int compareTo(final SortElement o)
    {
      return key.compareTo(o.key);
    }

    @Override
    public String toString()
    {
      return String.valueOf(key) + ": " + String.valueOf(dependency);
    }
  }

  private static final class ModuleDependencyElement
  {
    private final Element moduleElement;
    private final String  moduleName;

    public ModuleDependencyElement(final Element moduleElement)
    {
      this.moduleElement = moduleElement;
      this.moduleName = moduleElement.getAttributeValue("name");
    }

    protected Element getModuleElement() {
      return this.moduleElement;
    }

    @Override
    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }

      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      ModuleDependencyElement that = (ModuleDependencyElement) o;

      return ObjectUtils.equals(this.moduleName, that.moduleName);
    }

    @Override
    public int hashCode()
    {
      return ObjectUtils.hashCode(this.moduleName);
    }
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  private static String calcSlot(final ExecutionContext context,
      final ModuleDescriptor module, final Collection<Dependency> dependencies)
  {
    final SlotStrategy strategy = context.getSlotStrategy();
    final String moduleSlot = module.getSlot();
    final String defaultSlot = context.getDefaultSlot();
    final Artifact artifact = calcArtifact(dependencies);
    final String slot = strategy.calcSlot(defaultSlot, moduleSlot, artifact);
    return slot;
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
    addMainClass(module);
    addProperties(module);
    addResources(module, dependencies);
    addDependencies(module, dependencies);
    addExports(module);

    return document;
  }

  private void addMainClass(final ModuleDescriptor module)
  {
    final String xml = module.getApplyToModule().getMainClassXml();
    if (xml != null)
    {
      final Element element = adopt(xml);
      root.addContent(element);
    }
  }

  private void addProperties(final ModuleDescriptor module)
  {
    final List<String> xmls = module.getApplyToModule().getPropertiesXml();
    if (xmls.isEmpty())
    {
      return;
    }

    final Element propertiesElement = new Element("properties", context.getTargetNamespace());
    for (final String xml : xmls)
    {
      final Element element = adopt(xml);
      propertiesElement.addContent(element);
    }
    root.addContent(propertiesElement);
  }

  private void addResources(ModuleDescriptor module, final Collection<Dependency> dependencies)
  {
    final Element resources = new Element("resources", context.getTargetNamespace());

    List<String> resourceRootsXml = module.getApplyToModule().getResourceRootsXml();
    for (final String xml : resourceRootsXml)
    {
      final Element element = adopt(xml);
      resources.addContent(element);
    }

    if (!dependencies.isEmpty())
    {

      final List<SortElement> sorted = createSortedResources(dependencies);
      for (final SortElement element : sorted)
      {
        final Artifact depart = element.dependency.getArtifact();
        if (context.isGenerateFeaturePackDefinition())
        {
            final Element artifact = new Element("artifact", context.getTargetNamespace());
            artifact.setAttribute("name", "${" + depart.getGroupId() + ":" + depart.getArtifactId() + "}");

            String filter = module.getMatcher().findFilter(depart);
            if (filter != null) {
              final Element filterElement = adopt(filter);
              artifact.addContent(filterElement);
            }

            resources.addContent(artifact);
        } else {
            final Element resource = new Element("resource-root", context.getTargetNamespace());
            final String fileName = depart.getFile().getName();
            resource.setAttribute("path", fileName);

            String filter = module.getMatcher().findFilter(depart);
            if (filter != null) {
              final Element filterElement = adopt(filter);
              resource.addContent(filterElement);
            }
            resources.addContent(resource);
        }
      }
    }

    if( !resources.getChildren().isEmpty() ) {
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
        final String sortKey = file.getName() + ":"+ dependency.getArtifact().getGroupId();
        sorted.add(new SortElement(sortKey, dependency));
      }
    }
    Collections.sort(sorted);
    return sorted;
  }

  private void addDependencies(final ModuleDescriptor module,
      final Collection<Dependency> dependencies)
  {
    final ApplyToModule applyToModule = module.getApplyToModule();
    final List<String> staticDependencies = applyToModule.getDependenciesXml();
    if (!(dependencies.isEmpty() && staticDependencies.isEmpty()))
    {
      final Element dependenciesElement = new Element("dependencies", context.getTargetNamespace());

      final List<ModuleDependencyElement> staticDependencyElements = getStaticDependencyElements(staticDependencies);
      final List<ModuleDependencyElement> resolvedDependencyElements = getResolvedDependencyElements(module, dependencies);
      final List<ModuleDependencyElement> combinedDependencies = new ArrayList<ModuleDependencyElement>();

      // Make static module dependencies take precedence over resolved dependencies with the same module name
      resolvedDependencyElements.removeAll(staticDependencyElements);
      combinedDependencies.addAll(staticDependencyElements);
      combinedDependencies.addAll(resolvedDependencyElements);

      // Add <module> content to <dependencies> element
      for(ModuleDependencyElement element : combinedDependencies)
      {
        dependenciesElement.addContent(element.getModuleElement());
      }

      root.addContent(dependenciesElement);
    }
  }

  private List<ModuleDependencyElement> getResolvedDependencyElements(final ModuleDescriptor module,
      final Collection<Dependency> dependencies)
  {
    final Set<SortElement> sorted =
        createSortedDependencies(module, dependencies);

    final ApplyToDependencies apply = module.getApplyToDependencies();

    final List<ModuleDependencyElement> moduleDependencyElements = new ArrayList<ModuleDependencyElement>();

    for (final SortElement element : sorted)
    {
      final String name = element.key;
      final Element moduleElement = new Element("module", context.getTargetNamespace());
      moduleElement.setAttribute("name", name);

      final DependenciesDescriptor dd = apply.getDescriptorThatMatches(name);

      if(isIncludableDependency(element, dd))
      {
        handleOptional(element, moduleElement, dd);
        handleExport(moduleElement, dd);
        handleServices(moduleElement, dd);
        handleSlot(module, element, moduleElement);
        moduleDependencyElements.add(new ModuleDependencyElement(moduleElement));
      }
    }

    return moduleDependencyElements;
  }

  private boolean isIncludableDependency(final SortElement element, final DependenciesDescriptor dd)
  {
    /*
     * A dependency is considered NOT valid for inclusion within a module if:
     *   - The dependency is flagged as 'skipped'
     *   - The dependency is optional and the ignoreOptionalDependencies property is true
     */

    boolean isSkipped = dd.getSkip() != null && dd.getSkip() == true;
    boolean isOptional = (dd.getOptional() != null && dd.getOptional() == true) || element.dependency.isOptional();

    if(isSkipped) {
      return false;
    }

    if(isOptional && context.isIgnoreOptionalDependencies()) {
      return false;
    }

    return true;
  }

  private void handleOptional(final SortElement element,
      final Element moduleElement, final DependenciesDescriptor dd)
  {
    final Boolean ddOptional = dd.getOptional();
    if ((ddOptional != null && ddOptional)
        || (ddOptional == null || element.dependency.isOptional()))
    {
      moduleElement.setAttribute("optional", "true");
    }
  }

  private void handleExport(final Element moduleElement,
      final DependenciesDescriptor dd)
  {
    final Boolean ddExport = dd.getExport();
    if (ddExport != null && ddExport)
    {
      moduleElement.setAttribute("export", "true");
    }
  }

  private void handleServices(final Element moduleElement,
      final DependenciesDescriptor dd)
  {
    final String services = dd.getServices();
    if (services != null && !"none".equals(services))
    {
      moduleElement.setAttribute("services", services);
    }
  }

  private void handleSlot(final ModuleDescriptor module,
      final SortElement element, final Element moduleElement)
  {
    final SlotStrategy slotStrategy = context.getSlotStrategy();
    final Dependency dependency = element.dependency;
    final String defaultSlot = calcDefaultSlot(module, dependency);
    final String slot =
        slotStrategy.calcSlot(dependency.getArtifact(), defaultSlot);
    if (!SlotStrategy.MAIN_SLOT.equals(slot))
    {
      moduleElement.setAttribute("slot", slot);
    }
  }

  private Set<SortElement> createSortedDependencies(
      final ModuleDescriptor module, final Collection<Dependency> dependencies)
  {
    final Set<SortElement> sorted = new TreeSet<SortElement>();
    for (final Dependency dependency : dependencies)
    {
      final List<Dependency> resolvedDependencies = context.resolve(dependency);
      addSortedDependencies(sorted, module, resolvedDependencies);
    }
    return sorted;
  }

  private String calcDefaultSlot(final ModuleDescriptor module,
      final Dependency dependency)
  {
    final ModuleDescriptor depModule = context.getModule(dependency);
    final String depModuleSlot = depModule.getSlot();
    if (StringUtils.isNotBlank(depModuleSlot))
    {
      return depModuleSlot;
    }

    final boolean inheritSlot = module.getDirectives().getInheritSlot();
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
  private List<ModuleDependencyElement> getStaticDependencyElements(final List<String> staticDependencies)
  {
    final List<ModuleDependencyElement> moduleElements = new ArrayList<ModuleDependencyElement>();

    if (!staticDependencies.isEmpty())
    {
      for (final String xml : staticDependencies)
      {
        final ModuleDependencyElement element = new ModuleDependencyElement(adopt(xml));
        moduleElements.add(element);
      }
    }

    return moduleElements;
  }

  // CHECKSTYLE:ON

  private void addSortedDependencies(final Set<SortElement> sorted,
      final ModuleDescriptor owningModule, final List<Dependency> dependencies)
  {
    for (final Dependency dependency : dependencies)
    {
      try
      {
        final ModuleDescriptor module = context.getModule(dependency);
        final String name = module.getName();
        if (!name.equals(owningModule.getName()))
        {
          /*
           * It's possible for a module to have resources that share the same dependencies. Potentially these
           * dependencies could be declared differently in their respective POM files. E.g one resource may specify the
           * dependency as optional and another resource may declare it as being mandatory.
           *
           * In this scenario, always assume that the dependency should be mandatory.
           */
          final SortElement e = new SortElement(name, dependency);
          if(sorted.contains(e))
          {
            final Iterator<SortElement> iter = sorted.iterator();
            while (iter.hasNext())
            {
              final SortElement current = iter.next();

              // We are processing a non-optional dependency that has already been added to the set as optional
              if(current.equals(e) && current.dependency.isOptional() && !dependency.isOptional())
              {
                // Remove so that it can be replaced later with a non-optional dependency
                sorted.remove(current);
                break;
              }
            }
          }
          sorted.add(e);
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

  private void addExports(final ModuleDescriptor module2)
  {
    final String xml = module.getApplyToModule().getExportsXml();
    if (xml != null)
    {
      final Element element = adopt(xml);
      root.addContent(element);
    }
  }

  private Element adopt(String xml) {
    final Element element = xmlFragmentParser.parse(xml);
    XmlUtils.adjustNamespaces(element, context.getTargetNamespace());
    return element;
  }

  // --- object basics --------------------------------------------------------

}
