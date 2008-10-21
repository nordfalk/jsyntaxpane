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
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.Token;

/**
 * This class highlights any pairs of the given language.  Pairs are defined
 * with the Token.pairValue.
 *
 * @author Ayman Al-Sairafi
 */
public class PairsMarker implements CaretListener {

    JTextComponent pane;

    public PairsMarker(JEditorPane pane, Color markerColor) {
        this.pane = pane;
    }


    @Override
    public void caretUpdate(CaretEvent e) {
        Markers.removeMarkers(pane, PAIRS_MARKER);
        int pos = e.getDot();
        SyntaxDocument doc = SyntaxActions.getSyntaxDocument(pane);
        Token token = doc.getTokenAt(pos);
        if (token != null && token.pairValue != 0) {
            Markers.markToken(pane, token, PAIRS_MARKER);
            Token other = doc.getPairFor(token);
            if (other != null) {
                Markers.markToken(pane, other, PAIRS_MARKER);
            }
        }
    }
    
    public static final Markers.SimpleMarker PAIRS_MARKER = new Markers.SimpleMarker(new Color(0xffcc33));
    
}
