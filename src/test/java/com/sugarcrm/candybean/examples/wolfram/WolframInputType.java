package com.sugarcrm.candybean.examples.wolfram;

/**
 * An Enum of several different types of Wolfram Input type tests, with questions and answers.
 * @author Shehryar Farooq
 */
public enum WolframInputType {
	
	DERIVATIVE("derivative of x^4 sin x","d/dx(x^4 sin(x)) = x^3 (4 sin(x)+x cos(x))"),
	ADDITION("125 + 375","500"),
	BEDMAS("1/4 * (4 - 1/2)","7/8"),
	DIGITS("pi to 200 digits","3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303820"),
	BINARY("219 to binary","11011011_2"),
	FACTORS("factor 2x^5 - 19x^4 + 58x^3 - 67x^2 + 56x – 48","(2 x-3) (x-4)^2 (x^2+1)"),
	PATTERN("5, 14, 23, 32, 41, ...","{5, 14, 23, 32, 41, ...}"),
	MORSE("Morse code - . .-.. . --. .-. .- .--. ....","TELEGRAPH"),
	SPELL("spell 40","forty"),
	CONVERSION("120 miles in kilometers","193.1 km  (kilometers)"),
	VOLUME("How many baseballs fit in a Boeing 747?","4.8 million"),
	DIAMETER("How many grains of rice would it take to stretch around the moon?","1.8 billion"),
	HISTORY("1st prime minister of Pakistan","Liaquat Ali Khan"),
	EMPLOYEES("number of IBM employees","466995 people  (2012 estimate)"),
	NUTRITION("How much Calcium in skim milk?","325 mg  (milligrams)"),
	AUTHOR("Who wrote Stairway to Heaven?","Jimmy Page  |  Robert Plant");
	
	/**
	 * The input to the test type
	 */
	private String input;
	
	/**
	 * The textual answer to the test type.
	 */
	private String answer;
	
	/**
	 * Default field constructor
	 * @param input
	 * @param answer
	 */
	WolframInputType(String input, String answer){
		this.input = input;
		this.answer = answer;
	}

	/**
	 * Default getter
	 * @return Returns the input for this test
	 */
	public String getInput() {
		return input;
	}

	/**
	 * Default getter
	 * @return Returns the expected answer to this test
	 */
	public String getAnswer() {
		return answer;
	}

}