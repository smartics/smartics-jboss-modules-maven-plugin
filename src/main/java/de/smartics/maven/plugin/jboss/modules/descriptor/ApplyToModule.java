/*
 * Copyright 2013-2014 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.descriptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Stores information that has to be applied to a module if the matcher matches
 * the name of a module.
 */
public final class ApplyToModule
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The main class information as XML fragment.
   */
  private String mainClassXml;

  /**
   * Matches the name of a property to its property specification in form of an
   * XML fragment. The fragment is read from module descriptors XML documents
   * and can be added to a JBoss <code>module.xml</code>.
   */
  private final Map<String, String> propertiesXml;

  /**
   * Matches the name of a module dependency (that is the name of a module) to
   * its dependency specification in form of an XML fragment. The fragment is
   * read from module descriptors XML documents and can be added to a JBoss
   * <code>module.xml</code>.
   */
  private final Map<String, String> dependenciesXml;

  /**
   * The exports information as XML fragment.
   */
  private String exportsXml;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ApplyToModule(final Builder builder)
  {
    mainClassXml = builder.mainClassXml;
    propertiesXml = builder.propertiesXml;
    dependenciesXml = builder.dependenciesXml;
    exportsXml = builder.exportsXml;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ApplyToModule}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The main class information as XML fragment.
     */
    private String mainClassXml;

    /**
     * Matches the name of a property to its property specification in form of
     * an XML fragment. The fragment is read from module descriptors XML
     * documents and can be added to a JBoss <code>module.xml</code>.
     */
    private final Map<String, String> propertiesXml =
        new LinkedHashMap<String, String>();

    /**
     * Matches the name of a module dependency (that is the name of a module) to
     * its dependency specification in form of an XML fragment. The fragment is
     * read from module descriptors XML documents and can be added to a JBoss
     * <code>modules.xml</code>.
     */
    private final Map<String, String> dependenciesXml =
        new LinkedHashMap<String, String>();

    /**
     * The exports information as XML fragment.
     */
    private String exportsXml;

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Adds the given main class XML fragment.
     *
     * @param fragment the XML fragment.
     * @return a reference to this builder.
     */
    public Builder withMainClassXml(final String fragment)
    {
      mainClassXml = fragment;
      return this;
    }

    /**
     * Adds the given property.
     *
     * @param name the name of the property.
     * @param fragment the XML fragment containing the property information.
     */
    public void addPropertyXml(final String name, final String fragment)
    {
      // TODO: Warn if element is already stored?
      propertiesXml.put(name, fragment);
    }

    /**
     * Adds the given module dependency.
     *
     * @param moduleName the name of the module that is the dependency.
     * @param xmlFragment the XML fragment containing the dependency
     *          information.
     */
    public void addDependencyXml(final String moduleName,
        final String xmlFragment)
    {
      // TODO: Warn if element is already stored?
      dependenciesXml.put(moduleName, xmlFragment);
    }

    /**
     * Adds the given exports XML fragment.
     *
     * @param fragment the XML fragment.
     * @return a reference to this builder.
     */
    public Builder withExportsXml(final String fragment)
    {
      exportsXml = fragment;
      return this;
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds an instance of {@link ApplyToModule}.
     *
     * @return the instance.
     */
    public ApplyToModule build()
    {
      return new ApplyToModule(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the main class XML fragment.
   *
   * @return the main class XML fragment.
   */
  public String getMainClassXml()
  {
    return mainClassXml;
  }

  /**
   * Returns the list of property XML fragments.
   *
   * @return the list of property XML fragments.
   */
  public List<String> getPropertiesXml()
  {
    return new ArrayList<String>(propertiesXml.values());
  }

  /**
   * Returns the list of dependency XML fragments.
   *
   * @return the list of dependency XML fragments.
   */
  public List<String> getDependenciesXml()
  {
    return new ArrayList<String>(dependenciesXml.values());
  }

  /**
   * Returns the exports XML fragment.
   *
   * @return the exports XML fragment.
   */
  public String getExportsXml()
  {
    return exportsXml;
  }

  // --- business -------------------------------------------------------------

  /**
   * Merges the given instance into this instance.
   *
   * @param applyToModule the instance to merge into this instance.
   */
  public void merge(final ApplyToModule applyToModule)
  {
    mergeMainClass(applyToModule);
    mergeExports(applyToModule);
    merge("properties", propertiesXml, applyToModule.propertiesXml);
    merge("dependencies", dependenciesXml, applyToModule.dependenciesXml);
  }

  private void mergeMainClass(final ApplyToModule applyToModule)
  {
    if (mainClassXml != null && applyToModule.mainClassXml != null
        && !mainClassXml.equals(applyToModule.mainClassXml))
    {
      throw new IllegalStateException(String.format(
          "Cannot merge differen main class information: %s differs from %s.",
          mainClassXml, applyToModule.mainClassXml));
    }
    if (mainClassXml == null)
    {
      mainClassXml = applyToModule.mainClassXml;
    }
  }

  private void mergeExports(final ApplyToModule applyToModule)
  {
    if (exportsXml != null && applyToModule.exportsXml != null
        && !exportsXml.equals(applyToModule.exportsXml))
    {
      throw new IllegalStateException(String.format(
          "Cannot merge differen main class information: %s differs from %s.",
          exportsXml, applyToModule.exportsXml));
    }
    if (exportsXml == null)
    {
      exportsXml = applyToModule.exportsXml;
    }
  }

  private void merge(final String id, final Map<String, String> targetMap,
      final Map<String, String> sourceAap)
  {
    for (final Entry<String, String> entry : sourceAap.entrySet())
    {
      final String name = entry.getKey();
      final String xml = entry.getValue();
      final String storedXml = targetMap.get(name);
      if (storedXml == null)
      {
        targetMap.put(name, xml);
      }
      else
      {
        if (!storedXml.equals(xml))
        {
          // TODO: warn duplication, use id
        }
      }
    }
  }

  // --- object basics --------------------------------------------------------

  /**
   * {@inheritDoc}
   * <p>
   * Provides the properties via reflection for displaying debug information.
   * </p>
   */
  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
