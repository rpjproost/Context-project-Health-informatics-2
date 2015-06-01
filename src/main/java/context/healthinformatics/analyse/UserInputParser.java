package context.healthinformatics.analyse;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

import context.healthinformatics.sequentialdataanalysis.*;

public class UserInputParser extends BaseParser<Task> {
	private Task t;
	public Rule task() {
		return Sequence(ZeroOrMore(' '), method(), EOI, push(t));
	}
	
	public Rule method() {
		return OneOrMore(filter());
	}
	
	public Rule filterOperation() {
		return OneOrMore(filterDataOperation(), filterCodeOperation(), filterCommentOperation());
	}

	/**
	 * rule for dataOperation when using a filter.
	 * @return the rule.
	 */
	private Rule filterDataOperation() {
		return Sequence(strData(), Optional(strWhere()), ANY, push(new Constraints() {
		@Override public void run() {
			setChunks(SingletonInterpreter.getInterpreter().getChunks());
			try {
				constraintOnData(match(), "Result");
			} catch (SQLException e) {
				Logger.getLogger(Constraints.class.getName()).warning(e.getMessage());
			}
			}; }
		));
	}
	
	/**
	 * rule for codeOperation when using a filter.
	 * @return the rule.
	 */
	private Rule filterCodeOperation() {
		return Sequence(strCode(), Optional(strWhere()), Optional(filterEquals(), filterContains());
		
	private Object filterCommentOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	private Rule filter() {
		return Sequence(strFilter(), filterOperation());
	}
	
	private Rule strFilter() {
		return Sequence(ZeroOrMore(' '), Ch('f'), Ch('i'), Ch('l'), Ch('t'), Ch('e'), Ch('r'));
	}
	
	private Rule strChunk() {
		return Sequence(ZeroOrMore(' '), Ch('c'), Ch('h'), Ch('u'), Ch('n'), Ch('c'), Ch('k'));
	}
	
	private Rule strCode() {
		return Sequence(ZeroOrMore(' '), Ch('c'), Ch('o'), Ch('d'), Ch('e'));
	}
	
	private Rule strComment() {
		return Sequence(ZeroOrMore(' '), Ch('c'), Ch('o'), Ch('m'), Ch('m'),
				Ch('e'), Ch('n'), Ch('t'));
	}
	
	private Rule strConnect() {
		return Sequence(ZeroOrMore(' '), Ch('c'), Ch('o'), Ch('n'), Ch('n'),
				Ch('e'), Ch('c'), Ch('t'));
	}

	private Rule strData() {
		return Sequence(ZeroOrMore(' '), Ch('d'), Ch('a'), Ch('t'), Ch('a'));
	}
	
	private Rule strWhere() {
		return Sequence(ZeroOrMore(' '), Ch('w'), Ch('h'), Ch('e'), Ch('r'), Ch('e'));
	}
}