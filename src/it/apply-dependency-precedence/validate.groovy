/*
 * Copyright 2013-2025 smartics, Kronseder & Reiner GmbH
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
def base = 'target/jboss-modules/ognl/main'
def moduleFile = new File(basedir, base + '/module.xml')
assert moduleFile.exists()

def module = new XmlSlurper().parse(moduleFile)

/*
The expectation is that the statically defined javassist module dependency will override
any apply-to-depenencies operations for javassist.

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="ognl">
  <resources>
    <resource-root path="ognl-3.0.8.jar" />
  </resources>
  <dependencies>
    <module name="javassist" export="true" />
  </dependencies>
</module>
*/
def name = module.@name.text()
assert 'ognl' == name

def mods = module.dependencies.module;
assert 1 == mods.size()
assert 'javassist' == mods[0].@name.text()
assert 'true' == mods[0].@export.text()


