package com.sugarcrm.voodoo.automation;

import com.sugarcrm.voodoo.IAutomation.Strategy;


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
