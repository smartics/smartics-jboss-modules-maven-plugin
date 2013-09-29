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

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import de.smartics.maven.plugin.jboss.modules.Services;

/**
 * Resolves the <code>port</code> element.
 */
@SuppressWarnings("rawtypes")
public class ServicesConverter implements Converter
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Default constructor.
   */
  public ServicesConverter()
  {
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  @Override
  public boolean canConvert(final Class type)
  {
    return Services.class.equals(type);
  }

  @Override
  public void marshal(final Object source,
      final HierarchicalStreamWriter writer, final MarshallingContext context)
  {
    throw new UnsupportedOperationException("Only unmashalling supported.");
  }

  @Override
  public Object unmarshal(final HierarchicalStreamReader reader,
      final UnmarshallingContext context)
  {
    // final List<Services> servicesList = new ArrayList<Services>();
    //
    // while (reader.hasMoreChildren())
    // {
    // reader.moveDown();
    final Services services = new Services();

    while (reader.hasMoreChildren())
    {
      reader.moveDown();

      final String name = reader.getNodeName();
      if ("value".equals(name))
      {
        final String value = reader.getValue();
        services.setValue(value);
      }
      else if ("includes".equals(name))
      {
        final List<String> includes = new ArrayList<String>();
        while (reader.hasMoreChildren())
        {
          reader.moveDown();
          final String value = reader.getValue();
          includes.add(value);
          reader.moveUp();
        }
        services.setIncludes(includes);
      }
      else if ("excludes".equals(name))
      {
        final List<String> excludes = new ArrayList<String>();
        while (reader.hasMoreChildren())
        {
          reader.moveDown();
          final String value = reader.getValue();
          excludes.add(value);
          reader.moveUp();
        }
        services.setExcludes(excludes);
      }

      reader.moveUp();
    }
    return services;
    // servicesList.add(services);
    // reader.moveUp();
    // }
    //
    // return servicesList;
  }

  // --- object basics --------------------------------------------------------

}
