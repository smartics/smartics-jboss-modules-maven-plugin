/*
 * Copyright 2013-2020 smartics, Kronseder & Reiner GmbH
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

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToDependencies;
import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToModule;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.DependenciesDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.DependenciesDescriptor.Builder;
import de.smartics.maven.plugin.jboss.modules.descriptor.Directives;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;

/**
 * The worker to do the parsing on a given document. It provides internal state
 * so that this has not to be passed around.
 */
final class ModulesDescriptorBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The name space of the documents parsed by this parser.
   */
  static final Namespace NS = Namespace
      .getNamespace("http://smartics.de/ns/jboss-modules-descriptor/1");

  // --- members --------------------------------------------------------------

  /**
   * The document to be parsed.
   */
  private final Document document;

  /**
   * The descriptor to be filled.
   */
  private final ModulesDescriptor modulesDescriptor;

  /**
   * The module descriptor currently under work.
   */
  private ModuleDescriptor.Builder builder;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   *
   * @param documentId the identifier of the document to be parsed. Used for
   *          error reporting mainly.
   * @throws NullPointerException if {@code documentId} is <code>null</code>.
   * @throws IllegalArgumentException if {@code documentId} is blank.
   */
  ModulesDescriptorBuilder(final String documentId, final Document document)
    throws NullPointerException, IllegalArgumentException
  {
    assert document != null : "Document must not be 'null'.";
    this.document = document;
    this.modulesDescriptor = new ModulesDescriptor(documentId);
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Starts the parsing of the document.
   *
   * @return the parsed information.
   */
  public ModulesDescriptor build()
  {
    final Element rootElement = document.getRootElement();

    final List<Element> moduleElements = rootElement.getChildren("module", NS);
    for (final Element moduleElement : moduleElements)
    {
      parseModule(moduleElement);
    }

    return modulesDescriptor;
  }

  private void parseModule(final Element moduleElement)
  {
    builder = new ModuleDescriptor.Builder();

    final String moduleName = moduleElement.getAttributeValue("name");
    final String slot = moduleElement.getAttributeValue("slot");
    builder.withName(moduleName);
    builder.withSlot(slot);

    parseDirectives(moduleElement.getChild("directives", NS));
    parseMatch(moduleElement.getChild("match", NS));
    parseApplyToDependencies(moduleElement
        .getChild("apply-to-dependencies", NS));
    parseApplyToModule(moduleElement.getChild("apply-to-module", NS));

    final ModuleDescriptor descriptor = builder.build();
    modulesDescriptor.addDescriptor(descriptor);
  }

  private void parseDirectives(final Element directivesElement)
  {
    if (directivesElement == null)
    {
      return;
    }

    final Directives.Builder builder = new Directives.Builder();
    final String skip = directivesElement.getChildText("skip", NS);
    builder.withSkip(skip);
    final String inheritSlot =
        directivesElement.getChildText("inherit-slot", NS);
    builder.withInheritSlot(inheritSlot);
    final Directives directives = builder.build();
    this.builder.with(directives);
  }

  private void parseMatch(final Element matchElement)
  {
    if (matchElement == null)
    {
      return;
    }

    final ArtifactMatcher.Builder builder = new ArtifactMatcher.Builder();

    new AbstractArtifactClusionAdder("includes", "include")
    {
      @Override
      public void add(final ArtifactClusion clusion)
      {
        builder.addInclude(clusion);
      }
    }.addClusions(matchElement);
    new AbstractArtifactClusionAdder("excludes", "exclude")
    {
      @Override
      public void add(final ArtifactClusion clusion)
      {
        builder.addExclude(clusion);
      }
    }.addClusions(matchElement);

    final ArtifactMatcher matcher = builder.build();
    this.builder.with(matcher);
  }

  private void parseApplyToDependencies(final Element applyToDependenciesElement)
  {
    if (applyToDependenciesElement == null)
    {
      return;
    }

    final ApplyToDependencies.Builder builder =
        new ApplyToDependencies.Builder();

    final List<Element> dependenciesElements =
        applyToDependenciesElement.getChildren("dependencies", NS);
    for (final Element dependenciesElement : dependenciesElements)
    {
      final DependenciesDescriptor.Builder dependencyBuilder =
          new DependenciesDescriptor.Builder();
      parseMatcher(dependencyBuilder, dependenciesElement);
      parseApply(dependencyBuilder, dependenciesElement);

      final DependenciesDescriptor dependencies = dependencyBuilder.build();
      builder.add(dependencies);
    }

    final ApplyToDependencies applyToDependencies = builder.build();
    this.builder.with(applyToDependencies);
  }

  private void parseMatcher(
      final DependenciesDescriptor.Builder dependencyBuilder,
      final Element dependenciesElement)
  {
    final Element matchElement = dependenciesElement.getChild("match", NS);
    if (matchElement == null)
    {
      return;
    }

    final ModuleMatcher.Builder builder = new ModuleMatcher.Builder();
    new AbstractModuleClusionAdder("includes", "include")
    {
      @Override
      public void add(final ModuleClusion clusion)
      {
        builder.addInclude(clusion);
      }
    }.addClusions(matchElement);
    new AbstractModuleClusionAdder("excludes", "exclude")
    {
      @Override
      public void add(final ModuleClusion clusion)
      {
        builder.addExclude(clusion);
      }
    }.addClusions(matchElement);

    final ModuleMatcher matcher = builder.build();
    dependencyBuilder.with(matcher);
  }

  private void parseApply(final Builder builder,
      final Element dependenciesElement)
  {
    final Element applyElement = dependenciesElement.getChild("apply", NS);
    if (applyElement == null)
    {
      return;
    }

    final String slot = applyElement.getChildText("slot", NS);
    final String export = applyElement.getChildText("export", NS);
    final String services = applyElement.getChildText("services", NS);
    final String optional = applyElement.getChildText("optional", NS);

    builder.withSlot(slot);
    builder.withExport(export);
    builder.withServices(services);
    builder.withOptional(optional);

    final XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
    final Element importElement = applyElement.getChild("imports", NS);
    if (importElement != null)
    {
      final String imports = outputter.outputString(importElement);
      builder.withImportsXml(imports);
    }
    final Element exportElement = applyElement.getChild("exports", NS);
    if (exportElement != null)
    {
      final String exports = outputter.outputString(exportElement);
      builder.withExportsXml(exports);
    }
  }

  private void parseApplyToModule(final Element applyToModuleElement)
  {
    if (applyToModuleElement == null)
    {
      return;
    }

    final ApplyToModule.Builder mBuilder = new ApplyToModule.Builder();

    final XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
    for (final Element child : applyToModuleElement.getChildren())
    {
      handleChild(mBuilder, outputter, child);
    }

    builder.with(mBuilder.build());
  }

  private void handleChild(final ApplyToModule.Builder mBuilder,
      final XMLOutputter outputter, final Element child)
  {
    final String elementName = child.getName();
    if ("dependencies".equals(elementName))
    {
      handleDependencies(mBuilder, outputter, child);
    }
    else if ("properties".equals(elementName))
    {
      for (final Element propertyElement : child.getChildren("property",
          NS))
      {
        final String name = propertyElement.getAttributeValue("name");
        final String fragment = outputter.outputString(propertyElement);
        mBuilder.addPropertyXml(name, fragment);
      }
    }
    else if ("exports".equals(elementName))
    {
      final String fragment = outputter.outputString(child);
      mBuilder.withExportsXml(fragment);
    }
    else if ("main-class".equals(elementName))
    {
      final String fragment = outputter.outputString(child);
      mBuilder.withMainClassXml(fragment);
    }
    else
    {
      // TODO warn or add to end?
    }
  }

  private void handleDependencies(final ApplyToModule.Builder mBuilder,
      final XMLOutputter outputter, final Element child)
  {
    int nonModuleCounter = 0;

    for (final Element childElement : child.getChildren())
    {
      final String childElementName = childElement.getName();
      final String name;
      if ("module".equals(childElementName))
      {
        name = childElement.getAttributeValue("name");
      }
      else
      {
        // i.e. system, maybe others.
        nonModuleCounter++;
        name = "non-module@" + nonModuleCounter;
      }
      final String fragment = outputter.outputString(childElement);
      mBuilder.addDependencyXml(name, fragment);
    }
  }

  // --- object basics --------------------------------------------------------

}
