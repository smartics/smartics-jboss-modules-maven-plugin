/*
 * Copyright 2013-2025 smartics, Kronseder & Reiner GmbH
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
def base = "${targetDir}/org/reflections/main"
def artifactFile = new File(basedir, base + '/reflections-0.9.7.jar')
assert artifactFile.exists()

def notExists1 = new File(basedir, base + '/dom4j')
assert !notExists1.exists()
def notExists2 = new File(basedir, base + '/org/dom4j')
assert !notExists2.exists()


def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.reflections' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'reflections-0.9.7.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 8 == mods.size()
assert 'org.dom4j' == mods[4].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.reflections">
  <resources>
    <resource-root path="reflections-0.9.7.RC1sm.jar" />
  </resources>
  <dependencies>
    <module name="com.google.code.gson" optional="true" />
    <module name="com.google.guava" />
    <module name="javassist" />
    <module name="javax.servlet.servlet-api" optional="true" />
    <module name="org.dom4j" />
    <module name="org.jboss.jboss-vfs" optional="true" />
    <module name="org.slf4j.slf4j-api" optional="true" />
    <module name="org.slf4j.slf4j-simple" optional="true" />
  </dependencies>
</module>
*/
