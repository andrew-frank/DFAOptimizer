/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import pl.dfa.learner.automaton.DFAFactory;


/**
 * Solution of the DFA learning problem suitable for use in PSO metaheuristic 
 * @see https://en.wikipedia.org/wiki/Particle_swarm_optimization 
 * @see https://en.wikipedia.org/wiki/Deterministic_finite_automaton
 *
 */
public class Solution { 
	
	/**
	 * Set of states 
	 */
	private Set<Integer> states; 
	
	/**
	 * Set of inputs 
	 */
	private Set<Integer> inputs; 
	
	/**
	 * Transition tables for each allowed input 
	 */
	private Map<Integer, DoubleTransitionTable> transitions; 
	
	/**
	 * Double representation of the accepted states 
	 */
	private Map<Integer, Double> acceptedStates; 
	
	/**
	 * Minimum boundary for the accepted state 
	 */
	private double acceptedMin; 
	
	/**
	 * Maximum boundary for the accepted state
	 */
	private double acceptedMax; 
	
	/**
	 * Solution evaluation 
	 */
	private double evaluation; 
	
	/**
	 * Random number generator 
	 */
	private Random random;
	
	/**
	 * Evaluation state flag, <code>true</code> 
	 * if this solution has been evaluated since the last change. 
	 * 
	 */
	private boolean evaluated; 
	
	/**
	 * Number of states 
	 */
	private double stateNumber; 
	

