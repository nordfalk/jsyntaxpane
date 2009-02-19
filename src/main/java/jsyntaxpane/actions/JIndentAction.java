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

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

public class JIndentAction extends DefaultSyntaxAction {

    /**
     * creates new JIndentAction.
     * This class should be mapped to VK_ENTER.  It performs proper indentation
     * for Java Type languages and automatically inserts "*" in multi-line comments
     * Initial Code contributed by ser... AT mail.ru
     */
    public JIndentAction() {
        super("JINDENT");
    }

    /**
     * {@inheritDoc}
     * @param e 
     */
    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc,
            int dot, ActionEvent e) {
        int pos = target.getCaretPosition();
        int start = sDoc.getParagraphElement(pos).getStartOffset();
        String line = ActionUtils.getLine(target);
        String lineToPos = line.substring(0, pos - start);
        String prefix = ActionUtils.getIndent(line);
        Token t = sDoc.getTokenAt(pos);
        if (TokenType.isComment(t)) {
            if (line.trim().endsWith("*/")) {
                prefix = prefix.substring(0, prefix.length() - 1);
            } else if (line.trim().startsWith("*")) {
                prefix += "* ";
            } else if (line.trim().startsWith("/*")) {
                prefix += " * ";
            }
        } else if (lineToPos.trim().endsWith("{")) {
            prefix += ActionUtils.getTab(target);
        } else {
            String noComment = sDoc.getUncommentedText(start, pos); // skip EOL comments

            if (noComment.trim().endsWith("{")) {
                prefix += ActionUtils.getTab(target);
            }
        }
        target.replaceSelection("\n" + prefix);
    }
}
