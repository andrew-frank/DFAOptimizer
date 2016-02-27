/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.dfa.learner.automaton.DFA;
import pl.dfa.learner.automaton.DFAFactory;
import pl.dfa.learner.automaton.DFALoadTest;

/**
 * 
 * Results of the PSO metaheuristic
 * Keeps track of <i>n</i> best found solutions. 
 * New solutions are added using the <code>addResult</code> method. 
 * The result array is always kept sorted. 
 *
 */
public class Results {

	/**
	 * Best found solutions 
	 */
	public Solution[] bestSolutions; 
	
	private static Logger logger = LogManager.getLogger(Results.class); 
	
	/**
	 * Initialises the results object that keeps track of 
	 * <code>toRemember</code> best solutions. 
	 * 
	 * @param toRemember number of best solutions to keep 
	 */
	public Results(int toRemember) { 
		this.bestSolutions = new Solution[5]; 
	} 
	
	
	/**
	 * Adds a new solution and keeps the order of the best solutions from the 
	 * best (index 0) to the worst. 
	 * 
	 * @param solution solution to add 
	 */
	public void addResult(Solution solution) { 
		double current = solution.getEvaluation(); 
		int pos = bestSolutions.length; 
		int initialPos = pos - 1; 
		for(int i = initialPos; i >= 0; i--) { 
			if(bestSolutions[i] == null) { 
				pos = i;
				continue; 
			}
			if(current < this.bestSolutions[i].getEvaluation()) { 
				pos = i;
			} 
		} 
		if(pos != bestSolutions.length) { 
			add(solution, pos); 
		} 
		logger.trace("Found new top "+this.bestSolutions.length+", place "+pos); 
	}
	
	
	
	private boolean checkPos(Solution[] best, int pos, double currentEvaluation) {
		if(pos == 0) { 
			return true; 
		} 
		if(best[pos] == null) { 
			if(best[pos-1] == null) { 
				return true; 
			} 
		} 
		if(Math.abs(best[pos-1].getEvaluation() - currentEvaluation) < 0.0000001) { 
			return false; 
		}
		return true;
	}


	/**
	 * Adds a solution to the specified index (rank), shifting the solutions 
	 * behind this one behind. 
	 * 
	 * @param solution solution to add 
	 * @param pos solution rank among the current best found solutions 
	 */
	private void add(Solution solution, int pos) { 
		for(int i = this.bestSolutions.length-1; i > pos; i--) { 
			this.bestSolutions[i] = this.bestSolutions[i-1]; 
		} 
		this.bestSolutions[pos] = solution; 
		
	}


	@Override 
	public String toString() { 
		StringBuilder builder = new StringBuilder(); 
		builder.append("PSO optimisation results \n"); 
		for(int i = 0; i < bestSolutions.length; i++) { 
			builder.append("Solution "+i); 
			if(i == 0) { 
				if(this.bestSolutions[0] == null) { 
					builder.append("0: null \n"); 
					continue; 
				}
				builder.append("(best found, cost: "+this.bestSolutions[0].getEvaluation()+"): \n"); 
			} else { 
				if(this.bestSolutions[i] == null) {  
					builder.append(": "); 
					builder.append("null"); 
					builder.append("\n"); 
					continue; 
				}
				builder.append("(cost: "+this.bestSolutions[i].getEvaluation()+"): \n"); 
			} 
			builder.append(this.bestSolutions[i].toString(true)); 
			builder.append("\n"); 
		}
		return builder.toString(); 
	} 
	
	
	/**
	 * Returns the best results as a set of DFA-s. 
	 * 
	 * @return DFA string representation of the best solutions 
	 */
	public String toStringDFAs() { 
		StringBuilder builder = new StringBuilder(); 
		builder.append("PSO results \n\n\n"); 
		for(int i = 0; i < this.bestSolutions.length; i++) { 
			if(this.bestSolutions[i] == null) { 
				break; 
			}
			if(i == 0) { 
				builder.append("Best found\nEvaluation: "+this.bestSolutions[i].getEvaluation()+" \n"); 
			} else { 
				builder.append("Solution"+i+"\nEvaluation "+this.bestSolutions[i].getEvaluation()+" \n"); 
			} 
			DFA dfa = DFAFactory.convertFromSolution(this.bestSolutions[i]); 
			builder.append(dfa.toString(true)); 
			builder.append("\n\n\n"); 
		}
		return builder.toString(); 
	}
} 
