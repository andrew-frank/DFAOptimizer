package pl.dfa.learner.automaton.pso;

import java.util.ArrayList;
import java.util.List;

import pl.dfa.learner.automaton.ComputeResults;
import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAComputer;
import pl.dfa.learner.automaton.DFAFactory;


/**
 * 
 * Class used to evaluate solutions. 
 * Based on the word sets, divided into acceptable and unacceptable words. 
 *
 */
public class Evaluator {

	/**
	 * Word set used for the evaluation 
	 */
	private WordSet wordSet; 
	
	
	/**
	 * Creates an <code>Evaluator</code> based on a <code>wordSet</code>. 
	 * @param wordSet word set used for evaluations 
	 */
	public Evaluator(WordSet wordSet) { 
		this.wordSet = wordSet; 
	} 
	
	
	/**
	 * Evaluates a solution. The evaluation is calculated as 
	 * an overall ratio for the correctly accepted vs. 
	 * incorrectly accepted words for the word set defined for 
	 * this evaluator and the automaton represented by the solution. 
	 * Word is correctly accepted if the word is accepted in the word set 
	 * and the solution automaton also accepts it and, conversely, 
	 * if the word is not accepted in the word set and the automaton 
	 * is rejecting it. In all other cases, the word is not accepted. 
	 * In the best possible case where all the words are correctly 
	 * accepted, the evaluation equals to zero. In the worst case, where 
	 * all words are accepted incorrectly, the evaluation is equal to one `
	 * (case where the solution is an inverse of the DFA that created 
	 * the word set). 
	 * 
	 * @param solution solution to evaluate 
	 * @return ratio of correctly accepted words 
	 */
	public double evaluate(Solution solution) { 
		DFA toEvaluate = DFAFactory.convertFromSolution(solution); 
		DFAComputer computer = new DFAComputer(toEvaluate); 
		long errors = 0; 
		for(List<Integer> acceptableWord: wordSet.getAccepted()) { 
			ComputeResults results = computer.compute(acceptableWord); 
			if(!results.isAccepted()) { 
				errors++; 
			} 
		} 
		for(List<Integer> unAcceptableWord: wordSet.getNonAccepted()) { 
			ComputeResults results = computer.compute(unAcceptableWord); 
			if(results.isAccepted()) { 
				errors++; 
			} 
		} 
		double evaluation = ((double) errors)/((double) wordSet.size()); 
		solution.setEvaluation(evaluation); 
		return evaluation; 
	}


	/**
	 * Evaluates the solution and returns the list of incorrectly accepted words. 
	 * 
	 * @param solution solution to evaluate 
	 * @return incorrectly evaluated words 
	 */
	public List<List<Integer>> evaluateVerbose(Solution solution) { 
		if(solution == null) { 
			return null; 
		}
		List<List<Integer>> failed = new ArrayList<List<Integer>>();  
		DFA toEvaluate = DFAFactory.convertFromSolution(solution); 
		DFAComputer computer = new DFAComputer(toEvaluate); 
		long errors = 0; 
		for(List<Integer> acceptableWord: wordSet.getAccepted()) { 
			ComputeResults results = computer.compute(acceptableWord); 
			if(!results.isAccepted()) { 
				failed.add(acceptableWord); 
				errors++; 
			} 
		} 
		for(List<Integer> unAcceptableWord: wordSet.getNonAccepted()) { 
			ComputeResults results = computer.compute(unAcceptableWord); 
			if(results.isAccepted()) { 
				failed.add(unAcceptableWord); 
				errors++; 
			} 
		} 
		double evaluation = ((double) errors)/((double) wordSet.size()); 
		solution.setEvaluation(evaluation); 
		return failed; 
	}
}
