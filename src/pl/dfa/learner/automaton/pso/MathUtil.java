/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.List;


/**
 * Several utility methods 
 *
 *
 */
public class MathUtil {
	
	
	/**
	 * Calculates average evaluation for a list of particles. 
	 * 
	 * @param particles PSO metaheuristic particles 
	 * @return average evaluation for the given swarm 
	 */
	public static double getAverageEvaluation(List<Particle> particles) { 
		double sum = 0; 
		for(Particle particle: particles) { 
			sum += particle.getSolution().getEvaluation(); 
		} 
		return sum/particles.size(); 
	} 
	
	
	/**
	 * Returns the best evaluation from a list of particles 
	 * 
	 * @param particles PSO metaheuristic particles 
	 * @return best evaluation in the given swarm 
	 */
	public static double getBestEval(List<Particle> particles) { 
		double best = Double.MAX_VALUE; 
		for(Particle particle: particles) { 
			if(particle.getSolution().getEvaluation() < best) { 
				best = particle.getSolution().getEvaluation(); 
			} 
		} 
		return best; 
	}


	/**
	 * Returns the worst evaluation from a list of particles 
	 * 
	 * @param particles PSO metaheuristic particles 
	 * @return worst evaluation in the given swarm 
	 */
	public static double getWorstEval(List<Particle> particles) {
		double worst = -Double.MAX_VALUE; 
		for(Particle particle: particles) { 
			if(particle.getSolution().getEvaluation() > worst) { 
				worst = particle.getSolution().getEvaluation(); 
			} 
		} 
		return worst;
	}
	
}
