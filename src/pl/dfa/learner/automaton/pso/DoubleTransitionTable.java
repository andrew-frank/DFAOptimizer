/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;



/**
 * Transition table for DFA-s with transitions encoded as double values. 
 * Each transition table stores the next state when a single input is received. 
 * 
 * Such representation is suitable for use with the PSO algorithm. 
 * For each input state, an output state is defined as a double number between 0.5 and 
 * <i>stateNumber</i>+0.5, where <i>stateNumber</i> is the number of states in this automaton. 
 * The next state can be determined by rounding the double encoded value. 
 * 
 */
public class DoubleTransitionTable {

	/**
	 * States set 
	 */
	private Set<Integer> states; 
	
	/**
	 * Table name
	 */
	private String name; 
	
	/**
	 * Transition table 
	 */
	private Map<Integer, Double> nextStates; 
	
	/**
	 * Random number generator
	 */
	private Random random; 
	
	/**
	 * Minimum allowed value for the next state 
	 */
	double min; 
	
	
	/**
	 * Maximum allowed value for the next state
	 */
	double max; 
	
	
	/**
	 * Creates a new transition table with a specified name 
	 * @param states set of states 
	 * @param name table name
	 */
	public DoubleTransitionTable(Set<Integer> states, String name) { 
		this.states = states; 
		this.name = name; 
		this.nextStates = new TreeMap<Integer, Double>(); 
		this.random = SingletonRandom.getRandom(); 
		initNextStates(); 
		initBoundaries(); 
	} 
	
	
	/**
	 * Creates a new transition table with a specified name and boundaries. 
	 * @param states set of states 
	 * @param name table name 
	 * @param min minimum value of the encoded states 
	 * @param max maximum value of the encoded states 
	 */
	public DoubleTransitionTable(Set<Integer> states, String name, double min, double max) { 
		this.states = states; 
		this.name = name; 
		this.nextStates = new TreeMap<Integer, Double>(); 
		this.random = SingletonRandom.getRandom(); 
		this.min = min; 
		this.max = max; 
		initNextStates(); 
	} 
	

	/**
	 * Copy constructor 
	 * 
	 * @param table <code>table</code> object to copy
	 */
	public DoubleTransitionTable(DoubleTransitionTable table) {
		this.states = new TreeSet<Integer>(table.states); 
		this.name = table.name; 
		this.random = table.random; 
		this.min = table.min; 
		this.max = table.max; 
		this.nextStates = new TreeMap<Integer, Double>(table.nextStates); 
		
	}


	/**
	 * Initialises the boundaries fore the encoded states 
	 */
	private void initBoundaries() {
		this.min = Collections.min(states) - 0.4999; 
		this.max = Collections.max(states) + 0.4999; 
	}

	
	/**
	 * Sets the value of the next state 
	 * 
	 * @param state current state 
	 * @param nextState next state, encoded as a <code>double</code> number 
	 */
	public void set(Integer state, Double nextState) { 
		Double next = checkBoundaries(nextState); 
		this.nextStates.put(state, next); 
	}
	
	
	/**
	 * Ensures that the state value is within the set boundaries. 
	 * 
	 * @param nextState encoded state to check
	 * @return Minimum value if the <code>nextState</code> is too low, maximum if 
	 * 			it is too big or the unchanged value if it is within boundaries. 
	 */
	private Double checkBoundaries(Double nextState) {
		if(nextState < this.min) { 
			return this.min; 
		} 
		if(nextState > this.max) { 
			return this.max; 
		} 
		return nextState;
	}

	
	/**
	 * Initialises the next states map 
	 */
	private void initNextStates() {
		for(Integer state: states) { 
			this.nextStates.put(state, Double.valueOf(-1)); 
		} 
		
	}

	
	/**
	 * Randomises all elements of the table in a way that all elements are within boundaries. 
	 * Ensures that values are integers. 
	 */
	public void randomise() { 
		List<Integer> statesList = new ArrayList<Integer>(this.states); 
		for(Integer state: this.states) { 
			int randomIndex = this.random.nextInt(statesList.size()); 
			this.nextStates.put(state, Double.valueOf(statesList.get(randomIndex))); 
		} 
	} 
	
	
	/**
	 * Randomises all elements of the table in a way that all elements are within boundaries. 
	 */
	public void randomiseNonRounded() {
		List<Integer> statesList = new ArrayList<Integer>(this.states); 
		for(Integer state: this.states) { 
			int randomIndex = this.random.nextInt(statesList.size()); 
			double newValue = Double.valueOf(statesList.get(randomIndex)) - 0.5 + random.nextDouble(); 
			newValue = checkBoundaries(newValue); 
			this.nextStates.put(state, newValue); 
		} 
	} 
	
	
	/**
	 * Randomises all elements of the table in a way that all elements are within boundaries
	 * @param maxAbsoluteValue maximum absolute value of the table elements 
	 */
	public void randomiseNonRounded(double maxAbsoluteValue) { 
		List<Integer> statesList = new ArrayList<Integer>(this.states); 
		for(Integer state: this.states) { 
			int randomIndex = this.random.nextInt(statesList.size()); 
			double newValue = random.nextDouble() * maxAbsoluteValue; 
			if(random.nextBoolean()) { 
				newValue = -newValue; 
			}
			newValue = checkBoundaries(newValue); 
			this.nextStates.put(state, newValue); 
		} 
	} 
	
	
	@Override 
	public String toString() { 
		StringBuilder builder = new StringBuilder(); 
		builder.append(this.name); 
		builder.append("\n"); 
		for(Integer state: this.states) { 
			builder.append(state+":\t"+this.nextStates.get(state)); 
			builder.append("\n"); 
		} 
		return builder.toString(); 
	}

	
	/**
	 * Gets the next state for a given state and the input 
	 * defined for this table, encoded as a <code>double</code> number. 
	 * 
	 * @param state input state
	 * @return next state 
	 */
	public double getNext(Integer state) { 
		if(this.nextStates == null) { 
			System.err.println("Error. "); 
		}
		return this.nextStates.get(state);
	}

	
}
