/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
  "abstract"                     |
  "boolean"                      |
  "break"                        |
  "byte"                         |
  "case"                         |
  "catch"                        |
  "char"                         |
  "class"                        |
  "const"                        |
  "continue"                     |
  "do"                           |
  "double"                       |
  "enum"                         |
  "else"                         |
  "extends"                      |
  "final"                        |
  "finally"                      |
  "float"                        |
  "for"                          |
  "default"                      |
  "implements"                   |
  "import"                       |
  "instanceof"                   |
  "int"                          |
  "interface"                    |
  "long"                         |
  "native"                       |
  "new"                          |
  "goto"                         |
  "if"                           |
  "public"                       |
  "short"                        |
  "super"                        |
  "switch"                       |
  "synchronized"                 |
  "package"                      |
  "private"                      |
  "protected"                    |
  "transient"                    |
  "return"                       |
  "void"                         |
  "static"                       |
  "while"                        |
  "this"                         |
  "throw"                        |
  "throws"                       |
  "try"                          |
  "volatile"                     |
  "strictfp"                     |

  /* Groovy reserved words not in Java */
  "as"                           |
  "asssert"                      | 
  "def"                          |
  "in"                           |
  "threadsafe"                   |
  /* Booleans and null */
  "true"                         |
  "false"                        |
  "null"                         { return token(TokenType.KEYWORD); }


  /* Builtin Types and Object Wrappers */
  "Boolean"                      |
  "Byte"                         |
  "Double"                       |
  "Float"                        |
  "Integer"                      |
  "Object"                       |
  "Short"                        |
  "String"                       |
  "Regex"                        { return token(TokenType.TYPE); }
  
  /* Groovy commonly used methods */
  "print"                        |
  "println"                      { return token(TokenType.KEYWORD); }

  /* operators */

  "("                            |
  ")"                            |
  "{"                            | 
  "}"                            | 
  "["                            | 
  "]"                            | 
  ";"                            | 
  ","                            | 
  "."                            | 
  "@"                            | 
  "="                            | 
  ">"                            | 
  "<"                            |
  "!"                            | 
  "~"                            | 
  "?"                            | 
  ":"                            | 
  "=="                           | 
  "<="                           | 
  ">="                           | 
  "!="                           | 
  "&&"                           | 
  "||"                           | 
  "++"                           | 
  "--"                           | 
  "+"                            | 
  "-"                            | 
  "*"                            | 
  "/"                            | 
  "&"                            | 
  "|"                            | 
  "^"                            | 
  "%"                            | 
  "<<"                           | 
  ">>"                           | 
  ">>>"                          | 
  "+="                           | 
  "-="                           | 
  "*="                           | 
  "/="                           | 
  "&="                           | 
  "|="                           | 
  "^="                           | 
  "%="                           | 
  "<<="                          | 
  ">>="                          | 
  ">>>="                         { return token(TokenType.OPERATOR); }

  "~="                           | 
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

