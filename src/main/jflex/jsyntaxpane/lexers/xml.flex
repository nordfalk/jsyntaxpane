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

%%

%public
%class XmlLexer
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
    public XmlLexer() {
        super();
    }

    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }

    private Token token(TokenType type, int pairValue) {
        return new Token(type, yychar, yylength(), (byte)pairValue);
    }

    private static final byte TAG_OPEN      =  1;
    private static final byte TAG_CLOSE     = -1;

    private static final byte INSTR_OPEN    =  2;
    private static final byte INSTR_CLOSE   = -2;

    private static final byte CDATA_OPEN    =  3;
    private static final byte CDATA_CLOSE   = -3;

    private static final byte COMMENT_OPEN  =  4;
    private static final byte COMMENT_CLOSE = -4;
%}

%state COMMENT, CDATA

/* main character classes */

/* comments */
CommentStart = "<!--"
CommentEnd   = "-->"

LetterDigit = [a-zA-Z0-9_]

/* XML Processing Instructions */
InstrStart = "<?" {LetterDigit}*
InstrEnd   = "?>"

/* CDATA  */
CDataStart = "<![CDATA["
CDataEnd   = "]]>"

/* Tags */
TagStart = "<" {LetterDigit}+ (":" | "-" | {LetterDigit} )* (">" ?)
TagEnd = ("</" {LetterDigit}+ (":" | "-" | {LetterDigit} )* ">") | "/>"

/* attribute */
Attribute = {LetterDigit}+ (":" | "-" | {LetterDigit} )* "="

/* string and character literals */
DQuoteStringChar = [^\r\n\"]
SQuoteStringChar = [^\r\n\']

%%

<YYINITIAL> {
  \"{DQuoteStringChar}*\"        |
  \'{SQuoteStringChar}*\'        { return token(TokenType.STRING); }
  
  "&"  [a-z]+ ";"                |
  "&#" [:digit:]+ ";"            { return token(TokenType.KEYWORD2); }

  {TagStart}                     { return token(TokenType.TYPE, TAG_OPEN); }
  {TagEnd}                       { return token(TokenType.TYPE, TAG_CLOSE); }

  {Attribute}                    { return token(TokenType.IDENTIFIER); }

  {InstrStart}                   { return token(TokenType.TYPE, INSTR_OPEN); }
  {InstrEnd}                     { return token(TokenType.TYPE, INSTR_CLOSE); }

  {CDataStart}                   {
                                     yybegin(CDATA);
                                     return token(TokenType.COMMENT2, CDATA_OPEN);
                                 }
  {CommentStart}                 {
                                     yybegin(COMMENT);
                                     return token(TokenType.COMMENT2, COMMENT_OPEN);
                                 }
}

<CDATA> {
  {CDataEnd}                     {
                                     yybegin(YYINITIAL);
                                     return token(TokenType.COMMENT2, COMMENT_CLOSE);
                                 }
  ~{CDataEnd}                    {
                                     yypushback(3);
                                     return token(TokenType.COMMENT);
                                 }
}

<COMMENT> {
  {CommentEnd}                     {
                                     yybegin(YYINITIAL);
                                     return token(TokenType.TYPE2, COMMENT_CLOSE);
                                 }
  ~{CommentEnd}                    {
                                     yypushback(3);
                                     return token(TokenType.COMMENT);
                                 }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

