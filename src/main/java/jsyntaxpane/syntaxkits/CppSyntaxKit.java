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
package jsyntaxpane.syntaxkits;

import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.Lexer;
import jsyntaxpane.actions.FindReplaceActions;
import jsyntaxpane.actions.SyntaxActions;
import jsyntaxpane.lexers.CppLexer;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class CppSyntaxKit extends DefaultSyntaxKit {

    public CppSyntaxKit() {
        super(new CppLexer());
    }

    /**
     * Consruct a JavaSyntaxKit user the supplied lexer.  This is protected so
     * only subclasses may extend this with a new lexer.
     * @param lexer
     */
    CppSyntaxKit(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), SyntaxActions.JAVA_INDENT);
        FindReplaceActions finder = new FindReplaceActions();
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control F"), finder.getFindDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control H"), finder.getReplaceDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("F3"), finder.getFindNextAction());
    }
}
