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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import jsyntaxpane.util.JarServiceProvider;

/**
 * The STyles to use for each TokenType.  The defaults are created here, and
 * then the resource META-INF/services/syntaxstyles.properties is read and
 * merged.  You can also pass a properties instance and merge your prefered
 * styles into the default styles
 * 
 * @author Ayman
 */
public class SyntaxStyles {

    /**
     * You can call the
     * @param styles
     * @param s
     */
    public void mergeStyles(Properties styles) {
        for (String token : styles.stringPropertyNames()) {
            String stv = styles.getProperty(token);
            try {
                TokenType tt = TokenType.valueOf(token);
                SyntaxStyle tokenStyle = new SyntaxStyle(stv);
                put(tt, tokenStyle);
            } catch (IllegalArgumentException ex) {
                LOG.warning("illegal token type or style for: " + token);
            }
        }
    }

    Map<TokenType, SyntaxStyle> styles;
    private static SyntaxStyles instance = createInstance();
    private static Logger LOG = Logger.getLogger(SyntaxStyles.class.getName());

    private SyntaxStyles() {
    }

    /**
     * Create default styles
     * @return
     */
    private static SyntaxStyles createInstance() {
        SyntaxStyles s = new SyntaxStyles();
        s.put(TokenType.OPERATOR, new SyntaxStyle(Color.BLACK, true, false));
        s.put(TokenType.KEYWORD, new SyntaxStyle(new Color(0x333399), false, true));
        s.put(TokenType.TYPE, new SyntaxStyle(Color.BLACK, false, true));
        s.put(TokenType.STRING, new SyntaxStyle(new Color(0xcc6600), false, false));
        s.put(TokenType.NUMBER, new SyntaxStyle(new Color(0x999933), true, false));
        s.put(TokenType.REGEX, new SyntaxStyle(new Color(0xcc6600), false, false));
        s.put(TokenType.IDENTIFIER, new SyntaxStyle(Color.BLACK, false, false));
        s.put(TokenType.COMMENT, new SyntaxStyle(new Color(0x339933), false, true));
        s.put(TokenType.DEFAULT, new SyntaxStyle(Color.BLACK, false, false));

        Properties styles = JarServiceProvider.getProperties(SyntaxStyles.class);
        s.mergeStyles(styles);
        return s;
    }

    public static SyntaxStyles getInstance() {
        return instance;
    }

    public void put(TokenType type, SyntaxStyle style) {
        if (styles == null) {
            styles = new HashMap<TokenType, SyntaxStyle>();
        }
        styles.put(type, style);
    }

    /**
     * Set the graphics font and others to the style for the given token
     * @param g
     * @param type
     */
    public void setGraphicsStyle(Graphics g, TokenType type) {
        Font c = g.getFont();
        SyntaxStyle ss = styles.get(type);
        if (ss != null) {
            g.setFont(g.getFont().deriveFont(ss.getFontStyle()));
            g.setColor(ss.getColor());
        } else {
            g.setFont(g.getFont().deriveFont(Font.PLAIN));
            g.setColor(Color.BLACK);
        }
    }
}
