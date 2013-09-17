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
def base = 'target/jboss-modules/org/apache/commons/configuration/main'
def artifactFile = new File(basedir, base + '/commons-configuration-1.8.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()


def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons.configuration' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'commons-configuration-1.8.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 13 == mods.size()
assert 'org.apache.commons.lang' == mods[8].@name.text()
assert 'import' == mods[8].@services.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.configuration">
  <resources>
    <resource-root path="commons-configuration-1.8.jar" />
  </resources>
  <dependencies>
    <module name="javax.servlet.servlet-api" />
    <module name="log4j" optional="true" />
    <module name="org.apache.commons.beanutils" optional="true" />
    <module name="org.apache.commons.codec" optional="true" />
    <module name="org.apache.commons.collections" optional="true" />
    <module name="org.apache.commons.digester" optional="true" />
    <module name="org.apache.commons.jexl" optional="true" />
    <module name="org.apache.commons.jxpath" optional="true" />
    <module name="org.apache.commons.lang" services="import" />
    <module name="org.apache.commons.logging" />
    <module name="org.apache.commons.vfs2" optional="true" />
    <module name="xml-apis" />
    <module name="xml-resolver" optional="true" />
  </dependencies>
</module>
*/
