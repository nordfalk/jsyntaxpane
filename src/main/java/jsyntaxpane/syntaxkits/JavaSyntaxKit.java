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

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.TokenType;
import jsyntaxpane.actions.FindReplaceActions;
import jsyntaxpane.actions.Markers;
import jsyntaxpane.actions.MapCompletion;
import jsyntaxpane.actions.PairsMarker;
import jsyntaxpane.actions.SyntaxActions;
import jsyntaxpane.actions.TokenMarker;
import jsyntaxpane.lexers.JavaLexer;
import jsyntaxpane.util.JarServiceProvider;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class JavaSyntaxKit extends DefaultSyntaxKit {

    public JavaSyntaxKit() {
        super(new JavaLexer());
    }

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), SyntaxActions.JAVA_INDENT);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control SPACE"),
            new MapCompletion(COMPLETIONS));
        FindReplaceActions finder = new FindReplaceActions();
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control F"), finder.getFindDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control H"), finder.getReplaceDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("F3"), finder.getFindNextAction());
    }

    @Override
    public void install(JEditorPane editorPane) {
        super.install(editorPane);
        tokenMarker = new TokenMarker(editorPane, new Color(0xffffbb),
                HIGHLITED_TOKENTYPES);
        pairMarker = new PairsMarker(editorPane, Color.ORANGE);
        
        editorPane.addCaretListener(tokenMarker);
        editorPane.addCaretListener(pairMarker);
    }

    @Override
    public void deinstall(JEditorPane editorPane) {
        super.deinstall(editorPane);
        editorPane.removeCaretListener(pairMarker);
        editorPane.removeCaretListener(tokenMarker);
        Markers.removeHighlights(editorPane);
    }
    
    private TokenMarker tokenMarker;
    private PairsMarker pairMarker;

    public static Map<String, String> COMPLETIONS;
    public static Set<TokenType> HIGHLITED_TOKENTYPES = new HashSet<TokenType>();

    static {
        COMPLETIONS = JarServiceProvider.readStringsMap("jsyntaxpane.javasyntaxkit.completions");
        HIGHLITED_TOKENTYPES.add(TokenType.IDENTIFIER);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE2);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE3);
    }
}
