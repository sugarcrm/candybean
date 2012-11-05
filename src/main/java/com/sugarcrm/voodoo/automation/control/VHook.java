package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.IAutomation;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;


public class VHook {
	public final Strategy hookStrategy;
	public final String hookString;

	public VHook(Strategy hookStrategy, String hookString) {
		this.hookStrategy = hookStrategy;
		this.hookString = hookString;
	}
	
	public String toString() {
		return "VHook(" + this.hookStrategy + "," + this.hookString + ")";
	}
}
