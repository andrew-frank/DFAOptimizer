/**
 * 
 */
package pl.dfa.learner.automaton;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.javatuples.Pair;



/**
 * Transition table for the DFA 
 *
 */
public class TransitionTable { 
	
	/**
	 * Next states for pairs of (state, input) 
	 */
	private Map<Pair<Integer, Integer>, Integer> transitionTable; 
	
	/**
	 * Set of states 
	 */
	private Set<Integer> states; 
	
	/**
	 * Set of allowed inputs 
	 */
	private Set<Integer> inputs; 
	
	
	public TransitionTable(Set<Integer> states, Set<Integer> inputs, Map<Pair<Integer, Integer>, Integer> transitions) { 
		this.transitionTable = transitions; 
		this.states = states; 
		this.inputs = inputs; 
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransitionTable [transitionTable="
				+ (transitionTable != null ? toString(
						transitionTable.entrySet()) : null) + "]";
	}

	private String toString(Collection<?> collection) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext(); i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}


	/**
	 * Gets the next state for a given state and an input 
	 * 
	 * @param currentState current automaton state 
	 * @param input input 
	 * @return next automaton state 
	 */
	public Integer getTransition(Integer currentState, Integer input) {
		Pair<Integer, Integer> transition = new Pair<Integer, Integer>(currentState, input); 
		return this.transitionTable.get(transition);
	}



	public String toString(boolean verbose) {
		if(!verbose) { 
			return this.toString(); 
		} else { 
			return toStringVerbose(); 
		} 
	}



	private String toStringVerbose() {
		StringBuilder builder = new StringBuilder(); 
		String tab = 		"     "; 
		String tabDashes =  "-----"; 
		builder.append(tab); 
		builder.append(tab); 
		for(Integer input: this.inputs) { 
			builder.append(input); 
			builder.append(tab); 
		} 
		builder.append("\n"); 
		builder.append(tabDashes); 
		builder.append(tabDashes); 
		for(int i = 0; i < this.inputs.size(); i++) { 
			builder.append(tabDashes); 
		} 
		builder.append("\n"); 
		for(Integer initialState: this.states) { 
			builder.append("q"); 
			builder.append(initialState); 
			int length = String.valueOf(initialState).length()+1; 
			String tabSubset = tab.substring(0, tabDashes.length() - length); 
			builder.append(tabSubset); 
			builder.append("|"); 
			int lastLength = 1; 
			for(Integer input: this.inputs) { 
				builder.append(tab.substring(0, tab.length() - lastLength)); 
				builder.append("q"); 
				int nextState = this.getTransition(initialState, input); 
				builder.append(nextState); 
				lastLength = String.valueOf(nextState).length(); 
			}
			builder.append("\n"); 
		}
		return builder.toString(); 
 	}

	
}
