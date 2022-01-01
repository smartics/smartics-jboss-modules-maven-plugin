/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
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
package de.smartics.maven.plugin.jboss.modules.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.eclipse.aether.artifact.Artifact;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * The naming strategy for module slots.
 */
public enum SlotStrategy
{
  // ***************************** Enumeration ******************************

  // ******************************** Fields ********************************

  // --- constants ----------------------------------------------------------

  /**
   * The module is set to the main slot (default).
   */
  MAIN("main"),

  /**
   * The module is set to the major artifact version slot.
   */
  VERSION_MAJOR("version-major");

  /**
   * The main slot.
   */
  public static final String MAIN_SLOT = "main";

  // --- members ------------------------------------------------------------

  /**
   * The identifier of the strategy.
   */
  private String id;

  // ***************************** Constructors *****************************

  private SlotStrategy(final String id)
  {
    this.id = id;
  }

  // ******************************** Methods *******************************

  // --- init ---------------------------------------------------------------

  // --- get&set ------------------------------------------------------------

  // --- business -----------------------------------------------------------

  /**
   * Returns the slot strategy identified by the given {@code id}.
   *
   * @param id the identifier of the requested slot strategy.
   * @return the requested slot strategy.
   * @throws IllegalArgumentException if {@code id} is not a valid slot
   *           strategy.
   */
  public static SlotStrategy fromString(final String id)
    throws IllegalArgumentException
  {
    for (final SlotStrategy strategy : values())
    {
      if (id.equals(strategy.id))
      {
        return strategy;
      }
    }

    throw new IllegalArgumentException(String.format(
        "Invalid slot strategy '%s'. Allowed values are: %s", id,
        Arrays.toString(values())));
  }

  // --- object basics ------------------------------------------------------

  @Override
  public String toString()
  {
    return id;
  }

  /**
   * Calculates the name for the slot.
   *
   * @param artifact the artifact with additional information. If
   *          <code>null</code>: a static prefix will be assumed.
   * @param defaultSlot the name of the default slot to use.
   * @return the name of the slot.
   */
  public String calcSlot(final Artifact artifact, final String defaultSlot)
  {
    if (this == VERSION_MAJOR)
    {
      final String versionString = calcVersion(artifact);
      final ArtifactVersion version = new DefaultArtifactVersion(versionString);
      final int majorVersion = version.getMajorVersion();
      final String slot;
      if (!(StringUtils.isBlank(defaultSlot) || MAIN_SLOT.equals(defaultSlot)))
      {
        slot = defaultSlot + majorVersion;
      }
      else
      {
        slot = String.valueOf(majorVersion);
      }
      return slot;
    }

    return StringUtils.isBlank(defaultSlot) ? MAIN_SLOT : defaultSlot;
  }

  /**
   * Calculates the name for the slot.
   *
   * @param defaultSlot the name of the default slot. May be blank.
   * @param moduleSlot the name of the module slot. May be blank.
   * @param artifact the artifact with additional information. If
   *          <code>null</code>: a static prefix will be assumed.
   * @return the name of the slot.
   */
  public String calcSlot(final String defaultSlot, final String moduleSlot,
      final Artifact artifact)
  {
    final String fallBackSlot =
        StringUtils.isBlank(moduleSlot) ? defaultSlot : moduleSlot;
    final String slot = calcSlot(artifact, fallBackSlot);
    return slot;
  }

  private String calcVersion(final Artifact artifact)
  {
    if (artifact != null)
    {
      return artifact.getVersion();
    }
    else
    {
      return "VersionX";
    }
  }
}
