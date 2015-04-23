/*
 * Copyright 2013-2015 smartics, Kronseder & Reiner GmbH
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
package test.de.smartics.maven.plugin.jboss.modules.parser;

import java.io.InputStream;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Before;

import de.smartics.maven.plugin.jboss.modules.descriptor.ModulesDescriptor;
import de.smartics.maven.plugin.jboss.modules.parser.ModulesXmlParser;
import de.smartics.maven.plugin.jboss.modules.test.utils.IoTestUtils;

/**
 * Tests {@link ModulesXmlParser}.
 */
public abstract class AbstractModulesXmlParserTest
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  private final String resourceId;

  private ModulesXmlParser uut;

  protected ModulesDescriptor result;

  // ****************************** Inner Classes *****************************

  protected AbstractModulesXmlParserTest(final String resourceId)
  {
    this.resourceId = resourceId;
  }

  // ********************************* Methods ********************************

  // --- prepare --------------------------------------------------------------

  @Before
  public void setUp() throws Exception
  {
    uut = new ModulesXmlParser();
    final InputStream input =
        IoTestUtils
            .openStreamFromResource(AbstractModulesXmlParserTest.class, resourceId);
    try
    {
      this.result = uut.parse(resourceId, input);
    }
    finally
    {
      IOUtil.close(input);
    }

  }

  // --- helper ---------------------------------------------------------------

  // --- tests ----------------------------------------------------------------

}
