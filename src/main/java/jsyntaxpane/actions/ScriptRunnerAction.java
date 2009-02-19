/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.util.Configuration;

/**
 * Executes the script in the component's text using a ScriptEngine
 * The Configuration must contain the key [prefix.]ACTION_NAME.ScriptExtension
 * and its value is the ScriptExtension that getEngineByExtension returns
 * If no engine is found, then an option is given to the user to disable the action
 * 
 * @author Ayman Al-Sairafi
 */
public class ScriptRunnerAction extends DefaultSyntaxAction {

    public ScriptRunnerAction() {
        super("SCRIPT_EXECUTE");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc,
            int dot, ActionEvent e) {
        try {
            ScriptEngine eng = getEngine(target);
            if (eng != null) {
                getEngine(target).eval(target.getText());
            }
        } catch (ScriptException ex) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(target),
                    "Error executing script:\n" + ex.getMessage(),
                    "Script Error",
                    JOptionPane.ERROR_MESSAGE);
            ActionUtils.setCaretPosition(target,
                    ex.getLineNumber(),
                    ex.getColumnNumber());
        }
    }

    private ScriptEngine getEngine(JTextComponent target) {
        if (engine == null) {
            if (sem == null) {
                sem = new ScriptEngineManager();
            }
            engine = sem.getEngineByExtension(scriptExtension);
        }
        if (engine == null) {
            int result = JOptionPane.showOptionDialog(target,
                    "Script Engine for [" + scriptExtension + "] not found. Disable this Action?",
                    "jsyntaxpane",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null);
            if (result == JOptionPane.YES_OPTION) {
                setEnabled(false);
            }
        }
        return engine;
    }

    @Override
    public void config(Configuration config, String name) {
        scriptExtension = config.getString(name + ".ScriptExtension");
    }
    
    protected static ScriptEngineManager sem;
    private ScriptEngine engine;
    private String scriptExtension;
}
