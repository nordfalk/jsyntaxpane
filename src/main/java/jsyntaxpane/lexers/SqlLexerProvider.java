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
public class SqlLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return SqlLexer.class;
    }

    public String[] getNames() {
        return SqlLexer.LANGS;
    }

}
