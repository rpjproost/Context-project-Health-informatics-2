package context.healthinformatics.graphs;

import java.util.ArrayList;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.sequentialdataanalysis.Chunk;

public class LineChart {
	
	public ArrayList<Integer> getChunkData() {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (Chunk c : chunks) {
			res.add(c.getAmountOfChilds());
		}
		return res;
	}

}
