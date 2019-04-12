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

package io.sarl.lang.compiler.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.FileExtensionProvider;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.LazyStringInputStream;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import io.sarl.lang.sarl.SarlScript;


/** Front-end that enables access to the compiling features from an interpreter.
 *
 * <p>This class is inspired from {@code ResourceHelper}, {@code ParseHelper}
 * and {@code ValidationTestHelper} from Xtext.
 *
 * <p>This on-the-fly compiler provides tools for parsing and validating the SARL programs.
 * It consider only one resource at a time. It does not generate target programs (Java, etc.).
 * This on-the-fly compiler is useful for testing or interpreting SARL code.
 * For a full SARL batch compiler, see {@link SarlBatchCompiler}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 * @see SarlBatchCompiler
 */
public class SarlOnTheFlyCompiler {

	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;

	@Inject
	private IResourceFactory resourceFactory;

	private String fileExtension;

	/** Change the SARL filename extension.
	 *
	 * @param extensionProvider the provider of the extensions for SARL scripts.
	 */
	@Inject
	public void setFileExtensionProvider(FileExtensionProvider extensionProvider) {
		setFileExtension(extensionProvider.getPrimaryFileExtension());
	}

	/** Change the SARL filename extension.
	 *
	 * @param fileExtension the extension for SARL scripts.
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/** Create a SARL resource with the given content.
	 *
	 * @param in the content of the resource.
	 * @param uriToUse the URI of the resource.
	 * @param options the generations options.
	 * @param resourceSet the container of the resource.
	 * @return the created resource.
	 */
	public Resource resource(InputStream in, URI uriToUse, Map<?, ?> options, ResourceSet resourceSet) {
		final Resource resource = this.resourceFactory.createResource(uriToUse);
		resourceSet.getResources().add(resource);
		try {
			resource.load(in, options);
			return resource;
		} catch (IOException e) {
			throw new WrappedException(e);
		}
	}

	/** Compute the URI of a resource.
	 *
	 * @param resourceSet the resource set
	 * @return the URI.
	 */
	public URI computeUnusedUri(ResourceSet resourceSet) {
		final String name = "__synthetic"; //$NON-NLS-1$
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			final URI syntheticUri = URI.createURI(name + i + "." + this.fileExtension); //$NON-NLS-1$
			if (resourceSet.getResource(syntheticUri, false) == null) {
				return syntheticUri;
			}
		}
		throw new IllegalStateException();
	}

	/** Parse the given content.
	 *
	 * @param in the content to parse.
	 * @param uriToUse the URI of the resource.
	 * @param options the parsing options.
	 * @param resourceSet the container of the resource.
	 * @return the SARL script.
	 */
	public SarlScript parse(InputStream in, URI uriToUse, Map<?, ?> options, ResourceSet resourceSet) {
		final Resource resource = resource(in, uriToUse, options, resourceSet);
		final SarlScript root = (SarlScript) (resource.getContents().isEmpty() ? null : resource.getContents().get(0));
		return root;
	}

	/** Parse the given content.
	 *
	 * @param text the content to parse.
	 * @return the SARL script.
	 */
	public SarlScript parse(CharSequence text) {
		return parse(text, this.resourceSetProvider.get());
	}

	/** Parse the given content.
	 *
	 * @param text the content to parse.
	 * @param resourceSet the container of the resource.
	 * @return the SARL script.
	 */
	public SarlScript parse(CharSequence text, ResourceSet resourceSet) {
		return parse(getAsStream(text), computeUnusedUri(resourceSet), null, resourceSet);
	}

	/** Parse the given content.
	 *
	 * @param text the content to parse.
	 * @param uriToUse the URI of the resource.
	 * @param resourceSet the container of the resource.
	 * @return the SARL script.
	 */
	public SarlScript parse(CharSequence text, URI uriToUse, ResourceSet resourceSet) {
		return parse(getAsStream(text), uriToUse, null, resourceSet);
	}

	/** Create a stream from the given sequence of characters.
	 *
	 * @param text the content of the stream.
	 * @return the stream.
	 */
	@SuppressWarnings("static-method")
	protected InputStream getAsStream(CharSequence text) {
		return new LazyStringInputStream(text == null ? "" : text.toString()); //$NON-NLS-1$
	}

	/** Validate the given model.
	 *
	 * @param model the model to validate.
	 * @param mode the types of validation to apply. By default is it {@link CheckMode#ALL}.
	 * @return the issues.
	 */
	public List<Issue> validate(EObject model, CheckMode mode) {
		return validate(model.eResource(), mode);
	}

	/** Validate the given model.
	 *
	 * @param model the model to validate.
	 * @return the issues.
	 */
	public List<Issue> validate(EObject model) {
		return validate(model.eResource(), null);
	}

	/** Validate the given model.
	 *
	 * @param resource the resource to validate.
	 * @return the issues.
	 */
	public List<Issue> validate(Resource resource) {
		return validate(resource, null);
	}

	/** Validate the given model.
	 *
	 * @param resource the resource to validate.
	 * @param mode the types of validation to apply. By default is it {@link CheckMode#ALL}.
	 * @return the issues.
	 */
	@SuppressWarnings("static-method")
	public List<Issue> validate(Resource resource, CheckMode mode) {
		final IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider()
				.getResourceValidator();
		return validator.validate(resource, mode == null ? CheckMode.ALL : mode, CancelIndicator.NullImpl);
	}

}
