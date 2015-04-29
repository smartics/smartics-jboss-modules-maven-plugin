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
def base = "${targetDir}/org/jdom2/main"
def artifactFile = new File(basedir, base + '/jdom2-2.0.4.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.jdom2' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'jdom2-2.0.4.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 4 == mods.size()
assert 'javax.api' == mods[0].@name.text()
assert 'org.apache.xalan' == mods[1].@name.text()
assert 'org.apache.xerces' == mods[2].@name.text()
assert 'org.jaxen' == mods[3].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.jdom2">
  <resources>
    <resource-root path="jdom2-2.0.4.jar" />
  </resources>
  <dependencies>
    <module name="javax.api" />
    <module name="org.apache.xalan" optional="true" />
    <module name="org.apache.xerces" optional="true" />
    <module name="org.jaxen" optional="true" />
  </dependencies>
</module>
*/