	/**
	 * Creates a new solution object with the specified maximum number of states and 
	 * a set of allowed inputs.  
	 * @param maxStates maximum number of states 
	 * @param inputs allowed inputs 
	 */
	public Solution(int maxStates, Set<Integer> inputs) { 
		this.random = SingletonRandom.getRandom(); 
		this.stateNumber = this.random.nextDouble() * maxStates; 
		if(this.stateNumber < 0.5) { 
			this.stateNumber = 0.5; 
		}
		this.states = DFAFactory.getIntegerSet((int)Math.round(this.stateNumber)); 
		this.inputs = inputs; 
		this.transitions = new TreeMap<Integer, DoubleTransitionTable>(); 
		this.acceptedStates = new TreeMap<Integer, Double>(); 
		this.evaluated = false; 
		initTransitions(); 
		initAcceptedStates(); 
	} 
	
	
	/**
	 * Copy constructor 
	 * 
	 * @param solution <code>solution</code> to copy 
	 */
	public Solution(Solution solution) { 
		this.stateNumber = solution.stateNumber; 
		this.states = solution.getStates(); 
		this.inputs = solution.getInputs(); 
		this.transitions = new TreeMap<Integer, DoubleTransitionTable>(); 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = solution.getTransitionTable(input); 
			this.transitions.put(input, table); 
		} 
		this.acceptedStates = new TreeMap<Integer, Double>(solution.acceptedStates); 
		this.evaluation = solution.evaluation; 
		this.random = solution.random; 
		this.evaluated = solution.evaluated; 
		this.acceptedMin = solution.acceptedMin; 
		this.acceptedMax = solution.acceptedMax; 
	}
	

	/**
	 * Gets the transition table for a specified input. 
	 * 
	 * @param input
	 * @return transition table in a form suitable for PSO 
	 */
	private DoubleTransitionTable getTransitionTable(Integer input) {
		DoubleTransitionTable table = this.transitions.get(input); 
		DoubleTransitionTable copy = new DoubleTransitionTable(table); 
		return copy;
	}


	/**
	 * Initialises the transition table 
	 */
	private void initTransitions() { 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = new DoubleTransitionTable(
					states, "Transition table for input "+input); 
			this.transitions.put(input, table); 
		}
	} 
	
	
	/**
	 * Initialises the accepted states map and the boundaries 
	 */
	private void initAcceptedStates() { 
		this.acceptedMin = 0.0; 
		this.acceptedMax = 1.0; 
		for(Integer state: this.states) { 
			this.acceptedStates.put(state, Double.valueOf(-1)); 
		} 
	} 
	
	
	/**
	 * Randomises the entire solution by setting all of 
	 * the elements (transitions, acceptable states) 
	 * to random numbers. Ensures all values are integers. 
	 */
	public void randomise() { 
		this.evaluated = false; 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = this.transitions.get(input); 
			table.randomise(); 
		} 
		for(Integer state: this.states) { 
			this.acceptedStates.put(state, random.nextDouble()); 
		}
	} 
	
	
	/**
	 * Randomises the entire solution by setting all of 
	 * the elements (transitions, acceptable states) 
	 * to random numbers. 
	 */
	public void randomiseNonRounded() { 
		this.evaluated = false; 
		for(Integer input: this.inputs) { 
			DoubleTransitionTable table = this.transitions.get(input); 
			table.randomiseNonRounded(); 
		} 
		for(Integer state: this.states) { 
			this.acceptedStates.put(state, random.nextDouble()); 
		}
	} 
	
	
	/**
	 * Sets the transition from the state <code>state</code> with 
	 * an input <code>input</input> to the state <code<>nextState</code>. 
	 * 
	 * @param state current state
	 * @param input current input 
	 * @param next next state, encoded as a double 
	 */
	public void setTransition(Integer state, Integer input, double next) { 
		this.evaluated = false; 
		DoubleTransitionTable inputTable = this.transitions.get(input); 
		inputTable.set(state, next); 
	} 
	
	
	/**
	 * Sets if the state is accepted, encoded as a <code>dououble</code> 
	 * 
	 * @param state state to set acceptance 
	 * @param accepted acceptance, encoded as a double (0-0.499 for non accepted, 0.5-1 for accepted): 
	 */
	public void setAccepted(Integer state, Double accepted) { 
		this.evaluated = false; 
		accepted = checkBoundaries(accepted); 
		this.acceptedStates.put(state, accepted); 
	} 
	
	
	/**
	 * Ensures that the next state values are within boundaries 
	 * 
	 * @param nextState nextState 
	 * @return maximum value if it's too large, minimum value if it's too small, 
	 * 			the value unchanged if it's within the boundaries 
	 */
	private Double checkBoundaries(Double nextState) {
		if(nextState < this.acceptedMin) { 
			return this.acceptedMin; 
		} 
		if(nextState > this.acceptedMax) { 
			return this.acceptedMax; 
		} 
		return nextState;
	}
	
	
	/**
	 * Get's the next state encoded as a <code>double</code> 
	 * @param state current state 
	 * @param input current input 
	 * @return next state, encoded as a <code>double</code> 
	 */
	public double getNextValue(Integer state, Integer input) { 
		DoubleTransitionTable inputTable = this.transitions.get(input); 
		return inputTable.getNext(state); 
	} 
	
	
	/**
	 * Get's the next state, calculated by rounding the current value 
	 * in this representation 
	 *  
	 * @param state current state 
	 * @param input current input 
	 * @return next state, encoded as an <code>Integer</code>.  
	 */
	public Integer getNextState(Integer state, Integer input) { 
		DoubleTransitionTable inputTable = this.transitions.get(input); 
		double nextDouble = inputTable.getNext(state); 
		Integer nextState = (int)Math.round(nextDouble);
		if(this.states.contains(Integer.valueOf(nextState))) { 
			return nextState; 
		} else { 
			return getClosest(nextState); 
		} 
	} 
	
	
	/**
	 * Gets closest state in case of discontinuities 
	 * @param nextState rounded number state 
	 * @return closest state in the state set to <code>nextState</code> 
	 */
	private Integer getClosest(int nextState) {
		long difference = Long.MAX_VALUE; 
		Integer closest = null; 
		for(Integer state: this.states) { 
			long current = Math.abs(nextState - state); 
			if(current < difference) { 
				closest = state; 
				difference = current; 
			}
		}
		return closest; 
	}


	/**
	 * Gets a safe copy of the state set for this solution 
	 * @return set of states 
	 */
	public Set<Integer> getStates() { 
		return new TreeSet<Integer>(this.states); 
	} 
	
	
	/**
	 * Gets a safe copy of the inputs  set for this solution 
	 * @return set of inputs 
	 */
	public Set<Integer> getInputs() { 
		return new TreeSet<Integer>(this.inputs); 
	} 
	
	
	
	@Override 
	public String toString() { 
		return toString(false); 
	} 
	
	
	public String toString(boolean verbose) { 
		StringBuilder builder = new StringBuilder(); 
		if(verbose) { 
			for(Integer input: this.inputs) { 
				builder.append(this.transitions.get(input)); 
				builder.append("\n"); 
			} 
			builder.append("\nAccepted: \n"); 
			for(Integer state: this.states) { 
				builder.append(state); 
				builder.append(":\t"); 
				builder.append(this.acceptedStates.get(state)); 
				builder.append("\n"); 
			} 
		} else { 
			if(this.evaluated) 	{ 
				builder.append("Evaluation: "+this.evaluation+"\n"); 
			} else { 
				builder.append("Evaluation: null \n"); 
			} 
		}
		return builder.toString(); 
	}

	
	/**
	 * Gets the acceptance for a state, encoded as a <code>double</code> 
	 * 
	 * @param state state to check if it's accepted 
	 * @return state acceptance, encoded as a <code>double</code> 
	 */
	public double getAccepted(Integer state) { 
		return this.acceptedStates.get(state); 
	}
	

	/**
	 * Gets the set of accepted states 
	 * @return accepted states 
	 */
	public Set<Integer> getAccepted() {
		Set<Integer> accepted = new LinkedHashSet<Integer>(); 
		for(Integer state: states) { 
			if(this.acceptedStates.get(state) >= 0.5) { 
				accepted.add(state); 
			}
		}
		return accepted; 
	}


	/**
	 * @return the evaluation
	 */
	public double getEvaluation() { 
		if(!this.evaluated) { 
			throw new IllegalStateException("This solution is not evaluated. "); 
		}
		return evaluation;
	}


	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(double evaluation) { 
		this.evaluated = true; 
		this.evaluation = evaluation;
	}


	/**
	 * Gets the state number 
	 * 
	 * @return
	 */
	public Integer getStateNumber() {
		return this.states.size();
	} 
	
}
