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
def commons = "${targetDir}/org/apache/commons"
def base = commons + '/beanutils/1'
def artifactFile = new File(basedir, base + '/commons-beanutils-1.8.3.jar')
assert artifactFile.exists()

def baseColl = commons + '/collections/3'
def artifactFileColl = new File(basedir, baseColl + '/commons-collections-3.2.1.jar')
assert artifactFileColl.exists()

def baseLog = commons + '/logging/1'
def artifactFileLog = new File(basedir, baseLog + '/commons-logging-1.1.1.jar')
assert artifactFileLog.exists()


def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons.beanutils' == name
def slot = module.@slot.text()
assert '1' == slot

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'commons-beanutils-1.8.3.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 2 == mods.size()
assert 'org.apache.commons.collections' == mods[0].@name.text()
assert '3' == mods[0].@slot.text()
assert 'org.apache.commons.logging' == mods[1].@name.text()
assert '1' == mods[1].@slot.text()


def modulesFileColl = new File(basedir, baseColl + '/module.xml')
assert modulesFileColl.exists()
def moduleColl = new XmlSlurper().parse(modulesFileColl)

def nameColl = moduleColl.@name.text()
assert 'org.apache.commons.collections' == nameColl
def slotColl = moduleColl.@slot.text()
assert '3' == slotColl

def modulesFileLog = new File(basedir, baseLog + '/module.xml')
assert modulesFileLog.exists()
def moduleLog = new XmlSlurper().parse(modulesFileLog)

def nameLog = moduleLog.@name.text()
assert 'org.apache.commons.logging' == nameLog
def slotLog = moduleLog.@slot.text()
assert '1' == slotLog

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.beanutils" slot="1">
  <resources>
    <resource-root path="commons-beanutils-1.8.3.jar" />
  </resources>
  <dependencies>
    <module name="org.apache.commons.collections" optional="true" slot="3" />
    <module name="org.apache.commons.logging" slot="1" />
  </dependencies>
</module>

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.collections" slot="3">
  <resources>
    <resource-root path="commons-collections-3.2.1.jar" />
  </resources>
  <dependencies />
</module>

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.logging" slot="1">
  <resources>
    <resource-root path="commons-logging-1.1.1.jar" />
  </resources>
  <dependencies>
    <module name="avalon-framework" optional="true" slot="4" />
    <module name="javax.servlet.servlet-api" optional="true" slot="2" />
    <module name="logkit" optional="true" slot="1" />
    <module name="org.apache.log4j" optional="true" slot="1" />
  </dependencies>
</module>
*/
