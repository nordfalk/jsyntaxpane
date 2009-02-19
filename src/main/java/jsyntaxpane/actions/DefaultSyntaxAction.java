/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.util.Configuration;

/**
 * The DefaultSyntaxAction.  You can extend this class or implement the interface
 * SyntaxAction to create your own actions.
 * 
 * @author Ayman Al-Sairafi
 */
abstract public class DefaultSyntaxAction extends TextAction implements SyntaxAction {

    public DefaultSyntaxAction(String actionName) {
        super(actionName);
    }

    @Override
    public void config(Configuration config, String name) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent text = getTextComponent(e);
        SyntaxDocument sdoc = ActionUtils.getSyntaxDocument(text);
        if(text != null) {
            actionPerformed(text, sdoc, text.getCaretPosition(), e);
        }
    }

    /**
     * Convenience method that will be called if the Action is performed on a
     * JTextComponent.  SyntaxActions should generally override this method.
     * @param target (non-null JTextComponent from Action.getSource
     * @param sDoc (SyntaxDOcument of the text component, could be null)
     * @param dot (position of caret at text document)
     * @param e actual ActionEvent passed to actionPerformed
     */
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc,
            int dot, ActionEvent e) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static final String ACTION_PREFIX = "Action.";
}
