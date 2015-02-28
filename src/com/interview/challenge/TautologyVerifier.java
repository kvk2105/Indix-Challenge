package com.interview.challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TautologyVerifier {
	private static String originalPropositionalStatement = "";
	private static List<String> combinations = new ArrayList<String>();
	private static final String fals = "F", tru = "T";
	private static Map<Character, Boolean> variableTruthValueMapping = new HashMap<Character, Boolean>();

	/**
	 * Generate the combinations of elements given in the form of a string
	 * array..
	 * 
	 * @param ip
	 *            An array of strings holding the truth values.
	 * @param len
	 *            Number of truth values in a single combination.
	 * @param t
	 *            A buffer helps in finding the combinations.
	 */
	public static void generateCombinations(String[] ip, int len,
			String combination) {
		if (combination.split("_").length == len) {
			combinations.add(combination);
		} else {
			for (int index = 0; index < ip.length; index++) {
				String buffer = combination;
				combination = combination + ip[index] + "_";
				generateCombinations(ip, len, combination);
				combination = buffer;
			}
		}
	}

	/**
	 * For given optimized propositional statement, compute the unique variables
	 * in it and generate the appropriate number of combinations of truth
	 * values.
	 * 
	 * @param input
	 *            A propositional statement.
	 */
	public static List<String> generateTruthValueCombinations(String input) {
		Set<Character> variables = new HashSet<Character>();
		for (char inputChar : input.toCharArray()) {
			if (inputChar != '&' && inputChar != '|' && inputChar != '!' && inputChar != '(' && inputChar != ')')
				variables.add(inputChar);
		}
		char[] ip = new char[variables.size()];
		int index = 0;
		Iterator<Character> variableIterator = variables.iterator();
		while (variableIterator.hasNext()) {
			ip[index++] = variableIterator.next();
		}
		int numberOfCombinations = ip.length;
		String target = "";
		String[] truthVaues = { "TRUE", "FALSE" };
		generateCombinations(truthVaues, numberOfCombinations, target);
		return TautologyVerifier.combinations;
	}

	/**
	 * For a given propositional statement, obtain the optimized statement.
	 * Optimization is carried out by identifying the standard patterns and
	 * replacing them with their corresponding truth values. Standard patterns:
	 * a&!a =False, !a&a =False a|!a=Truth, !a|a=Truth a&a=a, a|a=a
	 * 
	 * @param PropositionalStatement
	 *            The given propositional statement.
	 * @return Optimized propositional statement.
	 */
	public static String getOptimizedStatement(String PropositionalStatement) {
		if (!PropositionalStatement.contains("(") || !PropositionalStatement.contains(""))
			throw new RuntimeException(
					"Propositional statement doee not have parantheses.");
		originalPropositionalStatement = PropositionalStatement;
		int indexTracker = 0;
		char[] ip = PropositionalStatement.toCharArray();
		int rightBraceTracker = 0, leftBraceTracker = 0;
		for (int index = 0; index < ip.length; index = indexTracker) {
			while (index < ip.length && ip[index] != ')')
				index++;
			rightBraceTracker = index;
			indexTracker = index + 1;
			while (ip[index] != '(' && index >= 0)
				index--;
			leftBraceTracker = index;
			String partialStatement = originalPropositionalStatement.substring(leftBraceTracker + 1, rightBraceTracker);
			String[] splitStatement;
			if (partialStatement.contains("&")) {
				splitStatement = partialStatement.split("&");
				if (splitStatement[0].length() == splitStatement[1].length()) {
					if (splitStatement[0].equals(splitStatement[1])) {
						originalPropositionalStatement = originalPropositionalStatement.replace("(" + partialStatement+ ")", splitStatement[0]);
					} else {
						optimizeIfStandardPattern(splitStatement[0], splitStatement[1], "&", partialStatement);
					}
				} else {
					if (splitStatement[0].contains(splitStatement[1]) || splitStatement[1].contains(splitStatement[0])) {
						originalPropositionalStatement = originalPropositionalStatement.replace("(" + partialStatement+ ")", fals);
					} else {
						optimizeIfStandardPattern(splitStatement[0], splitStatement[1], "&", partialStatement);
					}
				}
			} else if (partialStatement.contains("|")) {
				splitStatement = partialStatement.split("|");
				if (splitStatement[0].length() == splitStatement[1].length()) {
					if (splitStatement[0].equals(splitStatement[1])) {
						originalPropositionalStatement = originalPropositionalStatement.replace("(" + partialStatement+ ")", splitStatement[0]);
					} else {
						optimizeIfStandardPattern(splitStatement[0], splitStatement[1], "|", partialStatement);
					}
				} else {
					if (splitStatement[0].contains(splitStatement[1]) || splitStatement[1].contains(splitStatement[0])) {
						originalPropositionalStatement = originalPropositionalStatement.replace("(" + partialStatement+ ")", tru);
					} else {
						optimizeIfStandardPattern(splitStatement[0], splitStatement[1], "|", partialStatement);
					}
				}
			}
			ip = originalPropositionalStatement.toCharArray();
			if (!PropositionalStatement.equals(originalPropositionalStatement) && originalPropositionalStatement.contains("(")) {
				indexTracker = 0;
				while (indexTracker < ip.length && ip[indexTracker] != ')')
					indexTracker++;
			} else if (partialStatement.length() >= 3) {
				while (indexTracker < ip.length && ip[indexTracker] != '(')
					indexTracker++;
			}
		}
		return originalPropositionalStatement;
	}

	/**
	 * 
	 * @param firstPart
	 *            One of the two strings to be detected for the presence of
	 *            standard pattern.
	 * @param secondPart
	 *            One of the two strings to be detected for the presence of
	 *            standard pattern.
	 * @param op
	 *            OPerator.
	 * @param paritalStatement
	 *            The string to be determined if it is a standard pattern.
	 */
	public static void optimizeIfStandardPattern(String firstPart, String secondPart, String op,
			String paritalStatement) {
		if (op.equals("&")) {
			if (firstPart.contains("T")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", secondPart);
			} else if (secondPart.contains("T")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", firstPart);
			} else if (firstPart.contains("F") || secondPart.contains("F")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", fals);
			}
		} else {
			if (firstPart.contains("T")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", secondPart);
			} else if (secondPart.contains("T")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", firstPart);
			} else if (firstPart.contains("F")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", secondPart);
			} else if (secondPart.contains("F")) {
				originalPropositionalStatement = originalPropositionalStatement.replace("(" + paritalStatement+ ")", firstPart);
			}
		}
	}

	/**
	 * Checks if the given propositional statement is a tautology.
	 * 
	 * @param ps
	 *            A propositional statement.
	 * @return Returns true if given propositional statement is tautology, else
	 *         false.
	 */
	public static boolean isTautology(String ps) {
		if (!ps.contains("&") && !ps.contains("|")) {
			ps = ps.replace("(", "").replace(")", "");
			boolean value = false;
			if (ps.length() != 1)
				value = variableTruthValueMapping.get(ps.toCharArray()[1]);
			else
				value = variableTruthValueMapping.get(ps.toCharArray()[0]);
			return (ps.length() != 1 ? !value : value);
		} else {
			char[] psChar = ps.toCharArray();
			int i = 0;
			while (psChar[i] != '&' && psChar[i] != '|')
				i++;
			String[] subPS = { ps.substring(0, i),
					ps.substring(i + 1, ps.length()) };
			if (psChar[i] == '&')
				return isTautology(subPS[0]) && isTautology(subPS[1]);
			else
				return isTautology(subPS[0]) || isTautology(subPS[1]);
		}
	}

	public boolean checkForTautology(String pstmt) {
		boolean FLAG = false;
		// Optimizing the given statement.
		String optimizedStatement = getOptimizedStatement(pstmt
				.replace(" ", ""));
		if (optimizedStatement.length() == 1) {
			if (optimizedStatement.equals("T")) {
				FLAG = true;
				System.out.println(FLAG);
			} else {
				FLAG = false;
				System.out.println(FLAG);
			}
		} else {
			char[] stmt = new char[optimizedStatement.length()];
			stmt = optimizedStatement.toCharArray();
			int j = 0;
			List<Character> variables = new ArrayList<Character>();
			// Parsing the optimized statement and recording only the variables.
			// Assuming the variables are taken only from characters between 'a'
			// and 'z' inclusive.
			while (j < stmt.length) {
				if (stmt[j] >= 'a' && stmt[j] <= 'z'
						&& !variables.contains(stmt[j]))
					variables.add(stmt[j]);
				j++;
			}

			boolean finalResult = true;
			List<String> tvCombinations = generateTruthValueCombinations(optimizedStatement);
			// Check for all combinations of truth values.
			for (String comb : tvCombinations) {
				String[] tv = comb.split("_");
				for (int i = 0; i < variables.size(); i++) {
					char c = variables.get(i);
					boolean b = Boolean.valueOf(tv[i]);
					variableTruthValueMapping.put(c, b);
				}
				boolean currentResult = isTautology(optimizedStatement);
				// If atleast one truthValue combination evaluates to False,
				// then its not a tautology.
				if (!currentResult) {
					finalResult = false;
					break;
				} else {
					finalResult = finalResult && currentResult;
				}
			}
			if (finalResult) {
				FLAG = true;
				System.out.println(FLAG);
			} else {
				FLAG = false;
				System.out.println(FLAG);
			}
		}
		return FLAG;
	}
}