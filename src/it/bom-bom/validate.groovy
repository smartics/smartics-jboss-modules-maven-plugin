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
/*
  Resolution will derive version 2.5, although smartics-commons depends on 2.6.
  This is due to Maven's resolution algorithm.
 */
def baseC = "${targetDir}/commons-lang/main"
def artifactFileC = new File(basedir, baseC + '/commons-lang-2.5.jar')
assert artifactFileC.exists()

def base = "${targetDir}/de/smartics/test/main"
def artifactFile1 = new File(basedir, base + '/smartics-commons-0.5.2.jar')
assert artifactFile1.exists()
def artifactFile2 = new File(basedir, base + '/test-setup-artifacts-commons-1.0.0.jar')
assert artifactFile2.exists()
def artifactFile3 = new File(basedir, base + '/test-setup-artifacts-domain-1.0.0.jar')
assert artifactFile3.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)
/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.test">
  <resources>
    <resource-root path="smartics-commons-0.5.2.jar" />
    <resource-root path="test-setup-artifacts-commons-1.0.0.jar" />
    <resource-root path="test-setup-artifacts-domain-1.0.0.jar" />
  </resources>
  <dependencies>
    <module name="javax.api" />
    <module name="com.google.code.findbugs.jsr305" />
    <module name="commons-io" />
    <module name="commons-lang" />
  </dependencies>
</module>
*/
assert 'de.smartics.test' == module.@name.text()

def resourceRoots = module.resources."resource-root"
assert 3 == resourceRoots.size()
assert 'smartics-commons-0.5.2.jar' == resourceRoots[0].@path.text()
assert 'test-setup-artifacts-commons-1.0.0.jar' == resourceRoots[1].@path.text()
assert 'test-setup-artifacts-domain-1.0.0.jar' == resourceRoots[2].@path.text()

def mods = module.dependencies.module;
assert 4 == mods.size()
assert 'javax.api' == mods[0].@name.text()
assert 'com.google.code.findbugs.jsr305' == mods[1].@name.text()
assert 'commons-io' == mods[2].@name.text()
assert 'commons-lang' == mods[3].@name.text()
