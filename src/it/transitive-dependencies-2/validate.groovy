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
def base = "${targetDir}/org/apache/commons/jxpath/main"
def artifactFile = new File(basedir, base + '/commons-jxpath-1.3.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()


def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons.jxpath' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'commons-jxpath-1.3.jar' == resourceRoots[0].@path.text()

def mods = module.dependencies.module;
assert 6 == mods.size()
assert 'jdom' == mods[2].@name.text()
