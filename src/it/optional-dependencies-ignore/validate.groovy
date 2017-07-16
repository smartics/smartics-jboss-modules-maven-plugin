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
def base = commons + '/logging/main'
def artifactFile = new File(basedir, base + '/commons-logging-1.2.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons.logging' == name

/*
 * All dependencies of org.apache.commons.logging are optional so we
 * expect that the generated module.xml file has an empty module dependencies section
 */
def mods = module.dependencies.module;
assert 0 == mods.size()

def modulesFileLog = new File(basedir, base + '/module.xml')
assert modulesFileLog.exists()
def moduleLog = new XmlSlurper().parse(modulesFileLog)

def nameLog = moduleLog.@name.text()
assert 'org.apache.commons.logging' == nameLog
def slotLog = moduleLog.@slot.text()
assert '' == slotLog

def modsLog = moduleLog.dependencies.module;
assert 0 == modsLog.size()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.logging">
  <resources>
    <resource-root path="commons-logging-1.2.jar" />
  </resources>
  <dependencies />
</module>
>*/
