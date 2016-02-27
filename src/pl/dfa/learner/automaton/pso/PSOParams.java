/**
 * 
 */
package pl.dfa.learner.automaton.pso;


/**
 * Parameters of the PSO metaheuristic 
 * @see https://en.wikipedia.org/wiki/Particle_swarm_optimization 
 *
 */
public class PSOParams {
 
	// self explanatory 
	private int maxIterations; 
	private int particlesCount; 
	private double velWeight; 
	private double personalWeight; 
	private double globalWeight; 
	private int allowedTimeMins; 
	
	
	public PSOParams() { 
		this.maxIterations = 40000; 
		this.particlesCount = 50; 
		this.velWeight = 2;  // 4.1 in the literature
		this.personalWeight = 10; // 2.8 in the literature
		this.globalWeight = 2;  // 1.3 in the literature 
		this.allowedTimeMins = 3; 
	} 


	/**
	 * @return the maxIterations
	 */
	public int getMaxIterations() {
		return maxIterations;
	}


	/**
	 * @param maxIterations the maxIterations to set
	 */
	public PSOParams setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations; 
		return this; 
	}


	/**
	 * @return the particlesCount
	 */
	public int getParticlesCount() {
		return particlesCount;
	}


	/**
	 * @param particlesCount the particlesCount to set
	 */
	public PSOParams setParticlesCount(int particlesCount) {
		this.particlesCount = particlesCount; 
		return this; 
	}


	/**
	 * @return the velWeight
	 */
	public double getVelWeight() {
		return velWeight;
	}


	/**
	 * @param velWeight the velWeight to set
	 */
	public PSOParams setVelWeight(double velWeight) {
		this.velWeight = velWeight; 
		return this; 
	}


	/**
	 * @return the personalWeight
	 */
	public double getPersonalWeight() {
		return personalWeight;
	}


	/**
	 * @param personalWeight the personalWeight to set
	 */
	public PSOParams setPersonalWeight(double personalWeight) {
		this.personalWeight = personalWeight; 
		return this; 
	}


	/**
	 * @return the globalWeight
	 */
	public double getGlobalWeight() {
		return globalWeight;
	}


	/**
	 * @param globalWeight the globalWeight to set
	 */
	public PSOParams setGlobalWeight(double globalWeight) {
		this.globalWeight = globalWeight; 
		return this; 
	}


	public double getNormalisationCoefficient() { 
		return this.globalWeight + this.personalWeight + this.velWeight;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PSOParams [maxIterations=" + maxIterations
				+ ", particlesCount=" + particlesCount + ", velWeight="
				+ velWeight + ", personalWeight=" + personalWeight
				+ ", globalWeight=" + globalWeight + "]";
	}


	/**
	 * @return the runDurationMins
	 */
	public int getAllowedTimeMins() {
		return this.allowedTimeMins;
	}


	/**
	 * @param runDurationMins the runDurationMins to set
	 */
	public void setAllowedTimeMins(int allowedTimeMins) {
		this.allowedTimeMins = allowedTimeMins;
	}


	public long getAllowedTimeMillis() {
		return this.allowedTimeMins * 60000; 
	} 
	
	
}
