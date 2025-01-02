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
def base = "${targetDir}/de/smartics/test/redirect-toplevel-dependency-extension/other"
def artifactFile = new File(basedir, base + '/redirect-toplevel-dependency-extension-testing.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)
/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.test.redirect-toplevel-dependency-extension" slot="other">
  <resources>
    <resource-root path="redirect-toplevel-dependency-extension-testing.jar" />
  </resources>
  <dependencies>
    <module name="de.smartics.util.smartics-jboss-utils" />
    <module name="de.smartics.util.smartics-validation-utils" slot="other-v" />
  </dependencies>
</module>*/
def name = module.@name.text()
assert 'de.smartics.test.redirect-toplevel-dependency-extension' == name
assert 'other' ==  module.@slot.text()

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'redirect-toplevel-dependency-extension-testing.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 2 == mods.size()
assert 'de.smartics.util.smartics-jboss-utils' == mods[0].@name.text()
assert '' == mods[0].@slot.text()
assert 'de.smartics.util.smartics-validation-utils' == mods[1].@name.text()
assert 'other-v' == mods[1].@slot.text()


/* --------------------- */

def base2 = "${targetDir}/de/smartics/util/smartics-validation-utils/main"
def artifactFile2 = new File(basedir, base2 + '/smartics-validation-utils-0.1.3.jar')
assert !artifactFile2.exists()
