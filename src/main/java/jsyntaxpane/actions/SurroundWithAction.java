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
import jsyntaxpane.util.Configuration;

/**
 * Surround the selection with some text.
 *
 * The pairs are hard-coded here.
 */
public class SurroundWithAction extends DefaultSyntaxAction {

    private boolean indent = true;
    private String prefix = "";
    private String postfix = "";

    public SurroundWithAction() {
        super("SURROUND_ACTION");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc,
            int dot, ActionEvent e) {
        String[] selection;
        selection = ActionUtils.getSelectedLines(target);
        String lineIndent = (selection.length == 0) ? "" : ActionUtils.getIndent(selection[0]);
        StringBuilder repl = new StringBuilder();
        appendWithIndent(repl, getPrefix(), lineIndent);
        String tab = ActionUtils.getTab(target);
        for (String line : selection) {
            if (indent) {
                repl.append(tab);
            }
            repl.append(line);
            repl.append("\n");
        }
        appendWithIndent(repl, getPostfix(), lineIndent);
        target.replaceSelection(repl.toString());
    }

    @Override
    public void config(Configuration config, String name) {
        this.prefix = config.getString(name + ".prefix", "");
        this.postfix = config.getString(name + ".postfix", "");
        this.indent = config.getBoolean(name + ".indent", true);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    void appendWithIndent(StringBuilder sb, String lines, String indent) {
        sb.append(indent);
        for (int i = 0; i < lines.length(); i++) {
            sb.append(lines.charAt(i));
            // add indentation to everythin except the last line
            if (lines.charAt(i) == '\n' && i < lines.length() - 1) {
                sb.append(indent);
            }
        }
    }
}
