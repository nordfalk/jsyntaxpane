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

import javax.swing.KeyStroke;
import javax.swing.text.Keymap;

/**
 *
 * @author subwiz
 */
public class KeymapUtil {
    private static final KeyStroke KS_ENTER = KeyStroke.getKeyStroke("ENTER");
    private static final KeyStroke KS_TAB = KeyStroke.getKeyStroke("TAB");
    
    public static void installCommonKeymap(Keymap km){
        km.addActionForKeyStroke(KS_TAB, SyntaxActions.INDENT);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke("shift TAB"), SyntaxActions.UNINDENT);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke("control Z"), SyntaxActions.UNDO);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke("control Y"), SyntaxActions.REDO);
    }

    public static void installGroovyJavaKeymap(Keymap km){
        km.addActionForKeyStroke(KeyStroke.getKeyStroke('('), SyntaxActions.LPARAN);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke('['), SyntaxActions.LSQUARE);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke('"'), SyntaxActions.DQUOTE);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke('\''), SyntaxActions.SQUOTE);
        km.addActionForKeyStroke(KS_ENTER, SyntaxActions.JAVA_INDENT);
    }

    public static void installMarkupKeymap(Keymap km){
        km.addActionForKeyStroke(KS_ENTER, SyntaxActions.SMART_INDENT);
    }
}
