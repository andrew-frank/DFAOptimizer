/**
 * 
 */
package pl.dfa.learner.automaton;

import java.util.List;

/**
 * Computes the DFA output for a given series of inputs. 
 *
 */
public class DFAComputer {

	/**
	 * Automaton 
	 */
	private DFA automaton; 
	
	/**
	 * Current state 
	 */
	private Integer currentState; 
	
	/**
	 * Automaton in the initial state 
	 * @param automaton automaton to compute outputs for 
	 */
	public DFAComputer(DFA automaton) { 
		this.automaton = automaton; 
		this.currentState = automaton.getInitialState(); 
		
	} 
	
	
	/**
	 * Runs one transition of an automaton 
	 * 
	 * @param input input 
	 * @return transition results 
	 */
	public ComputeResults performTransition(Integer input) { 
		this.currentState = automaton.getTransition(this.currentState, input); 
		
		return new ComputeResults(this.currentState, automaton.isAccepted(this.currentState)); 
	}
	
	
	/**
	 * Runs the output for a given word 
	 * @param inputs word 
	 * @return output for the word <code>inputs</code> 
	 */
	public ComputeResults compute(List<Integer> inputs) { 
		this.reset(); 
		for(Integer input: inputs) { 
			currentState = automaton.getTransition(currentState, input); 
		} 
		return new ComputeResults(currentState, automaton.isAccepted(currentState)); 
	}


	/**
	 * Resets the automaton 
	 */
	public void reset() {
		this.currentState = this.automaton.getInitialState(); 
		
	}
	
}
