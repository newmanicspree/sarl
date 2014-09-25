/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.docs.gettingstarted

import com.google.inject.Inject
import io.sarl.docs.utils.SARLParser
import io.sarl.docs.utils.SARLSpecCreator
import io.sarl.eclipse.wizards.newproject.Config
import org.jnario.runner.CreateWith

import static extension io.sarl.docs.utils.SpecificationTools.*
import static extension org.junit.Assume.*

/* @outline
 *
 * For developing with SARL, you should create a project.
 * This document describes two ways for created SARL projects
 * in Eclipse.
 */
@CreateWith(SARLSpecCreator)
describe "Create First Project" {
	
	@Inject extension SARLParser

	/* For creating a project, you should open your Eclipse and click on
	 * **File > New > Projects**, and select *SARL Project* in
	 * the SARL category.
	 *
	 * <center><img alt="Select the SARL Project Type" src="new_sarl_project_screen_1.png" width="60%" /></center>
	 *
	 *
	 * After clicking on **Next**, the wizard is displaying the first page for creating a SARL project.
	 */
	describe "Create a SARL Project" {
		
		/* You must enter the name of your project. You could change the standard SARL and Java environment
		 * configurations as well.
		 * 
		 * <center><img alt="Enter the Project Information" src="new_sarl_project_screen_2.png" width="60%" /></center>
		 *
		 *
		 * Then you could click on **Next** for continuing the edition of the project's properties, or
		 * simply click on the **Finish** button for creating the project with the default properties.
		 * 
		 * 
		 * The rest of this section is devoted to the edition of the additional properties for the SARL project.
		 * 
		 * @filter(.*) 
		 */
		fact "Step 1: Entering the project information" {
			"new_sarl_project_screen_1.png" should beAccessibleFrom this
			"new_sarl_project_screen_2.png" should beAccessibleFrom this
		}
		 
		/* The second page of the wizard contains the building settings.
		 * Two tabs are really interesting: the *Source* and the *Libraries*.
		 * 
		 * The *Source* tab defines the folders in your project that must contains source code files.
		 * By default, a SARL project is composed of four source folders:
		 *
		 * * `src/main/java`: for your Java classes;
		 * * `src/main/sarl`: for your SARL scripts;
		 * * `src/main/generated-sources/xtend`: for the Java codes generated by the SARL compiler (you should not change them yourself);
		 * * `src/main/resources`: for the files that are not SARL nor Java code.
		 * 
		 * The default output folder is `target/classes`.
		 * 
		 * <note>The names of these folders are following the
		 * conventions of a Maven-based project (described below). In this way, you will be able to 
		 * turn the Maven nature on your SARL project on/off.</note>
		 *
		 * <center><img alt="Source Code Folders" src="new_sarl_project_screen_3.png" width="60%" /></center>
		 * 
		 * @filter(.*) 
		 */
		fact "Step 2: Configuration of the source folders" {
			Config::FOLDER_SOURCE_JAVA should be "src/main/java"
			Config::FOLDER_SOURCE_SARL should be "src/main/sarl"
			Config::FOLDER_SOURCE_GENERATED should be "src/main/generated-sources/xtend"
			Config::FOLDER_RESOURCES should be "src/main/resources"
			Config::FOLDER_BIN should be "target/classes"
			"new_sarl_project_screen_3.png" should beAccessibleFrom this
		}
		 
	}

	/* For creating a project with both the Maven and SARL natures, you should open your 
	 * Eclipse and click on
	 * **File > New > Others > Maven > Maven Project**.
	 * 
	 * Follow the steps of the project creation wizard, and finally click on the **Finish** button.
	 */
	describe "Create a Project with the Maven and SARL Natures" {
		
		/* Open the file `pom.xml`, and edit it for obtaining a content similar to the
		 * configuration below.
		 * 
		 * Replace the version number `%sarlversion%` of SARL
		 * with the one you want to use. You could search on the
		 * [Maven Central Repository](http://search.maven.org) for
		 * the last available version.
		 * The file [VERSION.txt](%sarlmavenrepository%/VERSION.txt)
		 * provides the latest version numbers of the SARL artifacts, as well.
		 * 
		 *     <project>
		 *        ...
		 *        <properties>
		 *           ...
		 *           <sarl.version>%sarlversion%</sarl.version>
		 *        </properties>
		 *        ...
		 *        <build>
		 *           <plugins>
		 *              ...
		 *              <plugin>
		 *                 <groupId>org.codehaus.mojo</groupId>
		 *                 <artifactId>build-helper-maven-plugin</artifactId>
		 *                 <executions>
		 *                    <execution>
		 *                       <goals>
		 *                          <goal>add-source</goal>
		 *                       </goals>
		 *                       <configuration>
		 *                          <sources>
		 *                             <source>src/main/sarl</source>
		 *                             <source>src/main/generated-sources/xtend/</source>
		 *                             <source>src/test/generated-sources/xtend/</source>
		 *                          </sources>
		 *                       </configuration>
		 *                    </execution>
		 *                 </executions>
		 *              </plugin>
		 *              <plugin>
		 *                 <groupId>org.apache.maven.plugins</groupId>
		 *                 <artifactId>maven-clean-plugin</artifactId>
		 *                 <executions>
		 *                    <execution>
		 *                       <phase>clean</phase>
		 *                       <goals>
		 *                          <goal>clean</goal>
		 *                       </goals>
		 *                       <configuration>
		 *                          <filesets>
		 *                             <fileset>
		 *                                <directory>src/main/generated-sources/xtend</directory>
		 *                             </fileset>
		 *                             <fileset>
		 *                                <directory>src/test/generated-sources/xtend</directory>
		 *                             </fileset>
		 *                          </filesets>
		 *                       </configuration>
		 *                    </execution>
		 *                 </executions>
		 *              </plugin>
		 *              <plugin>
		 *                 <groupId>org.eclipse.xtext</groupId>
		 *                 <artifactId>xtext-maven-plugin</artifactId>
		 *                 <executions>
		 *                    <execution>
		 *                       <goals>
		 *                          <goal>generate</goal>
		 *                       </goals>
		 *                    </execution>
		 *                 </executions>
		 *                 <configuration>
		 *                    <compilerSourceLevel>%compilerlevel%</compilerSourceLevel>
		 *                    <compilerTargetLevel>%compilerlevel%</compilerTargetLevel>
		 *                    <encoding>%encoding%</encoding>
		 *                    <languages>
		 *                       <language>
		 *                          <setup>io.sarl.lang.SARLStandaloneSetup</setup>
		 *                          <outputConfigurations>
		 *                             <outputConfiguration>
		 *                                <outputDirectory>src/main/generated-sources/xtend/</outputDirectory>
		 *                             </outputConfiguration>
		 *                          </outputConfigurations>
		 *                       </language>
		 *                    </languages>
		 *                 </configuration>
		 *                 <dependencies>
		 *                    <dependency>
		 *                       <groupId>io.sarl.lang</groupId>
		 *                       <artifactId>io.sarl.lang</artifactId>
		 *                       <version>${sarl.version}</version>
		 *                    </dependency>
		 *                    <dependency>
		 *                       <groupId>io.sarl.lang</groupId>
		 *                       <artifactId>io.sarl.lang.core</artifactId>
		 *                       <version>${sarl.version}</version>
		 *                    </dependency>
		 *                 </dependencies>
		 *              </plugin>
		 *           </plugins>
		 *        </build>
		 *        ...
		 *        <dependencies>
		 *           ...
		 *           <dependency>
		 *              <groupId>io.sarl.maven</groupId>
		 *              <artifactId>io.sarl.maven</artifactId>
		 *              <version>${sarl.version}</version>
		 *           </dependency>
		 *           ...
		 *        </dependencies>
		 *        ...
		 *        <repositories>
		 *           ...
		 *           <!-- The following repositories are needed until the
		 *                SARL is released on the Maven Central -->
		 *           <repository>
		 *              <id>sarl-repository</id>
		 *              <url>%sarlmavenrepository%</url>
		 *           </repository>
		 *        </repositories>
		 *        ...
		 *     </project>
		 * 
		 * @filter(.*) 
		 */
		fact "Edit the Maven configuration" {
			// Check if the SARL code is generated in the expected folder
			Config::FOLDER_SOURCE_GENERATED should be "src/main/generated-sources/xtend"
			// The checks are valid only if the macro replacements were done.
			// The replacements are done by Maven.
			// So, Eclipse Junit tools do not make the replacements.
			System.getProperty("sun.java.command", "").startsWith("org.eclipse.jdt.internal.junit.").assumeFalse
			// This documentation is valid only for the SNAPSHOT (repository definition)
			"%sarlversion%" should endWith "-SNAPSHOT"
			"%sarlversion%" should beMavenVersion true
			// URLs should not end with a slash
			"%website%" should beURL "!file"
			"%sarlmavenrepository%" should beURL "!file"
		}
		
		/* For executing your SARL program, you must select a
		 * [runtime environment](%website%/runtime/index.html).
		 * 
		 * The runtime environment that is recommended by the developers of SARL
		 * is [Janus](http://www.janusproject.io). 
		 * 
		 * If you want to embed the runtime environment inside the Jar files
		 * of your SARL application, it is recommended to put it in the
		 * Maven dependencies.
		 * 
		 * <note>You could remove the dependencies to the 
		 * SARL artifacts in the previous Maven configuration. Indeed, the Janus platform depends
		 * already on. You will obtain the SARL artifacts by transitivity.</note>
		 *
		 * Replace the version number (`%janusversion%`) of the [Janus platform](http://www.janusproject.io)
		 * with the one you want to use. You could search on the
		 * [Maven Central Repository](http://search.maven.org) for
		 * the last available version.
		 * The file [VERSION.txt](%janusmavenrepository%/VERSION.txt)
		 * provides the latest version numbers of the Janus artifacts, as well.
		 * 
		 * 
		 *     <project>
		 *        ...
		 *        <properties>
		 *           ...
		 *           <janus.version>%janusversion%</janus.version>
		 *        </properties>
		 *        ...
		 *        <dependencies>
		 *           ...
		 *           <dependency>
		 *              <groupId>io.janusproject</groupId>
		 *              <artifactId>io.janusproject.kernel</artifactId>
		 *              <version>${janus.version}</version>
		 *           </dependency>
		 *           ...
		 *        </dependencies>
		 *        ...
		 *        <repositories>
		 *           ...
		 *           <!-- The following repositories are needed until the
		 *                Janus is released on the Maven Central -->
		 *           <repository>
		 *              <id>janus-repository</id>
		 *              <url>%janusmavenrepository%</url>
		 *           </repository>
		 *        </repositories>
		 *        ...
		 *     </project>
		 * 
		 * @filter(.*) 
		 */
		fact "Configuration a runtime environment (optional)" {
			// The checks are valid only if the macro replacements were done.
			// The replacements are done by Maven.
			// So, Eclipse Junit tools do not make the replacements.
			System.getProperty("sun.java.command", "").startsWith("org.eclipse.jdt.internal.junit.").assumeFalse
			// This documentation is valid only for the SNAPSHOT (repository definition)
			"%janusversion%" should endWith "-SNAPSHOT"
			"%janusversion%" should beMavenVersion true
			// URLs should not end with a slash
			"%website%" should beURL "!file"
			"%janusmavenrepository%" should beURL "!file"
		}
	} 
	
	/*
	 * In the next section, we will learn how to create our first agent.
	 * 
	 * [Next>](AgentDefinitionIntroductionSpec.html)
	 * 
	 * @filter(.*)
	 */
	fact "What's next?" {
		"AgentDefinitionIntroductionSpec.html" should beAccessibleFrom this
	}

}
