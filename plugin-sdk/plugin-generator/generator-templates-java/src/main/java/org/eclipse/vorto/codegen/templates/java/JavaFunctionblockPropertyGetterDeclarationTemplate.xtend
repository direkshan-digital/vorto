/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * Use Plugin SDK API instead!
 */
@Deprecated
class JavaFunctionblockPropertyGetterDeclarationTemplate implements ITemplate<FunctionblockProperty>{

	var String getterPrefix;
	var String interfacePrefix;
	
	new(String getterPrefix, String interfacePrefix) {
		this.getterPrefix = getterPrefix;
		this.interfacePrefix = interfacePrefix;
	}
	
	override getContent(FunctionblockProperty fbProperty,InvocationContext invocationContext) {
		'''
				/**
				* Getter for «fbProperty.name».
				*/
				public «interfacePrefix»«fbProperty.type.name» «getterPrefix»«fbProperty.name.toFirstUpper»();
		'''
	}
}