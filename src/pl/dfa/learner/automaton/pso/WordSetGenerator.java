/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import pl.dfa.learner.automaton.ComputeResults;
import pl.dfa.learner.automaton.DFAComputer;
import pl.dfa.learner.automaton.DFA;

/**
 * Generates sets of words for training or testing the machine learning algorithm 
 * for learning the DFA functioning 
 * 
 * @see https://en.wikipedia.org/wiki/Deterministic_finite_automaton
 *
 */
public class WordSetGenerator {

	/**
	 * Reference automaton 
	 */
	private DFA automaton; 
	
	/**
	 * Reference automaton state computer 
	 */
	private DFAComputer computer; 
	
	/**
	 * Set of allowed inputs 
	 */
	private Set<Integer> inputs; 
	
	
	/**
	 * Creates a new generator of word sets, based on a reference DFA 
	 * 
	 * @param automaton reference DFA 
	 */
	public WordSetGenerator(DFA automaton) { 
		this.automaton = automaton; 
		this.inputs = automaton.getInputs(); 
		this.computer = new DFAComputer(automaton); 
	} 
	
	
	/**
	 * Generates set of words having words up to <code>maxWordLength</code> in length 
	 * @param maxWordLength maximum allowed word length 
	 * @return wordSet having all the words  up to <code>maxWordLength</code> 
	 *  		characters in length 
	 */
	public WordSet generateWordSet(int maxWordLength) { 
		return generateWordSet(maxWordLength, 0); 
	} 
	
	
	/**
	 * Generates WordSet having words up to <code>maxWordLength</code> in length 
	 * and having at most <code>maxElements</code> words 
	 * 
	 * @param maxWordLength maximum allowed word length
	 * @param maxElements maximum allowed number of elements 
	 * @return generated word set 
	 */
	public WordSet generateWordSet(int maxWordLength, long maxElements) { 
		WordSet wordSet = new WordSet(); 
		int wordLength = 1; 
		Set<List<Integer>> lastSet = new LinkedHashSet<List<Integer>>(); 
		while(wordLength <= maxWordLength) { 
			addWords(wordSet, wordLength, lastSet, maxElements); 
			if(maxElements > 0 && wordSet.size() > maxElements) { 
				break; 
			} 
			wordLength++; 
		}
		return wordSet; 
	}


	/**
	 * Adds the words to the word set recursively 
	 * 
	 * @param wordSet currently built word set 
	 * @param wordLength current word length 
	 * @param last elements added during the last call  
	 * @param maxElements maximum number of elements 
	 */
	private void addWords(WordSet wordSet, int wordLength, 
			Set<List<Integer>> last, long maxElements) { 
		if(last.isEmpty()) { 
			addSingleSizedElements(wordSet, last); 
			return; 
		} 
		Set<List<Integer>> added = new LinkedHashSet<List<Integer>>(); 
		for(List<Integer> lastAdded: last) { 
			for(Integer inputToAppend: this.inputs) { 
				if(maxElements > 0 && wordSet.size() >= maxElements) { 
					return; 
				}
				List<Integer> newWord = new ArrayList<Integer>(lastAdded); 
				newWord.add(inputToAppend); 
				ComputeResults results = this.computer.compute(newWord); 
				if(results.isAccepted()) { 
					wordSet.addAccepted(newWord);
 				} else { 
 					wordSet.addNonAccepted(newWord);
 				}
				added.add(newWord); 
			} 
		}
		last.clear();
		last.addAll(added); 
	}


	/**
	 * Adds initial words, containing just one input 
	 * 
	 * @param wordSet
	 * @param last
	 */
	private void addSingleSizedElements(WordSet wordSet, Set<List<Integer>> last) {
		Set<List<Integer>> added = new LinkedHashSet<List<Integer>>(); 
		
		for(Integer input: this.inputs) { 
			List<Integer> singleElementList = new ArrayList<Integer>(); 
			singleElementList.add(input); 
			ComputeResults results = computer.compute(singleElementList); 
			
			if(results.isAccepted()) { 
				wordSet.addAccepted(singleElementList);
			} else { 
				wordSet.addNonAccepted(singleElementList); 
			} 
			added.add(singleElementList); 
		}
		last.addAll(added);
	}
}
