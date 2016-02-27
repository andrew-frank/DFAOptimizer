/**
 * 
 */
package pl.dfa.learner.automaton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.javatuples.Pair;

import pl.dfa.learner.automaton.pso.Solution;

/**
 * Initialises DFA-s from various sources 
 *
 */
public class DFAFactory {

	
	/**
	 * Load DFA from file 
	 * 
	 * @param input input file 
	 * @return loaded DFA 
	 * @throws IOException in case of I/O error
	 * @throws NumberFormatException in case of format errors
	 * @throws NullPointerException in case of format errors
	 * @throws IllegalArgumentException in case of format errors
	 * @throws IndexOutOfBoundsException in case of format errors
	 * 			 
	 */
	public static DFA parseFromFile(File input) throws IOException { 
		BufferedReader reader = new BufferedReader(new FileReader(input)); 
		String line = reader.readLine(); 
		String[] elements = line.split(","); 
		int statesNumber = Integer.parseInt(elements[0].trim()); 
		int symbolsNumber = Integer.parseInt(elements[1].trim()); 
		Set<Integer> states = getIntegerSet(statesNumber); 
		Set<Integer> symbols = getIntegerSet(symbolsNumber); 
		int transitionTableSize = statesNumber * symbolsNumber; 
		TransitionTable transitionTable = loadTransitionTable(elements, statesNumber, symbolsNumber); 
		Integer initialState = 1; 
		Set<Integer> acceptedStates = loadAcceptable(elements, 2+transitionTableSize); 
		reader.close(); 
		return new DFA(states, symbols, transitionTable, initialState, acceptedStates); 
	} 
	
	
	
	/**
	 * Loads DFA from the particle swarm optimisation metaheuristic representation 
	 * 
	 * @param solution solution
	 * @return DFA represented by the <code>solution</code> 
	 */
	public static DFA convertFromSolution(Solution solution) { 
		if(solution == null) { 
			return null; 
		}
		Set<Integer> states = solution.getStates(); 
		Set<Integer> inputs = solution.getInputs(); 
		TransitionTable table = loadTransitionTable(solution, states, inputs); 
		Integer initialState = 1; 
		Set<Integer> acceptedStates = solution.getAccepted(); 
		return new DFA(states, inputs, table, initialState, acceptedStates); 
	}
	

	private static TransitionTable loadTransitionTable(Solution solution, 
			Set<Integer> states, Set<Integer> inputs) {
		Map<Pair<Integer, Integer>, Integer> transitions = 
				new TreeMap<Pair<Integer, Integer>, Integer>(); 
		for(Integer state: states) { 
			for(Integer input: inputs) { 
				Integer nextState = (int)Math.round(solution.getNextState(state, input)); 
				if(!states.contains(nextState)) { 
					throw new IllegalArgumentException("Unknown state. "); 
				} 
				Pair<Integer, Integer> transition = new Pair<Integer, Integer>(state, input); 
				transitions.put(transition, nextState); 
			}
		} 
		TransitionTable table = new TransitionTable(states, inputs, transitions); 
		return table;
	}

	
	private static Set<Integer> loadAcceptable(String[] elements, int initial) {
		Set<Integer> acceptable = new LinkedHashSet<Integer>(); 
		for(int i = initial; i < elements.length; i++) { 
			Integer acceptableState = Integer.parseInt(elements[i].trim()); 
			acceptable.add(acceptableState); 
		}
		return acceptable;
	}


	public static Set<Integer> getIntegerSet(int statesNumber) {
		Set<Integer> numberSet = new LinkedHashSet<Integer>(); 
		for(Integer i = 1; i <= statesNumber; i++) { 
			numberSet.add(i); 
		}
		return numberSet;
	}


	private static TransitionTable loadTransitionTable(String[] elements,
			int states, int symbols) {
		int transitionTableSize = states * symbols; 
		int lastIndex = transitionTableSize +2; 
		Integer currentState = 1; 
		Integer currentSymbol = 1; 
		Set<Integer> statesSet = getIntegerSet(states); 
		Set<Integer> symbolsSet = getIntegerSet(symbols); 
		Map<Pair<Integer, Integer>, Integer> transitions = new TreeMap<Pair<Integer, Integer>, Integer>(); 
		for(int i = 2; i < lastIndex; i++) { 
			Integer nextState = Integer.parseInt(elements[i].trim()); 
			Pair<Integer, Integer> transition = new Pair<Integer, Integer>(currentState, currentSymbol); 
			transitions.put(transition, nextState); 
			currentSymbol++; 
			if(currentSymbol > symbols) { 
				currentState++; 
				currentSymbol = 1; 
			}
			
		}
		return new TransitionTable(statesSet, symbolsSet, transitions);
	}


	private static TransitionTable parseTransitions(String line, Set<Integer> states, Set<Integer> inputs) {
		Map<Pair<Integer, Integer>, Integer> transitions = new HashMap<Pair<Integer, Integer>, Integer>(); 
		String[] transitionElements = line.split("\\|"); 
		for(int i = 0; i < transitionElements.length; i++) { 
			String element = transitionElements[i].trim(); 
			String[] stateTransition = element.split("-"); 
			String initialStateInputString = stateTransition[0].trim();
			String nextStateString = stateTransition[1].trim(); 
			String[] initialStateInputElements = initialStateInputString.split(","); 
			String initialStateString = initialStateInputElements[0]; 
			String inputString = initialStateInputElements[1]; 
			Integer initial = Integer.parseInt(initialStateString.trim()); 
			Integer input = Integer.parseInt(inputString.trim()); 
			Integer next = Integer.parseInt(nextStateString.trim()); 
			Pair<Integer, Integer> transition = new Pair<Integer, Integer>(initial , input); 
			transitions.put(transition, next); 
		}
		TransitionTable transitionTable = new TransitionTable(states, inputs, transitions); 
		return transitionTable;
	} 
	

	private static Set<Integer> parseLineNumberSet(String line) {
		if(line == null) { 
			throw new IllegalArgumentException("First line needs to contain the DFA states. "); 
		}
		String[] lineElements = line.split(","); 
		Set<Integer> states = new TreeSet<Integer>(); 
		for(int i = 0; i < lineElements.length; i++) { 
			String element = lineElements[i].trim(); 
			Integer toAdd = Integer.parseInt(element); 
			states.add(toAdd); 
		}
		return states;
	} 
	
}
