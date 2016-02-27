package pl.dfa.learner.automaton;


/**
 * The output of a DFA for given words. 
 * Keeps track of the final DFA state and it's acceptance. 
 * 
 *
 */
public class ComputeResults {

	public Integer finalState; 
	public boolean accepted;
	
	
	/**
	 * Initialises a results object 
	 * 
	 * @param finalState
	 * @param isAccepted
	 */
	public ComputeResults(Integer finalState, boolean isAccepted) {
		super();
		this.finalState = finalState;
		this.accepted = isAccepted;
	}


	/**
	 * Checks the acceptance 
	 * 
	 * @return <code>true</code> if the final state was accepted, <code>false</code> otherwise. 
	 */
	public boolean isAccepted() { 
		return this.accepted; 
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComputeResults [finalState=" + finalState + ", isAccepted="
				+ accepted + "]";
	} 
	
	
}
