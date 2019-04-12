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

package io.sarl.sarlsh.commands;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import org.eclipse.xtend.core.xtend.XtendMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.interpreter.IEvaluationResult;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;

import io.sarl.lang.compiler.batch.SarlOnTheFlyCompiler;
import io.sarl.lang.interpreter.SarlInterpreter;
import io.sarl.lang.jvmmodel.SarlJvmModelAssociations;
import io.sarl.lang.sarl.SarlAction;
import io.sarl.lang.sarl.SarlClass;
import io.sarl.lang.sarl.SarlScript;
import io.sarl.sarlsh.Constants;
import io.sarl.sarlsh.api.AwareEvaluationContext;
import io.sarl.sarlsh.api.ShellAPI;
import io.sarl.sarlsh.api.ShellExitException;
import io.sarl.sarlsh.configs.SarlConfig;
import io.sarl.sarlsh.configs.subconfigs.ValidatorConfig;

/**
 * Command for running the SARL shell.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.10
 */
public class ShellCommand extends CommandWithMetadata {

	private static final String LINKAGE_ERROR_CODE = "org.eclipse.xtext.diagnostics.Diagnostic.Linking"; //$NON-NLS-1$

	private final Provider<LineReader> lineReader;

	private final Provider<SarlOnTheFlyCompiler> compiler;

	private final Provider<SarlInterpreter> interpreter;

	private Provider<SarlJvmModelAssociations> associations;

	private final Provider<AwareEvaluationContext> context;

	private final Provider<SarlConfig> config;

	private final Logger logger;


	/** Constructor.
	 *
	 * @param lineReader the provider of the terminal line reader.
	 * @param compiler the provider of SARL compilers.
	 * @param interpreter the provider of SARL interpreters.
	 * @param associations the associator.
	 * @param context the provider of evaluation contexts.
	 * @param config the provider of configuration.
	 * @param logger the logger to be used for errors and warnings.
	 */
	public ShellCommand(Provider<LineReader> lineReader,
			Provider<SarlOnTheFlyCompiler> compiler,
			Provider<SarlInterpreter> interpreter,
			Provider<SarlJvmModelAssociations> associations,
			Provider<AwareEvaluationContext> context,
			Provider<SarlConfig> config,
			Logger logger) {
		super(CommandMetadata
				.builder(ShellCommand.class)
				.description(Messages.ShellCommand_0));
		assert lineReader != null;
		assert compiler != null;
		assert interpreter != null;
		assert associations != null;
		assert context != null;
		assert config != null;
		assert logger != null;
		this.lineReader = lineReader;
		this.compiler = compiler;
		this.interpreter = interpreter;
		this.associations = associations;
		this.context = context;
		this.config = config;
		this.logger = logger;
	}

	@SuppressWarnings({ "checkstyle:npathcomplexity", "resource" })
	@Override
	public CommandOutcome run(Cli cli) {
		try {
			final LineReader reader = this.lineReader.get();
			final Terminal terminal = reader.getTerminal();
			final SarlOnTheFlyCompiler compiler = this.compiler.get();
			final SarlInterpreter machine = this.interpreter.get();
			final SarlJvmModelAssociations assoc = this.associations.get();
			final AwareEvaluationContext context = this.context.get();
			final SarlConfig config = this.config.get();
			int interpretedResultNumber = Constants.FIRST_INTERPRETED_VALUE_INDEX;
			final List<String> expressionTypes = new LinkedList<>();
			while (true) {
				String line = null;
				try {
					try {
						line = reader.readLine(Constants.PROMPT, null, (MaskingCallback) null, null);
					} catch (UserInterruptException e) {
						// Ignore
					} catch (EndOfFileException e) {
						ShellAPI.exit();
					}
					if (line == null) {
						continue;
					}
					line = line.trim();
					if (Strings.isNullOrEmpty(line)) {
						continue;
					}

					final SarlScript script = compiler.parse(buildScript(expressionTypes, line));

					final List<Issue> issues = compiler.validate(script);
					boolean error = false;
					if (issues != null) {
						for (final Issue issue : issues) {
							if (!isIgnorableIssue(issue, context)) {
								error = printIssue(config, issue) || error;
							}
						}
					}

					if (!error) {
						final SarlAction action = extractAction(script);
						//context.newValue(XbaseScopeProvider.THIS, thisElement);
						final IEvaluationResult result = machine.evaluate(action.getExpression(), context, CancelIndicator.NullImpl);
						if (result != null) {
							if (result.getException() != null) {
								printError(result.getException());
							} else {
								final String variableName = ShellAPI.$buildInterpretedExpressionResultName(interpretedResultNumber);
								final Object rawResult = result.getResult();
								context.newValue(
										QualifiedName.create(variableName),
										rawResult);
								printValue(terminal, variableName, rawResult);
								expressionTypes.add(extractReturnType(action, assoc).getIdentifier());
								++interpretedResultNumber;
							}
						}
					}
				} catch (ShellExitException exception) {
					terminal.flush();
					final int code = exception.getReturnCode();
					if (code != 0) {
						return CommandOutcome.failed(code, ""); //$NON-NLS-1$
					}
					return CommandOutcome.succeeded();
				} catch (Throwable exception) {
					printError(Throwables.getRootCause(exception));
				}
			}
		} catch (Throwable exception) {
			return CommandOutcome.failed(Constants.ERROR_CODE,
					exception.getLocalizedMessage(), Throwables.getRootCause(exception));
		}
	}

