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

package io.sarl.sarlsh.modules.commands;

import static io.bootique.BQCoreModule.extend;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jline.reader.LineReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sarl.lang.compiler.batch.SarlOnTheFlyCompiler;
import io.sarl.lang.interpreter.SarlInterpreter;
import io.sarl.lang.jvmmodel.SarlJvmModelAssociations;
import io.sarl.sarlsh.Constants;
import io.sarl.sarlsh.api.AwareEvaluationContext;
import io.sarl.sarlsh.commands.ShellCommand;
import io.sarl.sarlsh.configs.SarlConfig;

/** Module for the shell command.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.100
 */
public class ShellCommandModule extends AbstractModule {

	@Override
	protected void configure() {
		extend(binder()).addCommand(ShellCommand.class);
	}

	/** Provide the command for running the shell.
	 *
	 * @param lineReader the CLI provider.
	 * @param compiler the compiler provider.
	 * @param interpreter the interpreter provider.
	 * @param associations the provider of association accessors.
	 * @param context evaluation context provider.
	 * @param config the configuration provider.
	 * @return the command.
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public ShellCommand provideShellCommand(
			Provider<LineReader> lineReader,
			Provider<SarlOnTheFlyCompiler> compiler,
			Provider<SarlInterpreter> interpreter,
			Provider<SarlJvmModelAssociations> associations,
			Provider<AwareEvaluationContext> context,
			Provider<SarlConfig> config) {
		final Logger logger = LoggerFactory.getLogger(Constants.PROGRAM_NAME);
		return new ShellCommand(lineReader, compiler, interpreter, associations, context, config, logger);
	}

}
