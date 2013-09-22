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
package help.de.smartics.maven.plugin.jboss.modules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.Module;

/**
 * Builds test instances of {@link Module}.
 */
public final class ModuleBuilder
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * The default name found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_NAME = "de.smartics.test";

  /**
   * The default slot found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  private static final String DEFAULT_SLOT = "test";

  /**
   * The default property key found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_PROPERTY_KEY = "test";

  /**
   * The default property value found in instances of this builder.
   * <p>
   * The value of this constant is {@value}.
   * </p>
   */
  public static final String DEFAULT_PROPERTY_VALUE = "testValue";

  // --- members --------------------------------------------------------------

  /**
   * The name of the module. Is used for the <code>name</code> attribute in the
   * <code>module.xml</code> base element.
   */
  private String name;

  /**
   * The slot to write to. If empty, the default slot is provided in the
   * {@link de.smartics.maven.plugin.jboss.modules.JBossModulesArchiveMojo#defaultSlot
   * defaultSlot} configuration of the Mojo.
   */
  private String slot;

  /**
   * The list of inclusions.
   */
  private List<Clusion> includes;

  /**
   * The list of exclusions.
   */
  private List<Clusion> excludes;

  /**
   * The map of properties to add to the <code>module.xml</code>.
   */
  private Map<String, String> properties;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ModuleBuilder()
  {
  }

  private ModuleBuilder(final ModuleBuilder moduleBuilder)
  {
    this.name = moduleBuilder.name;
    this.slot = moduleBuilder.slot;
    this.properties = moduleBuilder.properties;
    this.includes = moduleBuilder.includes;
    this.excludes = moduleBuilder.excludes;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the name of the module. Is used for the <code>name</code> attribute
   * in the <code>module.xml</code> base element.
   *
   * @return the name of the module.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the module. Is used for the <code>name</code> attribute in
   * the <code>module.xml</code> base element.
   *
   * @param name the name of the module.
   */
  public ModuleBuilder withName(final String name)
  {
    this.name = name;
    return this;
  }

  /**
   * Sets the slot to write to. If empty, the default slot is provided in the
   * {@link de.smartics.maven.plugin.jboss.modules.JBossModulesArchiveMojo#defaultSlot
   * defaultSlot} configuration of the Mojo.
   *
   * @param slot the slot to write to.
   */
  public ModuleBuilder withSlot(final String slot)
  {
    this.slot = slot;
    return this;
  }

  /**
   * Sets the list of inclusions.
   *
   * @param includes the list of inclusions.
   */
  public ModuleBuilder withIncludes(final List<Clusion> includes)
  {
    this.includes = includes;
    return this;
  }

  /**
   * Sets the list of exclusions.
   *
   * @param excludes the list of exclusions.
   */
  public ModuleBuilder withExcludes(final List<Clusion> excludes)
  {
    this.excludes = excludes;
    return this;
  }

  /**
   * Sets the map of properties to add to the <code>module. xml</code>.
   *
   * @param properties the map of properties to add to the
   *          <code>module.xml</code>.
   */
  public ModuleBuilder withProperties(final Map<String, String> properties)
  {
    this.properties = properties;
    return this;
  }

  // --- business -------------------------------------------------------------

  /**
   * Builds an empty instance of {@link ModuleBuilder}.
   *
   * @return the created instance.
   */
  public static ModuleBuilder a()
  {
    final ModuleBuilder builder = new ModuleBuilder();
    return builder;
  }

  /**
   * Builds a default instance of {@link ModuleBuilder}.
   *
   * @return the created instance.
   */
  public static ModuleBuilder aDefault()
  {
    final ModuleBuilder builder = a();

    builder.withName(DEFAULT_NAME);
    builder.withSlot(DEFAULT_SLOT);

    final Map<String, String> properties = new HashMap<String, String>();
    properties.put(DEFAULT_PROPERTY_KEY, DEFAULT_PROPERTY_VALUE);
    builder.withProperties(properties);

    final ClusionBuilder clusionBuilder = ClusionBuilder.a();
    builder.withIncludes(Arrays.asList(clusionBuilder.build()));
    builder.withIncludes(Arrays.asList(clusionBuilder.withArtifactId(
        ClusionBuilder.ALT_ARTIFACT_ID).build()));

    return builder;
  }

  public ModuleBuilder but()
  {
    return new ModuleBuilder(this);
  }

  /**
   * Builds an instance of {@link Module}.
   *
   * @return the created instance.
   */
  public Module build()
  {
    final Module instance = new Module();

    instance.setName(name);
    instance.setSlot(slot);
    instance.setProperties(properties);
    instance.setIncludes(includes);
    instance.setExcludes(excludes);

    return instance;
  }

  // --- object basics --------------------------------------------------------

}
