/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsyntaxpane;

import javax.swing.JEditorPane;

/**
 *
 * @author subwiz
 */
public interface SyntaxLang {
    public String[] getLanguageNames();
    public void install(JEditorPane editorPane);
    public Lexer getLexer();
}
