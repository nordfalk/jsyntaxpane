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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import jsyntaxpane.util.JarServiceProvider;

public class SyntaxKit extends DefaultEditorKit implements ViewFactory {

    public static Font DEFAULT_FONT;
    private String lang;
    private static HashMap<String, SyntaxLanguage> SYNTAX_LANG_MAP = null;
    private static String[] LANGS;
    private Lexer lexer;
    private static Logger LOG = Logger.getLogger(SyntaxKit.class.getName());
    
    static {
        initKit();
    }

    /**
     * Create a new Kit for the given language (text/tal, text/hex-dump, etc)
     * @param lang
     */
    public SyntaxKit(String lang) {
        super();
        this.lang = lang;
        createLexer(lang);
    }

    public SyntaxKit(Lexer lexer, String lang) {
        super();
        this.lexer = lexer;
        this.lang = lang;
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

        SyntaxLanguage synLang = SYNTAX_LANG_MAP.get(lang);
        if (synLang != null) {
            synLang.install(editorPane);
        } else {
            LOG.warning("SyntaxLanguage for language not found: " + lang);
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
     * Use the services to find a suitable lexer
     * @param lang
     */
    public void createLexer(String lang) {
        if (SYNTAX_LANG_MAP.containsKey(lang)) {
            lexer = SYNTAX_LANG_MAP.get(lang).getLexer();
        } else if (SYNTAX_LANG_MAP.containsKey("text/" + lang)) {
            lexer = SYNTAX_LANG_MAP.get("text/" + lang).getLexer();
        } else {
            LOG.warning("Unable to find lexer for language: " + lang);
        }
    }

    /**
     * This is called to initialize the list of lexers we have.  You can call 
     * this at initialization, or it will be called when needed.
     */
    public static void initKit() {
        SYNTAX_LANG_MAP = new HashMap<String, SyntaxLanguage>();

        // attempt to find a suitable default font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        Arrays.sort(fonts);
        if (Arrays.binarySearch(fonts, "Courier new") >= 0) {
            DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 12);
        } else if (Arrays.binarySearch(fonts, "Courier") >= 0) {
            DEFAULT_FONT = new Font("Courier", Font.PLAIN, 12);
        } else if (Arrays.binarySearch(fonts, "Monospaced") >= 0) {
            DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 12);
        }

        try {
            List<Object> sp = JarServiceProvider.getServiceProviders(SyntaxLanguage.class);
            for (Object o : sp) {
                SyntaxLanguage synLang = (SyntaxLanguage) o;
                LOG.finest(synLang.getLanguageNames()[0]);
                registerLang(synLang, synLang.getLanguageNames());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            assert true : ex;
        }
    }

    /**
     * Add the given lexer, that supports the given languages to out list
     * @param lexer Lexer to add
     * @param langs supported array of languages
     */
    public static void registerLang(SyntaxLanguage synLang, String[] langs) {
        for (String lang : langs) {
            SYNTAX_LANG_MAP.put(lang, synLang);
        }
        // throw away the old list, and create a new one when needed
        LANGS = null;
    }

    /**
     * Get a sorted String Array of supported languages.
     * @return
     */
    public static String[] getLangs() {
        if (LANGS == null) {
            int i = 0;
            LANGS = new String[SYNTAX_LANG_MAP.size()];
            for (String l : SYNTAX_LANG_MAP.keySet()) {
                LANGS[i++] = l;
            }
            Arrays.sort(LANGS);
        }
        return LANGS;
    }
}