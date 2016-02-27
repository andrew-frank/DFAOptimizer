/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAFactory;
import pl.dfa.learner.automaton.pso.DFARepresentation;
import pl.dfa.learner.automaton.pso.Solution;

/**
 * Test for the solution class 
 *
 */
public class SolutionTest {

	private static Logger logger = LogManager.getLogger(SolutionTest.class); 
	
	public static void main(String[] args) { 
		Set<Integer> states = new TreeSet<Integer>(); 
		Set<Integer> inputs = new TreeSet<Integer>(); 
		DFARepresentationTest.initSet(states, 4); 
		DFARepresentationTest.initSet(inputs, 2); 
		Solution solution = new Solution(20, inputs); 
		logger.info(solution); 
		solution.randomise(); 
		states = solution.getStates(); 
		inputs = solution.getInputs(); 
		logger.info(solution); 
		DFARepresentation representation = new DFARepresentation(states, inputs); 
		logger.info(representation); 
		representation.updateTo(solution); 
		logger.info(representation); 
		
		solution.randomiseNonRounded(); 
		logger.info(solution); 
		solution.setTransition(2, 1, 1.14); 
		solution.setTransition(3, 1, 0);
		solution.setTransition(4, 1, 7); 
		logger.info(solution); 
		representation.updateTo(solution); 
		logger.info(representation); 
		
		DFA solutionDFA = DFAFactory.convertFromSolution(solution); 
		logger.info(solutionDFA); 
		
		DFA dfa;
		try {
			dfa = DFAFactory.parseFromFile(new File("examples\\accepting.dfa")); 
			logger.info("Reference DFA: "+dfa); 
			WordSetGenerator generator = new WordSetGenerator(dfa); 
			WordSet wordSet = generator.generateWordSet(10, 1000); 
			Evaluator evaluator = new Evaluator(wordSet); 
			solution.randomise(); 
			logger.info("Solution DFA: "+DFAFactory.convertFromSolution(solution));
			double evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setAccepted(1, Double.valueOf(0)); 
			solution.setAccepted(2, Double.valueOf(1)); 
			solution.setAccepted(3, Double.valueOf(0)); 
			solution.setAccepted(4, Double.valueOf(1)); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(1, 1, 2); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(1, 2, 4);
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(2, 1, 3); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(2, 2, 3); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(3, 1, 3); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(3,  2, 3); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(4, 1, 4); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
			solution.setTransition(4, 2, 4); 
			evaluation = evaluator.evaluate(solution); 
			logger.info(evaluation); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	} 
}