	/** Replies if the given issue could be ignored by hard-coded behavior.
	 *
	 * <p>By default, the issue is ignorable if it corresponds to a linkage error
	 * to variable that is defined into the given context
	 *
	 * @param issue the issue to test.
	 * @param context the evaluation context.
	 * @return {@code true} if the given issue is ignorable; {@code false} if not.
	 */
	@SuppressWarnings("static-method")
	protected boolean isIgnorableIssue(Issue issue, AwareEvaluationContext context) {
		if (LINKAGE_ERROR_CODE.equals(issue.getCode())) {
			final Object[] data = issue.getData();
			if (data != null && data.length > 0) {
				final Object candidate = data[0];
				if (candidate instanceof CharSequence) {
					final String varName = ((CharSequence) candidate).toString();
					if (context.isDeclaredValue(varName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@SuppressWarnings("static-method")
	private String buildScript(List<String> types, String line) {
		final StringBuilder code = new StringBuilder();
		code.append("package io.sarl.sarlsh.internal.script\n") //$NON-NLS-1$
			.append("import static extension ").append(ShellAPI.class.getName()).append(".*\n") //$NON-NLS-1$ //$NON-NLS-2$
			.append("class _____Synthetic_____ {\n"); //$NON-NLS-1$
		int i = Constants.FIRST_INTERPRETED_VALUE_INDEX;
		for (final String type : types) {
			code.append("  static var ") //$NON-NLS-1$
				.append(ShellAPI.$buildInterpretedExpressionResultName(i))
				.append(" : "); //$NON-NLS-1$
			if (type == null) {
				code.append("Object"); //$NON-NLS-1$
			} else {
				code.append(type);
			}
			code.append("\n"); //$NON-NLS-1$
			++i;
		}
		code.append("\n  private new { exit }\n") //$NON-NLS-1$
			.append("  static def ___synthetic___ {\n    ") //$NON-NLS-1$
			.append(line)
			.append("\n  }\n"); //$NON-NLS-1$
		code.append("}\n"); //$NON-NLS-1$
		return code.toString();
	}

	@SuppressWarnings("static-method")
	private SarlAction extractAction(SarlScript script) {
		final List<XtendMember> members = ((SarlClass) script.getXtendTypes().get(0)).getMembers();
		return (SarlAction) members.get(members.size() - 1);
	}

	@SuppressWarnings("static-method")
	private JvmTypeReference extractReturnType(SarlAction action, SarlJvmModelAssociations assoc) {
		final JvmOperation operation = assoc.getDirectlyInferredOperation(action);
		final JvmTypeReference type = operation.getReturnType();
		if (type == null || "void".equals(type.getIdentifier())) { //$NON-NLS-1$
			return null;
		}
		return type;
	}

	/** Standard formatting of the issue's message.
	 *
	 * @param issue the issue.
	 * @return the error message.
	 */
	protected static String formatMessage(Issue issue) {
		return MessageFormat.format("{0} ({1})", issue.getMessage(), issue.getCode()); //$NON-NLS-1$
	}

	/** Print an issue according to the given configuration.
	 *
	 * @param config the current configuration.
	 * @param issue the error to output.
	 * @return {@code true} if the issue is an error.
	 */
	protected boolean printIssue(SarlConfig config, Issue issue) {
		if (issue.isSyntaxError()) {
			this.logger.error(formatMessage(issue));
			return true;
		}
		final ValidatorConfig validatorConfig = config.getValidator();
		Severity severity = validatorConfig.getWarningLevels().get(issue.getCode());
		if (severity == null || issue.getSeverity().compareTo(severity) > 0) {
			severity = issue.getSeverity();
		}
		if (severity != null) {
			switch (severity) {
			case ERROR:
				this.logger.error(formatMessage(issue));
				return true;
			case WARNING:
				if (validatorConfig.getAllErrors()) {
					this.logger.error(formatMessage(issue));
					return true;
				}
				if (validatorConfig.getAllWarnings() || !validatorConfig.getIgnoreWarnings()) {
					this.logger.warn(formatMessage(issue));
				}
				break;
			case INFO:
				this.logger.info(formatMessage(issue));
				break;
			case IGNORE:
			default:
			}
		}
		return false;
	}

	/** Print an error message on the terminal.
	 *
	 * @param exception the error to output.
	 */
	protected void printError(Throwable exception) {
		assert exception != null;
		final Throwable cause = Throwables.getRootCause(exception);
		final String msg = Strings.nullToEmpty(cause.getLocalizedMessage());
		this.logger.error(msg, exception);
		//this.logger.error(msg);
		//this.logger.debug("", cause); //$NON-NLS-1$
	}

	/** Print a value on the terminal.
	 *
	 * @param output the output terminal.
	 * @param variableName the name of the variable that contains the result.
	 * @param value the value to print.
	 */
	@SuppressWarnings({"static-method", "resource"})
	protected void printValue(Terminal output, String variableName, Object value) {
		final PrintWriter out = output.writer();
		out.print(variableName);
		out.print(" = "); //$NON-NLS-1$
		out.println(Objects.toString(value));
		output.flush();
	}

}
