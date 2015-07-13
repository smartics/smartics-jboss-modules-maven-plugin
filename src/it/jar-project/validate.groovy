/*
 * Copyright 2013-2015 smartics, Kronseder & Reiner GmbH
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
def base = "${targetDir}/org/jgroups/main"
def artifactFile = new File(basedir, base + '/jgroups-3.2.7.Final.jar')
assert artifactFile.exists()

/* dependencyManagement dependencies are not included in case of a non-pom project */
def depMgmtFile =  new File(basedir, targetDir + '/de/smartics/test/setup/test-setup-artifacts-commons/main/test-setup-artifacts-commons-1.0.0.jar');
assert !depMgmtFile.exists()


def jarBase = targetDir + '/de/smartics/sandbox/jar-project/main'
/*
def file = new File(basedir, jarBase + '/jar-project-testing.jar')
assert file.exists()
*/

def modulesFile = new File(basedir, jarBase + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'de.smartics.sandbox.jar-project' == name

/*
def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'jar-project-testing.jar' == resourceRoots[0].@path.text()
*/

def mods = module.dependencies.module;
assert 1 == mods.size()
assert 'org.jgroups' == mods[0].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="de.smartics.maven.plugin.jar-project">
  <resources>
    <resource-root path="jar-project-testing.jar" />
  </resources>
  <dependencies>
    <module name="org.jgroups" />
  </dependencies>
</module>
*/
