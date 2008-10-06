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
package jsyntaxpane;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class SyntaxDocument extends PlainDocument {

    Lexer lexer;
    List<Token> tokens;
    UndoManager undo = new CompoundUndoManager();

    public SyntaxDocument(Lexer lexer) {
        super();
        putProperty(PlainDocument.tabSizeAttribute, 4);
        this.lexer = lexer;
        // Listen for undo and redo events
        addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                if (evt.getEdit().isSignificant()) {
                    undo.addEdit(evt.getEdit());
                }
            }
        });
    }

    /**
     * Parse the entire document and return list of tokens that do not already
     * exist in the tokens list.  There may be overlaps, and replacements, 
     * which we will cleanup later.
     * @return list of tokens that do not exist in the tokens field 
     */
    private void parse() {
        // if we have no lexer, then we must have no tokens...
        if (lexer == null) {
            tokens = null;
            return;
        }
        List<Token> toks = new ArrayList<Token>(getLength() / 10);
        long ts = System.nanoTime();
        int len = getLength();
        try {
            Segment seg = new Segment();
            getText(0, getLength(), seg);
            CharArrayReader reader = new CharArrayReader(seg.array, seg.offset, seg.count);
            lexer.yyreset(reader);
            Token token;
            while ((token = lexer.yylex()) != null) {
                toks.add(token);
            }
        } catch (BadLocationException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            // This will not be thrown from the Lexer
            log.log(Level.SEVERE, null, ex);
        } finally {
            // Benchmarks:
            // Parsed 574038 chars in 81 ms, giving 74584 tokens
//                System.out.printf("Parsed %d in %d ms, giving %d tokens\n",
//                        len, (System.nanoTime() - ts) / 1000000, toks.size());
            if (log.isLoggable(Level.FINEST)) {
                log.finest(String.format("Parsed %d in %d ms, giving %d tokens\n",
                        len, (System.nanoTime() - ts) / 1000000, toks.size()));
            }
            tokens = toks;
        }
    }

    @Override
    protected void fireChangedUpdate(DocumentEvent e) {
        parse();
        super.fireChangedUpdate(e);
    }

    @Override
    protected void fireInsertUpdate(DocumentEvent e) {
        parse();
        super.fireInsertUpdate(e);
    }

    @Override
    protected void fireRemoveUpdate(DocumentEvent e) {
        parse();
        super.fireRemoveUpdate(e);
    }

    @Override
    protected void fireUndoableEditUpdate(UndoableEditEvent e) {
        parse();
        super.fireUndoableEditUpdate(e);
    }
    
    /**
     * Replace the token with the replacement string
     * @param token
     * @param replacement
     */
    public void replaceToken(Token token, String replacement) {
        try {
            replace(token.start, token.length, replacement, null);
        } catch (BadLocationException ex) {
            log.log(Level.WARNING, "unable to replace token: " + token, ex);
        }
    }

    /**
     * This class is used to iterate over tokens between two positions
     * 
     */
    class TokenIterator implements Iterator<Token> {

        int start;
        int end;
        int ndx = 0;

        @SuppressWarnings("unchecked")
        private TokenIterator(int start, int end) {
            this.start = start;
            this.end = end;
            if (tokens != null && !tokens.isEmpty()) {
                Token token = new Token(TokenType.COMMENT, start, end - start);
                ndx = Collections.binarySearch((List) tokens, token);
                // we will probably not find the exact token...
                if (ndx < 0) {
                    // so, start from one before the token where we should be...
                    // -1 to get the location, and another -1 to go back..
                    ndx = (-ndx - 1 - 1 < 0) ? 0 : (-ndx - 1 - 1);
                    Token t = tokens.get(ndx);
                    // if the prev token does not overlap, then advance one
                    if (t.start + t.length <= start) {
                        ndx++;
                    }

                }
            }
        }

        @Override
        public boolean hasNext() {
            if (tokens == null) {
                return false;
            }
            if (ndx >= tokens.size()) {
                return false;
            }
            Token t = tokens.get(ndx);
            if (t.start >= end) {
                return false;
            }
            return true;
        }

        @Override
        public Token next() {
            return tokens.get(ndx++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Return an iterator of tokens between p0 and p1.
     * @param start
     * @param end
     * @return
     */
    protected Iterator<Token> getTokens(int start, int end) {
        return new TokenIterator(start, end);
    }

    /**
     * Find the token at a given position.  May return null if no token is
     * found (whitespace skipped) or if the position is out of range:
     * @param pos
     * @return
     */
    public Token getTokenAt(int pos) {
        if (tokens == null || tokens.isEmpty() || pos > getLength()) {
            return null;
        }
        Token tok = null;
        Token tKey = new Token(TokenType.DEFAULT, pos, 1);
        @SuppressWarnings("unchecked")
        int ndx = Collections.binarySearch((List) tokens, tKey);
        if (ndx < 0) {
            // so, start from one before the token where we should be...
            // -1 to get the location, and another -1 to go back..
            ndx = (-ndx - 1 - 1 < 0) ? 0 : (-ndx - 1 - 1);
            Token t = tokens.get(ndx);
            if ((t.start <= pos) && (pos <= (t.start + t.length))) {
                tok = t;
            }
        } else {
            tok = tokens.get(ndx);
        }
        return tok;
    }

    /**
     * Perform an undo action, if possible
     */
    public void doUndo() {
        if (undo.canUndo()) {
            undo.undo();
            parse();
        }
    }

    /**
     * Perform a redo action, if possible.
     */
    public void doRedo() {
        if (undo.canRedo()) {
            undo.redo();
            parse();
        }
    }

    /**
     * This will discard all undoable edits
     */
    public void clearUndos() {
        undo.discardAllEdits();
    }
    // our logger instance...
    private static Logger log = Logger.getLogger(SyntaxDocument.class.getName());
}
