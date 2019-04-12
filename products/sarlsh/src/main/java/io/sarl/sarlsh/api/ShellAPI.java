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

package io.sarl.sarlsh.api;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * API for the SARL shell.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public final class ShellAPI {

	/** Prefix of the variable names that will contains the results of the interpreted expressions.
	 */
	public static final String INTERPRETER_RESULT_NAME_PREFIX = "expr"; //$NON-NLS-1$

	private ShellAPI() {
		//
	}

	/** Stop the interpreter.
	 *
	 * <p>The interpreter stops with exit code {@code 0}.
	 */
	@Inline(value = "new $2(0)", imported = ShellExitException.class, constantExpression = true)
	public static void exit() {
		exit(0);
	}

	/** Stop the interpreter.
	 *
	 * <p>The interpreter stops with given exit code.
	 *
	 * @param returnCode the code to give to the caller of the interpreter.
	 */
	@Inline(value = "new $2($1)", imported = ShellExitException.class, constantExpression = true)
	public static void exit(int returnCode) {
		throw new ShellExitException(returnCode);
	}

	/** Create the name of the variable that should contains the result of an interpreted expression.
	 *
	 * @param index the index of the interpreted expression.
	 * @return the variable name.
	 */
	@Pure
	public static String $buildInterpretedExpressionResultName(int index) {
		return INTERPRETER_RESULT_NAME_PREFIX + Integer.toString(index);
	}

}
