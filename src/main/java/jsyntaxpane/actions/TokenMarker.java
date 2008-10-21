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

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

/**
 * This class highlights Tokens within a document whenever the caret is moved
 * 
 * @author Ayman Al-Sairafi
 */
public class TokenMarker implements CaretListener {

    JEditorPane pane;
    Set<TokenType> tokenTypes;

    /**
     * Constructs a new Token highlighter
     * @param pane Editor Pane to listen to
     * @param markerColor the color of the marker
     * @param types the set of <code>TokenTypes</code> to highlight
     */
    public TokenMarker(JEditorPane pane, Color markerColor, Set<TokenType> types) {
        this.pane = pane;
        this.tokenTypes = types;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int pos = e.getDot();
        Markers.removeMarkers(pane, TOKEN_MARKER);
        SyntaxDocument doc = SyntaxActions.getSyntaxDocument(pane);
        Token token = doc.getTokenAt(pos);
        if (token != null && tokenTypes.contains(token.type)) {
            addMarkers(token);
        }
    }

    /**
     * add highlights for the given pattern
     * @param pattern
     */
    void addMarkers(Token tok) {
        SyntaxDocument sDoc = (SyntaxDocument) pane.getDocument();
        sDoc.readLock();
        String text = tok.getText(sDoc);
        Iterator<Token> it = sDoc.getTokens(0, sDoc.getLength());
        while (it.hasNext()) {
            Token nextToken = it.next();
            if (nextToken.length == tok.length && text.equals(nextToken.getText(sDoc))) {
                Markers.markToken(pane, nextToken, TOKEN_MARKER);
            }
        }
        sDoc.readUnlock();
    }
    
    public static final Markers.SimpleMarker TOKEN_MARKER = new Markers.SimpleMarker(new Color(0xffffbb));
    
}
