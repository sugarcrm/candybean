package com.sugarcrm.candybean.model;

import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.utilities.Utils.Triplet;

public class VLinkValue {
	
	public enum Type { CLICK, STRING, SELECT }
	
	public Triplet<Type, VControl, Object> requiredValue;

}
