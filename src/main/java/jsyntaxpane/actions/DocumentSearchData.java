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
package jsyntaxpane.actions;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.actions.gui.ReplaceDialog;

/**
 * Data that is shared by Find / Replace and Find Next actions for a Document
 * The data here will be added as a property of the Document using the key
 * PROPERTY_KEY.  Only through the getFtmEditor can you crate a new instance.
 *
 * The class is responsible for handling the doFind and doReplace all actions.
 *
 * The class is also responsible for displaying the Find / Replace dialog
 *
 * @author Ayman Al-Sairafi
 */
public class DocumentSearchData {

    private static final String PROPERTY_KEY = "SearchData";
    private Pattern pattern = null;
    private boolean wrap = true;
    private ReplaceDialog dlg;

    /**
     * This prevent creating a new instance.  You must call the getFromEditor
     * to crate a new instance attached to a Document
     * 
     */
    private DocumentSearchData() {
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean isWrap() {
        return wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * Get the Search data from a Document.  If document does not have any
     * search data, then a new instance is added, put and reurned.
     * @param target JTextCOmponent we are attaching to
     * @return
     */
    public static DocumentSearchData getFromEditor(JTextComponent target) {
        if (target == null) {
            return null;
        }
        Object o = target.getDocument().getProperty(PROPERTY_KEY);
        if (o instanceof DocumentSearchData) {
            DocumentSearchData documentSearchData = (DocumentSearchData) o;
            return documentSearchData;
        } else {
            DocumentSearchData newDSD = new DocumentSearchData();
            target.getDocument().putProperty(PROPERTY_KEY, newDSD);
            return newDSD;
        }
    }
    
    /**
     * Perform a replace all operation on the given component.
     * Note that this create a new duplicate String big as the entire
     * document and then assign it to the target text component
     * @param target
     * @param replacement
     */
    public void doReplaceAll(JTextComponent target, String replacement) {
        SyntaxDocument sDoc = ActionUtils.getSyntaxDocument(target);
        if (sDoc == null) {
            return;
        }
        if (getPattern() == null) {
            return;
        }
        Matcher matcher = sDoc.getMatcher(getPattern());
        String newText = matcher.replaceAll(replacement);
        target.setText(newText);
    }

    /**
     * Perform a FindNext operation on the given text component.  Position
     * the caret at the start of the next found pattern
     * @param target
     */
    public void doFindNext(JTextComponent target) {
        if (getPattern() == null) {
            return;
        }
        SyntaxDocument sDoc = ActionUtils.getSyntaxDocument(target);
        if (sDoc == null) {
            return;
        }
        int start = target.getCaretPosition() + 1;
        // we must advance the position by one, otherwise we will find
        // the same text again
        if (start >= sDoc.getLength()) {
            start = 0;
        }
        Matcher matcher = sDoc.getMatcher(getPattern(), start);
        if (matcher != null && matcher.find()) {
            // since we used an offset in the matcher, the matcher location
            // MUST be offset by that location
            target.select(matcher.start() + start, matcher.end() + start);
        } else {
            if (isWrap()) {
                matcher = sDoc.getMatcher(getPattern());
                if (matcher != null && matcher.find()) {
                    target.select(matcher.start(), matcher.end());
                } else {
                    msgNotFound(target);
                }
            } else {
                msgNotFound(target);
            }
        }
    }

    /**
     * Display an OptionPane dialog that the search string is not found
     * @param target
     */
    public void msgNotFound(Component target) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(target),
                "Search String " + getPattern() + " not found",
                "Find", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show the Find and Replace dialog for the given frame
     * @param target
     */
    public void showDialog(JTextComponent target) {
        if (dlg == null) {
            dlg = new ReplaceDialog(target, this);
        }
        dlg.setVisible(true);
    }
}
