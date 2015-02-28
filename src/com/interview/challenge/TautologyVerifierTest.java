package com.interview.challenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class TautologyVerifierTest {

	private TautologyVerifier tv = new TautologyVerifier();

	@Test(expected = RuntimeException.class)
	public final void shouldThrowExceptionWhenStatementHasNoParanthese() {
		tv.checkForTautology("a|!a");
	}

	@Test
	public final void shouldReturnFalseForBelowStandardPatternWithOneVariable() {
		assertEquals(false, tv.checkForTautology("(!a & a)"));
	}

	@Test
	public final void shouldReturnTrueForBelowStandardPatternWithTwoVariables() {
		assertEquals(true, tv.checkForTautology("((!a & a) | (b | !b))"));
	}

	@Test
	public final void TestForGivenTestCases() {
		assertEquals(true, tv.checkForTautology("(!a | (a & a))"));
		assertEquals(false, tv.checkForTautology("(!a | (b & !a))"));
		assertEquals(true, tv.checkForTautology("(!a | a)"));
		assertEquals(true,
				tv.checkForTautology("((a & (!b | b)) | (!a & (!b | b)))"));
	}
}