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
public class PropertiesLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return PropertiesLexer.class;
    }

    public String[] getNames() {
        return PropertiesLexer.LANGS;
    }

}
