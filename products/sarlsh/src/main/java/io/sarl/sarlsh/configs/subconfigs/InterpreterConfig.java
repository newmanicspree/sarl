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

package io.sarl.sarlsh.configs.subconfigs;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

import io.sarl.sarlsh.configs.SarlConfig;

/**
 * Configuration for the interpreter.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
@BQConfig("Configuration of the SARL validator")
public class InterpreterConfig {

	/**
	 * Prefix for the configuration entries of the path modules.
	 */
	public static final String PREFIX = SarlConfig.PREFIX + ".interpreter"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates if warnings are ignored.
	 */
	public static final String SHOW_STACK_TRACE_NAME = PREFIX + ".showStackTrace"; //$NON-NLS-1$

	private boolean showStackTrace;

	/** Replies if the stack trace should be displayed with internal errors.
	 *
	 * @return {@code true} to display the stack trace.
	 */
	public boolean getShowStackTrace() {
		return this.showStackTrace;
	}

	/** Change the flag if the stack trace should be displayed with internal errors.
	 *
	 * @param showStackTrace is {@code true} to display the stack trace.
	 */
	@BQConfigProperty("Specify if the stack trace must be displayed for internal errors")
	public void setShowStackTrace(boolean showStackTrace) {
		this.showStackTrace = showStackTrace;
	}

}
