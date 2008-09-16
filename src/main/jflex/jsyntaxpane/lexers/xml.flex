/**
 * XML Simple Syntax by Ayman Al-Sairafi
 */

package jsyntaxpane.lexers;

import jsyntaxpane.Lexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

%%

%public
%class XmlLexer
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
    public XmlLexer() {
        super();
    }

    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }

%}

/* main character classes */

/* comments */
Comment = "<!--" [^--] ~"-->" | "<!--" "-"+ "->"

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]+


LetterDigit = [a-zA-Z0-9_]

/*
TagStart = [<>] 
TagEnd = "</" | ">" | "/>"
Tag = {TagStart} | {TagEnd}
Tag = [<>/]
*/

DocType = "<?xml" [^?]* "?>"

/* Tag Delimiters */
TagStart = "<" {LetterDigit}+ (":" | "-" | {LetterDigit} )*
TagEnd = ("</" {LetterDigit}+ (":" | "-" | {LetterDigit} )* ">") | "/>" | ">"
Tag = {TagStart} | {TagEnd}

/* attribute */
Attribute = {LetterDigit}+ "="
lcase = [a-z]

/* string and character literals */
DQuoteStringChar = [^\r\n\"]
SQuoteStringChar = [^\r\n\']

%%

<YYINITIAL> {
  \"{DQuoteStringChar}+\"        { return token(TokenType.STRING); }
  \'{SQuoteStringChar}+\'        { return token(TokenType.STRING); }
  
  {Comment}                      { return token(TokenType.COMMENT); }

  {Tag}                          { return token(TokenType.OPER); }

  {Attribute}                    { return token(TokenType.IDENT); }

  {DocType}                      { return token(TokenType.KEYWORD); }

  "&"  {lcase}+ ";"              { return token(TokenType.OPER); }
  "&#" [:digit:]+ ";"            { return token(TokenType.OPER); }

  {LetterDigit}+                 { return token(TokenType.IDENT); }

  {WhiteSpace}+                  { /* skip */ }
}


/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

