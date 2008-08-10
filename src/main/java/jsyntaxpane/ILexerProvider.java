package jsyntaxpane;

/**
 *
 * @author subwiz
 */
public interface ILexerProvider {
    public Class getLexerClass();
    public String[] getNames();
}
