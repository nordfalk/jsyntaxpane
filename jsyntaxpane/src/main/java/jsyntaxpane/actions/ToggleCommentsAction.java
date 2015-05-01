/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 * Copyright 2013-2014 Hanns Holger Rutz.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;

/**
 * This action will toggle comments on or off on selected whole lines.
 * 
 * @author Ayman Al-Sairafi, Hanns Holger Rutz
 */
public class ToggleCommentsAction extends DefaultSyntaxAction {

    protected String  lineCommentStart    = "//";
    protected Pattern lineCommentPattern  = null;

    /**
     * creates new JIndentAction.
     * Initial Code contributed by ser... AT mail.ru
     */
    public ToggleCommentsAction() {
        super("toggle-comment");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc,
            int dot, ActionEvent e) {
        if (lineCommentPattern == null) {
            lineCommentPattern  = Pattern.compile("(^\\s*)(" + lineCommentStart + "\\s?)(.*)");
        }
        String[] lines = ActionUtils.getSelectedLines(target);
        int start = target.getSelectionStart();
        StringBuilder toggled = new StringBuilder();
        boolean allComments = true;
        for (int i = 0; i < lines.length; i++) {
            Matcher m1 = lineCommentPattern.matcher(lines[i]);
            if (!m1.find()) {
                allComments = false;
                break;
            }
        }
        for (int i = 0; i < lines.length; i++) {
            if (allComments) {
                Matcher m1 = lineCommentPattern.matcher(lines[i]);
                m1.find();
                toggled.append(m1.replaceFirst("$1$3"));
            } else {
                toggled.append(lineCommentStart);
                toggled.append(' ');
                toggled.append(lines[i]);
            }
            toggled.append('\n');
        }
        target.replaceSelection(toggled.toString());
        target.select(start, start + toggled.length());
    }

    public void setLineComments(String value) {
        String v1 = value.replace("\"", "");
        lineCommentStart   = v1.charAt(v1.length() - 1) == ' ' ? v1.substring(0, v1.length() - 1) : v1;
        lineCommentPattern = null;
    }
}
