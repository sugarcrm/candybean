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
package com.sugarcrm.candybean.automation.element;

import java.util.*;

/**
 * An element that represents and allows for interaction with a selector.
 * 
 * @author Conrad Warmbold
 */
public interface Selector {

    public boolean isMultiSelector();
	public void select(String value);
    public void select(int index);
    public void select(List<String> options);
    public void selectAll();
    public void deselect(String text);
    public void deselect(int index);
    public void deselect(List<String> options);
    public void deselectAll();
    public String getFirstSelectedOption();
    public List<String> getAllSelectedOptions();
    public List<String> getOptions();
    public boolean isSelected();
    public boolean isSelected(String text);
    public boolean isSelected(int index);
    public boolean isSelected(List<String> options);
    public boolean isSelected(List<String> options, boolean exclusive);
}
