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
public class JavaLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return JavaLexer.class;
    }

    public String[] getNames() {
        return JavaLexer.LANGS;
    }

}
