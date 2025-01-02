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
def base = "${targetDir}/test-setup-artifacts-domain/main"
def moduleFile = new File(basedir, base + '/module.xml')
assert moduleFile.exists()

def module = new XmlSlurper().parse(moduleFile)
/*
  test-setup-artifacts-domain has two dependencies:
    1. commons-lang
    2. test-setup-artifacts-commons

  => Test case: only the test-setup-artifacts-commons has to be exported

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="test-setup-artifacts-domain">
  <resources>
    <resource-root path="test-setup-artifacts-domain-1.0.0.jar" />
  </resources>
  <dependencies>
    <module name="commons-lang" />
    <module name="de.smartics.test.setup.test-setup-artifacts-commons" export="true" />
  </dependencies>
</module>
*/
def name = module.@name.text()
assert 'test-setup-artifacts-domain' == name

def mods = module.dependencies.module;
assert 2 == mods.size()
assert 'commons-lang' == mods[0].@name.text()
assert '' == mods[0].@export.text()
assert 'de.smartics.test.setup.test-setup-artifacts-commons' == mods[1].@name.text()
assert 'true' == mods[1].@export.text()
