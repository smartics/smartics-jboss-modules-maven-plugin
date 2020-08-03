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
def base = 'target/jboss-modules/de/smartics/test/setup'
def excludedFile = new File(basedir, base + '/test-setup-artifacts-commons/main/module.xml')
assert !excludedFile.exists()

def modulesFile = new File(basedir, base + '/test-setup-artifacts-domain/main/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="">
  <resources>
    <resource-root path="test-setup-artifacts-domain-1.0.0.jar" />
  </resources>
  <dependencies>
    <module name="commons-lang" />
  </dependencies>
</module>
*/

assert 'de.smartics.test.setup.test-setup-artifacts-domain' == module.@name.text()

def mods = module.dependencies.module;
assert 1 == mods.size()
assert 'commons-lang' == mods[0].@name.text()
