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
def base = "${targetDir}/org/springframework/orm/main"
def artifactFile = new File(basedir, base + '/spring-orm-4.1.6.RELEASE.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()


def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.springframework.orm' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'spring-orm-4.1.6.RELEASE.jar' == resourceRoots[0].@path.text()

// Expectation is that com.ibm.websphere:websphere_uow_api:jar:0.0.1 will not be found
// But we should still have the correct dependencies generated in the module.xml
def mods = module.dependencies.module;
assert 16 == mods.size()
assert 'aopalliance' == mods[0].@name.text()
assert 'javax.jdo.jdo-api' == mods[1].@name.text()
assert 'javax.servlet.javax.servlet-api' == mods[2].@name.text()
assert 'org.apache.openjpa' == mods[3].@name.text()
assert 'org.eclipse.persistence.javax.persistence' == mods[4].@name.text()
assert 'org.eclipse.persistence.org.eclipse.persistence.core' == mods[5].@name.text()
assert 'org.eclipse.persistence.org.eclipse.persistence.jpa' == mods[6].@name.text()
assert 'org.hibernate.hibernate-core' == mods[7].@name.text()
assert 'org.hibernate.hibernate-entitymanager' == mods[8].@name.text()
assert 'org.springframework.spring-aop' == mods[9].@name.text()
assert 'org.springframework.spring-beans' == mods[10].@name.text()
assert 'org.springframework.spring-context' == mods[11].@name.text()
assert 'org.springframework.spring-core' == mods[12].@name.text()
assert 'org.springframework.spring-jdbc' == mods[13].@name.text()
assert 'org.springframework.spring-tx' == mods[14].@name.text()
assert 'org.springframework.spring-web' == mods[15].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.springframework.orm">
  <resources>
    <resource-root path="spring-orm-4.1.6.RELEASE.jar" />
  </resources>
  <dependencies>
    <module name="aopalliance" optional="true" />
    <module name="javax.jdo.jdo-api" optional="true" />
    <module name="javax.servlet.javax.servlet-api" optional="true" />
    <module name="org.apache.openjpa" optional="true" />
    <module name="org.eclipse.persistence.javax.persistence" optional="true" />
    <module name="org.eclipse.persistence.org.eclipse.persistence.core" optional="true" />
    <module name="org.eclipse.persistence.org.eclipse.persistence.jpa" optional="true" />
    <module name="org.hibernate.hibernate-core" optional="true" />
    <module name="org.hibernate.hibernate-entitymanager" optional="true" />
    <module name="org.springframework.spring-aop" optional="true" />
    <module name="org.springframework.spring-beans" />
    <module name="org.springframework.spring-context" optional="true" />
    <module name="org.springframework.spring-core" />
    <module name="org.springframework.spring-jdbc" />
    <module name="org.springframework.spring-tx" />
    <module name="org.springframework.spring-web" optional="true" />
  </dependencies>
</module>
*/
