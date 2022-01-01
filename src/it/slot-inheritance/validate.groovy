/*
 * Copyright 2013-2022 smartics, Kronseder & Reiner GmbH
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
def setup = "${targetDir}/de/smartics/test/setup/special"
def setupCommonsFile = new File(basedir, setup + '/test-setup-artifacts-commons-1.0.0.jar')
assert setupCommonsFile.exists()
def setupDomainFile = new File(basedir, setup + '/test-setup-artifacts-domain-1.0.0.jar')
assert setupDomainFile.exists()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.test.setup" slot="special">
  <resources>
    <resource-root path="test-setup-artifacts-commons-1.0.0.jar" />
    <resource-root path="test-setup-artifacts-domain-1.0.0.jar" />
  </resources>
  <dependencies>
    <module name="commons-lang" slot="special" />
  </dependencies>
</module>
*/

def modulesFile = new File(basedir, setup + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

assert 'de.smartics.test.setup' == module.@name.text()
assert 'special' == module.@slot.text()

def resourceRoots = module.resources."resource-root"
assert 2 == resourceRoots.size()
assert 'test-setup-artifacts-commons-1.0.0.jar' == resourceRoots[0].@path.text()
assert 'test-setup-artifacts-domain-1.0.0.jar' == resourceRoots[1].@path.text()

def mods = module.dependencies.module;
assert 1 == mods.size()
assert 'commons-lang' == mods[0].@name.text()
assert 'special' == mods[0].@slot.text()


/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="commons-lang" slot="special">
  <resources>
    <resource-root path="commons-lang-2.5.jar" />
  </resources>
  <dependencies />
</module>
*/
def commons = "${targetDir}/commons-lang/special"
def commonsFile = new File(basedir, commons + '/commons-lang-2.5.jar')
assert commonsFile.exists()

def commonsModulesFile = new File(basedir, commons + '/module.xml')
assert commonsModulesFile.exists()
def commonsModule = new XmlSlurper().parse(commonsModulesFile)

assert 'commons-lang' == commonsModule.@name.text()
assert 'special' == commonsModule.@slot.text()

def commonsResourceRoots = commonsModule.resources."resource-root"
assert 1 == commonsResourceRoots.size()
assert 'commons-lang-2.5.jar' == commonsResourceRoots[0].@path.text()
