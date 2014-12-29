package org.vaadin.artur.demo;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.declarative.Design;

@DesignRoot
public class DemoPanel extends Panel {
	TextArea TA1, TA2, TA3;
	
	public DemoPanel() {
		Design.read(this);	
	}
}
