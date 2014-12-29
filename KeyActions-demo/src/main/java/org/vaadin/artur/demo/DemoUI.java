package org.vaadin.artur.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.KeyAction;
import org.vaadin.artur.KeyAction.KeyActionEvent;
import org.vaadin.artur.KeyAction.KeyActionListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tests.util.Log;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.artur.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	private Log log = new Log(5);

	@Override
	protected void init(VaadinRequest request) {
		log.log("Both TextAreas, the Panel and the UI listen to enter, shift-enter and alt-f");
		VerticalLayout content = new VerticalLayout();

		DemoPanel p = new DemoPanel();
		content.addComponent(log);
		content.addComponent(p);

		setId("UI");
		addListener(this, "Enter", KeyCode.ENTER);
		addListener(this, "Shift enter", KeyCode.ENTER, ModifierKey.SHIFT);
		addListener(this, "ALT-f", KeyCode.F, ModifierKey.ALT);
		
		addListener(p, "Enter", KeyCode.ENTER);
		addListener(p, "Shift enter", KeyCode.ENTER, ModifierKey.SHIFT);
		addListener(p, "ALT-f", KeyCode.F, ModifierKey.ALT);

		addListener(p.TA1, "Enter", KeyCode.ENTER);
		addListener(p.TA1, "Shift enter", KeyCode.ENTER, ModifierKey.SHIFT);
		addListener(p.TA1, "ALT-f", KeyCode.F, ModifierKey.ALT);

		KeyAction action = addListener(p.TA2, "Enter", KeyCode.ENTER);
		action.setStopPropagation(true);
		action.setPreventDefault(true);

		action = addListener(p.TA2, "Shift enter", KeyCode.ENTER,
				ModifierKey.SHIFT);
		action.setStopPropagation(true);
		action.setPreventDefault(true);

		action = addListener(p.TA2, "ALT-f", KeyCode.F,
				ModifierKey.ALT);
		action.setStopPropagation(true);
		action.setPreventDefault(true);
		
		action = addListener(p.TA3, "Enter", KeyCode.ENTER);
		action.setStopPropagation(true);
		action.setPreventDefault(false);
		
		action = addListener(p.TA3, "Shift enter", KeyCode.ENTER,
				ModifierKey.SHIFT);
		action.setStopPropagation(true);
		action.setPreventDefault(false);
		
		action = addListener(p.TA3, "ALT-f", KeyCode.F,
				ModifierKey.ALT);
		action.setStopPropagation(true);
		action.setPreventDefault(false);
		
		setContent(content);
	}

	private KeyAction addListener(AbstractComponent component,
			final String keyname, int keycode, int... modifierKey) {
		KeyAction action = new KeyAction(keycode, modifierKey);
		action.addKeypressListener(new KeyActionListener() {
			@Override
			public void keyPressed(KeyActionEvent keyPressEvent) {
				log.log(keyname + " pressed on "
						+ keyPressEvent.getComponent().getId());
			}
		});
		action.extend(component);
		return action;
	}

}
