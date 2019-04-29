/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2019 the original authors or authors.
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

package io.sarl.sarldoc.modules.commands;

import static io.bootique.BQCoreModule.extend;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.command.CommandManager;
import org.slf4j.Logger;

import io.sarl.lang.sarlc.configs.SarlcConfig;
import io.sarl.lang.sarlc.tools.PathDetector;
import io.sarl.lang.sarlc.tools.SARLClasspathProvider;
import io.sarl.sarldoc.commands.SarldocCommand;
import io.sarl.sarldoc.configs.SarldocConfig;

/** Module for the sarldoc command.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$compiler
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public class SarldocCommandModule extends AbstractModule {

	@Override
	protected void configure() {
		extend(binder()).addCommand(SarldocCommand.class);
	}

	/** Provide the command for running sarldoc.
	 *
	 * @param manager the provider of the manager of the commands.
	 * @param dconfig the provider of sarldoc configuration.
	 * @param cconfig the provider of sarlc configuration.
	 * @param defaultBootClasspath the provider of the default boot class path.
	 * @param pathDetector the provider of path detector.
	 * @param loggerProvider the provider of SLF4J logger.
	 * @return the command.
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public SarldocCommand provideSarldocCommand(Provider<CommandManager> manager, Provider<SarldocConfig> dconfig,
			Provider<SarlcConfig> cconfig, Provider<SARLClasspathProvider> defaultBootClasspath,
			Provider<PathDetector> pathDetector, Provider<Logger> loggerProvider) {
		return new SarldocCommand(manager, loggerProvider, dconfig, cconfig, defaultBootClasspath, pathDetector);
	}

}
