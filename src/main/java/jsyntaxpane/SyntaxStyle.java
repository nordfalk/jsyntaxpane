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

public final class SyntaxStyle {

    private Color color;
    private int fontStyle;

    public SyntaxStyle() {
        super();
    }

    public SyntaxStyle(Color color, boolean bold, boolean italic) {
        super();
        this.color = color;
        setBold(bold);
        setItalic(italic);
    }

    public SyntaxStyle(Color color, int fontStyle) {
        super();
        this.color = color;
        this.fontStyle = fontStyle;
    }

    SyntaxStyle(String str) {
        String[] parts = str.split("\\s*,\\s*");
        if(parts.length != 2) {
            throw new IllegalArgumentException("style not correct format: " + str);
        }
        this.color = new Color(Integer.decode(parts[0]));
        this.fontStyle = Integer.decode(parts[1]);
    }

    public boolean isBold() {
        return (fontStyle & Font.BOLD) != 0;
    }

    public void setBold(Boolean bold) {
        if (bold) {
            fontStyle |= Font.BOLD;
        } else {
            int mask = -1 ^ Font.BOLD;
            fontStyle = (fontStyle & (mask));
        }
    }

    public String getColorString() {
        return String.format("0x%06x", color.getRGB() & 0x00ffffff);
    }

    public void setColorString(String color) {
        this.color = Color.decode(color);
    }

    public Boolean isItalic() {
        return (fontStyle & Font.ITALIC) != 0;
    }

    public void setItalic(Boolean italic) {
        if (italic) {
            fontStyle |= Font.ITALIC;
        } else {
            fontStyle = (fontStyle & ( -1 ^ Font.ITALIC));
        }
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public Color getColor() {
        return color;
    }
}
