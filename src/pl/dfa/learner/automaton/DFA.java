/**
 * 
 */
package pl.dfa.learner.automaton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.javatuples.Pair;

/**
 * Deterministic finite automaton. 
 * @see https://en.wikipedia.org/wiki/Deterministic_finite_automaton
 *
 */
public class DFA {

	/**
	 * Set of states 
	 */
	private Set<Integer> states; 
	
	/**
	 * Set of inputs 
	 */
	private Set<Integer> inputs; 
	
	/**
	 * Transition table 
	 */
	private TransitionTable transitionTable; 
	
	/**
	 * Initial state 
	 */
	private Integer initialState; 
	
	/**
	 * Set of accepted states 
	 */
	private Set<Integer> acceptedStates; 
	
	
	
	/**
	 * Creates a new <code>DFA</code>  object 
	 * 
	 * @param states set of states 
	 * @param inputs set of inputs 
	 * @param transitionTable transition table 
	 * @param initialState iniitial state 
	 * @param acceptedStates set of accepted states 
	 */
	public DFA(Set<Integer> states, Set<Integer> inputs,
			TransitionTable transitionTable, Integer initialState,
			Set<Integer> acceptedStates) {
		super();
		this.states = states;
		this.inputs = inputs;
		this.transitionTable = transitionTable;
		this.initialState = initialState;
		this.acceptedStates = acceptedStates;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(false); 
	}
	
	
	public String toString(boolean verbose) { 
		if(!verbose) { 
			return toStringShort();  
		} else { 
			return toStringVerbose(); 
		}
	} 
	
	
	private String toStringVerbose() {
		StringBuilder builder = new StringBuilder(); 
		builder.append("States ("+this.states.size()+"): "+this.states); 
		builder.append("\n"); 
		builder.append("Inputs: ("+this.inputs.size()+"): "+this.inputs); 
		builder.append("\n"); 
		builder.append("Transition table: \n"); 
		builder.append(this.transitionTable.toString(true)); 
		builder.append("Initial state: "+this.initialState); 
		builder.append("\n"); 
		builder.append("Accepted states: ("+this.acceptedStates.size()+"): "+this.acceptedStates); 
		return builder.toString(); 
	}



	public String toStringShort() { 
		final int maxLen = 10;
		return "DFA [states="
				+ (states != null ? toString(states, maxLen) : null)
				+ ", inputs="
				+ (inputs != null ? toString(inputs, maxLen) : null)
				+ ", transitionTable="
				+ transitionTable
				+ ", initialState="
				+ initialState
				+ ", acceptedStates="
				+ (acceptedStates != null ? toString(acceptedStates, maxLen)
						: null) + "]";
	}
	
	
	
	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}


	/**
	 * @return the initialState
	 */
	public Integer getInitialState() {
		return initialState;
	}


	/**
	 * Gets the acceptance status for a given state 
	 * @param currentState state to check if it's accepted 
	 * @return <code>true</code> if the <code>currentState</code> is accepted, 
	 * 			<code>false</code> otherwise 
	 * 
	 */
	public boolean isAccepted(Integer currentState) {
		return this.acceptedStates.contains(currentState);
	}


	/**
	 * Gets the next state for a given state and an input . 
	 * 
	 * @param currentState current state 
	 * @param input input 
	 * @return next state 
	 */
	public Integer getTransition(Integer currentState, Integer input) { 
		if(!this.states.contains(currentState)) { 
			throw new IllegalStateException("The current state to transition from is not supported by "
					+ "this automaton, currentState"+currentState+", states: "+this.states); 
		} 
		if(!this.inputs.contains(input)) { 
			throw new IllegalStateException("The current input is not supported by this automaton, input: "+input
					+", supported: "+this.inputs); 
		}
		return this.transitionTable.getTransition(currentState, input);
	}



	/**
	 * Gets the set of allowed inputs (safe copy). 
	 * @return set of allowed inputs 
	 */
	public Set<Integer> getInputs() { 
		return new TreeSet<Integer>(this.inputs);
	}


	/**
	 * Gets the set of states 
	 * @return set of states 
	 */
	public Set<Integer> getStates() { 
		return new TreeSet<Integer>(this.states); 
	} 



	
}
