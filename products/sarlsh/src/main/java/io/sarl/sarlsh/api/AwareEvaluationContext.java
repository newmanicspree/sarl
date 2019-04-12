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

import java.text.MessageFormat;
import java.util.Map;

import com.google.common.collect.Maps;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.lib.Pure;

/** An evaluation context that is aware of the declared variables.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public final class AwareEvaluationContext implements IEvaluationContext {

	private AwareEvaluationContext parent;

	private Map<QualifiedName, Object> values;

	/** Constructor.
	 */
	public AwareEvaluationContext() {
		//
	}

	/** Constructor.
	 *
	 * @param parent the parent context.
	 */
	public AwareEvaluationContext(AwareEvaluationContext parent) {
		this.parent = parent;
	}

	@Override
	public Object getValue(QualifiedName qualifiedName) {
		if (this.values != null && this.values.containsKey(qualifiedName)) {
			return this.values.get(qualifiedName);
		}
		if (this.parent != null) {
			return this.parent.getValue(qualifiedName);
		}
		return null;
	}

	@Override
	public void newValue(QualifiedName qualifiedName, Object value) {
		if (this.values == null) {
			this.values = Maps.newHashMap();
		}
		if (this.values.containsKey(qualifiedName)) {
			throw new IllegalStateException(MessageFormat.format(Messages.AwareEvaluationContext_0, qualifiedName));
		}
		this.values.put(qualifiedName, value);
	}

	@Override
	public void assignValue(QualifiedName qualifiedName, Object value) {
		if (this.values == null || !this.values.containsKey(qualifiedName)) {
			if (this.parent == null) {
				throw new IllegalStateException(Messages.AwareEvaluationContext_1);
			}
			this.parent.assignValue(qualifiedName, value);
		} else {
			this.values.put(qualifiedName, value);
		}
	}

	@Override
	public IEvaluationContext fork() {
		return new AwareEvaluationContext(this);
	}

	/** Replies if the given name corresponds to a declared value into this context.
	 *
	 * @param name the name to test.
	 * @return {@code true} if the given name is declared.
	 */
	@Pure
	public boolean isDeclaredValue(String name) {
		return isDeclaredValue(QualifiedName.create(name));
	}

	/** Replies if the given name corresponds to a declared value into this context.
	 *
	 * @param name the name to test.
	 * @return {@code true} if the given name is declared.
	 */
	@Pure
	public boolean isDeclaredValue(QualifiedName name) {
		if (this.values != null && this.values.containsKey(name)) {
			return true;
		}
		if (this.parent != null) {
			return this.parent.isDeclaredValue(name);
		}
		return false;
	}

}
