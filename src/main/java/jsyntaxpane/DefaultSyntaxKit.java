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
package jsyntaxpane;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import jsyntaxpane.util.JarServiceProvider;

/**
 * The DefaultSyntaxKit is the main entry to SyntaxPane.  To use the package, just 
 * set the EditorKit of the EditorPane to a new instance of this class.
 * 
 * You need to pass a proper lexer to the class.
 * 
 * @author ayman
 */
public class DefaultSyntaxKit extends DefaultEditorKit implements ViewFactory {

    public static Font DEFAULT_FONT;
    private Lexer lexer;
    private static Logger LOG = Logger.getLogger(DefaultSyntaxKit.class.getName());
    
    static {
        initKit();
    }

    /**
     * Create a new Kit for the given language 
     */
    public DefaultSyntaxKit(Lexer lexer) {
        super();
        this.lexer = lexer;
    }

    @Override
    public ViewFactory getViewFactory() {
        return this;
    }

    @Override
    public View create(Element element) {
        return new SyntaxView(element);
    }

    /**
     * Install the View on the given EditorPane.  This is called by Swing and
     * can be used to do anything you need on the JEditorPane control.  Here
     * I set some default Actions.
     * 
     * @param editorPane
     */
    @Override
    public void install(JEditorPane editorPane) {
        super.install(editorPane);
        editorPane.setFont(DEFAULT_FONT);
        if (lexer != null) {
            lexer.install(editorPane);
        }
    }

    /**
     * This is called by Swing to create a Document for the JEditorPane document
     * This may be called before you actually get a reference to the control.
     * We use it here to create a properl lexer and pass it to the 
     * SyntaxDcument we return.
     * @return
     */
    @Override
    public Document createDefaultDocument() {
        return new SyntaxDocument(lexer);
    }


    /**
     * This is called to initialize the list of lexers we have.  You can call 
     * this at initialization, or it will be called when needed.
     * 
     * The method will also add the appropriate EditorKit classes to the
     * corresponding ContentType of the JEditorPane.  After this is called,
     * you can simply call the editor.setCOntentType("text/java") on the 
     * control and you will be done.
     */
    public static void initKit() {
        // attempt to find a suitable default font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        Arrays.sort(fonts);
        if (Arrays.binarySearch(fonts, "Courier new") >= 0) {
            DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 12);
        } else if (Arrays.binarySearch(fonts, "Courier") >= 0) {
            DEFAULT_FONT = new Font("Courier", Font.PLAIN, 12);
        } else if (Arrays.binarySearch(fonts, "Monospaced") >= 0) {
            DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 13);
        }
        
        // read the Default Kits and their associated types
        Properties kitsForTypes = JarServiceProvider.readProperties("jsyntaxpane.kitsfortypes");
        for(String type:kitsForTypes.stringPropertyNames()) {
            String kitForType = kitsForTypes.getProperty(type);
            JEditorPane.registerEditorKitForContentType(type, kitForType);
        }
    }
}