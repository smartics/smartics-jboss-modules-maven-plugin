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
def base = 'target/jboss-modules/org/apache/commons/lang/def'
def artifactFile = new File(basedir, base + '/commons-lang-2.5.jar')
assert artifactFile.exists()

def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()
def module = new XmlSlurper().parse(modulesFile)
/*
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.apache.commons.lang">
  <resources>
    <resource-root path="commons-lang-2.5.jar" />
  </resources>
  <dependencies />
</module>
*/
assert 'org.apache.commons.lang' == module.@name.text()
assert 'def' == module.@slot.text()
