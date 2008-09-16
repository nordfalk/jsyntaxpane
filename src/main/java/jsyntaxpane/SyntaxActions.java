/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsyntaxpane;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.PlainDocument;
import javax.swing.text.TextAction;

/**
 * Keyboard Actions for various Syntax files.
 * @author Ayman Al-Sairafi
 */
public class SyntaxActions {

    // It is better to use any of these Actions than instatiate new
    // ones.
    public static PairAction LPARAN = new PairAction("LPARAN", "(", ")");
    public static PairAction LSQUARE = new PairAction("LSQUARE", "[", "]");
    public static PairAction DQUOTE = new PairAction("DQUOTE", "\"", "\"");
    public static PairAction SQUOTE = new PairAction("SQUOTE", "'", "'");
    public static SmartIndent SMART_INDENT = new SmartIndent();
    public static JavaIndent JAVA_INDENT = new JavaIndent();
    public static IndentAction INDENT = new IndentAction();
    public static UnindentAction UNINDENT = new UnindentAction();
    public static UndoAction UNDO = new UndoAction();
    public static RedoAction REDO = new RedoAction();

    /**
     * A Pair action inserts a pair of characters (left and right) around the
     * current selection, and then places the caret between them
     */
    public static class PairAction extends TextAction {

        String left;
        String right;

