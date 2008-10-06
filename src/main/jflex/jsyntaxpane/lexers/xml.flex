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

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
    }
%}

/* main character classes */

/* comments */
Comment = "<!--" [^--] ~"-->" | "<!--" "-"+ "->"

LetterDigit = [a-zA-Z0-9_]

DocType = "<?xml" [^?]* "?>"

/* Tag Delimiters */
TagStart = "<" {LetterDigit}+ (":" | "-" | {LetterDigit} )*
TagEnd = ("</" {LetterDigit}+ (":" | "-" | {LetterDigit} )* ">") | "/>" | ">"
Tag = {TagStart} | {TagEnd}

/* attribute */
Attribute = {LetterDigit}+ (":" | "-" | {LetterDigit} )* "="

/* string and character literals */
DQuoteStringChar = [^\r\n\"]
SQuoteStringChar = [^\r\n\']

%%

<YYINITIAL> {
  \"{DQuoteStringChar}*\"        |
  \'{SQuoteStringChar}*\'        { return token(TokenType.STRING); }
  
  {Comment}                      { return token(TokenType.COMMENT); }

  "&"  [a-z]+ ";"            |
  "&#" [:digit:]+ ";"            { return token(TokenType.KEYWORD2); }

  {Tag}                          { return token(TokenType.TYPE); }

  {Attribute}                    { return token(TokenType.IDENTIFIER); }

  {DocType}                      { return token(TokenType.KEYWORD); }

}


/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

