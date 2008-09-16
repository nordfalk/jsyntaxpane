/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsyntaxpane.lang;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import jsyntaxpane.KeymapUtil;
import jsyntaxpane.Lexer;
import jsyntaxpane.SyntaxLanguage;
import jsyntaxpane.lexers.JavaScriptLexer;

/**
 *
 * @author subwiz
 */
public class JavaScriptSyntaxLang implements SyntaxLanguage {

    private static final String[] LANGS = new String[] {"javascript", "js", "rhino"};

    public String[] getLanguageNames() {
        return LANGS;
    }

    public void install(JEditorPane editorPane) {
        Keymap km_parent = JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP);
        Keymap km_new = JTextComponent.addKeymap(null, km_parent);
        KeymapUtil.installCommonKeymap(km_new);
        KeymapUtil.installGroovyJavaKeymap(km_new);
        editorPane.setKeymap(km_new);
    }

    public Lexer getLexer() {
        return new JavaScriptLexer();
    }
    
}
