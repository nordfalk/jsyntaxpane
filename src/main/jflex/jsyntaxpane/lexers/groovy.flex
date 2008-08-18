/* Note that this lexer specification is not tuned for speed.
   It is in fact quite slow on integer and floating point literals, 
   because the input is read twice and the methods used to parse
   the numbers are not very fast. 
   For a production quality application (e.g. a Java compiler) 
   this could be optimized */

package jsyntaxpane.lexers;

import jsyntaxpane.DefaultLexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.SyntaxActions;

%%

%public
%class GroovyLexer
%extends DefaultLexer
%final
%unicode
%char
%type Token


%{
    /**
     * Default constructor is needed as we will always call the yyreset
     */
    public GroovyLexer() {
        super();
    }

    /**
     * Helper method to create and return a new Token from of TokenType
     */
    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }

    // These will be used to store Token Start positions and length for Complex 
    // Tokens that need different Lexer States, like STRING
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

    private static final String[] LANGS = {"groovy"};
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | 
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* Groovy and generally Java types have first UpperCase Letter */
// Type = [:uppercase:][:jletterdigit:]*

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
SingleCharacter = [^\r\n\'\\]
RegexCharacter  = [^\r\n\/]

%state STRING, CHARLITERAL, REGEX

%%

<YYINITIAL> {

  /* keywords */
  "abstract"                     { return token(TokenType.KEYWORD); }
  "boolean"                      { return token(TokenType.KEYWORD); }
  "break"                        { return token(TokenType.KEYWORD); }
  "byte"                         { return token(TokenType.KEYWORD); }
  "case"                         { return token(TokenType.KEYWORD); }
  "catch"                        { return token(TokenType.KEYWORD); }
  "char"                         { return token(TokenType.KEYWORD); }
  "class"                        { return token(TokenType.KEYWORD); }
  "const"                        { return token(TokenType.KEYWORD); }
  "continue"                     { return token(TokenType.KEYWORD); }
  "do"                           { return token(TokenType.KEYWORD); }
  "double"                       { return token(TokenType.KEYWORD); }
  "enum"                         { return token(TokenType.KEYWORD); }
  "else"                         { return token(TokenType.KEYWORD); }
  "extends"                      { return token(TokenType.KEYWORD); }
  "final"                        { return token(TokenType.KEYWORD); }
  "finally"                      { return token(TokenType.KEYWORD); }
  "float"                        { return token(TokenType.KEYWORD); }
  "for"                          { return token(TokenType.KEYWORD); }
  "default"                      { return token(TokenType.KEYWORD); }
  "implements"                   { return token(TokenType.KEYWORD); }
  "import"                       { return token(TokenType.KEYWORD); }
  "instanceof"                   { return token(TokenType.KEYWORD); }
  "int"                          { return token(TokenType.KEYWORD); }
  "interface"                    { return token(TokenType.KEYWORD); }
  "long"                         { return token(TokenType.KEYWORD); }
  "native"                       { return token(TokenType.KEYWORD); }
  "new"                          { return token(TokenType.KEYWORD); }
  "goto"                         { return token(TokenType.KEYWORD); }
  "if"                           { return token(TokenType.KEYWORD); }
  "public"                       { return token(TokenType.KEYWORD); }
  "short"                        { return token(TokenType.KEYWORD); }
  "super"                        { return token(TokenType.KEYWORD); }
  "switch"                       { return token(TokenType.KEYWORD); }
  "synchronized"                 { return token(TokenType.KEYWORD); }
  "package"                      { return token(TokenType.KEYWORD); }
  "private"                      { return token(TokenType.KEYWORD); }
  "protected"                    { return token(TokenType.KEYWORD); }
  "transient"                    { return token(TokenType.KEYWORD); }
  "return"                       { return token(TokenType.KEYWORD); }
  "void"                         { return token(TokenType.KEYWORD); }
  "static"                       { return token(TokenType.KEYWORD); }
  "while"                        { return token(TokenType.KEYWORD); }
  "this"                         { return token(TokenType.KEYWORD); }
  "throw"                        { return token(TokenType.KEYWORD); }
  "throws"                       { return token(TokenType.KEYWORD); }
  "try"                          { return token(TokenType.KEYWORD); }
  "volatile"                     { return token(TokenType.KEYWORD); }
  "strictfp"                     { return token(TokenType.KEYWORD); }

  /* Groovy reserved words not in Java */
  "as"                           { return token(TokenType.KEYWORD); }
  "asssert"                      { return token(TokenType.KEYWORD); }
  "def"                          { return token(TokenType.KEYWORD); }
  "in"                           { return token(TokenType.KEYWORD); }
  "threadsafe"                   { return token(TokenType.KEYWORD); }

  
  /* boolean literals */
  "true"                         { return token(TokenType.KEYWORD); }
  "false"                        { return token(TokenType.KEYWORD); }
  
  /* null literal */
  "null"                         { return token(TokenType.TYPE); }


  /* Builtin Types and Object Wrappers */
  "Boolean"                      { return token(TokenType.TYPE); }
  "Byte"                         { return token(TokenType.TYPE); }
  "Double"                       { return token(TokenType.TYPE); }
  "Float"                        { return token(TokenType.TYPE); }
  "Integer"                      { return token(TokenType.TYPE); }
  "Object"                       { return token(TokenType.TYPE); }
  "Short"                        { return token(TokenType.TYPE); }
  "String"                       { return token(TokenType.TYPE); }
  "Regex"                        { return token(TokenType.TYPE); }
  
  /* Groovy commonly used methods */
  "print"                        { return token(TokenType.KEYWORD); }
  "println"                        { return token(TokenType.KEYWORD); }

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
  "@"                            { return token(TokenType.OPERATOR); } 
  
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

  /* Groovy operators */
  "~="                           { return token(TokenType.OPERATOR); } 
  "?."                           { return token(TokenType.OPERATOR); } 

  /* string literal */
  \"                             {  
                                    yybegin(STRING); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

  /* character literal */
  \'                             {  
                                    yybegin(CHARLITERAL); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

  /* 
     Groovy Regex -- state cannot be easily used here due to / by itself being 
     a valid operator.  So if we flip into the REGEX state, we cannot distinguish
     a regular / 
  */
  "/" {RegexCharacter}+ "/"      { return token(TokenType.REGEX); }



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
  
  /* Types in Java and Groovy */ 
  // {Type}                         { return token(TokenType.TYPE); }

  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}+                  { /* skip */ }

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

<CHARLITERAL> {
  \'                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return new Token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {SingleCharacter}+             { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

<REGEX> {
  "/"                            { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return new Token(TokenType.REGEX, tokenStart, tokenLength + 1);
                                 }
  
  {RegexCharacter}+             { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

