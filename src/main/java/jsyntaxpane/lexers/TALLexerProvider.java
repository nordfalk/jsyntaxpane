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
public class TALLexerProvider implements ILexerProvider{

    public Class getLexerClass() {
        return TALLexer.class;
    }

    public String[] getNames() {
        return TALLexer.LANGS;
    }

}
