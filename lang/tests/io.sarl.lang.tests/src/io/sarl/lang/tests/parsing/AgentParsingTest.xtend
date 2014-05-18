/*
 * Copyright 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.lang.tests.parsing

import com.google.inject.Inject
import io.sarl.lang.SARLInjectorProvider
import io.sarl.lang.sarl.Model
import io.sarl.lang.sarl.SarlPackage
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

/**
 * @author $Author: srodriguez$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RunWith(XtextRunner)
@InjectWith(SARLInjectorProvider)
class AgentParsingTest {
	@Inject extension ParseHelper<Model>
	@Inject extension ValidationTestHelper

	@Test def void testParse() {
		val mas = '''
			package test
			agent A1 {}
			agent A2 {}
		'''.parse
		mas.assertNoErrors
		assertEquals(2, mas.elements.size)
	}

	@Test def parseBehaviorDeclaration() {
		val mas = '''
			event E {}
			agent A1 {
				on E {}
			}
		'''.parse
		mas.assertNoErrors
		assertEquals(2, mas.elements.size)
	}

	@Test def parseBehaviorWithGuard() {
		val mas = '''
			event E {}
			agent A1 {
				on E [ occurrence.source != null] {}
			}
		'''.parse
		mas.assertNoErrors
	}

	@Test def parseAgentWithAttributes() {
		val mas = '''
			agent A1 {
				var name : String = "Hello"
				var number : Integer
			}
		'''.parse
		mas.assertNoErrors
	}

	@Test def parseAgentWithConstAttributes() {
		val mas = '''
			
			agent A1 {
				val name : String = "Hello"
				var number : Integer
			}
		'''.parse
		mas.assertNoErrors
	}

	@Test def eventsMustBeDeclared() {
		val mas = '''
			
			agent A1 {
				on E  {}
			}
		'''.parse

		mas.assertError(SarlPackage::eINSTANCE.behaviorUnit, Diagnostic::LINKING_DIAGNOSTIC,
			"Couldn't resolve reference to Event 'E'.")
	}

	@Test @Ignore("not ready yet")
	def constAttributesMustHaveIniatlizer() {
		val mas = '''
			
			agent A1 {
				val name : String = "Hello"
				val number : Integer
			}
		'''.parse
		mas.assertError(
			SarlPackage::eINSTANCE.attribute,
			Diagnostic::SYNTAX_DIAGNOSTIC,
			"Constant attribute 'number' must be initialized ."
		)
	}

	@Test
	def capacityMustBeDeclaredBeforeUse() {
		val mas = '''
			agent A1 {
				uses MyCap
			}
		'''.parse		
		mas.assertError(
			SarlPackage::eINSTANCE.capacityUses,
			Diagnostic::LINKING_DIAGNOSTIC,
			"Couldn't resolve reference to Capacity 'MyCap'."
		)
	}

	@Test
	def agentCanDeclareCapacityUses() {
		val mas = '''
			capacity MyCap {
				def my_operation
			}
			
			agent A1 {
				uses MyCap
			}
		'''.parse
		mas.assertNoErrors

	}
	

}