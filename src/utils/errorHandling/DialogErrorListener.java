package utils.errorHandling;

import java.awt.Color;
import java.awt.Container;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class DialogErrorListener extends BaseErrorListener {
	@Override public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol,
			int line, int charPositionInLine,
			String msg,
			RecognitionException e)
	{
		Parser p = ((Parser)recognizer);
		List<String> stack = p.getRuleInvocationStack();
		Collections.reverse(stack);
		StringBuilder buf = new StringBuilder();
		buf.append("rule stack: "+stack+" ");
		buf.append("line "+line+":"+charPositionInLine+" at "+
				offendingSymbol+": "+msg);
		JDialog dialog = new JDialog();
		Container contentPane = dialog.getContentPane();
		contentPane.add(new JLabel(buf.toString()));
		contentPane.setBackground(Color.white);
		dialog.setTitle("Syntax error");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
}

