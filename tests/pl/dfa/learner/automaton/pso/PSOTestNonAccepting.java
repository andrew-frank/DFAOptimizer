/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAFactory;

/**
 * Runs the PSO for a given automaton 
 *
 */
public class PSOTestNonAccepting {

	private static Logger logger = LogManager.getLogger(PSOTestNonAccepting.class); 
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PSO pso = new PSO(new PSOParams()); 
		DFA dfa;
		try {
			dfa = DFAFactory.parseFromFile(new File("examples\\nonAccepting.dfa")); 
			logger.info(dfa); 
			WordSetGenerator generator = new WordSetGenerator(dfa); 
			WordSet wordSet = generator.generateWordSet(10); 
			logger.info(wordSet.toString()); 
			
			Results results = pso.search(dfa.getInputs(), wordSet); 
			logger.info(results); 
			Solution solution = results.bestSolutions[0]; 
			Evaluator evaluator = new Evaluator(wordSet); 
			List<List<Integer>> wrong = evaluator.evaluateVerbose(solution); 
			logger.info(dfa);
			logger.info(DFAFactory.convertFromSolution(solution));
			logger.info(wrong.size()); 
			logger.info(wrong); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
