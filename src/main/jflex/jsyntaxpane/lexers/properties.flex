/* 
  Java Properties Files
 */

package jsyntaxpane.lexers;

import jsyntaxpane.DefaultLexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.SyntaxActions;

%%

%public
%class PropertiesLexer
%extends DefaultLexer
%final
%unicode
%char
%type Token


%{
    /**
     * Create an empty lexer, yyrset will be called later to reset and assign
     * the reader
     */
    public PropertiesLexer() {
        super();
    }

    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
    }

    @Override
    public String[] getLanguages() {
        return LANGS;
    }

    private static final String[] LANGS = { "properties" };
%}

StartComment = #
WhiteSpace = [ \t]
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
KeyCharacter = [a-zA-Z0-9._ ]

%%

<YYINITIAL> 
{
	{KeyCharacter}*=                          { return token(TokenType.KEYWORD); }
        {StartComment} {InputCharacter}* {LineTerminator}?         
                                                  { return token(TokenType.COMMENT); }
        . | {LineTerminator}                      { /* skip */ }
}

<<EOF>>                   { return null; }

