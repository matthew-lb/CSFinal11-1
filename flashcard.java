import java.io.*;
import java.util.*;
import java.lang.*;

public class Flashcard {

	private ArrayList<String[]> words;   // List of the flashcards and the various information about them
	private ArrayList<Integer> prob;     // Probability of every number being chose
	private ArrayList<Integer> corrects; // List of the indices from words which the user can chose
	private boolean constructed;         // Whether the file was of the correct format and the constructor worked
	private String fileName;

	/*
	Info about the file the flashcards are read from plaintext files.
	The information about each flashcard comes in groups of 6 lines:
		Line 1: The term
		Line 2: The sentence the term comes from (a.k.a short defintion)
		Line 3: The group of 3 sentences it comes from (a.k.a. medium)
		Line 4: The paragraph it comes from (a.k.a. large defintion)
		Line 5: The current probability of the term being picked (int 1-10)
		Line 6: Whether or not the term has been chosen yet
	*/


	/*
	Section I: Constructor
	*/

	public Flashcard() {
		words = new ArrayList<String[]>();
		prob =  new ArrayList<Integer>();
		corrects = new ArrayList<Integer>();
		constructed = false;
		fileName = "";
	}

	public Flashcard(String fileN) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(fileN));
		fileName = fileN;
		words = new ArrayList<String[]>();
		prob =  new ArrayList<Integer>();
		corrects = new ArrayList<Integer>();
		constructed = true;
		String lin;
		String[] word = new String[4];
		int index = 0;
		int i;
		while (f.ready() && constructed) {
			word = new String[4];
			i = 0;
			while (f.ready() && i < 4) {
				lin = f.readLine();
				word[i] = lin;
				i++;
			}
			words.add(word);
			if (f.ready()) {
				try {
					prob.add(Integer.parseInt(f.readLine()));
				}
				catch (NumberFormatException e) {
					constructed = false;
				}

			}
			if (f.ready()) {
				lin = f.readLine();
				if ((lin.equals("true") || lin.equals("True"))) {
					corrects.add(index);
				} else if (!(lin.equals("false") || lin.equals("False"))) {
					constructed = false;
				}
			}
			else {
				constructed = false;
			}
			index++;
		}
		if (words.size() == 0)
			constructed = false;
		if (constructed)
			words = delStars(words);
	}

	public static ArrayList<String[]> delStars(ArrayList<String[]> wordy) {
		for (int i = 0; i < wordy.size(); i++) {
			for (int j = 1; j < 4; j++) {
				for (int k = 0; k < wordy.get(i)[j].length(); k++) {
					if (wordy.get(i)[j].charAt(k) == '*') {
						wordy.get(i)[j] = wordy.get(i)[j].substring(0,k) + wordy.get(i)[j].substring(k+1,wordy.get(i)[j].length());
						k--;
					}
				}
			}
		}
		return wordy;
	}

	/*
	Section II: Return/Print Functions
	*/

	public String toString() {
		String s = "";
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < words.get(0).length;j++) {
				s += words.get(i)[j] + "\n";
			}
			s += "\n";
		}
		return s;
	}

	public ArrayList<String[]> words() {
		return words;
	}

	public boolean constructed() {
		return constructed;
	}

	public ArrayList<Integer> prob() {
		return prob;	
	}

	public ArrayList<Integer> corrects() {
		return corrects;
	}

	public void removeFromCorrects(int i) {
		corrects.remove(i);
	}

	public void setIndexProb(int index, int value) {
		prob.set(index,value);
	}

	/*
	Section III: Functions for updating files/flashcards based on user input
	*/

	public void resetProb() {
		prob = new ArrayList<Integer>();
		for (int i = 0; i < words.size(); i++)
			prob.add(10);
	}

	public void resetCorrects() {
		corrects = new ArrayList<Integer>();
		for (int i = 0; i < words.size(); i++)
			corrects.add(i);
	}

	public void updateFile() throws FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < 4; j++) {
				out.println(words.get(i)[j]);
			}
			out.println(prob.get(i));
			out.println(corrects.contains(i));
		}
		out.close();
	}

	/*
	Section IV: Writing flashcards
	*/

	public void writeRTF(String fileName, String title) throws IOException {
		PrintWriter out = new PrintWriter(fileName);
		out.println("{\\rtf1\\ansi\\deff0 {\\fonttbl {\\f0 Times New Roman;}}");
		out.println("{\\pard\\itap0 \\qc \\fs32 " + title + " }");
		out.println("\\par");
		out.println("\\line");
		out.println("\\itap1\\trowd\\trgraph180");
		out.println("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
		out.println("\\cellx2500");
		out.println("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
		out.println("\\cellx10000");
		out.println("\\pard\\intbl\\fs28 Word \\cell");
		out.println("\\pard\\intbl\\fs28 Definition \\cell");
		out.println("\\row");
		for (int i = 0; i < words.size(); i++) {
			out.write("\\itap1\\trowd\\trgraph180");
			out.println("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
			out.println("\\cellx2500");
			out.println("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
			out.println("\\cellx10000");
			out.println("\\pard\\intbl\\fs24 \\b" + words.get(i)[0] + "\\b0\\cell");
			out.println("\\pard\\intbl\\fs24 " + words.get(i)[2] + "\\cell");
			out.println("\\row");
		}
		out.println("}");
		out.close();
	}

	public void writeRTF(String fileName) throws IOException {
		writeRTF(fileName, "Vocab List");
	}

}
