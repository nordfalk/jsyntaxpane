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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsyntaxpane.util.JarServiceProvider;

/**
 * This class can be used instead of using SyntaxKit directly to create
 * a SyntaxKit from a language string.  The initial list of lexers is
 * loaded from the file META-INF/resources/jsyntaxpane.lexers
 *
 * You can add to the list (and override the supplied lexers) by a call to
 * register method.
 *
 * @author ayman
 */
public class SyntaxKitFactory {

    private static Map<String, Class<? extends Lexer>> LANG_LEXERS;

    static {
        init();
    }

    /**
     * Add the given lexer to our list of supported languages.
     * @param lexer
     */
    public static void register(Lexer lexer) {
        for (String lang : lexer.getLanguages()) {
            LANG_LEXERS.put(lang, lexer.getClass());
        }
    }

    /**
     * This will initialize the factory with the lexer classes provided in the
     * META-INF/resource folder.
     */
    private static void init() {
        LANG_LEXERS = new HashMap<String, Class<? extends Lexer>>();
        try {
            List<Object> lexers = JarServiceProvider.getServiceProviders(Lexer.class);
            for (Object o : lexers) {
                if (o instanceof Lexer) {
                    Lexer lexer = (Lexer) o;
                    System.out.println("Found: " + o);
                    register(lexer);
                }
            }
            System.out.println("Languages: " + LANG_LEXERS);
        } catch (IOException ex) {
            Logger.getLogger(SyntaxKitFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a SyntaxKit (can be used in editor pane) for the given language
     * @param language
     * @return
     */
    public static SyntaxKit getKitForLanguage(String language) {
        Lexer lexer = null;
        try {
            if (LANG_LEXERS.containsKey(language)) {
                lexer = LANG_LEXERS.get(language).newInstance();
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(SyntaxKitFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SyntaxKitFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new SyntaxKit(lexer);
    }

    /**
     * return a list of all supported languages
     * @return
     */
    public static Object[] getLanguages() {
        return LANG_LEXERS.keySet().toArray();
    }
}
