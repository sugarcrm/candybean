package com.sugarcrm.candybean.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.utilities.Utils.Pair;

public class VPage {
	
	public enum Type { VISIBLE, TEXT, EXIST }
	
	public List<VPage> linkedPages;
	public Map<String, VControl> controls;
	public Pair<Type, VControl> loadAsserts;
	
	public VPage() {
//		this.controls = new HashMap<String, VControl>();
	}

}
