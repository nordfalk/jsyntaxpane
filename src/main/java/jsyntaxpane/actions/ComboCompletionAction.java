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

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.Token;
import jsyntaxpane.actions.gui.ComboCompletionDialog;
import jsyntaxpane.util.Configuration;
import jsyntaxpane.util.JarServiceProvider;

/**
 * ComboBox like Completion Action:
 * This will display a list of items to choose from, it can be used similar to
 * IntelliSense.  The List is obtained from a plain text file, each line being
 * an item in the list.
 * 
 * @author Ayman Al-Sairafi
 */
public class ComboCompletionAction extends DefaultSyntaxAction {

    Map<String, String> completions;
    ComboCompletionDialog dlg;
    private List<String> items;

    public ComboCompletionAction() {
        super("COMBO_COMPLETION");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sdoc,
            int dot, ActionEvent e) {
        if (sdoc == null) {
            return;
        }
        Token token = sdoc.getTokenAt(dot);
        String abbrev = "";
        try {
            if (token != null) {
                abbrev = token.getText(sdoc).toString();
                sdoc.remove(token.start, token.length);
                dot = token.start;
            }
            Window window = SwingUtilities.getWindowAncestor(target);
            if (dlg == null) {
                if (window instanceof Frame) {
                    Frame frame = (Frame) window;
                    dlg = new ComboCompletionDialog(frame, true, items);
                } else {
                    dlg = new ComboCompletionDialog(null, true, items);
                }
            }
            dlg.setLocationRelativeTo(window);
            Point p = window.getLocation();
            // Get location of Dot in rt
            Rectangle rt = target.modelToView(dot);
            Point loc = new Point(rt.x, rt.y);
            // convert the location from Text Componet coordinates to
            // Frame coordinates...
            loc = SwingUtilities.convertPoint(target, loc, window);
            // and then to Screen coordinates
            SwingUtilities.convertPointToScreen(loc, window);
            dlg.setLocation(loc);
            dlg.setFonts(target.getFont());
            dlg.setText(abbrev);
            dlg.setVisible(true);
            String res = dlg.getResult();
            ActionUtils.insertMagicString(target, dot, res);
        } catch (BadLocationException ex) {
            Logger.getLogger(ComboCompletionAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The completions will for now reside on another properties style file
     * referenced by prefix.Completions.File
     *
     * @param config
     * @param name
     */
    @Override
    public void config(Configuration config, String name) {
        // for now we will use just one list for anything.  This can be modified
        // by having a map from TokenType to String[] or something....
        String itemsUrl = config.getString(name + ".Items.URL");
        items = JarServiceProvider.readLines(itemsUrl);
    }

    public TextAction getAction(String key) {
        return this;
    }
}
