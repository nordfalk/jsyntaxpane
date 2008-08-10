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
public class XmlLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return XmlLexer.class;
    }

    public String[] getNames() {
        return XmlLexer.LANGS;
    }

}
