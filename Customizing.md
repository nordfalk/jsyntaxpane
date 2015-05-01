JSyntaxPane uses "Lexers" to distinguish different tokens of any supported languages.  Each Lexer must implement the `Lexer interface`.  The Lexers provided were all done using the great [JFlex](http://jflex.de/).  JFlex takes a lexer definition file, and creates a one class lexer.  The Lex files for the languages currently supported are in the the JFlex folder of the zip distribution file.

I will not go into too much details about how to write lex files.  the JFLex site has a [great manual](http://www.jflex.de/manual.html).  But in a nutshell, you can copy a provided lex file, modify it to your needs, then run it through JFlex.  Put the generated Java file in the jsyntaxpane package, then build.  Make sure you modify the `%class ` line and use the same name for the constructor in the block below.

The `TokenTypes` class is an enum of all supported `TokenTypes`.  You can also add more types if you need.  If you do that, also modify the `SyntaxStyle` and `SyntaxStyles` class to use those types.

You also need to modify the `SyntaxKit class createDefaultDocument method` to use your lexer for the given language.  You may also want to modify the `install method of SyntaxKit` to add other default actions for the component.  I'm considering an automated method to make all this _automated_, without changing the `SyntaxKit class`.

Once you are done, and built your lexer, you can change the `SyntaxTester` to test your lexer.  Modify the Tester class to use your lexer instead of the built in ones.  Whenever the caret is moved, the Token under the caret is displayed in the line below.  That makes testing very easy.

_If you create Lexers, please consider contributing them here._