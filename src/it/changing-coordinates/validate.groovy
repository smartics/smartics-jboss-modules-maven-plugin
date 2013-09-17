/*
 * Copyright 2013 smartics, Kronseder & Reiner GmbH
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
def base = 'target/jboss-modules/org/javassist/main'
def artifactFile = new File(basedir, base + '/javassist-3.17.1-GA.jar')
assert artifactFile.exists()

def skipped1 = new File(basedir, base + '/javassist-3.7.ga.jar')
assert !skipped1.exists()

def skipped2 = new File(basedir, base + '/javassist-3.9.0.GA.jar')
assert !skipped2.exists()


def modulesFile = new File(basedir, base + '/module.xml')
assert modulesFile.exists()

def module = new XmlSlurper().parse(modulesFile)

def name = module.@name.text()
assert 'org.javassist' == name

def resourceRoots = module.resources."resource-root"
assert 1 == resourceRoots.size()
assert 'javassist-3.17.1-GA.jar' == resourceRoots[0].@path.text()
