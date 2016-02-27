/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.pso.DFARepresentation;

/**
 * Tests the classes for representing solutions in the PSO 
 *
 */
public class DFARepresentationTest {
	
	private static Logger logger = LogManager.getLogger(DFARepresentationTest.class); 

	public static void main(String[] args) { 
		Set<Integer> states = new TreeSet<Integer>(); 
		Set<Integer> inputs = new TreeSet<Integer>(); 
		initSet(states, 5); 
		initSet(inputs, 2); 
		DFARepresentation dfaRepresentation = new DFARepresentation(states, inputs); 
		logger.info(dfaRepresentation); 
		dfaRepresentation.setNextState(1, 1, 4);
		dfaRepresentation.setNextState(2, 2, 1);
		logger.info(dfaRepresentation); 
		
		
	}

	public static void initSet(Set<Integer> states, int max) {
		for(int i = 1; i <= max; i++) { 
			states.add(i); 
		}
		
	}
}
