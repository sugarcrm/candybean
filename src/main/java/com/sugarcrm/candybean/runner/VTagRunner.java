/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.runner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class VTagRunner extends BlockJUnit4ClassRunner {
	public VTagRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	// TODO add validation/error checking, flexibility in provided class/method name, etc.
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		final List<FrameworkMethod> testMethods = getTestClass().getAnnotatedMethods(Test.class);
        if (testMethods == null || testMethods.size() == 0) return testMethods;
        final List<FrameworkMethod> finalTestMethods = new ArrayList<FrameworkMethod>(testMethods.size());
		try {
			for (final FrameworkMethod method : testMethods) {
				final VTag vTag = method.getAnnotation(VTag.class);
				if (vTag != null) {
					if (vTag.tags().length != 0) {
						if (!vTag.tagLogicClass().isEmpty() && !vTag.tagLogicMethod().isEmpty()) {
							for (String tag : vTag.tags()) {
//								System.out.println("method:" + method.getName() + ", tag:" + tag + ", logic class:" + vTag.tagLogicClass() + ", logic method:" + vTag.tagLogicMethod());
								Class<?> c = Class.forName(vTag.tagLogicClass());
								Method m = c.getDeclaredMethod(vTag.tagLogicMethod(), tag.getClass());
								if ((boolean) m.invoke(null, (Object) tag)) {
									finalTestMethods.add(method);
								}
							}
						} else {
							for (String tag : vTag.tags()) {
								if (Boolean.parseBoolean(System.getProperty(tag))) {
									finalTestMethods.add(method);
								}
							}
						}
					} else {
						finalTestMethods.add(method);
					}
				} else {
					finalTestMethods.add(method);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalTestMethods;
	}
}
