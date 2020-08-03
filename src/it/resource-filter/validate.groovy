/*
 * Copyright 2013-2020 smartics, Kronseder & Reiner GmbH
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
def base = "${targetDir}/org/apache/commons/main"
def artifactFile1 = new File(basedir, base + '/commons-lang-2.6.jar')
assert artifactFile1.exists()
def artifactFile2 = new File(basedir, base + '/commons-logging-1.1.1.jar')
assert artifactFile2.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.apache.commons' == name

def resourceRoots = module.resources."resource-root"
assert 2 == resourceRoots.size()

def commonsLang = resourceRoots[0]
assert 'commons-lang-2.6.jar' == commonsLang.@path.text()
assert commonsLang.children().size() == 1
def filter = commonsLang.filter
assert filter.children().size() == 4
assert filter.children()[0].name() == 'include'
assert filter.children()[0].@path == 'de/smartics/folder1/**'

assert filter.children()[1].name() == 'exclude'
assert filter.children()[1].@path == 'de/smartics/folder2'

assert filter.children()[2].name() == 'include-set'
assert filter.children()[2].children().size() == 2
assert filter.children()[2].children()[0].name() == 'path'
assert filter.children()[2].children()[0].@name == 'de/smartics/folder3'
assert filter.children()[2].children()[1].@name == 'de/smartics/folder4'

assert filter.children()[3].name() == 'exclude-set'
assert filter.children()[3].children().size() == 2
assert filter.children()[3].children()[0].@name == 'de/smartics/folder5'
assert filter.children()[3].children()[1].@name == 'de/smartics/folder6'


def commonsLogging = resourceRoots[1]
assert 'commons-logging-1.1.1.jar' == commonsLogging.@path.text()
assert commonsLogging.children().size() == 0

def mods = module.dependencies.module;
assert 6 == mods.size()
assert 'javax.api' == mods[0].@name.text()
assert 'javax.xml.stream.api' == mods[1].@name.text()

/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons">
  <resources>
    <resource-root path="commons-lang-2.6.jar">
      <filter>
        <include path="de/smartics/folder1/**" />
        <exclude path="de/smartics/folder2" />
        <include-set>
          <path name="de/smartics/folder3" />
          <path name="de/smartics/folder4" />
        </include-set>
        <exclude-set>
          <path name="de/smartics/folder5" />
          <path name="de/smartics/folder6" />
        </exclude-set>
      </filter>
    </resource-root>
    <resource-root path="commons-logging-1.1.1.jar" />
  </resources>
  <dependencies>
    <module name="javax.api" />
    <module name="javax.xml.stream.api" />
    <module name="avalon-framework" optional="true" />
    <module name="javax.servlet.servlet-api" optional="true" />
    <module name="log4j" optional="true" />
    <module name="logkit" optional="true" />
  </dependencies>
</module>
*/
