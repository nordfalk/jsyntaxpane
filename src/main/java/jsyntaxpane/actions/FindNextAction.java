package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;

/**
 * This class performs a Find Next operation by using the current pattern
 */
public class FindNextAction extends DefaultSyntaxAction {

    public FindNextAction() {
        super("FIND_NEXT");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sdoc,
            int dot, ActionEvent e) {
        DocumentSearchData dsd = DocumentSearchData.getFromEditor(target);
        if (dsd != null) {
            dsd.doFindNext(target);
        }
    }
}
