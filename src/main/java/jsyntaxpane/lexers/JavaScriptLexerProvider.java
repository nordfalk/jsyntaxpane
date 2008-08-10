/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsyntaxpane.lexers;

import jsyntaxpane.ILexerProvider;

/**
 *
 * @author subwiz
 */
public class JavaScriptLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return JavaScriptLexer.class;
    }

    public String[] getNames() {
        return JavaScriptLexer.LANGS;
    }

}
