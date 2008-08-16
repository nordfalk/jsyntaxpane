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
import jsyntaxpane.SyntaxLang;
import jsyntaxpane.lexers.SqlLexer;

/**
 *
 * @author subwiz
 */
public class SQLSyntaxLang implements SyntaxLang {

    private static final String[] LANGS = new String[] {"sql"};

    public String[] getLanguageNames() {
        return LANGS;
    }

    public void install(JEditorPane editorPane) {
        Keymap km_parent = JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP);
        Keymap km_new = JTextComponent.addKeymap(null, km_parent);
        KeymapUtil.installCommonKeymap(km_new);
        editorPane.setKeymap(km_new);
    }

    public Lexer getLexer() {
        return new SqlLexer();
    }

}
