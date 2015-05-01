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