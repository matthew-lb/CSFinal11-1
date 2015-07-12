import java.io.*;
import java.util.*;
import java.lang.*;

public class Driver {
	
	public static void main(String[] args) throws IOException {
		run();
	}

	public static void run() throws IOException {
		Scanner sc = new Scanner(System.in);
		String inText = "";
		File f;
		System.out.println("What would you like to do?:");
		System.out.println();
		System.out.println("1. Make flashcards out of a plaintext file or rich text file.");
		System.out.println();
		System.out.println("2. Be tested on a set of flashcards which have already been prepeared.");
		System.out.println();
		System.out.println("3. Make a rich text file which you can print out and use to test yourself on the flashcards.");
		System.out.println();
		System.out.print("Type 1, 2, or 3 to pick: ");
		inText = sc.next();
		while (FlashcardTest.eIndex(inText) == 0) {
			System.out.print("Please type 1, 2, or 3: ");
			inText = sc.next();
		}
		System.out.println();
		System.out.println();
		if (inText.equals("1")) {
			makeFlashcardFiles();
		} 
		else if (inText.equals("2")) {
			FlashcardTest.test();
		}
		else {
			makeRTFFile();
		}
	}

	public static void makeFlashcardFiles() throws IOException {
		Scanner sc = new Scanner(System.in);
		String inText = "";
		String origFile = "";
		String newFile = "";
		System.out.print("Would you rather have the code parse through a rich text file or a plaintext file? The plaintext file parser will work as long as it is in the correct form. The rich text file usually works, but is slightly more prone to errors.");
		System.out.println();
		System.out.print("Type 1 for the plaintext parser and 2 for the rich text parser: ");
		File f;
		inText = sc.next();
		while (!inText.equals("1") && !inText.equals("2")) {
			System.out.println();
			System.out.print("Please type 1 or 2: ");
			inText = sc.next();
		}
		System.out.println();
		if (inText.equals("1")) {
			System.out.println("Follow the following instructions to get your document into the proper form to be parsed for flashcards:");
			System.out.println();
			System.out.println("1. Open the document you want to make flashcards out of and copy and paste the test into a plaintext file.");
			System.out.println();
			System.out.println("2. Put *'s around the terms you want flashcards to be made for.");
			System.out.println();
			System.out.println("3. Leave one full line blank between paragraphs. It doesn't matter how the text is broken up amongst several lines within a paragraph as long as no full lines are blank.");
			System.out.println();
			System.out.println("4. Get rid of any titles or other text that is not apart of the main body.");
			System.out.println();
			System.out.println("Note: If you want a good example of before/after for this process look at sample.rtf and sample.txt.");
			System.out.println();
			System.out.print("Now enter the name of the file you would like to be parsed: ");
			inText = sc.next();
			f = new File(inText);
			while (!(f.exists() && !f.isDirectory())) {
				System.out.println();
				System.out.println("That was not a file.");
				System.out.println();
				System.out.print("Enter the name of the file you would like to be parsed: ");
				inText = sc.next();
				f = new File(inText);
			}
			origFile = inText;
			System.out.println();
			System.out.print("Now enter the name you would like the file containing the flashcards to be called (leave off the .txt): ");
			newFile = sc.next() + ".txt";
			Convert.txtToFlashcard(origFile, newFile);
		}
		else if (inText.equals("2")) {
			System.out.println("Follow the following instructions to get your document into the proper format: ");
			System.out.println();
			System.out.println("1. Bold any words you want to flashcards to be made for.");
			System.out.println();
			System.out.println("2. Get rid of any titles or other text that you don't want to appear in the flashcards.");
			System.out.println();
			System.out.println("Note: If the file contains any tables or lists the parser will treat those as if they were apart of the main body text.");
			System.out.println();
			System.out.print("Now enter the name of the file you would like to be parsed: ");
			inText = sc.next();
			f = new File(inText);
			while (!(f.exists() && !f.isDirectory())) {
				System.out.println();
				System.out.println("That was not a file.");
				System.out.println();
				System.out.print("Enter the name of the file you would like to be parsed: ");
				inText = sc.next();
				f = new File(inText);
			}
			origFile = inText;
			System.out.println();
			System.out.print("Now enter the name you would like the file containing the flashcards to be called (leave off the .txt): ");
			newFile = sc.next() + ".txt";
			Convert.rtfToFlashcard(origFile, newFile);
		}
		System.out.println();
		System.out.print("Would you like the definitions of the terms to contain the term or not? We recommend this for files similar to lat_vocab. Type y or n: ");
		inText = sc.next();
		while (!inText.equals("y") && !inText.equals("n")) {
			System.out.println();
			System.out.print("Please type y or n: ");
			inText = sc.next();
		}
		if (inText.equals("n")) {
			Convert.delWords(newFile);
		}
		System.out.println();
		System.out.println("Congratulations! Your file containing the flashcards has been made!");
		System.out.println();
		System.out.print("Type 'exit' to exit the program or type 'continue' if you want to start the program over and try doing thing with your newly created flashcards: ");
		inText = sc.next();
		while (!((inText.equals("exit")) || (inText.equals("continue")))) {
			System.out.println();
			System.out.print("Please type 'exit' or 'continue': ");
			inText = sc.next();
		}
		if (inText.equals("continue")) {
			run();
		}
	}

	public static void makeRTFFile() throws IOException {
		Scanner sc = new Scanner(System.in);
		String inText = "";
		String newFile = "";
		String title = "";
		System.out.print("Enter the name of the file you want to make the flashcards out of: ");
		inText = sc.next();
		File f = new File(inText);
		while (!(f.exists() && !f.isDirectory())) {
			System.out.println();
			System.out.println("That was not a file.");
			System.out.println();
			System.out.print("Enter the name of the file you want to make the flashcards out of: ");
			inText = sc.next();
			f = new File(inText);
		}
		Flashcard card = new Flashcard(inText);
		System.out.println();
		System.out.print("What would you like the name of the rtf file to be (Note: don't include the rtf): ");
		newFile = sc.next() + ".rtf";
		System.out.println();
		System.out.print("What would you like the title of the document to be?: ");
		title = sc.next();
		card.writeRTF(newFile, title);
	}
}