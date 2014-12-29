package org.vaadin.artur.client;

import org.vaadin.artur.KeyAction;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.ShortcutActionHandler.BeforeShortcutActionListener;
import com.vaadin.shared.ui.Connect;

@Connect(KeyAction.class)
public class KeyActionConnector extends AbstractExtensionConnector {

	@Override
	protected void extend(ServerConnector target) {
		AbstractComponentConnector connector = (AbstractComponentConnector) target;

		connector.getWidget().addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (match(event)) {
					triggerEvent(event);
				}
			}
		}, KeyDownEvent.getType());

	}

	protected void triggerEvent(final KeyDownEvent event) {
		if (getState().stopPropagation) {
			event.stopPropagation();
		}
		if (getState().preventDefault) {
			event.preventDefault();
		}

		// Deferred so keypress + keyup can be handled before this happens so
		// pressing enter in a text area does not cause the enter to be lost
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				if (getParent() instanceof BeforeShortcutActionListener) {
					((BeforeShortcutActionListener) getParent())
							.onBeforeShortcutAction(Event.as(event
									.getNativeEvent()));
				}

				getRpcProxy(KeyActionServerRpc.class).trigger();
			}
		});
	}

	protected boolean match(KeyDownEvent event) {
		if (event.getNativeKeyCode() != getState().keyCode) {
			return false;
		}

		if (!getState().shift == event.isShiftKeyDown()) {
			return false;
		}
		if (!getState().alt == event.isAltKeyDown()) {
			return false;
		}
		if (!getState().ctrl == event.isControlKeyDown()) {
			return false;
		}
		if (!getState().meta == event.isMetaKeyDown()) {
			return false;
		}

		return true;
	}

	@Override
	public KeyActionState getState() {
		return (KeyActionState) super.getState();
	}

}
