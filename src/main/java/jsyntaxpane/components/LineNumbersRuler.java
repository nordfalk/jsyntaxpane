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
package jsyntaxpane.components;

import java.awt.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import jsyntaxpane.actions.SyntaxActions;
import jsyntaxpane.util.Configuration;

/**
 * LineRuleis used to number the lines in the EdiorPane
 * @author Ayman Al-Sairafi
 */
public class LineNumbersRuler extends JComponent
        implements SyntaxComponent, CaretListener {

    private JEditorPane pane;
    private String format;
    private int lineCount = -1;
    public static final int R_MARIGIN = 5;
    public static final int L_MARIGIN = 5;

    public LineNumbersRuler() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setFont(pane.getFont());
        Rectangle clip = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        g.setColor(getForeground());
        int lh = getLineHeight();
        int end = clip.y + clip.height + lh;
        int lineNum = clip.y / lh + 1;
        // round the start to a multiple of lh, and shift by 2 pixels to align
        // properly to the text.
        for (int y = (clip.y / lh) * lh + lh - 2; y <= end; y += lh) {
            String text = String.format(format, lineNum);
            g.drawString(text, L_MARIGIN, y);
            lineNum++;
            if(lineNum > lineCount) {
                break;
            }
        }
    }

    /**
     * Upddate the size of the line numbers based on the length of the document
     */
    private void updateSize() {
        int newLineCount = SyntaxActions.getLineCount(pane) + 1;
        if(newLineCount == lineCount) {
            return;
        }
        lineCount = newLineCount;
        int h = lineCount * getLineHeight() + pane.getHeight();
        int d = (int) Math.log10(lineCount) + 1;
        if (d < 1) {
            d = 1;
        }
        int w = d * getCharWidth() + R_MARIGIN + L_MARIGIN;
        format = "%" + d + "d";
        setPreferredSize(new Dimension(w, h));
        getParent().doLayout();
    }

    private int getLineHeight() {
        return pane.getFontMetrics(pane.getFont()).getHeight();
    }

    private int getCharWidth() {
        return pane.getFontMetrics(pane.getFont()).charWidth('0');
    }

    /**
     * Get the JscrollPane that contains this EditorPane, or null if no
     * JScrollPane is the parent of this editor
     * @param editorPane
     * @return
     */
    public JScrollPane getScrollPane(JTextComponent editorPane) {
        Container p = editorPane.getParent();
        while (p != null) {
            if (p instanceof JScrollPane) {
                return (JScrollPane) p;
            }
            p = p.getParent();
        }
        return null;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        updateSize();
    }

    public void config(Configuration config, String prefix) {
    }

    public void install(JEditorPane editor) {
        this.pane = editor;
        JScrollPane sp = getScrollPane(pane);
        if (sp == null) {
            Logger.getLogger(this.getClass().getName()).warning(
                    "JEditorPane is not enclosed in JScrollPane, " +
                    "no LineNumbers will be displayed");
        } else {
            sp.setRowHeaderView(this);
            this.pane.addCaretListener(this);
            updateSize();
        }
    }

    public void deinstall(JEditorPane editor) {
        JScrollPane sp = getScrollPane(editor);
        if (sp != null) {
            editor.removeCaretListener(this);
            sp.setRowHeaderView(null);
        }
    }
}