        public PairAction(String actionName, String left, String right) {
            super(actionName);
            this.left = left;
            this.right = right;
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                String selected = target.getSelectedText();
                if (selected != null) {
                    target.replaceSelection(left + selected + right);
                } else {
                    target.replaceSelection(left + right);
                }
                target.setCaretPosition(target.getCaretPosition() - 1);
            }
        }
    }

    /**
     * This action performs SmartIndentation each time VK_ENTER is pressed
     * SmartIndentation is inserting the same amount of spaces as
     * the line above.  May not be too smart, but good enough.
     */
    public static class SmartIndent extends TextAction {

        public SmartIndent() {
            super("SMART_INDENT");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                String line = getLine(target);
                target.replaceSelection("\n" + getIndent(line));
            }
        }
    };

    public static class JavaIndent extends TextAction {

        public JavaIndent() {
            super("JAVA_INDENT");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                String line = getLine(target);
                String prefix = getIndent(line);
                Integer tabSize = (Integer) target.getDocument().getProperty(PlainDocument.tabSizeAttribute);
                if (line.trim().endsWith("{")) {
                    prefix += SPACES.substring(0, tabSize);
                } 
                target.replaceSelection("\n" + prefix);
            }
        }
    }

    /**
     * IndentAction is used to replace Tabs with spaces.  If there is selected 
     * text, then the lines spanning the selection will be shifted
     * right by one tab-width space  character
     */
    public static class IndentAction extends DefaultEditorKit.InsertTabAction {

        public IndentAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                String selected = target.getSelectedText();
                if (selected == null) {
                    PlainDocument pDoc = (PlainDocument) target.getDocument();
                    Integer tabStop = (Integer) pDoc.getProperty(PlainDocument.tabSizeAttribute);
                    // insert needed number of tabs:
                    int lineStart = pDoc.getParagraphElement(target.getCaretPosition()).getStartOffset();
                    // column 
                    int column = target.getCaretPosition() - lineStart;
                    int needed = tabStop - (column % tabStop);
                    target.replaceSelection(SPACES.substring(0, needed));
                } else {
                    String[] lines = getSelectedLines(target);
                    int start = target.getSelectionStart();
                    StringBuilder sb = new StringBuilder();
                    for (String line : lines) {
                        sb.append('\t');
                        sb.append(line);
                        sb.append('\n');
                    }
                    target.replaceSelection(sb.toString());
                    target.select(start, start + sb.length());
                }
            }
        }
    };

    /**
     * This is usually mapped to Shift-TAB to unindent the selection.  The 
     * current line, or the selected lines are un-indented by the tabstop of the
     * document.
     */
    public static class UnindentAction extends TextAction {

        public UnindentAction() {
            super("UNINDENT");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            Integer tabStop = (Integer) target.getDocument().getProperty(PlainDocument.tabSizeAttribute);
            String indent = SPACES.substring(0, tabStop);
            if (target != null) {
                String[] lines = getSelectedLines(target);
                int start = target.getSelectionStart();
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    if (line.startsWith(indent)) {
                        sb.append(line.substring(indent.length()));
                    } else if (line.startsWith("\t")) {
                        sb.append(line.substring(1));
                    } else {
                        sb.append(line);
                    }
                    sb.append('\n');
                }
                target.replaceSelection(sb.toString());
                target.select(start, start + sb.length());
            }
        }
    }

    /**
     * Undo action
     */
    public static class UndoAction extends TextAction {

        public UndoAction() {
            super("UNDO");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                if (target.getDocument() instanceof SyntaxDocument) {
                    SyntaxDocument sDoc = (SyntaxDocument) target.getDocument();
                    sDoc.doUndo();
                }
            }
        }
    }

    /**
     * Redo action
     */
    public static class RedoAction extends TextAction {

        public RedoAction() {
            super("REDO");
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                if (target.getDocument() instanceof SyntaxDocument) {
                    SyntaxDocument sDoc = (SyntaxDocument) target.getDocument();
                    sDoc.doRedo();
                }
            }
        }
    }

    /**
     * Perform Smart Indentation:  pos must be on a line: this method will
     * use the previous lines indentation (number of spaces before any non-space
     * character or end of line) and return that as the prefix
     * @param target
     * @param pos
     */
    private static String getIndent(String line) {
        if (line == null || line.length() == 0) {
            return "";
        }
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }
        return line.substring(0, i);
    }

    /**
     * Return the lines that span the election (split as an array of Strings)
     * if there is no selection then current line is returned.
     * 
     * Note that the strings returned will not contain the terminating line feeds
     * 
     * The text component will then have the full lines set as selection
     * @param target
     * @return String[] of lines spanning selection / or Dot
     */
    private static String[] getSelectedLines(JTextComponent target) {
        String[] lines = null;
        try {
            PlainDocument pDoc = (PlainDocument) target.getDocument();
            int start = pDoc.getParagraphElement(target.getSelectionStart()).getStartOffset();
            int end;
            if (target.getSelectionStart() == target.getSelectionEnd()) {
                end = pDoc.getParagraphElement(target.getSelectionEnd()).getEndOffset();
            } else {
                // if more than one line is selected, we need to subtract one from the end
                // so that we do not select the line with the caret and no selection in it
                end = pDoc.getParagraphElement(target.getSelectionEnd() - 1).getEndOffset();
            }
            target.select(start, end);
            lines = pDoc.getText(start, end - start).split("\n");
            target.select(start, end);
        } catch (BadLocationException ex) {
            Logger.getLogger(SyntaxActions.class.getName()).log(Level.SEVERE, null, ex);
            lines = EMPTY_STRING_ARRAY;
        }
        return lines;
    }

    /**
     * Return the line of text at the document's current position
     * @param target
     * @return
     */
    private static String getLine(JTextComponent target) {
        PlainDocument pDoc = (PlainDocument) target.getDocument();
        return getLineAt(pDoc, target.getCaretPosition());
    }

    /**
     * Return the line of text at the given position.  The returned value may
     * be null.  It will not contain the trailing new-line character.
     * @param doc
     * @param pos
     * @return
     */
    private static String getLineAt(PlainDocument doc, int pos) {
        String line = null;
        int start = doc.getParagraphElement(pos).getStartOffset();
        int end = doc.getParagraphElement(pos).getEndOffset();
        try {
            line = doc.getText(start, end - start);
            if (line != null && line.endsWith("\n")) {
                line = line.substring(0, line.length() - 1);
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(SyntaxActions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return line;
    }
    
    private static final Map<String, Keymap> KEYMAP_MAP = new HashMap<String, Keymap>();

    private static Keymap getKeymap(final String lang){
        Keymap km = KEYMAP_MAP.get(lang);
        if(km == null){
            km = JTextComponent.addKeymap(null,
                    JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP));
            KEYMAP_MAP.put(lang, km);
        }
        return km;
    }

    /**
     * Helper method to add an action to a component
     * @param control
     * @param key
     * @param action
     */
    public static void addAction(String lang, JTextComponent control, Character key,
            TextAction action) {
        Keymap km = getKeymap(lang);
        km.addActionForKeyStroke(KeyStroke.getKeyStroke(key),
                action);
        if(control.getKeymap() != km){
            control.setKeymap(km);
        }
    }

    /**
     * Add the given Action which is activated by KeyStroke to the control
     * @param control
     * @param stroke
     * @param action
     */
    public static void addAction(JTextComponent control, KeyStroke stroke,
            TextAction action) {
        control.getKeymap().addActionForKeyStroke(stroke, action);
    }


    /**
     * Add the given Action which is activated by KeyStroke to the control
     * @param control
     * @param stroke As specified by KeyStroke.getKeyStroke
     * @param action
     * @throws IllegalArgumentException if stroke is invalid
     */
    public static void addAction(final String lang, JTextComponent control, String stroke,
            TextAction action) {
        KeyStroke ks = KeyStroke.getKeyStroke(stroke);
        if (ks == null) {
            throw new IllegalArgumentException("invalid keystroke: " + stroke);
        }
        // TODO: should this be in synchronized block?
        // Not a problem if it will be called by EDT
        Keymap km = getKeymap(lang);
        km.addActionForKeyStroke(ks, action);
        if(km != control.getKeymap()){
            control.setKeymap(km);
        }
    }
    // This is used internally to avoid NPE if we have no Strings
    private static String[] EMPTY_STRING_ARRAY = new String[0];
    // This is used to quickly create Strings of at most 16 spaces (using substring)
    private static String SPACES = "                ";
}
