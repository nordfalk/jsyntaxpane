/* 
  Java Properties Files
 */

package jsyntaxpane.lexers;

import jsyntaxpane.Lexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

%%

%public
%class PropertiesLexer
%implements Lexer
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

    public static String[] LANGS = new String[] {"properties"};

    public String[] getNames(){
      return LANGS;
    }

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

