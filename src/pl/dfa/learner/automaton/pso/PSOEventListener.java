package pl.dfa.learner.automaton.pso;


/**
 * Event listener interface for integration with other software modules such as GUI 
 * 
 *
 */
public interface PSOEventListener {

	/**
	 * Notifies the listener that the algorithm execution has started 
	 */
	public void searchStarted(); 
	
	/**
	 * Updates the listener with the information about current search 
	 * progress (currently after each 50th iteration). 
	 * 
	 * @param iteration current iteration 
	 * @param avg average evaluation for all particles in the swarm 
	 * @param min best evaluation in the current swarm 
	 * @param max worst evaluation in the current swarm 
	 * @param best best solution found so far 
	 */
	public void searchUpdate(int iteration, double avg, double min, double max, double best); 
	
	/**
	 * Notifies the listener that an improvement in the search has been done 
	 * and that the new best so far with the evaluation <code>evaluation</code> 
	 * has been found.  
	 * 
	 * @param evaluation new best evaluation 
	 */
	public void foundNewBest(double evaluation); 
	
	/**
	 * Notifies the listener that the search progress has finished. This can be due to 
	 * an optimal solution (evaluation = 0) being found, reaching the maximum allowed 
	 * number of iterations or reaching the maximum allowed execution time, whichever 
	 * of those conditions happens first. 
	 * 
	 * @param results search results 
	 * @param wordSet training set 
	 */
	public void searchFinished(Results results, WordSet wordSet); 
	
}
