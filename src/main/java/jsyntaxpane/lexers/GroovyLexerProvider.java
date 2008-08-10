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
public class GroovyLexerProvider implements ILexerProvider {

    public Class getLexerClass() {
        return GroovyLexer.class;
    }

    public String[] getNames() {
        return GroovyLexer.LANGS;
    }
    
}
