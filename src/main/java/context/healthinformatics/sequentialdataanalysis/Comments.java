package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Class Comments.
 */
public class Comments extends Task {

	private Logger log = Logger.getLogger(Comments.class.getName());
	private String comment;
	private ArrayList<Chunk> changedChunks;
	private ArrayList<String> oldComments;
	private int indexcheck = 0;

	/**
	 * Constructor for comments without arguments.
	 * @param c Comment to be set.
	 */
	public Comments(String c) {
		comment = c;
		changedChunks = new ArrayList<Chunk>();
		oldComments = new ArrayList<String>();
	}

	@Override
	public ArrayList<Chunk> undo() {
		for (int i = 0; i < changedChunks.size(); i++) {
			changedChunks.get(i).setComment(oldComments.get(i));
		}
		return getChunks();
	}

	/**
	 * Set the comment on line line with the string comment.
	 * 
	 * @param line
	 *            the line of the comment.
	 * @param comment
	 *            the comment to be set.
	 */
	public void setCommentByLine(int line, String comment) {
		Chunk c = getChunkByLine(line, getChunks());
		if (c != null) {
			c.setComment(comment);
		} else {
			log.info("The line: " + line + "could not be found."
					+ "The comment '" + comment + "' was not set.");
		}
	}

	/**
	 * Method which sets the comment of all chunks in a list.
	 * @param chunks list of chunks.
	 * @param comment to be set.
	 */
	public void setCommentOfListOfChunks(ArrayList<Chunk> chunks, String comment) {
		for (Chunk chunk : chunks) {
			chunk.setComment(comment);
		}
	}

	/**
	 * Method which sets the comment for all chunks which have a certain comment.
	 * @param code to be set, if the condition is met.
	 */
	public void setCommentOnCode(String code) {
		for (int i = 0; i < getChunks().size(); i++) {
			Chunk c = getChunks().get(i);
			if (c.getCode().equals(code)) {
				oldComments.add(c.getComment());
				changedChunks.add(c);
				c.setComment(comment);
			}
		}
	}

	/**
	 * Method which replaces the comment for all chunks which have a certain comment.
	 * @param previousComment the chunk has to have.
	 */
	public void setCommentOnComment(String previousComment) {
		for (int i = 0; i < getChunks().size(); i++) {
			Chunk c = getChunks().get(i);
			if (c.getComment().equals(previousComment)) {
				oldComments.add(c.getComment());
				changedChunks.add(c);
				c.setComment(comment);
			}
		}
	}
	
	/**
	 * Method which replaces the comment for all chunks which have a certain comment.
	 * @param previousComment the chunk has to have.
	 */
	public void setCommentOnContainsComment(String previousComment) {
		for (int i = 0; i < getChunks().size(); i++) {
			Chunk c = getChunks().get(i);
			if (c.getComment().contains(previousComment)) {
				oldComments.add(c.getComment());
				changedChunks.add(c);
				c.setComment(comment);
			}
		}
	}

	/**
	 * Method that sets the comment for all chunks which
	 * Fulfill the conditions of the whereClause.
	 * @param whereClause is condition to be met.
	 * @throws SQLException e.
	 */
	public void setCommentOnData(String whereClause) throws SQLException {
		ArrayList<Integer> list = getLinesFromData(whereClause);
		setCommentOnChild(getChunks(), list);
		indexcheck = 0;
	}

	private void setCommentOnChild(ArrayList<Chunk> childs, ArrayList<Integer> list) {
		for (int i = 0; i < childs.size(); i++) {
			Chunk temp = childs.get(i);
			if (indexcheck < list.size() && temp.getLine() == list.get(indexcheck)) {
				oldComments.add(temp.getComment());
				changedChunks.add(temp);
				temp.setComment(comment);
				indexcheck++;
			}
			else if (temp.hasChild()) {
				setCommentOnChild(temp.getChildren(), list);
			}

		}
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		setCommentOnData(whereClause);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		setCommentOnCode(code);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		setCommentOnComment(comment);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		setCommentOnContainsComment(comment);
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		int i = Integer.parseInt(line);
		changedChunks.add(getChunks().get(i - 1));
		oldComments.add(getChunks().get(i - 1).getComment());
		getChunks().get(i - 1).setComment(comment);
		return getChunks();
	}
}
