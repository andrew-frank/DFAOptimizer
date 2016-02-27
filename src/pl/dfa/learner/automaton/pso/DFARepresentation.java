package pl.dfa.learner.automaton.pso;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * DFA representation for the PSO algorithm. 
 * Each DFA is represented as set of inputs, states and a binary transition table 
 * 
 *
 */
public class DFARepresentation {

	private Set<Integer> states; 
	private Set<Integer> inputs; 
	private Map<Integer, BinaryTransitionTable> transitionTable; 
	private Random random; 

	
	private static Logger logger = LogManager.getLogger(DFARepresentation.class); 
	
	
	public DFARepresentation(Set<Integer> states, Set<Integer> inputs) { 
		this.states = states; 
		this.inputs = inputs; 
		this.transitionTable = new TreeMap<Integer, BinaryTransitionTable>(); 
		this.random = SingletonRandom.getRandom(); 
		initTransitionTables(); 
	} 


	private void initTransitionTables() {
		for(Integer input: this.inputs) { 
			BinaryTransitionTable table = new BinaryTransitionTable(this.states, "Transition table for input "+input); 
			this.transitionTable.put(input, table); 
		} 
	} 


	public void setNextState(Integer initial, Integer input, Integer next) { 
		BinaryTransitionTable inputTransitions = this.transitionTable.get(input); 
		inputTransitions.setSingleBit(initial, next); 
	}
	
	
	public int getStateNumber() { 
		return this.states.size(); 
	} 
	
	
	public int getInputsNumber() { 
		return this.inputs.size(); 
	} 
	
	
	public void updateTo(Solution solution) { 
		for(Integer input: this.inputs) { 
			BinaryTransitionTable inputTable = this.transitionTable.get(input); 
			for(Integer state: this.states) { 
				Double nextDouble = solution.getNextValue(state, input); 
				Integer next = Integer.valueOf((int)Math.round(nextDouble)); 
				inputTable.setSingleBit(state, next);
			} 
		}
	}
	
	
	@Override 
	public String toString() { 
		StringBuilder builder = new StringBuilder(); 
		for(Integer input: this.inputs) { 
			builder.append(this.transitionTable.get(input)); 
			builder.append("\n"); 
		}
		return builder.toString(); 
	}
}
