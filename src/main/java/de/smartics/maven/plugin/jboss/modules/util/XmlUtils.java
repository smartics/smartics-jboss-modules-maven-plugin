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
package de.smartics.maven.plugin.jboss.modules.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.Namespace;

public class XmlUtils
{

  private XmlUtils()
  {
  }

  public static void adjustNamespaces(final Element element, Namespace ns)
  {
    element.setNamespace(null);
    final List<Namespace> namespaces =
        new ArrayList<Namespace>(element.getAdditionalNamespaces());
    for (final Namespace namespace : namespaces)
    {
      element.removeNamespaceDeclaration(namespace);
    }
    element.setNamespace(ns);
    for (final Element child : element.getChildren())
    {
      adjustNamespaces(child, ns);
    }
  }

}
