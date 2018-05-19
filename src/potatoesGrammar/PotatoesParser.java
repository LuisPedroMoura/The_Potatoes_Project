// Generated from PotatoesParser.g4 by ANTLR 4.7.1

	package potatoesGrammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PotatoesParser extends Parser {
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
		STRING=50, LINE_COMMENT=51, COMMENT=52, WS=53, INT=54;
	public static final int
		RULE_program = 0, RULE_code = 1, RULE_header_declaration = 2, RULE_javaCode = 3, 
		RULE_class_declaration = 4, RULE_class_content = 5, RULE_statement = 6, 
		RULE_declaration = 7, RULE_assignment = 8, RULE_assignment_operator = 9, 
		RULE_function = 10, RULE_function_return = 11, RULE_function_call = 12, 
		RULE_control_flow_statement = 13, RULE_for_loop = 14, RULE_while_loop = 15, 
		RULE_when = 16, RULE_when_case = 17, RULE_condition = 18, RULE_logical_operation = 19, 
		RULE_logical_operand = 20, RULE_logical_operator = 21, RULE_comparison = 22, 
		RULE_compare_operator = 23, RULE_operation = 24, RULE_array_declaration = 25, 
		RULE_diamond_begin = 26, RULE_diamond_end = 27, RULE_var = 28, RULE_var_declaration = 29, 
		RULE_type = 30, RULE_value = 31, RULE_values_list = 32;
	public static final String[] ruleNames = {
		"program", "code", "header_declaration", "javaCode", "class_declaration", 
		"class_content", "statement", "declaration", "assignment", "assignment_operator", 
		"function", "function_return", "function_call", "control_flow_statement", 
		"for_loop", "while_loop", "when", "when_case", "condition", "logical_operation", 
		"logical_operand", "logical_operator", "comparison", "compare_operator", 
		"operation", "array_declaration", "diamond_begin", "diamond_end", "var", 
		"var_declaration", "type", "value", "values_list"
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
		"WS", "INT"
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

	@Override
	public String getGrammarFileName() { return "PotatoesParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PotatoesParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PotatoesParser.EOF, 0); }
		public List<CodeContext> code() {
			return getRuleContexts(CodeContext.class);
		}
		public CodeContext code(int i) {
			return getRuleContext(CodeContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HEADER_BEGIN) | (1L << CLASS) | (1L << SCOPE_BEGIN))) != 0)) {
				{
				{
				setState(66);
				code();
				}
				}
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(72);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodeContext extends ParserRuleContext {
		public Class_contentContext class_content() {
			return getRuleContext(Class_contentContext.class,0);
		}
		public Header_declarationContext header_declaration() {
			return getRuleContext(Header_declarationContext.class,0);
		}
		public Class_declarationContext class_declaration() {
			return getRuleContext(Class_declarationContext.class,0);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitCode(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_code);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HEADER_BEGIN) {
				{
				setState(74);
				header_declaration();
				}
			}

			setState(78);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CLASS) {
				{
				setState(77);
				class_declaration();
				}
			}

			setState(80);
			class_content();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Header_declarationContext extends ParserRuleContext {
		public TerminalNode HEADER_BEGIN() { return getToken(PotatoesParser.HEADER_BEGIN, 0); }
		public JavaCodeContext javaCode() {
			return getRuleContext(JavaCodeContext.class,0);
		}
		public TerminalNode HEADER_END() { return getToken(PotatoesParser.HEADER_END, 0); }
		public Header_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterHeader_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitHeader_declaration(this);
		}
	}

	public final Header_declarationContext header_declaration() throws RecognitionException {
		Header_declarationContext _localctx = new Header_declarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_header_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(HEADER_BEGIN);
			setState(83);
			javaCode();
			setState(84);
			match(HEADER_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaCodeContext extends ParserRuleContext {
		public JavaCodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaCode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterJavaCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitJavaCode(this);
		}
	}

	public final JavaCodeContext javaCode() throws RecognitionException {
		JavaCodeContext _localctx = new JavaCodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_javaCode);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(86);
					matchWildcard();
					}
					} 
				}
				setState(91);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_declarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(PotatoesParser.CLASS, 0); }
		public TerminalNode ID() { return getToken(PotatoesParser.ID, 0); }
		public Class_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterClass_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitClass_declaration(this);
		}
	}

	public final Class_declarationContext class_declaration() throws RecognitionException {
		Class_declarationContext _localctx = new Class_declarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_class_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(CLASS);
			setState(93);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_contentContext extends ParserRuleContext {
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public Class_contentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterClass_content(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitClass_content(this);
		}
	}

	public final Class_contentContext class_content() throws RecognitionException {
		Class_contentContext _localctx = new Class_contentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_class_content);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(SCOPE_BEGIN);
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FUN) | (1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
				{
				setState(98);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NUMBER_TYPE:
				case BOOLEAN_TYPE:
				case STRING_TYPE:
				case VOID_TYPE:
				case ARRAY:
				case ID:
					{
					setState(96);
					declaration();
					}
					break;
				case FUN:
					{
					setState(97);
					function();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(103);
			match(SCOPE_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Statement_assignmentContext extends StatementContext {
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public Statement_assignmentContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterStatement_assignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitStatement_assignment(this);
		}
	}
	public static class Statement_function_callContext extends StatementContext {
		public Function_callContext function_call() {
			return getRuleContext(Function_callContext.class,0);
		}
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public Statement_function_callContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterStatement_function_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitStatement_function_call(this);
		}
	}
	public static class Statement_controlFlowStatementContext extends StatementContext {
		public Control_flow_statementContext control_flow_statement() {
			return getRuleContext(Control_flow_statementContext.class,0);
		}
		public Statement_controlFlowStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterStatement_controlFlowStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitStatement_controlFlowStatement(this);
		}
	}
	public static class Statement_declarationContext extends StatementContext {
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public Statement_declarationContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterStatement_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitStatement_declaration(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_statement);
		try {
			setState(115);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				_localctx = new Statement_declarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				declaration();
				setState(106);
				match(EOL);
				}
				break;
			case 2:
				_localctx = new Statement_assignmentContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				assignment();
				setState(109);
				match(EOL);
				}
				break;
			case 3:
				_localctx = new Statement_controlFlowStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(111);
				control_flow_statement();
				}
				break;
			case 4:
				_localctx = new Statement_function_callContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(112);
				function_call();
				setState(113);
				match(EOL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
	 
		public DeclarationContext() { }
		public void copyFrom(DeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Declaration_arrayContext extends DeclarationContext {
		public Array_declarationContext array_declaration() {
			return getRuleContext(Array_declarationContext.class,0);
		}
		public Declaration_arrayContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterDeclaration_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitDeclaration_array(this);
		}
	}
	public static class Declaration_varContext extends DeclarationContext {
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public Declaration_varContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterDeclaration_var(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitDeclaration_var(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_declaration);
		try {
			setState(119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new Declaration_arrayContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				array_declaration();
				}
				break;
			case 2:
				_localctx = new Declaration_varContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				var_declaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
	 
		public AssignmentContext() { }
		public void copyFrom(AssignmentContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Assignment_arrayContext extends AssignmentContext {
		public Array_declarationContext array_declaration() {
			return getRuleContext(Array_declarationContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public Values_listContext values_list() {
			return getRuleContext(Values_listContext.class,0);
		}
		public Assignment_arrayContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_array(this);
		}
	}
	public static class Assignment_varDeclaration_VarContext extends AssignmentContext {
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public Assignment_varDeclaration_VarContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_varDeclaration_Var(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_varDeclaration_Var(this);
		}
	}
	public static class Assigment_var_valueListContext extends AssignmentContext {
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public Values_listContext values_list() {
			return getRuleContext(Values_listContext.class,0);
		}
		public Assigment_var_valueListContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssigment_var_valueList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssigment_var_valueList(this);
		}
	}
	public static class Assignment_var_varContext extends AssignmentContext {
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public Assignment_var_varContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_var_var(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_var_var(this);
		}
	}
	public static class Assignment_var_valueContext extends AssignmentContext {
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Assignment_var_valueContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_var_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_var_value(this);
		}
	}
	public static class Assignment_varDeclaration_ValueContext extends AssignmentContext {
		public Var_declarationContext var_declaration() {
			return getRuleContext(Var_declarationContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Assignment_varDeclaration_ValueContext(AssignmentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_varDeclaration_Value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_varDeclaration_Value(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_assignment);
		try {
			setState(145);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				_localctx = new Assignment_arrayContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(121);
				array_declaration();
				setState(122);
				assignment_operator();
				setState(123);
				values_list();
				}
				break;
			case 2:
				_localctx = new Assignment_varDeclaration_VarContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(125);
				var_declaration();
				setState(126);
				assignment_operator();
				setState(127);
				var();
				}
				break;
			case 3:
				_localctx = new Assignment_varDeclaration_ValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(129);
				var_declaration();
				setState(130);
				assignment_operator();
				setState(131);
				value();
				}
				break;
			case 4:
				_localctx = new Assignment_var_varContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(133);
				var();
				setState(134);
				assignment_operator();
				setState(135);
				var();
				}
				break;
			case 5:
				_localctx = new Assignment_var_valueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(137);
				var();
				setState(138);
				assignment_operator();
				setState(139);
				value();
				}
				break;
			case 6:
				_localctx = new Assigment_var_valueListContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(141);
				var();
				setState(142);
				assignment_operator();
				setState(143);
				values_list();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assignment_operatorContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(PotatoesParser.EQUAL, 0); }
		public TerminalNode ADD_EQUAL() { return getToken(PotatoesParser.ADD_EQUAL, 0); }
		public TerminalNode SUB_EQUAL() { return getToken(PotatoesParser.SUB_EQUAL, 0); }
		public TerminalNode MULT_EQUAL() { return getToken(PotatoesParser.MULT_EQUAL, 0); }
		public TerminalNode DIV_EQUAL() { return getToken(PotatoesParser.DIV_EQUAL, 0); }
		public TerminalNode MOD_EQUAL() { return getToken(PotatoesParser.MOD_EQUAL, 0); }
		public Assignment_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterAssignment_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitAssignment_operator(this);
		}
	}

	public final Assignment_operatorContext assignment_operator() throws RecognitionException {
		Assignment_operatorContext _localctx = new Assignment_operatorContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_assignment_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << ADD_EQUAL) | (1L << SUB_EQUAL) | (1L << MULT_EQUAL) | (1L << DIV_EQUAL) | (1L << MOD_EQUAL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode FUN() { return getToken(PotatoesParser.FUN, 0); }
		public TerminalNode ID() { return getToken(PotatoesParser.ID, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public TerminalNode COLON() { return getToken(PotatoesParser.COLON, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public Function_returnContext function_return() {
			return getRuleContext(Function_returnContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(PotatoesParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PotatoesParser.COMMA, i);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(FUN);
			setState(150);
			match(ID);
			setState(151);
			match(PARENTHESIS_BEGIN);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY))) != 0)) {
				{
				{
				setState(152);
				type();
				setState(153);
				var();
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(154);
					match(COMMA);
					setState(155);
					type();
					setState(156);
					var();
					}
					}
					setState(162);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(168);
			match(PARENTHESIS_END);
			setState(169);
			match(COLON);
			setState(170);
			type();
			setState(171);
			match(SCOPE_BEGIN);
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << WHEN) | (1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
				{
				{
				setState(172);
				statement();
				}
				}
				setState(177);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(179);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURN) {
				{
				setState(178);
				function_return();
				}
			}

			setState(181);
			match(SCOPE_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_returnContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(PotatoesParser.RETURN, 0); }
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public Function_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_return; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterFunction_return(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitFunction_return(this);
		}
	}

	public final Function_returnContext function_return() throws RecognitionException {
		Function_returnContext _localctx = new Function_returnContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_function_return);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(RETURN);
			setState(184);
			var();
			setState(185);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_callContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PotatoesParser.ID, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PotatoesParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PotatoesParser.COMMA, i);
		}
		public Function_callContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterFunction_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitFunction_call(this);
		}
	}

	public final Function_callContext function_call() throws RecognitionException {
		Function_callContext _localctx = new Function_callContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_function_call);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			match(ID);
			setState(188);
			match(PARENTHESIS_BEGIN);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY))) != 0)) {
				{
				{
				setState(189);
				type();
				setState(190);
				var();
				setState(197);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(191);
					match(COMMA);
					setState(192);
					type();
					setState(193);
					var();
					}
					}
					setState(199);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(205);
			match(PARENTHESIS_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Control_flow_statementContext extends ParserRuleContext {
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public For_loopContext for_loop() {
			return getRuleContext(For_loopContext.class,0);
		}
		public While_loopContext while_loop() {
			return getRuleContext(While_loopContext.class,0);
		}
		public WhenContext when() {
			return getRuleContext(WhenContext.class,0);
		}
		public Control_flow_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_control_flow_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterControl_flow_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitControl_flow_statement(this);
		}
	}

	public final Control_flow_statementContext control_flow_statement() throws RecognitionException {
		Control_flow_statementContext _localctx = new Control_flow_statementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_control_flow_statement);
		try {
			setState(211);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(207);
				condition();
				}
				break;
			case FOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(208);
				for_loop();
				}
				break;
			case WHILE:
				enterOuterAlt(_localctx, 3);
				{
				setState(209);
				while_loop();
				}
				break;
			case WHEN:
				enterOuterAlt(_localctx, 4);
				{
				setState(210);
				when();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_loopContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(PotatoesParser.FOR, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public List<TerminalNode> EOL() { return getTokens(PotatoesParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(PotatoesParser.EOL, i);
		}
		public Logical_operationContext logical_operation() {
			return getRuleContext(Logical_operationContext.class,0);
		}
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public For_loopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_loop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterFor_loop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitFor_loop(this);
		}
	}

	public final For_loopContext for_loop() throws RecognitionException {
		For_loopContext _localctx = new For_loopContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_for_loop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(FOR);
			setState(214);
			match(PARENTHESIS_BEGIN);
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
				{
				setState(215);
				assignment();
				}
			}

			setState(218);
			match(EOL);
			setState(219);
			logical_operation();
			setState(220);
			match(EOL);
			setState(221);
			operation(0);
			setState(222);
			match(PARENTHESIS_END);
			setState(223);
			match(SCOPE_BEGIN);
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << WHEN) | (1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
				{
				{
				setState(224);
				statement();
				}
				}
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(230);
			match(SCOPE_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class While_loopContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(PotatoesParser.WHILE, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public Logical_operationContext logical_operation() {
			return getRuleContext(Logical_operationContext.class,0);
		}
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public While_loopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_loop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterWhile_loop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitWhile_loop(this);
		}
	}

	public final While_loopContext while_loop() throws RecognitionException {
		While_loopContext _localctx = new While_loopContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_while_loop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(WHILE);
			setState(233);
			match(PARENTHESIS_BEGIN);
			setState(234);
			logical_operation();
			setState(235);
			match(PARENTHESIS_END);
			setState(245);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SCOPE_BEGIN:
				{
				setState(236);
				match(SCOPE_BEGIN);
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << WHEN) | (1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
					{
					{
					setState(237);
					statement();
					}
					}
					setState(242);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(243);
				match(SCOPE_END);
				}
				break;
			case EOL:
				{
				setState(244);
				match(EOL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhenContext extends ParserRuleContext {
		public TerminalNode WHEN() { return getToken(PotatoesParser.WHEN, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public List<When_caseContext> when_case() {
			return getRuleContexts(When_caseContext.class);
		}
		public When_caseContext when_case(int i) {
			return getRuleContext(When_caseContext.class,i);
		}
		public WhenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_when; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterWhen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitWhen(this);
		}
	}

	public final WhenContext when() throws RecognitionException {
		WhenContext _localctx = new WhenContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_when);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(WHEN);
			setState(248);
			match(PARENTHESIS_BEGIN);
			{
			setState(249);
			var();
			}
			setState(250);
			match(PARENTHESIS_END);
			setState(251);
			match(SCOPE_BEGIN);
			setState(255);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NUMBER) | (1L << BOOLEAN) | (1L << STRING))) != 0)) {
				{
				{
				setState(252);
				when_case();
				}
				}
				setState(257);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(258);
			match(SCOPE_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class When_caseContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(PotatoesParser.ARROW, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode EOL() { return getToken(PotatoesParser.EOL, 0); }
		public When_caseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_when_case; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterWhen_case(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitWhen_case(this);
		}
	}

	public final When_caseContext when_case() throws RecognitionException {
		When_caseContext _localctx = new When_caseContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_when_case);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			value();
			setState(261);
			match(ARROW);
			setState(262);
			statement();
			setState(263);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(PotatoesParser.IF, 0); }
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public Logical_operationContext logical_operation() {
			return getRuleContext(Logical_operationContext.class,0);
		}
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public TerminalNode SCOPE_BEGIN() { return getToken(PotatoesParser.SCOPE_BEGIN, 0); }
		public TerminalNode SCOPE_END() { return getToken(PotatoesParser.SCOPE_END, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(IF);
			setState(266);
			match(PARENTHESIS_BEGIN);
			setState(267);
			logical_operation();
			setState(268);
			match(PARENTHESIS_END);
			setState(269);
			match(SCOPE_BEGIN);
			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << WHEN) | (1L << NUMBER_TYPE) | (1L << BOOLEAN_TYPE) | (1L << STRING_TYPE) | (1L << VOID_TYPE) | (1L << ARRAY) | (1L << ID))) != 0)) {
				{
				{
				setState(270);
				statement();
				}
				}
				setState(275);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(276);
			match(SCOPE_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Logical_operationContext extends ParserRuleContext {
		public List<Logical_operandContext> logical_operand() {
			return getRuleContexts(Logical_operandContext.class);
		}
		public Logical_operandContext logical_operand(int i) {
			return getRuleContext(Logical_operandContext.class,i);
		}
		public Logical_operatorContext logical_operator() {
			return getRuleContext(Logical_operatorContext.class,0);
		}
		public Logical_operationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_operation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterLogical_operation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitLogical_operation(this);
		}
	}

	public final Logical_operationContext logical_operation() throws RecognitionException {
		Logical_operationContext _localctx = new Logical_operationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_logical_operation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			logical_operand();
			setState(279);
			logical_operator();
			setState(280);
			logical_operand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Logical_operandContext extends ParserRuleContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode NOT() { return getToken(PotatoesParser.NOT, 0); }
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Logical_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterLogical_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitLogical_operand(this);
		}
	}

	public final Logical_operandContext logical_operand() throws RecognitionException {
		Logical_operandContext _localctx = new Logical_operandContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_logical_operand);
		int _la;
		try {
			setState(294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(283);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(282);
					match(NOT);
					}
				}

				setState(285);
				comparison();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(286);
					match(NOT);
					}
				}

				setState(289);
				var();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(290);
					match(NOT);
					}
				}

				setState(293);
				value();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Logical_operatorContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(PotatoesParser.AND, 0); }
		public TerminalNode OR() { return getToken(PotatoesParser.OR, 0); }
		public Logical_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterLogical_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitLogical_operator(this);
		}
	}

	public final Logical_operatorContext logical_operator() throws RecognitionException {
		Logical_operatorContext _localctx = new Logical_operatorContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_logical_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==OR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonContext extends ParserRuleContext {
		public List<OperationContext> operation() {
			return getRuleContexts(OperationContext.class);
		}
		public OperationContext operation(int i) {
			return getRuleContext(OperationContext.class,i);
		}
		public Compare_operatorContext compare_operator() {
			return getRuleContext(Compare_operatorContext.class,0);
		}
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitComparison(this);
		}
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_comparison);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			operation(0);
			setState(299);
			compare_operator();
			setState(300);
			operation(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Compare_operatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(PotatoesParser.EQUALS, 0); }
		public TerminalNode NOT_EQUAL() { return getToken(PotatoesParser.NOT_EQUAL, 0); }
		public TerminalNode LESS_THAN() { return getToken(PotatoesParser.LESS_THAN, 0); }
		public TerminalNode LESS_OR_EQUAL() { return getToken(PotatoesParser.LESS_OR_EQUAL, 0); }
		public TerminalNode GREATER_THAN() { return getToken(PotatoesParser.GREATER_THAN, 0); }
		public TerminalNode GREATER_OR_EQUAL() { return getToken(PotatoesParser.GREATER_OR_EQUAL, 0); }
		public Compare_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compare_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterCompare_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitCompare_operator(this);
		}
	}

	public final Compare_operatorContext compare_operator() throws RecognitionException {
		Compare_operatorContext _localctx = new Compare_operatorContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_compare_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALS) | (1L << NOT_EQUAL) | (1L << LESS_THAN) | (1L << LESS_OR_EQUAL) | (1L << GREATER_THAN) | (1L << GREATER_OR_EQUAL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperationContext extends ParserRuleContext {
		public OperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operation; }
	 
		public OperationContext() { }
		public void copyFrom(OperationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Operation_mult_divContext extends OperationContext {
		public Token op;
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode MULTIPLY() { return getToken(PotatoesParser.MULTIPLY, 0); }
		public TerminalNode DIVIDE() { return getToken(PotatoesParser.DIVIDE, 0); }
		public Operation_mult_divContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_mult_div(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_mult_div(this);
		}
	}
	public static class Operation_NUMBERContext extends OperationContext {
		public TerminalNode NUMBER() { return getToken(PotatoesParser.NUMBER, 0); }
		public Operation_NUMBERContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_NUMBER(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_NUMBER(this);
		}
	}
	public static class Operation_exprContext extends OperationContext {
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public Operation_exprContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_expr(this);
		}
	}
	public static class Operation_decrementContext extends OperationContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode DECREMENT() { return getToken(PotatoesParser.DECREMENT, 0); }
		public Operation_decrementContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_decrement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_decrement(this);
		}
	}
	public static class Operation_add_subContext extends OperationContext {
		public Token op;
		public List<OperationContext> operation() {
			return getRuleContexts(OperationContext.class);
		}
		public OperationContext operation(int i) {
			return getRuleContext(OperationContext.class,i);
		}
		public TerminalNode ADD() { return getToken(PotatoesParser.ADD, 0); }
		public TerminalNode SUBTRACT() { return getToken(PotatoesParser.SUBTRACT, 0); }
		public Operation_add_subContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_add_sub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_add_sub(this);
		}
	}
	public static class Operation_incrementContext extends OperationContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode INCREMENT() { return getToken(PotatoesParser.INCREMENT, 0); }
		public Operation_incrementContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_increment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_increment(this);
		}
	}
	public static class Operation_modulusContext extends OperationContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode MODULUS() { return getToken(PotatoesParser.MODULUS, 0); }
		public TerminalNode INT() { return getToken(PotatoesParser.INT, 0); }
		public Operation_modulusContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_modulus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_modulus(this);
		}
	}
	public static class Operation_parenthesisContext extends OperationContext {
		public TerminalNode PARENTHESIS_BEGIN() { return getToken(PotatoesParser.PARENTHESIS_BEGIN, 0); }
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode PARENTHESIS_END() { return getToken(PotatoesParser.PARENTHESIS_END, 0); }
		public Operation_parenthesisContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_parenthesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_parenthesis(this);
		}
	}
	public static class Operation_powerContext extends OperationContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode POWER() { return getToken(PotatoesParser.POWER, 0); }
		public Operation_powerContext(OperationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterOperation_power(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitOperation_power(this);
		}
	}

	public final OperationContext operation() throws RecognitionException {
		return operation(0);
	}

	private OperationContext operation(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		OperationContext _localctx = new OperationContext(_ctx, _parentState);
		OperationContext _prevctx = _localctx;
		int _startState = 48;
		enterRecursionRule(_localctx, 48, RULE_operation, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PARENTHESIS_BEGIN:
				{
				_localctx = new Operation_parenthesisContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(305);
				match(PARENTHESIS_BEGIN);
				setState(306);
				operation(0);
				setState(307);
				match(PARENTHESIS_END);
				}
				break;
			case ID:
				{
				_localctx = new Operation_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(309);
				var();
				}
				break;
			case NUMBER:
				{
				_localctx = new Operation_NUMBERContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(310);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(329);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(327);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						_localctx = new Operation_add_subContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(313);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(314);
						((Operation_add_subContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUBTRACT) ) {
							((Operation_add_subContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(315);
						operation(8);
						}
						break;
					case 2:
						{
						_localctx = new Operation_mult_divContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(316);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(317);
						((Operation_mult_divContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MULTIPLY || _la==DIVIDE) ) {
							((Operation_mult_divContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 3:
						{
						_localctx = new Operation_powerContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(318);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(319);
						match(POWER);
						}
						break;
					case 4:
						{
						_localctx = new Operation_modulusContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(320);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(321);
						match(MODULUS);
						setState(322);
						match(INT);
						}
						break;
					case 5:
						{
						_localctx = new Operation_incrementContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(323);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(324);
						match(INCREMENT);
						}
						break;
					case 6:
						{
						_localctx = new Operation_decrementContext(new OperationContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_operation);
						setState(325);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(326);
						match(DECREMENT);
						}
						break;
					}
					} 
				}
				setState(331);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Array_declarationContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(PotatoesParser.ARRAY, 0); }
		public Diamond_beginContext diamond_begin() {
			return getRuleContext(Diamond_beginContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Diamond_endContext diamond_end() {
			return getRuleContext(Diamond_endContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public Array_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterArray_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitArray_declaration(this);
		}
	}

	public final Array_declarationContext array_declaration() throws RecognitionException {
		Array_declarationContext _localctx = new Array_declarationContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_array_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(ARRAY);
			setState(333);
			diamond_begin();
			setState(334);
			type();
			setState(335);
			diamond_end();
			setState(336);
			var();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Diamond_beginContext extends ParserRuleContext {
		public TerminalNode LESS_THAN() { return getToken(PotatoesParser.LESS_THAN, 0); }
		public Diamond_beginContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_diamond_begin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterDiamond_begin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitDiamond_begin(this);
		}
	}

	public final Diamond_beginContext diamond_begin() throws RecognitionException {
		Diamond_beginContext _localctx = new Diamond_beginContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_diamond_begin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(LESS_THAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Diamond_endContext extends ParserRuleContext {
		public TerminalNode GREATER_THAN() { return getToken(PotatoesParser.GREATER_THAN, 0); }
		public Diamond_endContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_diamond_end; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterDiamond_end(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitDiamond_end(this);
		}
	}

	public final Diamond_endContext diamond_end() throws RecognitionException {
		Diamond_endContext _localctx = new Diamond_endContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_diamond_end);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(GREATER_THAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PotatoesParser.ID, 0); }
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitVar(this);
		}
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_var);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Var_declarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public TerminalNode ID() { return getToken(PotatoesParser.ID, 0); }
		public Var_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterVar_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitVar_declaration(this);
		}
	}

	public final Var_declarationContext var_declaration() throws RecognitionException {
		Var_declarationContext _localctx = new Var_declarationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_var_declaration);
		try {
			setState(349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER_TYPE:
			case BOOLEAN_TYPE:
			case STRING_TYPE:
			case VOID_TYPE:
			case ARRAY:
				enterOuterAlt(_localctx, 1);
				{
				setState(344);
				type();
				setState(345);
				var();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(347);
				match(ID);
				setState(348);
				var();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode NUMBER_TYPE() { return getToken(PotatoesParser.NUMBER_TYPE, 0); }
		public TerminalNode BOOLEAN_TYPE() { return getToken(PotatoesParser.BOOLEAN_TYPE, 0); }
		public TerminalNode STRING_TYPE() { return getToken(PotatoesParser.STRING_TYPE, 0); }
		public TerminalNode VOID_TYPE() { return getToken(PotatoesParser.VOID_TYPE, 0); }
		public Array_declarationContext array_declaration() {
			return getRuleContext(Array_declarationContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_type);
		try {
			setState(356);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(351);
				match(NUMBER_TYPE);
				}
				break;
			case BOOLEAN_TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(352);
				match(BOOLEAN_TYPE);
				}
				break;
			case STRING_TYPE:
				enterOuterAlt(_localctx, 3);
				{
				setState(353);
				match(STRING_TYPE);
				}
				break;
			case VOID_TYPE:
				enterOuterAlt(_localctx, 4);
				{
				setState(354);
				match(VOID_TYPE);
				}
				break;
			case ARRAY:
				enterOuterAlt(_localctx, 5);
				{
				setState(355);
				array_declaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(PotatoesParser.NUMBER, 0); }
		public TerminalNode BOOLEAN() { return getToken(PotatoesParser.BOOLEAN, 0); }
		public TerminalNode STRING() { return getToken(PotatoesParser.STRING, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NUMBER) | (1L << BOOLEAN) | (1L << STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Values_listContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PotatoesParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PotatoesParser.COMMA, i);
		}
		public Values_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_values_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).enterValues_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PotatoesParserListener ) ((PotatoesParserListener)listener).exitValues_list(this);
		}
	}

	public final Values_listContext values_list() throws RecognitionException {
		Values_listContext _localctx = new Values_listContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_values_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			value();
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(361);
				match(COMMA);
				setState(362);
				value();
				}
				}
				setState(367);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 24:
			return operation_sempred((OperationContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean operation_sempred(OperationContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 8);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\38\u0173\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\7\2F\n\2\f\2\16\2I\13\2\3\2\3\2\3\3\5\3N\n\3\3\3\5\3Q"+
		"\n\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\7\5Z\n\5\f\5\16\5]\13\5\3\6\3\6\3\6\3"+
		"\7\3\7\3\7\7\7e\n\7\f\7\16\7h\13\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\5\bv\n\b\3\t\3\t\5\tz\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u0094"+
		"\n\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\7\f\u00a1\n\f\f\f\16"+
		"\f\u00a4\13\f\7\f\u00a6\n\f\f\f\16\f\u00a9\13\f\3\f\3\f\3\f\3\f\3\f\7"+
		"\f\u00b0\n\f\f\f\16\f\u00b3\13\f\3\f\5\f\u00b6\n\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u00c6\n\16\f\16\16\16"+
		"\u00c9\13\16\7\16\u00cb\n\16\f\16\16\16\u00ce\13\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\5\17\u00d6\n\17\3\20\3\20\3\20\5\20\u00db\n\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\7\20\u00e4\n\20\f\20\16\20\u00e7\13\20\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u00f1\n\21\f\21\16\21\u00f4\13"+
		"\21\3\21\3\21\5\21\u00f8\n\21\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u0100"+
		"\n\22\f\22\16\22\u0103\13\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\7\24\u0112\n\24\f\24\16\24\u0115\13\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\25\3\26\5\26\u011e\n\26\3\26\3\26\5\26\u0122\n\26"+
		"\3\26\3\26\5\26\u0126\n\26\3\26\5\26\u0129\n\26\3\27\3\27\3\30\3\30\3"+
		"\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u013a\n\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\7\32\u014a\n\32\f\32\16\32\u014d\13\32\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3\37\3\37\5\37\u0160\n\37"+
		"\3 \3 \3 \3 \3 \5 \u0167\n \3!\3!\3\"\3\"\3\"\7\"\u016e\n\"\f\"\16\"\u0171"+
		"\13\"\3\"\3[\3\62#\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@B\2\b\3\2\n\17\3\2 !\3\2\"\'\3\2*+\3\2()\3\2\62\64\2\u0182"+
		"\2G\3\2\2\2\4M\3\2\2\2\6T\3\2\2\2\b[\3\2\2\2\n^\3\2\2\2\fa\3\2\2\2\16"+
		"u\3\2\2\2\20y\3\2\2\2\22\u0093\3\2\2\2\24\u0095\3\2\2\2\26\u0097\3\2\2"+
		"\2\30\u00b9\3\2\2\2\32\u00bd\3\2\2\2\34\u00d5\3\2\2\2\36\u00d7\3\2\2\2"+
		" \u00ea\3\2\2\2\"\u00f9\3\2\2\2$\u0106\3\2\2\2&\u010b\3\2\2\2(\u0118\3"+
		"\2\2\2*\u0128\3\2\2\2,\u012a\3\2\2\2.\u012c\3\2\2\2\60\u0130\3\2\2\2\62"+
		"\u0139\3\2\2\2\64\u014e\3\2\2\2\66\u0154\3\2\2\28\u0156\3\2\2\2:\u0158"+
		"\3\2\2\2<\u015f\3\2\2\2>\u0166\3\2\2\2@\u0168\3\2\2\2B\u016a\3\2\2\2D"+
		"F\5\4\3\2ED\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HJ\3\2\2\2IG\3\2\2\2"+
		"JK\7\2\2\3K\3\3\2\2\2LN\5\6\4\2ML\3\2\2\2MN\3\2\2\2NP\3\2\2\2OQ\5\n\6"+
		"\2PO\3\2\2\2PQ\3\2\2\2QR\3\2\2\2RS\5\f\7\2S\5\3\2\2\2TU\7\3\2\2UV\5\b"+
		"\5\2VW\7\4\2\2W\7\3\2\2\2XZ\13\2\2\2YX\3\2\2\2Z]\3\2\2\2[\\\3\2\2\2[Y"+
		"\3\2\2\2\\\t\3\2\2\2][\3\2\2\2^_\7\6\2\2_`\7\61\2\2`\13\3\2\2\2af\7\b"+
		"\2\2be\5\20\t\2ce\5\26\f\2db\3\2\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3"+
		"\2\2\2gi\3\2\2\2hf\3\2\2\2ij\7\t\2\2j\r\3\2\2\2kl\5\20\t\2lm\7\5\2\2m"+
		"v\3\2\2\2no\5\22\n\2op\7\5\2\2pv\3\2\2\2qv\5\34\17\2rs\5\32\16\2st\7\5"+
		"\2\2tv\3\2\2\2uk\3\2\2\2un\3\2\2\2uq\3\2\2\2ur\3\2\2\2v\17\3\2\2\2wz\5"+
		"\64\33\2xz\5<\37\2yw\3\2\2\2yx\3\2\2\2z\21\3\2\2\2{|\5\64\33\2|}\5\24"+
		"\13\2}~\5B\"\2~\u0094\3\2\2\2\177\u0080\5<\37\2\u0080\u0081\5\24\13\2"+
		"\u0081\u0082\5:\36\2\u0082\u0094\3\2\2\2\u0083\u0084\5<\37\2\u0084\u0085"+
		"\5\24\13\2\u0085\u0086\5@!\2\u0086\u0094\3\2\2\2\u0087\u0088\5:\36\2\u0088"+
		"\u0089\5\24\13\2\u0089\u008a\5:\36\2\u008a\u0094\3\2\2\2\u008b\u008c\5"+
		":\36\2\u008c\u008d\5\24\13\2\u008d\u008e\5@!\2\u008e\u0094\3\2\2\2\u008f"+
		"\u0090\5:\36\2\u0090\u0091\5\24\13\2\u0091\u0092\5B\"\2\u0092\u0094\3"+
		"\2\2\2\u0093{\3\2\2\2\u0093\177\3\2\2\2\u0093\u0083\3\2\2\2\u0093\u0087"+
		"\3\2\2\2\u0093\u008b\3\2\2\2\u0093\u008f\3\2\2\2\u0094\23\3\2\2\2\u0095"+
		"\u0096\t\2\2\2\u0096\25\3\2\2\2\u0097\u0098\7\20\2\2\u0098\u0099\7\61"+
		"\2\2\u0099\u00a7\7\21\2\2\u009a\u009b\5> \2\u009b\u00a2\5:\36\2\u009c"+
		"\u009d\7\23\2\2\u009d\u009e\5> \2\u009e\u009f\5:\36\2\u009f\u00a1\3\2"+
		"\2\2\u00a0\u009c\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2"+
		"\u00a3\3\2\2\2\u00a3\u00a6\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5\u009a\3\2"+
		"\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"+
		"\u00aa\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ab\7\22\2\2\u00ab\u00ac\7"+
		"\24\2\2\u00ac\u00ad\5> \2\u00ad\u00b1\7\b\2\2\u00ae\u00b0\5\16\b\2\u00af"+
		"\u00ae\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00b6\5\30\r\2\u00b5"+
		"\u00b4\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\7\t"+
		"\2\2\u00b8\27\3\2\2\2\u00b9\u00ba\7\25\2\2\u00ba\u00bb\5:\36\2\u00bb\u00bc"+
		"\7\5\2\2\u00bc\31\3\2\2\2\u00bd\u00be\7\61\2\2\u00be\u00cc\7\21\2\2\u00bf"+
		"\u00c0\5> \2\u00c0\u00c7\5:\36\2\u00c1\u00c2\7\23\2\2\u00c2\u00c3\5> "+
		"\2\u00c3\u00c4\5:\36\2\u00c4\u00c6\3\2\2\2\u00c5\u00c1\3\2\2\2\u00c6\u00c9"+
		"\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9"+
		"\u00c7\3\2\2\2\u00ca\u00bf\3\2\2\2\u00cb\u00ce\3\2\2\2\u00cc\u00ca\3\2"+
		"\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00cf\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf"+
		"\u00d0\7\22\2\2\u00d0\33\3\2\2\2\u00d1\u00d6\5&\24\2\u00d2\u00d6\5\36"+
		"\20\2\u00d3\u00d6\5 \21\2\u00d4\u00d6\5\"\22\2\u00d5\u00d1\3\2\2\2\u00d5"+
		"\u00d2\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d4\3\2\2\2\u00d6\35\3\2\2"+
		"\2\u00d7\u00d8\7\27\2\2\u00d8\u00da\7\21\2\2\u00d9\u00db\5\22\n\2\u00da"+
		"\u00d9\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\7\5"+
		"\2\2\u00dd\u00de\5(\25\2\u00de\u00df\7\5\2\2\u00df\u00e0\5\62\32\2\u00e0"+
		"\u00e1\7\22\2\2\u00e1\u00e5\7\b\2\2\u00e2\u00e4\5\16\b\2\u00e3\u00e2\3"+
		"\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\u00e8\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7\t\2\2\u00e9\37\3\2\2"+
		"\2\u00ea\u00eb\7\30\2\2\u00eb\u00ec\7\21\2\2\u00ec\u00ed\5(\25\2\u00ed"+
		"\u00f7\7\22\2\2\u00ee\u00f2\7\b\2\2\u00ef\u00f1\5\16\b\2\u00f0\u00ef\3"+
		"\2\2\2\u00f1\u00f4\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3"+
		"\u00f5\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f5\u00f8\7\t\2\2\u00f6\u00f8\7\5"+
		"\2\2\u00f7\u00ee\3\2\2\2\u00f7\u00f6\3\2\2\2\u00f8!\3\2\2\2\u00f9\u00fa"+
		"\7\31\2\2\u00fa\u00fb\7\21\2\2\u00fb\u00fc\5:\36\2\u00fc\u00fd\7\22\2"+
		"\2\u00fd\u0101\7\b\2\2\u00fe\u0100\5$\23\2\u00ff\u00fe\3\2\2\2\u0100\u0103"+
		"\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0104\3\2\2\2\u0103"+
		"\u0101\3\2\2\2\u0104\u0105\7\t\2\2\u0105#\3\2\2\2\u0106\u0107\5@!\2\u0107"+
		"\u0108\7\32\2\2\u0108\u0109\5\16\b\2\u0109\u010a\7\5\2\2\u010a%\3\2\2"+
		"\2\u010b\u010c\7\26\2\2\u010c\u010d\7\21\2\2\u010d\u010e\5(\25\2\u010e"+
		"\u010f\7\22\2\2\u010f\u0113\7\b\2\2\u0110\u0112\5\16\b\2\u0111\u0110\3"+
		"\2\2\2\u0112\u0115\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114"+
		"\u0116\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u0117\7\t\2\2\u0117\'\3\2\2\2"+
		"\u0118\u0119\5*\26\2\u0119\u011a\5,\27\2\u011a\u011b\5*\26\2\u011b)\3"+
		"\2\2\2\u011c\u011e\7\37\2\2\u011d\u011c\3\2\2\2\u011d\u011e\3\2\2\2\u011e"+
		"\u011f\3\2\2\2\u011f\u0129\5.\30\2\u0120\u0122\7\37\2\2\u0121\u0120\3"+
		"\2\2\2\u0121\u0122\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u0129\5:\36\2\u0124"+
		"\u0126\7\37\2\2\u0125\u0124\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0127\3"+
		"\2\2\2\u0127\u0129\5@!\2\u0128\u011d\3\2\2\2\u0128\u0121\3\2\2\2\u0128"+
		"\u0125\3\2\2\2\u0129+\3\2\2\2\u012a\u012b\t\3\2\2\u012b-\3\2\2\2\u012c"+
		"\u012d\5\62\32\2\u012d\u012e\5\60\31\2\u012e\u012f\5\62\32\2\u012f/\3"+
		"\2\2\2\u0130\u0131\t\4\2\2\u0131\61\3\2\2\2\u0132\u0133\b\32\1\2\u0133"+
		"\u0134\7\21\2\2\u0134\u0135\5\62\32\2\u0135\u0136\7\22\2\2\u0136\u013a"+
		"\3\2\2\2\u0137\u013a\5:\36\2\u0138\u013a\7\62\2\2\u0139\u0132\3\2\2\2"+
		"\u0139\u0137\3\2\2\2\u0139\u0138\3\2\2\2\u013a\u014b\3\2\2\2\u013b\u013c"+
		"\f\t\2\2\u013c\u013d\t\5\2\2\u013d\u014a\5\62\32\n\u013e\u013f\f\n\2\2"+
		"\u013f\u014a\t\6\2\2\u0140\u0141\f\b\2\2\u0141\u014a\7,\2\2\u0142\u0143"+
		"\f\7\2\2\u0143\u0144\7-\2\2\u0144\u014a\78\2\2\u0145\u0146\f\6\2\2\u0146"+
		"\u014a\7.\2\2\u0147\u0148\f\5\2\2\u0148\u014a\7/\2\2\u0149\u013b\3\2\2"+
		"\2\u0149\u013e\3\2\2\2\u0149\u0140\3\2\2\2\u0149\u0142\3\2\2\2\u0149\u0145"+
		"\3\2\2\2\u0149\u0147\3\2\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3\2\2\2\u014b"+
		"\u014c\3\2\2\2\u014c\63\3\2\2\2\u014d\u014b\3\2\2\2\u014e\u014f\7\60\2"+
		"\2\u014f\u0150\5\66\34\2\u0150\u0151\5> \2\u0151\u0152\58\35\2\u0152\u0153"+
		"\5:\36\2\u0153\65\3\2\2\2\u0154\u0155\7$\2\2\u0155\67\3\2\2\2\u0156\u0157"+
		"\7&\2\2\u01579\3\2\2\2\u0158\u0159\7\61\2\2\u0159;\3\2\2\2\u015a\u015b"+
		"\5> \2\u015b\u015c\5:\36\2\u015c\u0160\3\2\2\2\u015d\u015e\7\61\2\2\u015e"+
		"\u0160\5:\36\2\u015f\u015a\3\2\2\2\u015f\u015d\3\2\2\2\u0160=\3\2\2\2"+
		"\u0161\u0167\7\33\2\2\u0162\u0167\7\34\2\2\u0163\u0167\7\35\2\2\u0164"+
		"\u0167\7\36\2\2\u0165\u0167\5\64\33\2\u0166\u0161\3\2\2\2\u0166\u0162"+
		"\3\2\2\2\u0166\u0163\3\2\2\2\u0166\u0164\3\2\2\2\u0166\u0165\3\2\2\2\u0167"+
		"?\3\2\2\2\u0168\u0169\t\7\2\2\u0169A\3\2\2\2\u016a\u016f\5@!\2\u016b\u016c"+
		"\7\23\2\2\u016c\u016e\5@!\2\u016d\u016b\3\2\2\2\u016e\u0171\3\2\2\2\u016f"+
		"\u016d\3\2\2\2\u016f\u0170\3\2\2\2\u0170C\3\2\2\2\u0171\u016f\3\2\2\2"+
		"\"GMP[dfuy\u0093\u00a2\u00a7\u00b1\u00b5\u00c7\u00cc\u00d5\u00da\u00e5"+
		"\u00f2\u00f7\u0101\u0113\u011d\u0121\u0125\u0128\u0139\u0149\u014b\u015f"+
		"\u0166\u016f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}