# jsyntaxpane
Export from code.google.com/p/jsyntaxpane of r096 branch

A very simple to use and extend JEditorKit that supports few languages.  The main goal is to make it easy to have nice looking Java Swing Editors with support for Syntax Highlighting.

# Introduction #

JSyntaxPane provides you with a very simple way of highlighting your code / snippets in Java Swing.  Here is how to use it.

# Details #

  1. Load the latest release.

  1. You can build from source, or just take the JSyntaxPane.jar from the dist folder.  Add it to your classpath.

  1. JSyntaxPane needs a JEditorPane control.  Just set the control's editorKit property as follows:
```
    jEdtTest.setEditorKit(new SyntaxKit("java"));
```

The languages currently supported are Java, JavaScript, Groovy and XML.

To change the default colors, modify the SyntaxStyles class.

That's it!

#Building JSyntaxPane

# Pre-requsites #

Before building, you need to [checkout the source code](http://code.google.com/p/jsyntaxpane/source/checkout).

For building, you need to install [Maven](http://maven.apache.org/).


# Details #

To build from the command-line, just cd into the checked out folder and issue the command:

```
mvn package
```

The JFlex generated sources will be found in `target/generated-sources`. The binary jar will be built in the `target` folder.

# IDE Configuration #

## Netbeans 6.1 ##

**Tools>Plugins>Available Plugins and choose and install Maven.** After installation, just use the IDE's Open Project option to open JSyntaxPane checkedout project (no need to configure dependencies, source directories, Java platform versions etc.)--Maven projects can be directly be opened in Netbeans.

## Eclipse ##

http://maven.apache.org/eclipse-plugin.html

TODO -- Somebody write this!.



# Customizing

JSyntaxPane uses "Lexers" to distinguish different tokens of any supported languages.  Each Lexer must implement the `Lexer interface`.  The Lexers provided were all done using the great [JFlex](http://jflex.de/).  JFlex takes a lexer definition file, and creates a one class lexer.  The Lex files for the languages currently supported are in the the JFlex folder of the zip distribution file.

I will not go into too much details about how to write lex files.  the JFLex site has a [great manual](http://www.jflex.de/manual.html).  But in a nutshell, you can copy a provided lex file, modify it to your needs, then run it through JFlex.  Put the generated Java file in the jsyntaxpane package, then build.  Make sure you modify the `%class ` line and use the same name for the constructor in the block below.

The `TokenTypes` class is an enum of all supported `TokenTypes`.  You can also add more types if you need.  If you do that, also modify the `SyntaxStyle` and `SyntaxStyles` class to use those types.

You also need to modify the `SyntaxKit class createDefaultDocument method` to use your lexer for the given language.  You may also want to modify the `install method of SyntaxKit` to add other default actions for the component.  I'm considering an automated method to make all this _automated_, without changing the `SyntaxKit class`.

Once you are done, and built your lexer, you can change the `SyntaxTester` to test your lexer.  Modify the Tester class to use your lexer instead of the built in ones.  Whenever the caret is moved, the Token under the caret is displayed in the line below.  That makes testing very easy.

_If you create Lexers, please consider contributing them here._



## Version 0.9.5 ##
  * Reflection type completions for Java (F1)
  * Added support for HTML and XPath
  * Added pop-up menus to all kits (right-click).
  * Overhauled the Configurations classes. much easier to use and maintain.
  * b11 _Code templates for Java and others_
  
