/*
 * Copyright 2013-2017 smartics, Kronseder & Reiner GmbH
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
def base = commons + '/beanutils/other-slot'
def artifactFile = new File(basedir, base + '/commons-beanutils-1.8.3.jar')
assert artifactFile.exists()

def baseColl = commons + '/collections/other-slot'
def artifactFileColl = new File(basedir, baseColl + '/commons-collections-3.2.1.jar')
assert artifactFileColl.exists()

def baseLog = commons + '/logging/main'
def artifactFileLog = new File(basedir, baseLog + '/commons-logging-1.1.1.jar')
assert artifactFileLog.exists()


def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons.beanutils' == name
def slot = module.@slot.text()
assert 'other-slot' == slot

def mods = module.dependencies.module;
assert 2 == mods.size()
assert 'org.apache.commons.collections' == mods[0].@name.text()
assert 'other-slot' == mods[0].@slot.text()
assert 'org.apache.commons.logging' == mods[1].@name.text()
assert '' == mods[1].@slot.text()


def modulesFileColl = new File(basedir, baseColl + '/module.xml')
assert modulesFileColl.exists()
def moduleColl = new XmlSlurper().parse(modulesFileColl)

def nameColl = moduleColl.@name.text()
assert 'org.apache.commons.collections' == nameColl
def slotColl = moduleColl.@slot.text()
assert 'other-slot' == slotColl


def modulesFileLog = new File(basedir, baseLog + '/module.xml')
assert modulesFileLog.exists()
def moduleLog = new XmlSlurper().parse(modulesFileLog)

def nameLog = moduleLog.@name.text()
assert 'org.apache.commons.logging' == nameLog
def slotLog = moduleLog.@slot.text()
assert '' == slotLog

def modsLog = moduleLog.dependencies.module;
assert 4 == modsLog.size()
assert '' == modsLog[0].@slot.text()
assert '' == modsLog[1].@slot.text()
assert '' == modsLog[2].@slot.text()
assert '' == modsLog[3].@slot.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.beanutils" slot="other-slot">
  <resources>
    <resource-root path="commons-beanutils-1.8.3.jar" />
  </resources>
  <dependencies>
    <module name="org.apache.commons.collections" optional="true" slot="other-slot" />
    <module name="org.apache.commons.logging" slot="main" />
  </dependencies>
</module>


<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.collections" slot="other-slot">
  <resources>
    <resource-root path="commons-collections-3.2.1.jar" />
  </resources>
  <dependencies />
</module>


<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.logging">
  <resources>
    <resource-root path="commons-logging-1.1.1.jar" />
  </resources>
  <dependencies>
    <module name="avalon-framework" optional="true" />
    <module name="javax.servlet.servlet-api" optional="true" />
    <module name="log4j" optional="true" />
    <module name="logkit" optional="true" />
  </dependencies>
</module>*/
