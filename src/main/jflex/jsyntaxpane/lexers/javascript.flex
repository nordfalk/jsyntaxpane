
/* JavaScript language lexer specification 
   Modified from Java Lexer Specs by Ayman Al-Sairafi
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
%class JavaScriptLexer
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
    public JavaScriptLexer() {
        super();
    }

    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }

    // These will be used to store Token Start positions and length for Complex 
    // Tokens that need deifferent Lexer States, like STRING
    int tokenStart;
    int tokenLength;

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), SyntaxActions.JAVA_INDENT);
    }

    @Override
    public String[] getLanguages() {
        return LANGS;
    }

    private static final String[] LANGS = { "javascript", "js", "rhino" };
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
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

%state STRING

%%

<YYINITIAL> {

  /* keywords */
  "break"                        { return token(TokenType.KEYWORD); }
  "case"                         { return token(TokenType.KEYWORD); }
  "catch"                        { return token(TokenType.KEYWORD); }
  "continue"                     { return token(TokenType.KEYWORD); }
  "do"                           { return token(TokenType.KEYWORD); }
  "else"                         { return token(TokenType.KEYWORD); }
  "finally"                      { return token(TokenType.KEYWORD); }
  "for"                          { return token(TokenType.KEYWORD); }
  "default"                      { return token(TokenType.KEYWORD); }
  "delete"                       { return token(TokenType.KEYWORD); }
  "new"                          { return token(TokenType.KEYWORD); }
  "goto"                         { return token(TokenType.KEYWORD); }
  "if"                           { return token(TokenType.KEYWORD); }
  "switch"                       { return token(TokenType.KEYWORD); }
  "return"                       { return token(TokenType.KEYWORD); }
  "while"                        { return token(TokenType.KEYWORD); }
  "this"                         { return token(TokenType.KEYWORD); }
  "try"                          { return token(TokenType.KEYWORD); }
  "var"                          { return token(TokenType.KEYWORD); }
  "function"                     { return token(TokenType.KEYWORD); }
  "with"                         { return token(TokenType.KEYWORD); }
  "in"                           { return token(TokenType.KEYWORD); }
  
  /* boolean literals */
  "true"                         { return token(TokenType.KEYWORD); }
  "false"                        { return token(TokenType.KEYWORD); }
  
  /* null literal */
  "null"                         { return token(TokenType.KEYWORD); }

  /* Built-in Types*/
  "Array"                        { return token(TokenType.TYPE); }
  "Boolean"                      { return token(TokenType.TYPE); }
  "RegExp"                       { return token(TokenType.TYPE); }
  "String"                       { return token(TokenType.TYPE); }
  {Identifier} ":"               { return token(TokenType.TYPE); }

  
  /* operators */

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
  "~"                            { return token(TokenType.OPERATOR); } 
  "?"                            { return token(TokenType.OPERATOR); } 
  ":"                            { return token(TokenType.OPERATOR); } 
  "=="                           { return token(TokenType.OPERATOR); } 
  "<="                           { return token(TokenType.OPERATOR); } 
  ">="                           { return token(TokenType.OPERATOR); } 
  "!="                           { return token(TokenType.OPERATOR); } 
  "&&"                           { return token(TokenType.OPERATOR); } 
  "||"                           { return token(TokenType.OPERATOR); } 
  "++"                           { return token(TokenType.OPERATOR); } 
  "--"                           { return token(TokenType.OPERATOR); } 
  "+"                            { return token(TokenType.OPERATOR); } 
  "-"                            { return token(TokenType.OPERATOR); } 
  "*"                            { return token(TokenType.OPERATOR); } 
  "/"                            { return token(TokenType.OPERATOR); } 
  "&"                            { return token(TokenType.OPERATOR); } 
  "|"                            { return token(TokenType.OPERATOR); } 
  "^"                            { return token(TokenType.OPERATOR); } 
  "%"                            { return token(TokenType.OPERATOR); } 
  "<<"                           { return token(TokenType.OPERATOR); } 
  ">>"                           { return token(TokenType.OPERATOR); } 
  ">>>"                          { return token(TokenType.OPERATOR); } 
  "+="                           { return token(TokenType.OPERATOR); } 
  "-="                           { return token(TokenType.OPERATOR); } 
  "*="                           { return token(TokenType.OPERATOR); } 
  "/="                           { return token(TokenType.OPERATOR); } 
  "&="                           { return token(TokenType.OPERATOR); } 
  "|="                           { return token(TokenType.OPERATOR); } 
  "^="                           { return token(TokenType.OPERATOR); } 
  "%="                           { return token(TokenType.OPERATOR); } 
  "<<="                          { return token(TokenType.OPERATOR); } 
  ">>="                          { return token(TokenType.OPERATOR); } 
  ">>>="                         { return token(TokenType.OPERATOR); } 
  
  /* string literal */
  \"                             {  
                                    yybegin(STRING); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

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

<STRING> {
  \"                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return new Token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {StringCharacter}+             { tokenLength += yylength(); }

  \\[0-3]?{OctDigit}?{OctDigit}  { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

