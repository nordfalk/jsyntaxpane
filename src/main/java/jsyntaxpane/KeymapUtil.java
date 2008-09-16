/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsyntaxpane;

import javax.swing.JEditorPane;
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
