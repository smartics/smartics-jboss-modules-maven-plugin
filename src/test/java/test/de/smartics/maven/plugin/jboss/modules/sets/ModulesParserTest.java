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
package test.de.smartics.maven.plugin.jboss.modules.sets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.smartics.maven.plugin.jboss.modules.Clusion;
import de.smartics.maven.plugin.jboss.modules.Dependency;
import de.smartics.maven.plugin.jboss.modules.Export;
import de.smartics.maven.plugin.jboss.modules.Module;
import de.smartics.maven.plugin.jboss.modules.Services;
import de.smartics.maven.plugin.jboss.modules.sets.ModulesParser;
import de.smartics.testdoc.annotations.Uut;
import de.smartics.util.test.io.IoTestUtils;

/**
 * Tests {@link ModulesParser}.
 */
public class ModulesParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  @Uut
  private ModulesParser uut;

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  @Before
  public void setUp()
  {
    uut = new ModulesParser();
  }

  // --- helper ---------------------------------------------------------------

  private List<Module> runTest(final String systemId)
  {
    final InputStream input =
        IoTestUtils.openStreamFromResource(ModulesParserTest.class, systemId);
    final List<Module> modules = new ArrayList<Module>();
    try
    {
      uut.parse(modules, input, systemId);
      return modules;
    }
    finally
    {
      IOUtils.closeQuietly(input);
    }
  }

  private Module createBaseModule(final String name)
  {
    final Module expected = new Module();
    expected.setName(name);
    expected.setSlot("main");
    expected.setSkip(true);
    final List<Clusion> includes = new ArrayList<Clusion>(1);
    Collections.addAll(includes, Clusion.create("test-group", "test-id"));
    expected.setIncludes(includes);
    return expected;
  }

  // --- tests ----------------------------------------------------------------

  @Test
  public void readsModulesXmlWithIncludes()
  {
    final List<Module> modules = runTest("with-includes.xml");
    assertThat(modules.size(), is(1));

    final Module module = modules.get(0);

    final Module expected = createBaseModule("with-includes");

    assertThat(module.toString(), is(equalTo(expected.toString())));
  }

  @Test
  public void readsModulesXmlWithDependencies()
  {
    final List<Module> modules = runTest("with-dependencies.xml");
    assertThat(modules.size(), is(1));

    final Module module = modules.get(0);

    final Module expected = createBaseModule("with-dependencies");
    final List<Dependency> dependencies = new ArrayList<Dependency>(1);
    Collections.addAll(dependencies,
        Dependency.create("dep", "depslot", true, "import", true));
    expected.setDependencies(dependencies);

    assertThat(module.toString(), is(equalTo(expected.toString())));
  }

  @Test
  public void readsModulesXmlWithExport()
  {
    final List<Module> modules = runTest("with-export.xml");
    assertThat(modules.size(), is(1));

    final Module module = modules.get(0);

    final Module expected = createBaseModule("with-export");
    final Export export = new Export();
    final List<String> includes = new ArrayList<String>(2);
    Collections.addAll(includes, "test-name-incl1", "test-name-incl2");
    export.setIncludes(includes);
    final List<String> excludes = new ArrayList<String>(2);
    Collections.addAll(excludes, "test-name-ex1", "test-name-ex2");
    export.setExcludes(excludes);
    expected.setExport(export);

    assertThat(module.toString(), is(equalTo(expected.toString())));
  }

  @Test
  public void readsModulesXmlWithServices()
  {
    final List<Module> modules = runTest("with-services.xml");
    assertThat(modules.size(), is(1));

    final Module module = modules.get(0);

    final Module expected = createBaseModule("with-services");
    final List<Services> servicesList = new ArrayList<Services>();

    final Services port1 = new Services();
    port1.setValue("port1");
    final List<String> includes1 = new ArrayList<String>(2);
    Collections.addAll(includes1, "p1i1", "p1i2");
    port1.setIncludes(includes1);
    final List<String> excludes1 = new ArrayList<String>(2);
    Collections.addAll(excludes1, "p1e1", "p1e2");
    port1.setExcludes(excludes1);
    servicesList.add(port1);

    final Services port2 = new Services();
    port2.setValue("port2");
    final List<String> includes2 = new ArrayList<String>(2);
    Collections.addAll(includes2, "p2i1", "p2i2");
    port2.setIncludes(includes2);
    final List<String> excludes2 = new ArrayList<String>(2);
    Collections.addAll(excludes2, "p2e1", "p2e2");
    port2.setExcludes(excludes2);
    servicesList.add(port2);

    expected.setPort(servicesList);

    assertThat(module.toString(), is(equalTo(expected.toString())));
  }
}
