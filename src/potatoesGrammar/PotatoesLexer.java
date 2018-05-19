// Generated from PotatoesLexer.g4 by ANTLR 4.7.1

	package potatoesGrammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PotatoesLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		HEADER_BEGIN=1, HEADER_END=2, EOL=3, CLASS=4, MAIN=5, SCOPE_BEGIN=6, SCOPE_END=7, 
		EQUAL=8, ADD_EQUAL=9, SUB_EQUAL=10, MULT_EQUAL=11, DIV_EQUAL=12, MOD_EQUAL=13, 
		FUN=14, PARENTHESIS_BEGIN=15, PARENTHESIS_END=16, COMMA=17, COLON=18, 
		RETURN=19, IF=20, FOR=21, WHILE=22, WHEN=23, ARROW=24, NUMBER_TYPE=25, 
		BOOLEAN_TYPE=26, STRING_TYPE=27, VOID_TYPE=28, NOT=29, AND=30, OR=31, 
		EQUALS=32, NOT_EQUAL=33, LESS_THAN=34, LESS_OR_EQUAL=35, GREATER_THAN=36, 
		GREATER_OR_EQUAL=37, MULTIPLY=38, DIVIDE=39, ADD=40, SUBTRACT=41, POWER=42, 
		MODULUS=43, INCREMENT=44, DECREMENT=45, ARRAY=46, ID=47, NUMBER=48, BOOLEAN=49, 
		STRING=50, LINE_COMMENT=51, COMMENT=52, WS=53;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"HEADER_BEGIN", "HEADER_END", "EOL", "CLASS", "MAIN", "SCOPE_BEGIN", "SCOPE_END", 
		"EQUAL", "ADD_EQUAL", "SUB_EQUAL", "MULT_EQUAL", "DIV_EQUAL", "MOD_EQUAL", 
		"FUN", "PARENTHESIS_BEGIN", "PARENTHESIS_END", "COMMA", "COLON", "RETURN", 
		"IF", "FOR", "WHILE", "WHEN", "ARROW", "NUMBER_TYPE", "BOOLEAN_TYPE", 
		"STRING_TYPE", "VOID_TYPE", "NOT", "AND", "OR", "EQUALS", "NOT_EQUAL", 
		"LESS_THAN", "LESS_OR_EQUAL", "GREATER_THAN", "GREATER_OR_EQUAL", "MULTIPLY", 
		"DIVIDE", "ADD", "SUBTRACT", "POWER", "MODULUS", "INCREMENT", "DECREMENT", 
		"ARRAY", "ID", "LETTER", "NUMBER", "DIGIT", "BOOLEAN", "STRING", "ESC", 
		"LINE_COMMENT", "COMMENT", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'header*'", "'**'", "';'", "'class'", "'main'", "'{'", "'}'", "'='", 
		"'+='", "'-='", "'*='", "'/='", "'%='", "'fun'", "'('", "')'", "','", 
		"':'", "'return'", "'if'", "'for'", "'while'", "'when'", "'->'", "'number'", 
		"'boolean'", "'string'", "'void'", "'!'", "'&'", "'|'", "'=='", "'!='", 
		"'<'", "'<='", "'>'", "'>='", "'*'", "'/'", "'+'", "'-'", "'^'", "'%'", 
		"'++'", "'--'", "'Array'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "HEADER_BEGIN", "HEADER_END", "EOL", "CLASS", "MAIN", "SCOPE_BEGIN", 
		"SCOPE_END", "EQUAL", "ADD_EQUAL", "SUB_EQUAL", "MULT_EQUAL", "DIV_EQUAL", 
		"MOD_EQUAL", "FUN", "PARENTHESIS_BEGIN", "PARENTHESIS_END", "COMMA", "COLON", 
		"RETURN", "IF", "FOR", "WHILE", "WHEN", "ARROW", "NUMBER_TYPE", "BOOLEAN_TYPE", 
		"STRING_TYPE", "VOID_TYPE", "NOT", "AND", "OR", "EQUALS", "NOT_EQUAL", 
		"LESS_THAN", "LESS_OR_EQUAL", "GREATER_THAN", "GREATER_OR_EQUAL", "MULTIPLY", 
		"DIVIDE", "ADD", "SUBTRACT", "POWER", "MODULUS", "INCREMENT", "DECREMENT", 
		"ARRAY", "ID", "NUMBER", "BOOLEAN", "STRING", "LINE_COMMENT", "COMMENT", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PotatoesLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PotatoesLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\67\u0170\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22"+
		"\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30"+
		"\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3!\3\"\3\"\3\"\3#\3#\3$\3$"+
		"\3$\3%\3%\3&\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3-\3.\3"+
		".\3.\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\7\60\u0115\n\60\f\60\16\60\u0118"+
		"\13\60\3\61\3\61\3\62\3\62\5\62\u011e\n\62\3\62\3\62\7\62\u0122\n\62\f"+
		"\62\16\62\u0125\13\62\3\62\3\62\6\62\u0129\n\62\r\62\16\62\u012a\5\62"+
		"\u012d\n\62\5\62\u012f\n\62\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3"+
		"\64\3\64\3\64\5\64\u013c\n\64\3\65\3\65\3\65\7\65\u0141\n\65\f\65\16\65"+
		"\u0144\13\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\5\66\u014d\n\66\3\67\3"+
		"\67\3\67\3\67\7\67\u0153\n\67\f\67\16\67\u0156\13\67\3\67\3\67\3\67\3"+
		"\67\38\38\38\38\78\u0160\n8\f8\168\u0163\138\38\38\38\38\38\39\69\u016b"+
		"\n9\r9\169\u016c\39\39\5\u0142\u0154\u0161\2:\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S"+
		"+U,W-Y.[/]\60_\61a\2c\62e\2g\63i\64k\2m\65o\66q\67\3\2\7\5\2C\\aac|\4"+
		"\2--//\3\2\63;\3\2\62;\5\2\13\f\17\17\"\"\2\u017a\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2c\3\2\2\2\2g\3\2\2\2\2i\3"+
		"\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\3s\3\2\2\2\5{\3\2\2\2\7~\3\2\2"+
		"\2\t\u0080\3\2\2\2\13\u0086\3\2\2\2\r\u008b\3\2\2\2\17\u008d\3\2\2\2\21"+
		"\u008f\3\2\2\2\23\u0091\3\2\2\2\25\u0094\3\2\2\2\27\u0097\3\2\2\2\31\u009a"+
		"\3\2\2\2\33\u009d\3\2\2\2\35\u00a0\3\2\2\2\37\u00a4\3\2\2\2!\u00a6\3\2"+
		"\2\2#\u00a8\3\2\2\2%\u00aa\3\2\2\2\'\u00ac\3\2\2\2)\u00b3\3\2\2\2+\u00b6"+
		"\3\2\2\2-\u00ba\3\2\2\2/\u00c0\3\2\2\2\61\u00c5\3\2\2\2\63\u00c8\3\2\2"+
		"\2\65\u00cf\3\2\2\2\67\u00d7\3\2\2\29\u00de\3\2\2\2;\u00e3\3\2\2\2=\u00e5"+
		"\3\2\2\2?\u00e7\3\2\2\2A\u00e9\3\2\2\2C\u00ec\3\2\2\2E\u00ef\3\2\2\2G"+
		"\u00f1\3\2\2\2I\u00f4\3\2\2\2K\u00f6\3\2\2\2M\u00f9\3\2\2\2O\u00fb\3\2"+
		"\2\2Q\u00fd\3\2\2\2S\u00ff\3\2\2\2U\u0101\3\2\2\2W\u0103\3\2\2\2Y\u0105"+
		"\3\2\2\2[\u0108\3\2\2\2]\u010b\3\2\2\2_\u0111\3\2\2\2a\u0119\3\2\2\2c"+
		"\u012e\3\2\2\2e\u0130\3\2\2\2g\u013b\3\2\2\2i\u013d\3\2\2\2k\u014c\3\2"+
		"\2\2m\u014e\3\2\2\2o\u015b\3\2\2\2q\u016a\3\2\2\2st\7j\2\2tu\7g\2\2uv"+
		"\7c\2\2vw\7f\2\2wx\7g\2\2xy\7t\2\2yz\7,\2\2z\4\3\2\2\2{|\7,\2\2|}\7,\2"+
		"\2}\6\3\2\2\2~\177\7=\2\2\177\b\3\2\2\2\u0080\u0081\7e\2\2\u0081\u0082"+
		"\7n\2\2\u0082\u0083\7c\2\2\u0083\u0084\7u\2\2\u0084\u0085\7u\2\2\u0085"+
		"\n\3\2\2\2\u0086\u0087\7o\2\2\u0087\u0088\7c\2\2\u0088\u0089\7k\2\2\u0089"+
		"\u008a\7p\2\2\u008a\f\3\2\2\2\u008b\u008c\7}\2\2\u008c\16\3\2\2\2\u008d"+
		"\u008e\7\177\2\2\u008e\20\3\2\2\2\u008f\u0090\7?\2\2\u0090\22\3\2\2\2"+
		"\u0091\u0092\7-\2\2\u0092\u0093\7?\2\2\u0093\24\3\2\2\2\u0094\u0095\7"+
		"/\2\2\u0095\u0096\7?\2\2\u0096\26\3\2\2\2\u0097\u0098\7,\2\2\u0098\u0099"+
		"\7?\2\2\u0099\30\3\2\2\2\u009a\u009b\7\61\2\2\u009b\u009c\7?\2\2\u009c"+
		"\32\3\2\2\2\u009d\u009e\7\'\2\2\u009e\u009f\7?\2\2\u009f\34\3\2\2\2\u00a0"+
		"\u00a1\7h\2\2\u00a1\u00a2\7w\2\2\u00a2\u00a3\7p\2\2\u00a3\36\3\2\2\2\u00a4"+
		"\u00a5\7*\2\2\u00a5 \3\2\2\2\u00a6\u00a7\7+\2\2\u00a7\"\3\2\2\2\u00a8"+
		"\u00a9\7.\2\2\u00a9$\3\2\2\2\u00aa\u00ab\7<\2\2\u00ab&\3\2\2\2\u00ac\u00ad"+
		"\7t\2\2\u00ad\u00ae\7g\2\2\u00ae\u00af\7v\2\2\u00af\u00b0\7w\2\2\u00b0"+
		"\u00b1\7t\2\2\u00b1\u00b2\7p\2\2\u00b2(\3\2\2\2\u00b3\u00b4\7k\2\2\u00b4"+
		"\u00b5\7h\2\2\u00b5*\3\2\2\2\u00b6\u00b7\7h\2\2\u00b7\u00b8\7q\2\2\u00b8"+
		"\u00b9\7t\2\2\u00b9,\3\2\2\2\u00ba\u00bb\7y\2\2\u00bb\u00bc\7j\2\2\u00bc"+
		"\u00bd\7k\2\2\u00bd\u00be\7n\2\2\u00be\u00bf\7g\2\2\u00bf.\3\2\2\2\u00c0"+
		"\u00c1\7y\2\2\u00c1\u00c2\7j\2\2\u00c2\u00c3\7g\2\2\u00c3\u00c4\7p\2\2"+
		"\u00c4\60\3\2\2\2\u00c5\u00c6\7/\2\2\u00c6\u00c7\7@\2\2\u00c7\62\3\2\2"+
		"\2\u00c8\u00c9\7p\2\2\u00c9\u00ca\7w\2\2\u00ca\u00cb\7o\2\2\u00cb\u00cc"+
		"\7d\2\2\u00cc\u00cd\7g\2\2\u00cd\u00ce\7t\2\2\u00ce\64\3\2\2\2\u00cf\u00d0"+
		"\7d\2\2\u00d0\u00d1\7q\2\2\u00d1\u00d2\7q\2\2\u00d2\u00d3\7n\2\2\u00d3"+
		"\u00d4\7g\2\2\u00d4\u00d5\7c\2\2\u00d5\u00d6\7p\2\2\u00d6\66\3\2\2\2\u00d7"+
		"\u00d8\7u\2\2\u00d8\u00d9\7v\2\2\u00d9\u00da\7t\2\2\u00da\u00db\7k\2\2"+
		"\u00db\u00dc\7p\2\2\u00dc\u00dd\7i\2\2\u00dd8\3\2\2\2\u00de\u00df\7x\2"+
		"\2\u00df\u00e0\7q\2\2\u00e0\u00e1\7k\2\2\u00e1\u00e2\7f\2\2\u00e2:\3\2"+
		"\2\2\u00e3\u00e4\7#\2\2\u00e4<\3\2\2\2\u00e5\u00e6\7(\2\2\u00e6>\3\2\2"+
		"\2\u00e7\u00e8\7~\2\2\u00e8@\3\2\2\2\u00e9\u00ea\7?\2\2\u00ea\u00eb\7"+
		"?\2\2\u00ebB\3\2\2\2\u00ec\u00ed\7#\2\2\u00ed\u00ee\7?\2\2\u00eeD\3\2"+
		"\2\2\u00ef\u00f0\7>\2\2\u00f0F\3\2\2\2\u00f1\u00f2\7>\2\2\u00f2\u00f3"+
		"\7?\2\2\u00f3H\3\2\2\2\u00f4\u00f5\7@\2\2\u00f5J\3\2\2\2\u00f6\u00f7\7"+
		"@\2\2\u00f7\u00f8\7?\2\2\u00f8L\3\2\2\2\u00f9\u00fa\7,\2\2\u00faN\3\2"+
		"\2\2\u00fb\u00fc\7\61\2\2\u00fcP\3\2\2\2\u00fd\u00fe\7-\2\2\u00feR\3\2"+
		"\2\2\u00ff\u0100\7/\2\2\u0100T\3\2\2\2\u0101\u0102\7`\2\2\u0102V\3\2\2"+
		"\2\u0103\u0104\7\'\2\2\u0104X\3\2\2\2\u0105\u0106\7-\2\2\u0106\u0107\7"+
		"-\2\2\u0107Z\3\2\2\2\u0108\u0109\7/\2\2\u0109\u010a\7/\2\2\u010a\\\3\2"+
		"\2\2\u010b\u010c\7C\2\2\u010c\u010d\7t\2\2\u010d\u010e\7t\2\2\u010e\u010f"+
		"\7c\2\2\u010f\u0110\7{\2\2\u0110^\3\2\2\2\u0111\u0116\5a\61\2\u0112\u0115"+
		"\5a\61\2\u0113\u0115\5e\63\2\u0114\u0112\3\2\2\2\u0114\u0113\3\2\2\2\u0115"+
		"\u0118\3\2\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117`\3\2\2\2"+
		"\u0118\u0116\3\2\2\2\u0119\u011a\t\2\2\2\u011ab\3\2\2\2\u011b\u012f\7"+
		"\62\2\2\u011c\u011e\t\3\2\2\u011d\u011c\3\2\2\2\u011d\u011e\3\2\2\2\u011e"+
		"\u011f\3\2\2\2\u011f\u0123\t\4\2\2\u0120\u0122\t\5\2\2\u0121\u0120\3\2"+
		"\2\2\u0122\u0125\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124"+
		"\u012c\3\2\2\2\u0125\u0123\3\2\2\2\u0126\u0128\7\60\2\2\u0127\u0129\t"+
		"\5\2\2\u0128\u0127\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u0128\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012d\3\2\2\2\u012c\u0126\3\2\2\2\u012c\u012d\3\2"+
		"\2\2\u012d\u012f\3\2\2\2\u012e\u011b\3\2\2\2\u012e\u011d\3\2\2\2\u012f"+
		"d\3\2\2\2\u0130\u0131\t\5\2\2\u0131f\3\2\2\2\u0132\u0133\7h\2\2\u0133"+
		"\u0134\7c\2\2\u0134\u0135\7n\2\2\u0135\u0136\7u\2\2\u0136\u013c\7g\2\2"+
		"\u0137\u0138\7v\2\2\u0138\u0139\7t\2\2\u0139\u013a\7w\2\2\u013a\u013c"+
		"\7g\2\2\u013b\u0132\3\2\2\2\u013b\u0137\3\2\2\2\u013ch\3\2\2\2\u013d\u0142"+
		"\7$\2\2\u013e\u0141\5k\66\2\u013f\u0141\13\2\2\2\u0140\u013e\3\2\2\2\u0140"+
		"\u013f\3\2\2\2\u0141\u0144\3\2\2\2\u0142\u0143\3\2\2\2\u0142\u0140\3\2"+
		"\2\2\u0143\u0145\3\2\2\2\u0144\u0142\3\2\2\2\u0145\u0146\7$\2\2\u0146"+
		"j\3\2\2\2\u0147\u0148\7\61\2\2\u0148\u0149\7\61\2\2\u0149\u014d\7$\2\2"+
		"\u014a\u014b\7^\2\2\u014b\u014d\7^\2\2\u014c\u0147\3\2\2\2\u014c\u014a"+
		"\3\2\2\2\u014dl\3\2\2\2\u014e\u014f\7\61\2\2\u014f\u0150\7\61\2\2\u0150"+
		"\u0154\3\2\2\2\u0151\u0153\13\2\2\2\u0152\u0151\3\2\2\2\u0153\u0156\3"+
		"\2\2\2\u0154\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0157\3\2\2\2\u0156"+
		"\u0154\3\2\2\2\u0157\u0158\7\f\2\2\u0158\u0159\3\2\2\2\u0159\u015a\b\67"+
		"\2\2\u015an\3\2\2\2\u015b\u015c\7\61\2\2\u015c\u015d\7,\2\2\u015d\u0161"+
		"\3\2\2\2\u015e\u0160\13\2\2\2\u015f\u015e\3\2\2\2\u0160\u0163\3\2\2\2"+
		"\u0161\u0162\3\2\2\2\u0161\u015f\3\2\2\2\u0162\u0164\3\2\2\2\u0163\u0161"+
		"\3\2\2\2\u0164\u0165\7,\2\2\u0165\u0166\7\61\2\2\u0166\u0167\3\2\2\2\u0167"+
		"\u0168\b8\2\2\u0168p\3\2\2\2\u0169\u016b\t\6\2\2\u016a\u0169\3\2\2\2\u016b"+
		"\u016c\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u016e\3\2"+
		"\2\2\u016e\u016f\b9\2\2\u016fr\3\2\2\2\21\2\u0114\u0116\u011d\u0123\u012a"+
		"\u012c\u012e\u013b\u0140\u0142\u014c\u0154\u0161\u016c\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}