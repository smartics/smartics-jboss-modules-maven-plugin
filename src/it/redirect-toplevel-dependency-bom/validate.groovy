/*
 * Copyright 2013-2024 smartics, Kronseder & Reiner GmbH
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
def base = "${targetDir}/de/smartics/util/smartics-jboss-utils/main"
def artifactFile = new File(basedir, base + '/smartics-jboss-utils-0.1.1.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)
/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.util.smartics-jboss-utils">
  <resources>
    <resource-root path="smartics-jboss-utils-0.1.1.jar" />
  </resources>
  <dependencies>
    <module name="de.smartics.util.smartics-validation-utils" slot="other-v" />
    <module name="de.smartics.util.smartics-commons" slot="other" />
    <module name="org.jboss.jboss-vfs" slot="other" />
  </dependencies>
</module>*/
def name = module.@name.text()
assert 'de.smartics.util.smartics-jboss-utils' == name
assert '' ==  module.@slot.text()

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'smartics-jboss-utils-0.1.1.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 3 == mods.size()
assert 'de.smartics.util.smartics-validation-utils' == mods[0].@name.text()
assert 'other-v' == mods[0].@slot.text()
assert 'de.smartics.util.smartics-commons' == mods[1].@name.text()
assert 'other' == mods[1].@slot.text()
assert 'org.jboss.jboss-vfs' == mods[2].@name.text()
assert 'other' == mods[2].@slot.text()


/* --------------------- */

def base2 = targetDir + '/de/smartics/util/smartics-validation-utils/main'
def artifactFile2 = new File(basedir, base2 + '/smartics-validation-utils-0.1.3.jar')
assert !artifactFile2.exists()
