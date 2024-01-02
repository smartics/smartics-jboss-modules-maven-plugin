<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright 2013-2024 smartics, Kronseder & Reiner GmbH

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  version="1.0">
  	<xsl:output
		   method="xml"
		   indent="yes"
		   omit-xml-declaration="no"
		   media-type="text/xml"/>


	<xsl:template match="/">
		<document>
			<properties>
				<title>The smartics JBoss Modules XSD</title>
			</properties>
			<body>
				<section name="The smartics JBoss Modules XSD">
          <p>
            The smartics JBoss Modules XSD defines the structure for documents
            that specify module descriptors. A module descriptor matches
            Maven artifacts by its <code>groupId</code> and/or
            <code>artifactId</code> and allows to apply additional module
            information.
          </p>
          <p>
            This page provides a <a href="#Document_Outline">document outline</a>
            and <a href="#Document_Schema">document schema</a> (XSD).
          </p>
          <p>
            The XSD is also available for download: <a href="download/xsd/jboss-modules-descriptor.xsd">Download jboss-modules-descriptor.xsd</a>.
          </p>
          <p>
            For more information on JBoss modules, please refer to
            <a href="https://docs.jboss.org/author/display/MODULES/Module+descriptors">Module dependencies</a>
            on the <a href="https://docs.jboss.org/author/display/MODULES/Home">JBoss Modules Space</a> at
            <a href="http://www.jboss.org/">JBoss</a>.
          </p>

          <subsection name="Document Outline">
            <p>
              Here is an outline of a document instance:
            </p>
            <source><![CDATA[<modules xmlns="http://smartics.de/ns/jboss-modules-descriptor/1">
  <module
    name=""
    slot="">
    <directives>
      <skip></skip>
      <inherit-slot></inherit-slot>
    </directives>

    <match>
      <includes>
        <include>
          <groupId></groupId>
          <artifactId></artifactId>
        </include>
        ...
        <include>
          <groupId></groupId>
          <artifactId></artifactId>
        </include>
      </includes>
      <excludes>
        <exclude>
          <groupId></groupId>
          <artifactId></artifactId>
        </exclude>
        ...
        <exclude>
          <groupId></groupId>
          <artifactId></artifactId>
        </exclude>
      </excludes>
    </match>

    <apply-to-dependencies>
      <dependencies>
        <match>
          <includes>
            <include></include>
            ...
            <include></include>
          </includes>

          <excludes>
            <exclude></exclude>
            ...
            <exclude></exclude>
          </excludes>
        </match>
        <apply>
          <slot></slot>
          <export></export>
          <services></services>
          <optional></optional>
          <imports>
            <include path="" />
            <include-set>
              <path name="" />
              ...
            </include-set>
            <exclude-set>
              <path name="org/jboss/example/tests2" />
              ...
            </exclude-set>
          </imports>
          <exports>
            <exclude path="" />
            <include-set>
              <path name="" />
              ...
            </include-set>
            <exclude-set>
              <path name="org/jboss/example/tests" />
              ...
            </exclude-set>
          </exports>
        </apply>
      </dependencies>
      ...
    </apply-to-dependencies>


    <apply-to-module>
      <exports>
        <exclude path="**/impl/*" />
      </exports>

      <main-class name="de.smartics.text.Main" />

      <properties>
        <property
          name="my.property"
          value="foo" />
        ...
        <property
          name="my.property2"
          value="foo2" />
      </properties>

      <dependencies>
        <module name="javax.api" />
        ...
        <module
          name="de.smartics.test.x"
          services="export"
          export="true">
          <imports>
            <include path="META-INF/xyz" />
            <include path="META-INF" />
          </imports>
          <exports>
            <include path="META-INF/xyz" />
            <include path="META-INF" />
          </exports>
        </module>

        ...

        <system export="true">
          <paths>
            <path name="de/smartics/test/one" />
            ...
            <path name="de/smartics/test/n" />
          </paths>
        </system>
        ...
      </dependencies>
    </apply-to-module>
  </module>
  ...
</modules>]]></source>
          </subsection>
          <subsection name="Document Schema">
            <p>
              <a href="download/xsd/jboss-modules-descriptor.xsd">Download jboss-modules-descriptor.xsd</a>
            </p>
            <p>
              This is the XML schema for modules documents:
            </p>
  					<source><xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
  			  		<xsl:apply-templates select="xs:schema"/>
  			  		<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
  			    </source>
          </subsection>
			  </section>
			</body>
		</document>
	</xsl:template>

  <xsl:template match="xs:schema">
    <xsl:copy-of select="."/>
  </xsl:template>
</xsl:stylesheet>
