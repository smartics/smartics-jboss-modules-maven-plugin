smartics-jboss-modules-maven-plugin
===================================

Generates an archive of modules to be copied to an JBoss 7 installation.

##Overview

This [Maven] (http://maven.apache.org/) plugin creates an archive with [JBoss modules] (https://docs.jboss.org/author/display/MODULES/Home) to be installed to a [JBoss 7] (https://www.jboss.org/jbossas) server installation.

Crafting the modules by hand is more cumbersome than to define a recipe that allows to construct the modules folder with all dependencies already defined by a [Maven POM] (http://maven.apache.org/pom.html).

Please refer to

* [Usage Modules] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/usage-modules.html)
* [Modules XSD] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/jboss-modules-descriptor-doc.html)
* [Import Module Descriptors] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/import-modules.html)
* [Usage Jandex] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/usage-jandex.html)
* [Usage Index] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/usage-index.html)
for details on how to use this plugin.

In [Goals] (https://www.smartics.eu/smartics-jboss-modules-maven-plugin/plugin-info.html) you'll find a short description of the goals and its parameters.

Please note that this plugin only supports Maven from 3.1.x and up due to incompatibilities to the [Aether] (http://eclipse.org/aether/) implementation used by Maven. Please refer to [Bug 843] (http://www.smartics.eu/bugzilla/show_bug.cgi?id=843) for details. If you are looking for support for Maven 3.0.x, please refer to versions 0.x of this plugin or use [this branch] (https://github.com/smartics/smartics-jboss-modules-maven-plugin/tree/Last-Version-supporting-Maven-3.0.x).
