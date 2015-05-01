# Introduction #

JSyntaxPane provides you with a very simple to use, and now with simple method to configure, way to handle simple Syntax Highlighting and editing of various languages within your Java Swing application.

Currently supported out of the box are `Java, JavaScript, Properties, Groovy, C, C++, XML, SQL, Ruby and Python`.

Here is a very simple program to use:
```
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import jsyntaxpane.DefaultSyntaxKit;

public class SyntaxTester {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SyntaxTester().setVisible(true);
            }
        });
    }

    public SyntaxTester() {
        JFrame f = new JFrame(TestIssue47.class.getName());
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        DefaultSyntaxKit.initKit();

        final JEditorPane codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        c.add(scrPane, BorderLayout.CENTER);
        c.doLayout();
        codeEditor.setContentType("text/java");
        codeEditor.setText("public static void main(String[] args) {\n}");
        
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
```

A more elaborate tester is already provided in the binary JAR, which is an executable one.

## Basic How to ##
Basically here is what you need to do to use the component.
  1. load the latest jar (or build one yourself), then add it to your classpath.
  1. Call the ` jsyntaxpane.DefaultSyntaxKit.initKit() `
  1. Make sure the `JEditorPane` is added to a `JScrollPane` (this is automatically done for you if you use [NetBeans](http://www.netbeans.org).  _If that is not done, line numbers will not be shown_.
  1. Call ` editor.setContentType("text/java") ` or the other language you need.

And you are done.  You will have the vanilla Java support for your component.

## Configuring ##
As of 0.9.4, many settings of the component can be changed via a single Properties type configuration, or you can pass these properties to the `DefaultSyntaxKit`.  The properties can be used for all languages, or can be specific to one language, when prefixed by the class name of that `EditorKit`.

For example, the following will be used for all languages:
```
# Default color for the Caret, Black
CaretColor = 0x000000
```
And the following will only apply for Java, or any other language that uses the `JavaSyntaxKit`:
```
#
# Performs single color selection (Default = false)
#
JavaSyntaxKit.SingleColorSelect = true
```

You can configure a single setting by calling the `DefaultSyntaxKit.setProperty` method, or you can supply a Properties instance to merge it with the default properties.

All settings are described in the default properties file, located on resources/META-INF/services/jsyntaxpane.config.properties file