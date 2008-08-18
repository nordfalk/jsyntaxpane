
/* TAL Syntax By Ayman Al-Sairafi */

package jsyntaxpane.lexers;

import jsyntaxpane.DefaultLexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.SyntaxActions;

%%

%public
%class TALLexer
%extends DefaultLexer
%final
%unicode
%char
%type Token
%caseless


%{
    /**
     * Create an empty lexer, yyrset will be called later to reset and assign
     * the reader
     */
    public TALLexer() {
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

    private static final String[] LANGS = { "tal", "ptal" };
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment = "!" [^\r\n!]* ( "!" | {LineTerminator} )
EndOfLineComment = "--" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [A-Za-z_][A-Za-z0-9\^_]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = "%" [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]
    
/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%%

<YYINITIAL> {

  /* keywords */
  "begin"                        { return token(TokenType.KEYWORD); }
  "end"                          { return token(TokenType.KEYWORD); }
  "struct"                       { return token(TokenType.KEYWORD); }
  "fieldalign"                   { return token(TokenType.KEYWORD); }
  "shared"                       { return token(TokenType.KEYWORD); }
  "shared2"                      { return token(TokenType.KEYWORD); }
  "literal"                      { return token(TokenType.KEYWORD); }
  "for"                          { return token(TokenType.KEYWORD); }
  "do"                           { return token(TokenType.KEYWORD); }
  "while"                        { return token(TokenType.KEYWORD); }
  "?page"                        { return token(TokenType.KEYWORD); }
  "?section"                     { return token(TokenType.KEYWORD); }

  "int"                          { return token(TokenType.TYPE); }
  "string"                       { return token(TokenType.TYPE); }
  "int(32)"                      { return token(TokenType.TYPE); }
  "fixed"                        { return token(TokenType.TYPE); }
  "byte"                         { return token(TokenType.TYPE); }
  "float"                        { return token(TokenType.TYPE); }
  "filler"                       { return token(TokenType.TYPE); }


  "("                            { return token(TokenType.OPERATOR); }
  ")"                            { return token(TokenType.OPERATOR); }
  "{"                            { return token(TokenType.OPERATOR); } 
  "}"                            { return token(TokenType.OPERATOR); } 
  "["                            { return token(TokenType.OPERATOR); } 
  "]"                            { return token(TokenType.OPERATOR); } 
  ";"                            { return token(TokenType.OPERATOR); } 
  ","                            { return token(TokenType.OPERATOR); } 
  "."                            { return token(TokenType.OPERATOR); } 
  
  "="                            { return token(TokenType.OPERATOR); } 
  ">"                            { return token(TokenType.OPERATOR); } 
  "<"                            { return token(TokenType.OPERATOR); }
  "!"                            { return token(TokenType.OPERATOR); } 
  "?"                            { return token(TokenType.OPERATOR); } 
  ":"                            { return token(TokenType.OPERATOR); } 
  ":="                           { return token(TokenType.OPERATOR); } 
  "':='"                         { return token(TokenType.OPERATOR); } 
  "'=:'"                         { return token(TokenType.OPERATOR); } 
  "<>"                           { return token(TokenType.OPERATOR); } 
  "+"                            { return token(TokenType.OPERATOR); } 
  "-"                            { return token(TokenType.OPERATOR); } 
  "*"                            { return token(TokenType.OPERATOR); } 
  "/"                            { return token(TokenType.OPERATOR); } 
  "<<"                           { return token(TokenType.OPERATOR); } 
  ">>"                           { return token(TokenType.OPERATOR); } 
  
  /* string literal */
  \"{StringCharacter}+\"         { return token(TokenType.STRING); }

  /* character literal */
  \'{SingleCharacter}\'          { return token(TokenType.STRING); }

  /* numeric literals */

  {DecIntegerLiteral}            { return token(TokenType.NUMBER); }
  {DecLongLiteral}               { return token(TokenType.NUMBER); }
  
  {HexIntegerLiteral}            { return token(TokenType.NUMBER); }
  {HexLongLiteral}               { return token(TokenType.NUMBER); }
 
  {OctIntegerLiteral}            { return token(TokenType.NUMBER); }
  {OctLongLiteral}               { return token(TokenType.NUMBER); }
  
  {FloatLiteral}                 { return token(TokenType.NUMBER); }
  {DoubleLiteral}                { return token(TokenType.NUMBER); }
  {DoubleLiteral}[dD]            { return token(TokenType.NUMBER); }
  
  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}                   { }

  /* identifiers */ 
  {Identifier}                   { return token(TokenType.IDENTIFIER); }
}


/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

