/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairaf@gmail.com
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

/**
 *
 * @author A List of Styles
 */
public class SyntaxStyles {

    Map<TokenType, SyntaxStyle> styles;
    private static SyntaxStyles instance = createInstance();

    private SyntaxStyles() {
    }

    /**
     * Create default styles
     * @return
     */
    private static SyntaxStyles createInstance() {
        SyntaxStyles s = new SyntaxStyles();
        s.add(TokenType.OPER, new SyntaxStyle(Color.BLACK, true, false));
        s.add(TokenType.KEYWORD, new SyntaxStyle(new Color(0x333399), false, true));
        s.add(TokenType.TYPE, new SyntaxStyle(Color.BLACK, false, true));
        s.add(TokenType.STRING, new SyntaxStyle(new Color(0xcc6600), false, false));
        s.add(TokenType.NUMBER, new SyntaxStyle(new Color(0x999933), true, false));
        s.add(TokenType.REGEX, new SyntaxStyle(new Color(0xcc6600), false, false));
        s.add(TokenType.IDENT, new SyntaxStyle(Color.BLACK, false, false));
        s.add(TokenType.COMMENT, new SyntaxStyle(new Color(0x339933), false, true));
        s.add(TokenType.DEFAULT, new SyntaxStyle(Color.BLACK, false, false));
        return s;
    }

    public static SyntaxStyles getInstance() {
        return instance;
    }

    public void add(TokenType type, SyntaxStyle style) {
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
