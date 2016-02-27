/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Set of a words, used to represent learning or testing set. 
 * Divided into accepted and non accepted words 
 *
 */
public class WordSet {

	/**
	 * accepted words 
	 */
	public Set<List<Integer>> accepted; 
	
	/**
	 * Non accepted words 
	 */
	public Set<List<Integer>> nonAccepted; 
	
	private static final Logger logger = LogManager.getLogger(WordSet.class); 
	
	
	/**
	 * Initialises an empty word set 
	 */
	public WordSet() { 
		this.accepted = new LinkedHashSet<List<Integer>>(); 
		this.nonAccepted = new LinkedHashSet<List<Integer>>(); 
	} 
	
	
	/**
	 * Adds a word to the list of accepted words 
	 * 
	 * @param acceptedWord word to add 
	 */
	public void addAccepted(List<Integer> acceptedWord) { 
		if(this.accepted.contains(acceptedWord)) { 
			logger.warn("Duplicate word added "+acceptedWord+", "+
					this.accepted); 
			return; 
		} 
		if(this.accepted.contains(acceptedWord)) { 
			logger.warn("Duplicate, accepted word already added to nonAccepted "
					+acceptedWord+", "+nonAccepted); 
			return; 
		} 
		this.accepted.add(acceptedWord); 
	} 
	
	
	/**
	 * Adds a word to the list of non accepted words 
	 * 
	 * @param nonAcceptedWord word to add 
	 */
	public void addNonAccepted(List<Integer> nonAcceptedWord) { 
		if(this.nonAccepted.contains(nonAcceptedWord)) { 
			logger.warn("Duplicate word added "+nonAcceptedWord+", "+
					this.nonAccepted); 
			return; 
		} 
		if(this.accepted.contains(nonAcceptedWord)) { 
			logger.warn("Duplicate, non accepted word added to Accepted "
					+nonAcceptedWord+", "+nonAccepted); 
			return; 
		} 
		this.nonAccepted.add(nonAcceptedWord); 
	} 
	
	
	/**
	 * Gets the set of accepted words 
	 * 
	 * @return accepted words 
	 */
	public Set<List<Integer>> getAccepted() { 
		return this.accepted; 
	} 
	
	
	/**
	 * Gets the set of non accepted words 
	 * 
	 * @return set of non accepted words 
	 */
	public Set<List<Integer>> getNonAccepted() { 
		return this.nonAccepted; 
	}


	/**
	 * Gets the number of words in this set 
	 * @return number of words 
	 */
	public long size() { 
		return this.accepted.size() + this.nonAccepted.size();
	} 
	
	
	@Override
	public String toString() { 
		StringBuilder builder = new StringBuilder(); 
		builder.append("WordSet ("+this.size()+" words, "+this.accepted.size()+" accepted, "
				+this.nonAccepted.size()+" non accepted). "); 
		return builder.toString(); 
	} 
	
	
	public String toString(boolean content) { 
		StringBuilder builder = new StringBuilder(); 
		builder.append("WordSet ("+this.size()+" words, "
				+this.accepted.size()+" accepted, "
				+this.nonAccepted.size()+" non accepted). "); 
		if(content) { 
			builder.append("\n\nAccepted: \n"); 
			int lineLength = 0; 
			for(List<Integer> word: this.accepted) { 
				String toAdd = word.toString(); 
				builder.append(toAdd); 
				lineLength += toAdd.length(); 
				if(lineLength > 80) { 
					builder.append(", \n"); 
					lineLength = 0; 
					continue; 
				} 
				builder.append(", "); 
				lineLength += 2; 
			} 
			builder.append("\n\nNonAccepted: \n"); 
			lineLength = 0; 
			for(List<Integer> word: this.nonAccepted) { 
				String toAdd = word.toString(); 
				builder.append(toAdd); 
				lineLength += toAdd.length(); 
				if(lineLength > 80) { 
					builder.append(", \n"); 
					lineLength = 0; 
					continue; 
				}
				builder.append(", "); 
				lineLength += 2; 
			} 
		}
		return builder.toString(); 
	} 
	
}
