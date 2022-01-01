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
def langBase = "${targetDir}/org/apache/commons/lang/main"
def langArtifactFile = new File(basedir, langBase + '/commons-lang-2.6.jar')
assert langArtifactFile.exists()

def langModulesFile = new File(basedir, langBase + '/module.xml')
assert langModulesFile.exists()


def langModule = new XmlSlurper().parse(langModulesFile)

def langName = langModule.@name.text()
assert 'org.apache.commons.lang' == langName

def langResourceRoots = langModule.resources."resource-root"
assert 1 == langResourceRoots.size()
assert 'commons-lang-2.6.jar' == langResourceRoots[0].@path.text()


def loggingBase = "${targetDir}/org/apache/commons/logging/main"
def loggingArtifactFile = new File(basedir, loggingBase + '/commons-logging-1.1.1.jar')
assert loggingArtifactFile.exists()

def loggingModulesFile = new File(basedir, loggingBase + '/module.xml')
assert loggingModulesFile.exists()


def loggingModule = new XmlSlurper().parse(loggingModulesFile)

def loggingName = loggingModule.@name.text()
assert 'org.apache.commons.logging' == loggingName

def loggingResourceRoots = loggingModule.resources."resource-root"
assert 1 == loggingResourceRoots.size()
assert 'commons-logging-1.1.1.jar' == loggingResourceRoots[0].@path.text()
