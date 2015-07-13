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
def base = "${targetDir}/de/smartics/util/smartics-commons/main"
def artifactFile = new File(basedir, base + '/smartics-commons-0.5.2.jar')
assert !artifactFile.exists()

def baseTransDep = targetDir + '/commons-io/main'
def artifactFileTransDep = new File(basedir, baseTransDep + '/commons-io-1.4.jar')
assert !artifactFileTransDep.exists()
