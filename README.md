# jsyntaxpane

JSyntaxPane is a simple to use editorKit that adds syntax highlighting support to a standard JEditorPane.
The languages currently supported are C, C++, Java, JavaScript, Groovy, SQL, Python, Ruby, Scala, Bash, XPath, Clojure, Lua, XHTML and XML - [full list here](https://github.com/nordfalk/jsyntaxpane/blob/master/jsyntaxpane/src/main/resources/META-INF/services/jsyntaxpane/kitsfortypes.properties)

The original project JSyntaxPane can be found [on google-code](http://code.google.com/p/jsyntaxpane/). This is a fork from the 0.9.6 branch with [Hanns Holger Rutz](https://github.com/Sciss/SyntaxPane)'s work applied.

The original project is (C)opyright by Ayman Al-Sairafi and released under the [Apache License, Version 2.0](http://github.com/Sciss/JSyntaxPane/blob/master/licenses/JSyntaxPane-License.txt).


# How to use #

  1. Load the latest release.

  2. You can build from source, or just take the JSyntaxPane.jar from the dist folder.  Add it to your classpath.

  3a. Create a standard JEditorPane control and use a JSyntaxPane editorKit property as follows:
```
    jEditorPane.setEditorKit(new JavaSyntaxKit());
```

  3b. You can also just set ContentType to one of the available editorkits.
```
    DefaultSyntaxKit.initKit();
    jEditorPane.setContentType("text/java")
```


To change the default colors, modify the SyntaxStyles class.

#Building JSyntaxPane

For building, you need to install [Maven](http://maven.apache.org/).

To build from the command-line, just cd into the checked out folder and issue the command:

```
mvn package
```

The JFlex generated sources will be found in `target/generated-sources`. The binary jar will be built in the `target` folder.

## Netbeans 6.1+ ##

Choose **Tools>Plugins>Available Plugins and choose and install Maven.** After installation, just use the IDE's Open Project option to open JSyntaxPane checkedout project (no need to configure dependencies, source directories, Java platform versions etc.)--Maven projects can be directly be opened in Netbeans.


# Customizing

JSyntaxPane uses "Lexers" to distinguish different tokens of any supported languages.  Each Lexer must implement the `Lexer interface`.  The Lexers provided were all done using the great [JFlex](http://jflex.de/).  JFlex takes a lexer definition file, and creates a one class lexer.  The Lex files for the languages currently supported are in the the JFlex folder of the zip distribution file.

I will not go into too much details about how to write lex files.  the JFLex site has a [great manual](http://www.jflex.de/manual.html).  But in a nutshell, you can copy a provided lex file, modify it to your needs, then run it through JFlex.  Put the generated Java file in the jsyntaxpane package, then build.  Make sure you modify the `%class ` line and use the same name for the constructor in the block below.

The `TokenTypes` class is an enum of all supported `TokenTypes`.  You can also add more types if you need.  If you do that, also modify the `SyntaxStyle` and `SyntaxStyles` class to use those types.

You also need to modify the `SyntaxKit class createDefaultDocument method` to use your lexer for the given language.  You may also want to modify the `install method of SyntaxKit` to add other default actions for the component.  I'm considering an automated method to make all this _automated_, without changing the `SyntaxKit class`.

Once you are done, and built your lexer, you can change the `SyntaxTester` to test your lexer.  Modify the Tester class to use your lexer instead of the built in ones.  Whenever the caret is moved, the Token under the caret is displayed in the line below.  That makes testing very easy.

_If you create Lexers, please consider contributing them._

