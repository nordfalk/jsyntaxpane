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
package jsyntaxpane;

import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;

/**
 * This is a default, and abstract implemenatation of a Lexer with
 * some utility methods that Lexers can implement.
 *
 * @author ayman
 */
public abstract class DefaultLexer implements Lexer {

    /**
     * This method is called by the SyntaxKit whenever this language is set on
     * a pane.  This will add the language keybindings to the keymap of the
     * control.  You should *NOT* override this method if you need to add 
     * language specific key bindings, but override the addKeyActions method 
     * @param pane
     */
    public void install(JEditorPane pane) {
        Keymap km_parent = JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP);
        Keymap km_new = JTextComponent.addKeymap(null, km_parent);
        addKeyActions(km_new);
        pane.setKeymap(km_new);
    }

    /**
     * Add keyboard actions to this control.  
     * @param map
     */
    public void addKeyActions(Keymap map) {
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control Z"), SyntaxActions.UNDO);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control Y"), SyntaxActions.REDO);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("TAB"), SyntaxActions.INDENT);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("shift TAB"), SyntaxActions.UNINDENT);
    }

    /**
     * Return the current matched token as a string.  This is <b>expensive</b>
     * as it creates a new String object for the token.  Use with care.
     *
     * @return
     */
    protected CharSequence getTokenSrring() {
        return yytext();
    }
}
