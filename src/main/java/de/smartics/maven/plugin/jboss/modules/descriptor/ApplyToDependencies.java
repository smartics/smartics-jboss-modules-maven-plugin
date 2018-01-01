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
package de.smartics.maven.plugin.jboss.modules.descriptor;


import de.smartics.maven.plugin.jboss.modules.util.Arg;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;

import java.util.Collections;

/**
 * Stores information that has to be applied to dependencies if the matcher
 * matches the name of a module.
 */
public final class ApplyToDependencies
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The descriptors of dependencies to match modules names to have their
   * information applied.
   */
  private final Set<DependenciesDescriptor> descriptors;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  private ApplyToDependencies(final Builder builder)
  {
    descriptors = builder.descriptors;
  }

  // ****************************** Inner Classes *****************************

  /**
   * Builds instances of {@link ApplyToDependencies}.
   */
  public static final class Builder
  {
    // ******************************** Fields ********************************

    // --- constants ----------------------------------------------------------

    // --- members ------------------------------------------------------------

    /**
     * The descriptors of dependencies to match modules names to have their
     * information applied.
     */
    private final Set<DependenciesDescriptor> descriptors =
        new HashSet<DependenciesDescriptor>();

    // ***************************** Initializer ******************************

    // ***************************** Constructors *****************************

    // ***************************** Inner Classes ****************************

    // ******************************** Methods *******************************

    // --- init ---------------------------------------------------------------

    // --- get&set ------------------------------------------------------------

    /**
     * Adds the given descriptor to the list of descriptors.
     *
     * @param descriptor the descriptor to add.
     * @throws NullPointerException if {@code descriptor} is <code>null</code>.
     */
    public void add(final DependenciesDescriptor descriptor)
      throws NullPointerException
    {
      descriptors.add(Arg.checkNotNull("descriptor", descriptor));
    }

    // --- business -----------------------------------------------------------

    /**
     * Builds an instance of {@link ApplyToDependencies}.
     *
     * @return the instance.
     */
    public ApplyToDependencies build()
    {
      return new ApplyToDependencies(this);
    }

    // --- object basics ------------------------------------------------------
  }

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the descriptors of dependencies to match modules names to have
   * their information applied.
   *
   * @return the descriptors of dependencies to match modules names to have
   *         their information applied.
   */
  @SuppressWarnings("unchecked")
  public Set<DependenciesDescriptor> getDescriptors()
  {
    return Collections.unmodifiableSet(descriptors);
  }

  /**
   * Creates a merged descriptor from all descriptors that matches the given
   * module name.
   *
   * @param name the name of a module to match.
   * @return the merged descriptor.
   */
  public DependenciesDescriptor getDescriptorThatMatches(final String name)
  {
    final DependenciesDescriptor.Builder builder =
        new DependenciesDescriptor.Builder();

    for (final DependenciesDescriptor descriptor : descriptors)
    {
      if (descriptor.matches(name))
      {
        builder.merge(name, descriptor);
      }
    }

    final DependenciesDescriptor descriptor = builder.build();
    return descriptor;
  }

  // --- business -------------------------------------------------------------

  /**
   * Merges the given {@code applyToDependencies} instance into this instance.
   *
   * @param applyToDependencies the applyToDependencies to merge into this
   *          instance.
   */
  public void merge(final ApplyToDependencies applyToDependencies)
  {
    descriptors.addAll(applyToDependencies.descriptors);
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
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, null);
  }
}
