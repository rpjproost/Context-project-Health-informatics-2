package context.healthinformatics.analyse;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

import context.healthinformatics.sequentialdataanalysis.*;

public class UserInputParser extends BaseParser<Task> {
	private Task t;
	public Rule task() {
		return Sequence(ZeroOrMore(' '), method(), EOI);
	}
	
	public Rule method() {
		return OneOrMore(filter());
	}
	
	public Rule operation() {
		return OneOrMore(dataOperation(), codeOperation(), commentOperation());
	}
	
	/**
	 * rule for dataOperation.
	 * @return the rule.
	 */
	public Rule dataOperation() {
		return Sequence(strData(), Optional(strWhere()), push(new Constraints() 
		@overide run(){
			setChunks(SingletonInterpreter.getInterpreter().getChunks());
			onData(ANY)};
		));
	}

	private Rule filter() {
		return Sequence(strFilter(), operation());
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