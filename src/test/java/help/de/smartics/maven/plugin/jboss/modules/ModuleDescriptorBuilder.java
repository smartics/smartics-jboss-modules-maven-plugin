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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToDependencies;
import de.smartics.maven.plugin.jboss.modules.descriptor.ApplyToModule;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactMatcher;
import de.smartics.maven.plugin.jboss.modules.descriptor.ArtifactClusion;
import de.smartics.maven.plugin.jboss.modules.descriptor.Directives;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor;
import de.smartics.maven.plugin.jboss.modules.descriptor.ModuleDescriptor.Builder;

/**
 * Builds test instances of {@link ModuleDescriptor} supporting default module
 * descriptors to be tweaked for unit tests.
 */
public final class ModuleDescriptorBuilder
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
   * The delegate builder.
   */
  private final ModuleDescriptor.Builder builder =
      new ModuleDescriptor.Builder();

  private final ApplyToModule.Builder atmBuilder = new ApplyToModule.Builder();

  private final ArtifactMatcher.Builder matcherBuilder =
      new ArtifactMatcher.Builder();

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ModuleDescriptorBuilder()
  {
  }

  private ModuleDescriptorBuilder(final ModuleDescriptorBuilder moduleBuilder)
  {
    this.builder.withName(moduleBuilder.builder.getName());
    this.builder.withSlot(moduleBuilder.builder.getSlot());
    this.builder.with(moduleBuilder.builder.getDirectives());
    this.builder.with(moduleBuilder.builder.getMatcher());
    this.builder.with(moduleBuilder.builder.getApplyToDependencies());
    this.builder.with(moduleBuilder.builder.getApplyToModule());
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
    return builder.getName();
  }

  /**
   * Sets the name of the module. Is used for the <code>name</code> attribute in
   * the <code>module.xml</code> base element.
   *
   * @param name the name of the module.
   */
  public ModuleDescriptorBuilder withName(final String name)
  {
    builder.withName(name);
    return this;
  }

  /**
   * Sets the slot to write to. If empty, the default slot is provided in the
   * {@link de.smartics.maven.plugin.jboss.modules.JBossModulesArchiveMojo#defaultSlot
   * defaultSlot} configuration of the Mojo.
   *
   * @param slot the slot to write to.
   */
  public ModuleDescriptorBuilder withSlot(final String slot)
  {
    builder.withSlot(slot);
    return this;
  }

  public Builder with(final Directives directives)
  {
    return builder.with(directives);
  }

  public Builder with(final ArtifactMatcher matcher)
  {
    return builder.with(matcher);
  }

  public Builder with(final ApplyToDependencies applyToDependencies)
  {
    return builder.with(applyToDependencies);
  }

  public Builder with(final ApplyToModule applyToModule)
  {
    return builder.with(applyToModule);
  }

  public String getSlot()
  {
    return builder.getSlot();
  }

  public Directives getDirectives()
  {
    return builder.getDirectives();
  }

  public ArtifactMatcher getMatcher()
  {
    return builder.getMatcher();
  }

  public ApplyToDependencies getApplyToDependencies()
  {
    return builder.getApplyToDependencies();
  }

  public ApplyToModule getApplyToModule()
  {
    return builder.getApplyToModule();
  }

  // --- business -------------------------------------------------------------

  /**
   * Builds an empty instance of {@link ModuleDescriptorBuilder}.
   *
   * @return the created instance.
   */
  public static ModuleDescriptorBuilder a()
  {
    final ModuleDescriptorBuilder builder = new ModuleDescriptorBuilder();
    return builder;
  }

  /**
   * Builds a default instance of {@link ModuleDescriptorBuilder}.
   *
   * @return the created instance.
   */
  public static ModuleDescriptorBuilder aDefault()
  {
    final ModuleDescriptorBuilder builder = a();

    builder.withName(DEFAULT_NAME);
    builder.withSlot(DEFAULT_SLOT);

    final Map<String, String> properties = new HashMap<String, String>();
    properties.put(DEFAULT_PROPERTY_KEY, DEFAULT_PROPERTY_VALUE);
    builder.with(properties);

    final ClusionBuilder clusionBuilder = ClusionBuilder.a();
    builder.withInclude(clusionBuilder.build());
    builder.withInclude(clusionBuilder.withArtifactId(
        ClusionBuilder.ALT_ARTIFACT_ID).build());

    return builder;
  }

  public void with(final Map<String, String> properties)
  {
    for (final Entry<String, String> property : properties.entrySet())
    {
      final String name = property.getKey();
      final String value = property.getValue();

      final String fragment =
          "<property name=\"" + name + "\" value=\"" + value + "\" />";
      atmBuilder.addPropertyXml(name, fragment);
    }
  }

  public void withInclude(final ArtifactClusion include)
  {
    matcherBuilder.addInclude(include);
  }

  public void withExclude(final ArtifactClusion exclude)
  {
    matcherBuilder.addExclude(exclude);
  }

  public ModuleDescriptorBuilder but()
  {
    return new ModuleDescriptorBuilder(this);
  }

  /**
   * Builds an instance of {@link ModuleDescriptor}.
   *
   * @return the created instance.
   */
  public ModuleDescriptor build()
  {
    builder.with(atmBuilder.build());
    builder.with(matcherBuilder.build());

    return builder.build();
  }

  // --- object basics --------------------------------------------------------

}
