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

package io.sarl.sarlsh.modules.internal;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import io.sarl.sarlsh.Constants;

/** Module for creating the SARL CLI.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public class SarlCliModule extends AbstractModule {

	@Override
	protected void configure() {
		//
	}

	/** Replies the CLI terminal.
	 *
	 * @return the CLI terminal
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public Terminal provideCliTerminal() {
		try {
			return TerminalBuilder.builder()
					.encoding(Charset.defaultCharset())
					.name(Constants.PROGRAM_NAME)
					.build();
		} catch (IOException exception) {
			throw new IOError(exception);
		}
	}

	/** Replies the CLI line reader.
	 *
	 * @param terminal a provider of terminal.
	 * @return the CLI line reader
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public LineReader provideCliLineReader(Provider<Terminal> terminal) {
		final LineReader reader = LineReaderBuilder.builder()
				.appName(Constants.PROGRAM_NAME)
				.terminal(terminal.get())
				//.completer(new Completers.FileNameCompleter())
				//.parser(new DefaultParser())
				//.variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ") //$NON-NLS-1$
				.build();
		return reader;
	}

}
