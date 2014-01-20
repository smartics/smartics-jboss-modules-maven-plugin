/*
 * Copyright 2013-2014 smartics, Kronseder & Reiner GmbH
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
def base = 'target/jboss-modules/de/smartics/util/smartics-jboss-utils/main'
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
    <module name="de.smartics.util.smartics-commons" slot="other" />
    <module name="org.jboss.jboss-vfs" slot="other" />
  </dependencies>
</module>
*/
def name = module.@name.text()
assert 'de.smartics.util.smartics-jboss-utils' == name
assert '' ==  module.@slot.text()

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'smartics-jboss-utils-0.1.1.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 2 == mods.size()
assert 'de.smartics.util.smartics-commons' == mods[0].@name.text()
assert 'other' == mods[0].@slot.text()
assert 'org.jboss.jboss-vfs' == mods[1].@name.text()
assert 'other' == mods[1].@slot.text()


/* --------------------- */


def baseDep = 'target/jboss-modules/de/smartics/util/smartics-commons/other'
def artifactFileDep = new File(basedir, baseDep + '/smartics-commons-0.5.2.jar')
assert artifactFileDep.exists()

def modulesFileDep = new File(basedir, baseDep + '/module.xml')
assert modulesFileDep.exists()
def moduleDep = new XmlSlurper().parse(modulesFileDep)
/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.util.smartics-commons" slot="other">
  <resources>
    <resource-root path="smartics-commons-0.5.2.jar" />
  </resources>
  <dependencies>
    <module name="com.google.code.findbugs.jsr305" slot="other" />
    <module name="commons-io" slot="other" />
    <module name="commons-lang" slot="other" />
  </dependencies>
</module>
*/
assert 'de.smartics.util.smartics-commons' == moduleDep.@name.text()
assert 'other' ==  moduleDep.@slot.text()

def resourceRootsDep = moduleDep.resources."resource-root"
assert 1 == resourceRootsDep.size()
assert 'smartics-commons-0.5.2.jar' == resourceRootsDep[0].@path.text()

def modsDep = moduleDep.dependencies.module;
assert 3 == modsDep.size()
assert 'other' == modsDep[0].@slot.text()
assert 'other' == modsDep[1].@slot.text()
assert 'other' == modsDep[2].@slot.text()
