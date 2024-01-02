/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

//import de.smartics.testdoc.annotations.Uut;

/**
 * Tests {@link ModuleMap#createName(String, String)}.
 */
//@Uut(type = ModuleMap.class, method = "createName(String, String)")
public class ModuleMapCreateNameTest
{

  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  /**
   * Dummy string. Not relevant in the test.
   */
  private static final String DUMMY = "dummy";

  // --- members --------------------------------------------------------------

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  // --- helper ---------------------------------------------------------------

  @Test(expected = NullPointerException.class)
  public void groupIdMustNotBeNull()
  {
    ModuleMap.createName(null, DUMMY);
  }

  @Test(expected = NullPointerException.class)
  public void artifactIdMustNotBeNull()
  {
    ModuleMap.createName(DUMMY, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void groupIdMustNotBeBlank()
  {
    ModuleMap.createName("", DUMMY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void artifactIdMustNotBeBlank()
  {
    ModuleMap.createName(DUMMY, "");
  }

  @Test
  public void ifGroupIdIsEqualToTheArtifactIdTheNameIsTheGroupId()
  {
    final String id = "commons-test";
    final String name = ModuleMap.createName(id, id);
    assertThat(name, is(equalTo(id)));
  }

  @Test
  public void ifGroupIdEndsWithTheArtifactIdTheNameIsTheGroupId()
  {
    final String groupId = "de.smartics.test.commons-test";
    final String artifactId = "commons-test";
    final String name = ModuleMap.createName(groupId, artifactId);
    assertThat(name, is(equalTo(groupId)));
  }

  @Test
  public void ifArtifactIdAndGroupIdArDifferentTheArtifactIdIsAppended()
  {
    final String groupId = "de.smartics.test";
    final String artifactId = "commons-test";
    final String name = ModuleMap.createName(groupId, artifactId);
    final String expectedName = groupId + '.' + artifactId;
    assertThat(name, is(equalTo(expectedName)));
  }
  // --- tests ----------------------------------------------------------------

}
