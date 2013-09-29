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

import java.io.InputStream;
import java.util.List;

import org.jdom2.Namespace;

import com.thoughtworks.xstream.XStream;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.Dependency;
import de.smartics.maven.plugin.jboss.modules.Export;
import de.smartics.maven.plugin.jboss.modules.Module;
import de.smartics.maven.plugin.jboss.modules.Services;

/**
 * Parses XML documents of type {@link NS}.
 */
public final class ModulesParser
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The namespace <code>http://smartics.de/jboss-modules-descriptor</code>.
   */
  public static final Namespace NS = Namespace
      .getNamespace("http://smartics.de/jboss-modules-descriptor");

  // --- members --------------------------------------------------------------

  /**
   * The XStream parser.
   */
  private final XStream xstream;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public ModulesParser()
  {
    this.xstream = new XStream();
    xstream.alias("modules", ModuleDescritors.class);
    xstream.alias("module", Module.class);
    xstream.alias("include", Clusion.class);
    xstream.alias("exclude", Clusion.class);
    xstream.alias("dependency", Dependency.class);
    xstream.alias("export", Export.class);
    xstream.alias("port", Services.class);

    xstream.addImplicitCollection(ModuleDescritors.class, "modules");
    xstream.aliasField("services", Module.class, "port");

    xstream.registerConverter(new ExportConverter());
    xstream.registerConverter(new ServicesConverter());
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  /**
   * Parses the given {@code input} and adds all modules parsed to the given
   * {@code modules}.
   *
   * @param modules the list to add the modules parsed.
   * @param input the stream to parse.
   * @param systemId the system identifier of the stream.
   */
  public void parse(final List<Module> modules, final InputStream input,
      final String systemId)
  {
    final ModuleDescritors moduleDescriptors =
        (ModuleDescritors) xstream.fromXML(input);
    final List<Module> current = moduleDescriptors.getModules();
    modules.addAll(current);
  }

  // --- object basics --------------------------------------------------------

}
