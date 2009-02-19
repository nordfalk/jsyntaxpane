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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.util.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Attempt to prettify an XML document.
 * @author Ayman Al-Sairafi
 */
public class XmlPrettifyAction extends DefaultSyntaxAction {

    public XmlPrettifyAction() {
        super("XML_PRETTIFY");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (transformer == null) {
            return;
        }
        JTextComponent target = getTextComponent(e);
        try {
            SyntaxDocument sdoc = ActionUtils.getSyntaxDocument(target);
            StringWriter out = new StringWriter(sdoc.getLength());
            StringReader reader = new StringReader(target.getText());
            InputSource src = new InputSource(reader);
            Document doc = docBuilder.parse(src);
            //Setup indenting to "pretty print"
            transformer.transform(new DOMSource(doc), new StreamResult(out));
            target.setText(out.toString());
        } catch (SAXParseException ex) {
            showErrorMessage(target,
                    String.format("XML error: %s\nat(%d, %d)",
                    ex.getMessage(), ex.getLineNumber(), ex.getColumnNumber()));
            ActionUtils.setCaretPosition(target, ex.getLineNumber(), ex.getColumnNumber() - 1);
        } catch (TransformerException ex) {
            showErrorMessage(target, ex.getMessageAndLocation());
        } catch (SAXException ex) {
            showErrorMessage(target, ex.getLocalizedMessage());
        } catch (IOException ex) {
            showErrorMessage(target, ex.getLocalizedMessage());
        }
    }
    static Transformer transformer;
    static DocumentBuilder docBuilder;

    private static void showErrorMessage(JTextComponent text, String msg) {
        Component parent = SwingUtilities.getWindowAncestor(text);
        JOptionPane.showMessageDialog(parent, msg, "JsyntaxPAne XML", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void config(Configuration config, String name) {
        try {
            TransformerFactory tfactory = TransformerFactory.newInstance();
            transformer = tfactory.newTransformer();
            String indentOption = config.getString(name + ".Indent", "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, indentOption);
            String standAlone = config.getString(name + ".StandAlone", "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, standAlone);
            String omitDeclaration = config.getString(name + ".OmitDeclaration", "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitDeclaration);
            String indentAmount = config.getString(name + ".IndentAmount", "4");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indentAmount);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XmlPrettifyAction.class.getName()).log(Level.SEVERE, null, ex);
            transformer = null;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlPrettifyAction.class.getName()).log(Level.SEVERE, null, ex);
            transformer = null;
        }
    }
}
