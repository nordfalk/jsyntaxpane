package jsyntaxpane.actions;

import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.util.Configuration;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractUndoRedoAction extends DefaultSyntaxAction {
    private JEditorPane editor;
    protected SyntaxDocument doc;

    private final String property;

    protected AbstractUndoRedoAction(String property, String key) {
        super(key);
        this.property = property;
    }

    private PropertyChangeListener propListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            // System.out.println(property + " - " + e);
            setEnabled(updateState());
        }
    };

    abstract protected boolean updateState();

    private void removeDocument() {
        if (doc != null) {
            doc.removePropertyChangeListener(property, propListener);
            doc = null;
        }
    }

    private void setDocument(SyntaxDocument newDoc) {
        if (doc != null) throw new IllegalStateException();
        doc = newDoc;
        doc.addPropertyChangeListener(property, propListener);
        setEnabled(updateState());
    }

    private PropertyChangeListener docListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            // if (e.getPropertyName().equals("document")) {
                removeDocument();
                Object newDoc = e.getNewValue();
                if (newDoc instanceof SyntaxDocument) {
                    setDocument((SyntaxDocument) newDoc);
                    // editor.removePropertyChangeListener("document", docListener);
                }
//            } else {
//                System.out.println(e.getPropertyName() + " " + e.getNewValue());
//            }
        }
    };

    @Override
    public void install(JEditorPane editor, Configuration config, String name) {
        super.install(editor, config, name);

        if (this.editor != null) throw new IllegalStateException();

        this.editor = editor;
        editor.addPropertyChangeListener("document", docListener);

        // editor.addPropertyChangeListener("editorKit", docListener);

        // Document doc = editor.getDocument();
        // if (doc instanceof SyntaxDocument) {
        //     setDocument((SyntaxDocument) doc);
        // }
    }

    @Override
    public void deinstall(JEditorPane editor) {
        super.deinstall(editor);

        if (this.editor != editor) throw new IllegalStateException();

        editor.removePropertyChangeListener("document", docListener);
        removeDocument();
        this.editor = null;
    }
}