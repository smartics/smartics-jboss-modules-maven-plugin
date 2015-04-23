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
def commonsBase = 'target/jboss-modules/commons-lang'
def excludedFile1 = new File(basedir, commonsBase + '/commons-lang-2.5.jar')
assert !excludedFile1.exists()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.test.setup.test-setup-artifacts-exclusion">
  <resources>
    <resource-root path="test-setup-artifacts-exclusion-1.0.0-SNAPSHOT.jar" />
  </resources>
  <dependencies>
    <module name="de.smartics.test.setup.test-setup-artifacts-domain" />
  </dependencies>
</module>
*/

def base = 'target/jboss-modules/de/smartics/test/setup'
def modulesFile = new File(basedir, base + '/test-setup-artifacts-exclusion/main/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

assert 'de.smartics.test.setup.test-setup-artifacts-exclusion' == module.@name.text()

def mods = module.dependencies.module;
assert 1 == mods.size()
assert 'de.smartics.test.setup.test-setup-artifacts-domain' == mods[0].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.test.setup.test-setup-artifacts-domain">
  <resources>
    <resource-root path="test-setup-artifacts-domain-1.0.0.jar" />
  </resources>
  <dependencies>
    <module name="de.smartics.test.setup.test-setup-artifacts-commons" />
  </dependencies>
</module>
*/

def domainModulesFile = new File(basedir, base + '/test-setup-artifacts-domain/main/module.xml')
assert domainModulesFile.exists()

def domainModule = new XmlSlurper().parse(domainModulesFile)

assert 'de.smartics.test.setup.test-setup-artifacts-domain' == domainModule.@name.text()

def domainMods = domainModule.dependencies.module;
assert 1 == domainMods.size()
assert 'de.smartics.test.setup.test-setup-artifacts-commons' == domainMods[0].@name.text()
