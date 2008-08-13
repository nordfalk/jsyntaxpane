/*
  This Lexer created by Ayman Al-Sairafi for JSyntaxPane 
*/
package jsyntaxpane.lexers;

import jsyntaxpane.Lexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

%%

%public
%class SqlLexer
%implements Lexer
%final
%unicode
%char
%type Token
%caseless


%{
    /**
     * Default constructor is needed as we will always call the yyreset
     */
    public SqlLexer() {
        super();
    }

    /**
     * Helper method to create and return a new Token from of TokenType
     */
    private Token token(TokenType type) {
        return new Token(type, yychar, yylength());
    }


    // These will be used to store Token Start positions and length for Complex 
    // Tokens that need deifferent Lexer States, like STRING
    int tokenStart;
    int tokenLength;

    public static String[] LANGS = new String[] {"sql"};

    public String[] getContentTypes(){
      return LANGS;
    }

%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {EndOfLineComment}

EndOfLineComment = "--" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
    
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

Reserved = 
   "ADD"                 |
   "ALL"                 |
   "ALTER"               |
   "ANALYZE"             |
   "AND"                 |
   "AS"                  |
   "ASC"                 |
   "BEFORE"              |
   "BETWEEN"             |
   "BIGINT"              |
   "BINARY"              |
   "BLOB"                |
   "BOTH"                |
   "BY"                  |
   "CALL"                |
   "CASCADE"             |
   "CASE"                |
   "CHANGE"              |
   "CHAR"                |
   "CHARACTER"           |
   "CHECK"               |
   "COLLATE"             |
   "COLUMN"              |
   "CONDITION"           |
   "CONSTRAINT"          |
   "CONTINUE"            |
   "CONVERT"             |
   "CREATE"              |
   "CROSS"               |
   "CURSOR"              |
   "DATABASE"            |
   "DATABASES"           |
   "DEC"                 |
   "DECIMAL"             |
   "DECLARE"             |
   "DEFAULT"             |
   "DELAYED"             |
   "DELETE"              |
   "DESC"                |
   "DESCRIBE"            |
   "DETERMINISTIC"       |
   "DISTINCT"            |
   "DISTINCTROW"         |
   "DIV"                 |
   "DOUBLE"              |
   "DROP"                |
   "DUAL"                |
   "EACH"                |
   "ELSE"                |
   "ELSEIF"              |
   "ENCLOSED"            |
   "ESCAPED"             |
   "EXISTS"              |
   "EXIT"                |
   "EXPLAIN"             |
   "FALSE"               |
   "FETCH"               |
   "FLOAT"               |
   "FLOAT4"              |
   "FLOAT8"              |
   "FOR"                 |
   "FORCE"               |
   "FOREIGN"             |
   "FROM"                |
   "FULLTEXT"            |
   "GRANT"               |
   "GROUP"               |
   "HAVING"              |
   "IF"                  |
   "IGNORE"              |
   "IN"                  |
   "INDEX"               |
   "INFILE"              |
   "INNER"               |
   "INOUT"               |
   "INSENSITIVE"         |
   "INSERT"              |
   "INT"                 |
   "INTEGER"             |
   "INTERVAL"            |
   "INTO"                |
   "IS"                  |
   "ITERATE"             |
   "JOIN"                |
   "KEY"                 |
   "KEYS"                |
   "KILL"                |
   "LEADING"             |
   "LEAVE"               |
   "LEFT"                |
   "LIKE"                |
   "LIMIT"               |
   "LINES"               |
   "LOAD"                |
   "LOCK"                |
   "LONG"                |
   "LOOP"                |
   "MATCH"               |
   "MERGE"               |
   "MOD"                 |
   "MODIFIES"            |
   "NATURAL"             |
   "NOT"                 |
   "NULL"                |
   "NUMERIC"             |
   "ON"                  |
   "OPTIMIZE"            |
   "OPTION"              |
   "OPTIONALLY"          |
   "OR"                  |
   "ORDER"               |
   "OUT"                 |
   "OUTER"               |
   "OUTFILE"             |
   "PRECISION"           |
   "PRIMARY"             |
   "PROCEDURE"           |
   "PURGE"               |
   "READ"                |
   "READS"               |
   "REAL"                |
   "REFERENCES"          |
   "REGEXP"              |
   "RELEASE"             |
   "RENAME"              |
   "REPEAT"              |
   "REPLACE"             |
   "REQUIRE"             |
   "RESTRICT"            |
   "RETURN"              |
   "REVOKE"              |
   "RIGHT"               |
   "RLIKE"               |
   "SCHEMA"              |
   "SCHEMAS"             |
   "SELECT"              |
   "SENSITIVE"           |
   "SEPARATOR"           |
   "SET"                 |
   "SHOW"                |
   "SMALLINT"            |
   "SONAME"              |
   "SPATIAL"             |
   "SPECIFIC"            |
   "SQL"                 |
   "SQLEXCEPTION"        |
   "SQLSTATE"            |
   "SQLWARNING"          |
   "STARTING"            |
   "TABLE"               |
   "TERMINATED"          |
   "THEN"                |
   "TO"                  |
   "TRAILING"            |
   "TRIGGER"             |
   "TRUE"                |
   "TRUNCATE"            |
   "UNDO"                |
   "UNION"               |
   "UNIQUE"              |
   "UNLOCK"              |
   "UNSIGNED"            |
   "UPDATE"              |
   "USAGE"               |
   "USE"                 |
   "USING"               |
   "VALUES"              |
   "VARBINARY"           |
   "VARCHAR"             |
   "VARCHARACTER"        |
   "VARYING"             |
   "WHEN"                |
   "WHERE"               |
   "WHILE"               |
   "WITH"                |
   "WRITE"               |
   "XOR"                 |
   "ZEROFILL"
%%

<YYINITIAL> {

  /* keywords */
  {Reserved}                     { return token(TokenType.KEYWORD); }
  
  /* operators */

  "("                            { return token(TokenType.OPER); }
  ")"                            { return token(TokenType.OPER); }
  "{"                            { return token(TokenType.OPER); } 
  "}"                            { return token(TokenType.OPER); } 
  "["                            { return token(TokenType.OPER); } 
  "]"                            { return token(TokenType.OPER); } 
  ";"                            { return token(TokenType.OPER); } 
  ","                            { return token(TokenType.OPER); } 
  "."                            { return token(TokenType.OPER); } 
  "@"                            { return token(TokenType.OPER); } 
  
  "="                            { return token(TokenType.OPER); } 
  ">"                            { return token(TokenType.OPER); } 
  "<"                            { return token(TokenType.OPER); }
  "!"                            { return token(TokenType.OPER); } 
  "~"                            { return token(TokenType.OPER); } 
  "?"                            { return token(TokenType.OPER); } 
  ":"                            { return token(TokenType.OPER); } 

  /* string literal */
  \"{StringCharacter}+\"         { return token(TokenType.STRING); } 

  \'{SingleCharacter}+\          { return token(TokenType.STRING); } 

  /* numeric literals */

  {DecIntegerLiteral}            { return token(TokenType.NUMBER); }
 
  {FloatLiteral}                 { return token(TokenType.NUMBER); }
  
  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}+                  { /* skip */ }

  /* identifiers */ 
  {Identifier}                   { return token(TokenType.IDENT); }

}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

