import java.io.*;
import java.util.*;
import java.lang.*;

public class FlashcardTest {

	/*

	Types of testing:

	Test 1:
		Each term has an equal chance of being chose (i.e. completely random)

	Type 2:
		Each term has a different nonzero probability of being chosen based on how many times the user has gotten it right/wrong

	Type 3:
		Once the user has guessed a term correctly it stops reappearing
		
	*/

	public static Flashcard testSetup() throws IOException {
		String fileName;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the name of the file you would like to be tested on: ");
		fileName = sc.next();
		File f = new File(fileName);
		Flashcard t = new Flashcard();
		while (!t.constructed()) {
			while (!(f.exists() && !f.isDirectory())) {
				System.out.println("That was not a file.");
				System.out.println();
				System.out.print("Enter the name of the file you would like to be tested on: ");
				fileName = sc.next();
				f = new File(fileName);
			}
			t = new Flashcard(fileName);
			if (!t.constructed()) {
				System.out.println();
				System.out.print("That file was not of the correct format. Please enter the name of a new file: ");
				fileName = sc.next();
				f = new File(fileName);
			}
		}
		return t;
	}

	public static int eIndex(String j) {
		if (j.equals("1"))
			return 1;
		if (j.equals("2"))
			return 2;
		if (j.equals("3"))
			return 3;
		return 0;
	}

	public static Flashcard testType1(Flashcard t) {
		System.out.println("We'll now start giving you random flashcards! If you want to exit at any point, just type 'exit' ");
		String inText = "";
		int randIndex = 0;
		Scanner sc = new Scanner(System.in);
		while (!inText.equals("exit")) {
			System.out.println();
			System.out.println();
			randIndex = (int)Math.floor(t.words().size() * Math.random());
			System.out.println(t.words().get(randIndex)[0]);
			System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
			inText = sc.next();
			while (eIndex(inText) != 0) {
				System.out.println();
				System.out.println(t.words().get(randIndex)[eIndex(inText)]);
				System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
				inText = sc.next();
			}
		}
		return t;
	}

	public static Flashcard testType2(Flashcard t) {
		System.out.println("We'll now start giving you random flashcards! If you want to exit at any point, just type 'exit' ");
		String inText = "";
		int randIndex = 0;
		int randProb = 10;
		Scanner sc = new Scanner(System.in);
		while (!inText.equals("exit")) {
			System.out.println();
			System.out.println();
			do {
				randProb = (int)Math.floor(10 * Math.random());
				randIndex = (int)Math.floor(t.words().size() * Math.random());
			} while (t.prob().get(randIndex) < randProb);
			System.out.println(t.words().get(randIndex)[0]);
			System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
			inText = sc.next();
			while (eIndex(inText) != 0) {
				System.out.println();
				System.out.println(t.words().get(randIndex)[eIndex(inText)]);
				System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
				inText = sc.next();
			}
			if (!inText.equals("exit")) {
				System.out.println();
				System.out.print("Did you get that previous card correct (type y or n or s to skip): ");
				inText = sc.next();
			}
			while ((!inText.equals("y")) && (!inText.equals("n")) && (!inText.equals("s")) && (!inText.equals("exit"))) {
				System.out.println();
				System.out.print("Please type either y or n: ");
				inText = sc.next();
			}
			if (inText.equals("y") && t.prob().get(randIndex) > 1)
				t.setIndexProb(randIndex, t.prob().get(randIndex) - 1);
		}
		return t;
	}

	public static Flashcard testType3(Flashcard t) {
		System.out.println("We'll now start giving you random flashcards! If you want to exit at any point, just type 'exit' ");
		String inText = "";
		int randIndex = 0;
		Scanner sc = new Scanner(System.in);
		while (!inText.equals("exit") && t.corrects().size() > 0) {
			System.out.println();
			System.out.println();
			randIndex = (int)Math.floor(t.corrects().size() * Math.random());
			System.out.println(t.words().get(t.corrects().get(randIndex))[0]);
			System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
			inText = sc.next();
			while (eIndex(inText) != 0) {
				System.out.println();
				System.out.println(t.words().get(t.corrects().get(randIndex))[eIndex(inText)]);
				System.out.print("Type \"1\" to see the short definition \"2\" to see the medium sized one, and \"3\" to see the long defintion and anything else (except exit) to move onto the next card: ");
				inText = sc.next();
			}
			if (!inText.equals("exit")) {
				System.out.println();
				System.out.print("Did you get that previous card correct (type y or n or s to skip): ");
				inText = sc.next();
			}
			while ((!inText.equals("y")) && (!inText.equals("n")) && (!inText.equals("s")) && (!inText.equals("exit"))) {
				System.out.println();
				System.out.print("Please type either y or n: ");
				inText = sc.next();
			}
			if (inText.equals("y") && t.prob().get(randIndex) > 1)
				t.removeFromCorrects(randIndex);
		}
		if (t.corrects().size() == 0) {
			System.out.println("Congratulations! You have gotten all of the cards correct.");
		}
		return t;
	}

	public static void test() throws IOException {
		Flashcard card = testSetup();
		Scanner sc = new Scanner(System.in);
		String inText;
		System.out.println("There are three different ways in which you can be tested:");
		System.out.println();
		System.out.println("Type 1: You are randomly given flashcards and are tested on them.");
		System.out.println();
		System.out.println("Type 2: You are randomly given flashcards, but flashcards appear less often based on the number of times you have gotten them correct");
		System.out.println();
		System.out.println("Type 3: You are randomly given flashcards, but each flashcard stops appearing once you have gotten it correct");
		System.out.println();
		System.out.print("Type 1-3 to select the way in which you would like to be tested: ");
		inText = sc.next();
		while (eIndex(inText) == 0) {
			System.out.println();
			System.out.print("Please type 1,2, or 3:");
			inText = sc.next();
		}
		if (eIndex(inText) == 1) {
			FlashcardTest.testType1(card);
		}
		else if (eIndex(inText) == 2) {
			System.out.print("Would you like to continue where you left off or reset the probabilites (type 1 for former, 2 for the latter): ");
			while ((eIndex(inText) == 0) || (eIndex(inText) == 3)) {
				System.out.println();
				System.out.print("Please type 1 or 2:");
				inText = sc.next();
			}
			if (inText.equals("2"))
				card.resetProb();
			card = FlashcardTest.testType2(card);
		}
		else {
			System.out.print("Would you like to continue where you left off or start off being tested on all the cards again (type 1 for former, 2 for the latter): ");
			while ((eIndex(inText) == 0) || (eIndex(inText) == 3)) {
				System.out.println();
				System.out.print("Please type 1 or 2:");
				inText = sc.next();
			}
			if (inText.equals("2"))
				card.resetCorrects();
			card = FlashcardTest.testType3(card);
		}
		card.updateFile();
	}
}