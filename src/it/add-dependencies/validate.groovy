/*
 * Copyright 2013-2020 smartics, Kronseder & Reiner GmbH
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
def base = "${targetDir}/org/apache/commons/main"
def artifactFile1 = new File(basedir, base + '/commons-lang-2.6.jar')
assert artifactFile1.exists()
def artifactFile2 = new File(basedir, base + '/commons-logging-1.1.1.jar')
assert artifactFile2.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons' == name

def resourceRoots = module.resources."resource-root"
assert 2 == resourceRoots.size()
assert 'commons-lang-2.6.jar' == resourceRoots[0].@path.text()
assert 'commons-logging-1.1.1.jar' == resourceRoots[1].@path.text()

def mods = module.dependencies.module;
assert 6 == mods.size()
assert 'javax.api' == mods[0].@name.text()
assert 'javax.xml.stream.api' == mods[1].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons">
  <resources>
    <resource-root path="commons-lang-2.6.jar" />
    <resource-root path="commons-logging-1.1.1.jar" />
  </resources>
  <dependencies>
    <module name="javax.api" />
    <module name="javax.xml.stream.api" />
    <module name="avalon-framework" optional="true" />
    <module name="javax.servlet.servlet-api" optional="true" />
    <module name="log4j" optional="true" />
    <module name="logkit" optional="true" />
  </dependencies>
</module>
*/
