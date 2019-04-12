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

package io.sarl.sarlsh;

/** Constants for sarlsh.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public final class Constants {

	/** Return code when failure.
	 */
	public static final int ERROR_CODE = 255;

	/** Default prompt.
	 */
	public static final String PROMPT = "sarl> "; //$NON-NLS-1$

	/** Default name of the sarlsh program.
	 */
	public static final String PROGRAM_NAME = "sarlsh"; //$NON-NLS-1$

	/** Index of the first variable that will contain the results of the interpreted expressions.
	 */
	public static final int FIRST_INTERPRETED_VALUE_INDEX = 1;

	private Constants() {
		//
	}

}
