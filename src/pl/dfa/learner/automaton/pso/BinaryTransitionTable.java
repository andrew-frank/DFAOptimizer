/**
 * 
 */
package pl.dfa.learner.automaton.pso;

import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Binary transition table, initial version of the solution 
 * representation according to the documentation. 
 * For each state, stores a list of zeros and ones. 
 * Enables access and modifications. 
 * 
 *
 */
public class BinaryTransitionTable {

	/**
	 * The transition table. Zeros and ones are stored internally as bitsets. 
	 */
	private Map<Integer, BitSet> table; 
	
	/**
	 * Name of this table. 
	 */
	public String name; 
	
	/**
	 * Number of states
	 */
	int stateNumber; 
	
	
	/**
	 * Creates a new, empty, untitled binary transition table. 
	 */
	public BinaryTransitionTable() { 
		this.table = new TreeMap<Integer, BitSet>(); 
		this.name = "Untitled"; 
	} 


	/**
	 * Creates a new transition table, supporting automata with 
	 * specified <code>states</code> and gives the table a <code>name</code>. 
	 * 
	 * @param states set of automata states 
	 * @param name table name 
	 */
	public BinaryTransitionTable(Set<Integer> states, String name) { 
		this.table = new TreeMap<Integer, BitSet>(); 
		this.name = name; 
		reset(states); 
	}

	
	/**
	 * Resets the table for each of the <code>states</code>. 
	 * Table values are all put to zero for all of the states in the set. 
	 * 
	 * @param states states to reset
	 */
	private void reset(Set<Integer> states) {
		for(Integer state: states) { 
			this.table.put(state, new BitSet()); 
		} 
	} 
	
	
	/**
	 * Gets the bit value at the <code>previous</code> row and <code> next</code> column. 
	 * 
	 * @param previous row index 
	 * @param next column index
	 * @return bit value at the specified position as a <code>boolean</code>
	 */
	public boolean get(Integer previous, Integer next) { 
		return this.table.get(previous).get(next); 
	} 
	
	
	/**
	 * Gets the bit value at the <code>previous</code> row and <code> next</code> column. 
	 * 
	 * @param previous row index 
	 * @param next column index
	 * @param toSet bit value to set at the specified position as a <code>boolean</code>
	 */
	public void set(Integer previous, Integer next, boolean toSet) { 
		BitSet bits = this.table.get(previous); 
		bits.set(next, toSet); 
	} 
	
	
	/**
	 * Sets the bit at the <code>previous</code> row and <code> next</code> column to one. 
	 *  
	 * @param previous row index
	 * @param next column index
	 */
	public void setSingleBit(Integer previous, Integer next) { 
		BitSet bits = this.table.get(previous); 
		bits.clear(); 
		bits.set(next);
	} 
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(); 
		builder.append(this.name); 
		builder.append("\n\t"); 
		for(Integer state1: this.table.keySet()) { 
			builder.append(state1+"\t"); 
		}
		builder.append("\n"); 
		for(Integer state1: this.table.keySet()) { 
			builder.append(state1); 
			builder.append("\t"); 
			for(Integer state2: this.table.keySet()) { 
				BitSet enabled = this.table.get(state1); 
				builder.append(enabled.get(state2) ? 1 : 0); 
				builder.append("\t"); 
			} 
			builder.append("\n"); 
		}
		return builder.toString(); 
	} 
	
	
	/**
	 * Converts the double encoded solution to binary 
	 * 
	 * @param update solution to convert to this representation 
	 */
	public void updateTo(Map<Integer, Double> update) { 
		for(Integer state: this.table.keySet()) { 
			int nextState = (int)Math.round(update.get(state)); 
			this.setSingleBit(state, nextState);
		} 
	}
	
}
