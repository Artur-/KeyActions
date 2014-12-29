/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.artur;

import java.lang.reflect.Method;

import org.vaadin.artur.client.KeyActionServerRpc;
import org.vaadin.artur.client.KeyActionState;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

public class KeyAction extends AbstractExtension {

    public KeyAction(int keyCode, int... modifiers) {
        super();
        registerRpc(new KeyActionServerRpc() {

            @Override
            public void trigger() {
                fireEvent(new KeyActionEvent((Component) getParent()));
            }

        });
        getState().keyCode = keyCode;
        for (int modifier : modifiers) {
            if (modifier == ModifierKey.ALT) {
                getState().alt = true;
            } else if (modifier == ModifierKey.CTRL) {
                getState().ctrl = true;
            } else if (modifier == ModifierKey.META) {
                getState().meta = true;
            } else if (modifier == ModifierKey.SHIFT) {
                getState().shift = true;
            }
        }
    }

    public void setStopPropagation(boolean stopPropagation) {
        getState().stopPropagation = stopPropagation;
    }

    public boolean isStopPropagation() {
        return getState(false).stopPropagation;
    }

    public void setPreventDefault(boolean preventDefault) {
        getState().preventDefault = preventDefault;
    }

    public boolean isPreventDefault() {
        return getState(false).preventDefault;
    }

    @Override
    protected KeyActionState getState() {
        return (KeyActionState) super.getState();
    }

    @Override
    protected KeyActionState getState(boolean markAsDirty) {
        return (KeyActionState) super.getState(markAsDirty);
    }

    public void extend(AbstractComponent target) {
        super.extend(target);
    }

    public static class KeyActionEvent extends Component.Event {

        public KeyActionEvent(Component source) {
            super(source);
        }

    }

    public interface KeyActionListener extends ConnectorEventListener {

        public static final Method method = ReflectTools.findMethod(
                KeyActionListener.class, "keyPressed", KeyActionEvent.class);

        public void keyPressed(KeyActionEvent keyPressEvent);
    }

    public void removeKeyActionListener(KeyActionListener keyActionListener) {
        removeListener(KeyActionEvent.class, keyActionListener);
    }

    public void addKeypressListener(KeyActionListener keyActionListener) {
        addListener(KeyActionEvent.class, keyActionListener,
                KeyActionListener.method);
    }
}
