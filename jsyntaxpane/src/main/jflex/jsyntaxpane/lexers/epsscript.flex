/*
 * Copyright 2011 Ayman Al-Sairafi ayman.alsairafi@gmail.com
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


import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

%%

%public
%class EPSScriptLexer
%extends DefaultJFlexLexer
%final
%unicode
%char
%type Token
%ignorecase

%{
    /**
     * Create an empty lexer, yyrset will be called later to reset and assign
     * the reader
     */
    public EPSScriptLexer() {
        super();
    }

    @Override
    public int yychar() {
        return yychar;
    }

    private static final byte PARAN     = 1;
    private static final byte BRACKET   = 2;
    private static final byte CURLY     = 3;

%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = "#" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [a-zA-Z][a-zA-Z0-9_]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]
    
/* floating point literals */
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"]
SQStringCharacter = [^\r\n\']

%state STRING, SQSTRING

%%

<YYINITIAL> {

  /* keywords */
  "and"                          |
  "as"                           |
  "assert"                       |
  "break"                        |
  "class"                        |
  "case"                         |
  "continue"                     |
  "default"                      |
  "del"                          |
  "elif"                         |
  "else"                         |
  "except"                       |
  "exec"                         |
  "finally"                      |
  "for"                          |
  "from"                         |
  "global"                       |
  "if"                           |
  "import"                       |
  "in"                           |
  "is"                           |
  "lambda"                       |
  "not"                          |
  "or"                           |
  "pass"                         |
  "print"                        |
  "self"                         | /* not exactly keyword, but almost */
  "switch"                       |
  "raise"                        |
  "return"                       |
  "try"                          |
  "while"                        |
  "with"                         |
  "alter"                        |
  "exists"                       |
  "yield"                        { return token(TokenType.KEYWORD); }

  /* Built-in Types*/
  "number"                       |
  "boolean"                      |
  "string"                       |
  "true"                         |
  "false"                        |
  "tde"                          |
  "card"                         { return token(TokenType.KEYWORD2); }

  /* Built-in Types*/
  "Ellipsis"                     |
  "False"                        |
  "None"                         |
  "NotImplemented"               |
  "True"                         |
  "__import__"                   |
  "__name__"                     |
  "abs"                          |
  "apply"                        |
  "bool"                         |
  "buffer"                       |
  "callable"                     |
  "chr"                          |
  "classmethod"                  |
  "cmp"                          |
  "coerce"                       |
  "compile"                      |
  "complex"                      |
  "delattr"                      |
  "dict"                         |
  "dir"                          |
  "divmod"                       |
  "enumerate"                    |
  "eval"                         |
  "execfile"                     |
  "file"                         |
  "filter"                       |
  "float"                        |
  "frozenset"                    |
  "getattr"                      |
  "globals"                      |
  "hasattr"                      |
  "hash"                         |
  "help"                         |
  "hex"                          |
  "id"                           |
  "input"                        |
  "int"                          |
  "intern"                       |
  "isinstance"                   |
  "issubclass"                   |
  "iter"                         |
  "len"                          |
  "list"                         |
  "locals"                       |
  "long"                         |
  "map"                          |
  "max"                          |
  "min"                          |
  "object"                       |
  "oct"                          |
  "open"                         |
  "ord"                          |
  "pow"                          |
  "property"                     |
  "range"                        |
  "raw_input"                    |
  "reduce"                       |
  "reload"                       |
  "repr"                         |
  "reversed"                     |
  "round"                        |
  "set"                          |
  "setattr"                      |
  "slice"                        |
  "sorted"                       |
  "staticmethod"                 |
  "str"                          |
  "sum"                          |
  "super"                        |
  "tuple"                        |
  "type"                         |
  "unichr"                       |
  "unicode"                      |
  "vars"                         |
  "xrange"                       |
  "zip"                          {  return token(TokenType.TYPE);  }


  
  /* operators */

  "("                            { return token(TokenType.OPERATOR,  PARAN); }
  ")"                            { return token(TokenType.OPERATOR, -PARAN); }
  "{"                            { return token(TokenType.OPERATOR,  CURLY); }
  "}"                            { return token(TokenType.OPERATOR, -CURLY); }
  "["                            { return token(TokenType.OPERATOR,  BRACKET); }
  "]"                            { return token(TokenType.OPERATOR, -BRACKET); }
  "+"                            |
  "-"                            |
  "*"                            |
  "**"                           |
  "/"                            |
  "//"                           |
  "%"                            |
  "<<"                           |
  ">>"                           |
  "&"                            |
  "|"                            |
  "^"                            |
  "~"                            |
  "<"                            |
  ">"                            |
  "<="                           |
  ">="                           |
  "=="                           |
  "!="                           |
  "<>"                           |
  "@"                            |
  ","                            |
  ":"                            |
  "."                            |
  "`"                            |
  "="                            |
  ";"                            |
  "+="                           |
  "-="                           |
  "*="                           |
  "/="                           |
  "//="                          |
  "%="                           |
  "&="                           |
  "|="                           |
  "^="                           |
  ">>="                          |
  "<<="                          |
  "**="                          { return token(TokenType.OPERATOR); }
  
  /* string literal */

  \"                             {
                                    yybegin(STRING);
                                    tokenStart = yychar;
                                    tokenLength = 1;
                                 }

  \'                             {
                                    yybegin(SQSTRING);
                                    tokenStart = yychar;
                                    tokenLength = 1;
                                 }

  /* numeric literals */

  {DecIntegerLiteral}            |
  {DecLongLiteral}               |
  
  {FloatLiteral}                 { return token(TokenType.NUMBER); }
  
  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}                   { }

  /* identifiers */ 
  {Identifier}                   { return token(TokenType.IDENTIFIER); }

  "$" | "?"                      { return token(TokenType.ERROR); }
}

<STRING> {
  \"                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {StringCharacter}+             { tokenLength += yylength(); }

  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}


<SQSTRING> {
  "'"                            {
                                     yybegin(YYINITIAL);
                                     // length also includes the trailing quote
                                     return token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }

  {SQStringCharacter}+           { tokenLength += yylength(); }

  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

