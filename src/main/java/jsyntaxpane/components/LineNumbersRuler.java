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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.actions.ActionUtils;
import jsyntaxpane.actions.gui.GotoLineDialog;
import jsyntaxpane.util.Configuration;

/**
 * LineRuleis used to number the lines in the EdiorPane
 * @author Ayman Al-Sairafi
 */
public class LineNumbersRuler extends JComponent
        implements SyntaxComponent, PropertyChangeListener, DocumentListener {

    public static final String PROPERTY_BACKGROUND = "LineNumbers.Background";
    public static final String PROPERTY_FOREGROUND = "LineNumbers.Foreground";
    public static final String PROPERTY_LEFT_MARGIN = "LineNumbers.LeftMargin";
    public static final String PROPERTY_RIGHT_MARGIN = "LineNumbers.RightMargin";
    public static final String PROPERTY_Y_OFFSET = "LineNumbers.YOFFset";
    public static final int DEFAULT_R_MARGIN = 5;
    public static final int DEFAULT_L_MARGIN = 5;
    public static final int DEFAULT_Y_OFFSET = -2;
    private JEditorPane pane;
    private String format;
    private int lineCount = -1;
    private int r_margin = DEFAULT_R_MARGIN;
    private int l_margin = DEFAULT_L_MARGIN;
    private int y_offset = DEFAULT_Y_OFFSET;
    private int charHeight;
    private int charWidth;
    private MouseListener mouseListener = null;

    private Status status;

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
        int lh = charHeight;
        int end = clip.y + clip.height + lh;
        int lineNum = clip.y / lh + 1;
        // round the start to a multiple of lh, and shift by 2 pixels to align
        // properly to the text.
        for (int y = (clip.y / lh) * lh + lh + y_offset; y <= end; y += lh) {
            String text = String.format(format, lineNum);
            g.drawString(text, l_margin, y);
            lineNum++;
            if (lineNum > lineCount) {
                break;
            }
        }
    }

    /**
     * Update the size of the line numbers based on the length of the document
     */
    private void updateSize() {
        int newLineCount = ActionUtils.getLineCount(pane);
        if (newLineCount == lineCount) {
            return;
        }
        lineCount = newLineCount;
        int h = lineCount * charHeight + pane.getHeight();
        int d = (int) Math.log10(lineCount) + 1;
        if (d < 1) {
            d = 1;
        }
        int w = d * charWidth + r_margin + l_margin;
        format = "%" + d + "d";
        setPreferredSize(new Dimension(w, h));
        getParent().doLayout();
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
    public void config(Configuration config) {
        r_margin = config.getInteger(PROPERTY_RIGHT_MARGIN, DEFAULT_R_MARGIN);
        l_margin = config.getInteger(PROPERTY_LEFT_MARGIN, DEFAULT_L_MARGIN);
        y_offset = config.getInteger(PROPERTY_Y_OFFSET, DEFAULT_Y_OFFSET);
        Color foreground = config.getColor(PROPERTY_FOREGROUND, Color.BLACK);
        setForeground(foreground);
        Color back = config.getColor(PROPERTY_BACKGROUND, Color.WHITE);
        setBackground(back);
    }

    @Override
    public void install(JEditorPane editor) {
        this.pane = editor;
        charHeight = pane.getFontMetrics(pane.getFont()).getHeight();
        charWidth = pane.getFontMetrics(pane.getFont()).charWidth('0');
        editor.addPropertyChangeListener(this);
        JScrollPane sp = getScrollPane(pane);
        if (sp == null) {
            Logger.getLogger(this.getClass().getName()).warning(
                    "JEditorPane is not enclosed in JScrollPane, " +
                    "no LineNumbers will be displayed");
        } else {
            sp.setRowHeaderView(this);
            this.pane.getDocument().addDocumentListener(this);
            updateSize();
            mouseListener = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    GotoLineDialog.showForEditor(pane);
                }
            };
            addMouseListener(mouseListener);
        }
        status = Status.INSTALLING;
    }

    @Override
    public void deinstall(JEditorPane editor) {
        removeMouseListener(mouseListener);
        status = Status.DEINSTALLING;
        JScrollPane sp = getScrollPane(editor);
        if (sp != null) {
            editor.getDocument().removeDocumentListener(this);
            sp.setRowHeaderView(null);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("document")) {
            if (evt.getOldValue() instanceof SyntaxDocument) {
                SyntaxDocument syntaxDocument = (SyntaxDocument) evt.getOldValue();
                syntaxDocument.removeDocumentListener(this);
            }
            if (evt.getNewValue() instanceof SyntaxDocument && status.equals(Status.INSTALLING)) {
                SyntaxDocument syntaxDocument = (SyntaxDocument) evt.getNewValue();
                syntaxDocument.addDocumentListener(this);
                updateSize();
            }
        } else if (evt.getPropertyName().equals("font")) {
            charHeight = pane.getFontMetrics(pane.getFont()).getHeight();
            charWidth = pane.getFontMetrics(pane.getFont()).charWidth('0');
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateSize();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateSize();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateSize();
    }

    public int getLeftMargin() {
        return l_margin;
    }

    public void setLeftMargin(int l_margin) {
        this.l_margin = l_margin;
    }

    public int getRightMargin() {
        return r_margin;
    }

    public void setRightMargin(int r_margin) {
        this.r_margin = r_margin;
    }

    public int getYOffset() {
        return y_offset;
    }

    public void setYOffset(int y_offset) {
        this.y_offset = y_offset;
    }
}
