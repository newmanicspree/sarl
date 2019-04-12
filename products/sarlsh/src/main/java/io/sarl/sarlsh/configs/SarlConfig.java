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

package io.sarl.sarlsh.configs;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.ConfigurationFactory;

import io.sarl.sarlsh.configs.subconfigs.InterpreterConfig;
import io.sarl.sarlsh.configs.subconfigs.ValidatorConfig;

/**
 * Configuration for the sarl tool.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
@BQConfig("Configuration of the SARLC tool")
public class SarlConfig {

	/**
	 * Prefix for the configuration entries of the modules.
	 */
	public static final String PREFIX = "sarl"; //$NON-NLS-1$

	/**
	 * Name of the property that contains the classpath.
	 */
	public static final String CLASSPATH_NAME = PREFIX + ".classpath"; //$NON-NLS-1$

	/**
	 * Name of the property that contains the SARL boot classpath.
	 */
	public static final String BOOT_CLASSPATH_NAME = PREFIX + ".bootClasspath"; //$NON-NLS-1$

	private String classpath;

	private String bootClasspath;

	private InterpreterConfig interpreterConfig;

	private ValidatorConfig validatorConfig;

	/** Replies the configuration for SARLSH.
	 *
	 * @param configFactory the general configuration factory.
	 * @return the SARLC configuration.
	 */
	public static SarlConfig getConfiguration(ConfigurationFactory configFactory) {
		assert configFactory != null;
		return configFactory.config(SarlConfig.class, PREFIX);
	}

	/** Replies the classpath.
	 *
	 * @return the classpath
	 */
	public String getClasspath() {
		return this.classpath;
	}

	/** Change the class path.
	 *
	 * @param path the class path.
	 */
	@BQConfigProperty("Class path for the SARL compiler.")
	public void setClasspath(String  path) {
		this.classpath = path;
	}

	/** Replies the boot classpath.
	 *
	 * @return the boot classpath
	 */
	public String  getBootClasspath() {
		return this.bootClasspath;
	}

	/** Change the boot class path.
	 *
	 * @param path the boot class path.
	 */
	@BQConfigProperty("SARL boot class path for the SARL compiler.")
	public void setBootClasspath(String  path) {
		this.bootClasspath = path;
	}

	/** Replies the validator configuration.
	 *
	 * @return the validator configuration.
	 */
	public ValidatorConfig getValidator() {
		if (this.validatorConfig == null) {
			this.validatorConfig = new ValidatorConfig();
		}
		return this.validatorConfig;
	}

	/** Change the validator configuration.
	 *
	 * @param config the validator configuration.
	 */
	@BQConfigProperty("Configuration of the SARL validator.")
	public void setValidator(ValidatorConfig config) {
		this.validatorConfig = config;
	}

	/** Replies the interpreter configuration.
	 *
	 * @return the interpreter configuration.
	 */
	public InterpreterConfig getInterpreter() {
		if (this.interpreterConfig == null) {
			this.interpreterConfig = new InterpreterConfig();
		}
		return this.interpreterConfig;
	}

	/** Change the interpreter configuration.
	 *
	 * @param config the interpreter configuration.
	 */
	@BQConfigProperty("Configuration of the SARL interpreter.")
	public void setInterpreter(InterpreterConfig config) {
		this.interpreterConfig = config;
	}

}
