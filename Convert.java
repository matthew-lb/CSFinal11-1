import java.io.*;
import java.util.*;
import java.lang.*;

public class Convert {

	/*
	Methods which convert between the two specified file types.

	Writes the desired file does not return it
	*/

	public static boolean lowercaseNum(char t) {
		return ((((int)t >= 48) && ((int)t <= 57)) || (((int) t >= 65) && ((int) t <= 90)) || (((int)t >= 97) && ((int) t <= 122)));
	}

	public static boolean onlyWhiteSpace(String t) {
		for (int i = 0; i < t.length(); i++) {
			if ((t.charAt(i) != ' ') && (t.charAt(i) != '\t')) {
				return false;
			}
		}
		return true;
	}

	public static void rtfToTxtForm(String rtfFile, String newFileName) throws IOException, FileNotFoundException {
		ArrayList<Boolean> groups = new ArrayList<Boolean>();
		groups.add(true);
		ArrayList<Boolean> bolds = new ArrayList<Boolean>();
		bolds.add(false);
		BufferedReader f = new BufferedReader(new FileReader(rtfFile));
		PrintWriter out = new PrintWriter(newFileName);
		String lin = "";
		String text = "";
		String control = "";
		int j;
		char tempChar;
		boolean lastLineBlank = false;
		while (f.ready()) {
			lin = f.readLine();
			for (int i = 0; i < lin.length(); i++) {
				if ((lin.charAt(i) == '\\') && (i != lin.length() - 1) && groups.get( groups.size() - 1)) {
					if (lowercaseNum(lin.charAt(i+1))) {
						j = 2;
						while ((i + j < lin.length()) && lowercaseNum(lin.charAt(i+j))) {
							j++;
						}
						control = lin.substring(i+1,i+j);
						if (control.equals("b")) {
							text += '*';
							bolds.set( groups.size() - 1, true);
						}
						else if (control.equals("b0")) {
							text += '*';
							bolds.set( groups.size() - 1, false);
						}
						else if (control.equals("line") || control.equals("par")) {
							if (!onlyWhiteSpace(text) || !lastLineBlank) {
								out.println(text);
								lastLineBlank = false;
								if (onlyWhiteSpace(text)) {
									lastLineBlank = true;
								}
							}
							out.println();
							lastLineBlank = true;
							text = "";
						}
						else if (control.equals("fonttbl")) {
							groups.set( groups.size() - 1, false);
						}
						else if (control.equals("colortbl")) {
							groups.set( groups.size() - 1, false);
						}
						else if (control.equals("info")) {
							groups.set( groups.size() - 1, false);
						}
						else if (control.equals("stylesheet")) {
							groups.set( groups.size() - 1, false);
						}
						i = i + j - 2;
					}
					else if (lin.charAt(i+1) == '*') {
						groups.set( groups.size() - 1, false); 
					}
					else if (lin.charAt(i+1) == (char) 39) {
						tempChar = (char) Integer.parseInt(lin.substring(i+2,i+4),16);
						if (((int) tempChar == 147) || ((int) tempChar == 148))
							tempChar = '"';
						if ((int) tempChar == 146)
							tempChar = (char) 39;
						text += tempChar;
						i = i+2;
					}
					else if (lin.charAt(i+1) == '-') {
						text += '-';
					}
					else if (lin.charAt(i+1) == '_') {
						text += (char) 151;
					}
					i++;
				}
				else if ((lin.charAt(i) == '\\') && (i == lin.length() - 1)) {
					if (!onlyWhiteSpace(text) || !lastLineBlank) {
						out.println(text);
						lastLineBlank = false;
						if (onlyWhiteSpace(text)) {
							lastLineBlank = true;
						}
						text = "";
					}
				}
				else if (lin.charAt(i) == '{') {
					groups.add(false);
					groups.set(groups.size() - 1, groups.get(groups.size() - 2));
					bolds.add(false);
					bolds.set(bolds.size() - 1, bolds.get(bolds.size() - 2));
				}
				else if (lin.charAt(i) == '}') {
					if (bolds.get(bolds.size() - 1) != bolds.get(bolds.size() - 2)) {
						text += '*';
					}
					bolds.remove(bolds.size() - 1);
					groups.remove(groups.size() - 1);
				}
				else if (groups.get( groups.size() - 1)) {
					text += lin.charAt(i);
				}
			}
			if (!onlyWhiteSpace(text) || !lastLineBlank) {
				out.println(text);
				lastLineBlank = false;
				if (onlyWhiteSpace(text)) {
					lastLineBlank = true;
				}
			}
			text = "";
		}
		out.println();
		out.close();
	}

	public static boolean quoteCapsNum(int t) {
		return ((t == 32) || (t == 34) || ((t >= 48) && (t <= 57)) || ((t >= 65) && (t <= 90)));
	}

	public static void txtFormToFileForm(String txtFile, String newFileName) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(txtFile));
		PrintWriter out = new PrintWriter(newFileName);
		String sentence = "";
		String curLine = "";
		String tempLine = "";
		while (f.ready())  {
			tempLine = f.readLine();
			if (tempLine.equals("")) {
				for (int i = 0; i < curLine.length(); i++) {
					if (((int)curLine.charAt(i) == 147) || ((int)curLine.charAt(i) == 148)) {
						sentence += '"';
					}
					else if (((int)curLine.charAt(i) == 8220) || ((int)curLine.charAt(i) == 8221)) {
						sentence += '"';
					}
					else if (((int)curLine.charAt(i) == 146) || ((int)curLine.charAt(i) == 8217)) {
						sentence += (char) 39;
					}
					else {
						sentence += curLine.charAt(i);
					}
					if (curLine.charAt(i) == '.') {
						if (i >= curLine.length() - 2) {
							if (i == curLine.length() - 2) {
								sentence += curLine.charAt(i+1);
								i++;
							}
							out.println(sentence);
							out.println();
							sentence = "";
							curLine = "";
						}
						else {
							if (curLine.charAt(i+1) == ' ') {
								if (quoteCapsNum((int)curLine.charAt(i+2))) {
									if (i >= 3) {
										if (!(curLine.substring(i-2,i).equals("Mr") || curLine.substring(i-2,i).equals("Ms") || curLine.substring(i-3,i).equals("Mrs"))) {
											out.println(sentence);
											sentence = "";
										}
									}
								}
							}
						}
					}
				}
				if (!onlyWhiteSpace(sentence)) {
					out.println(sentence);
				}
				out.println();
				sentence = "";
				curLine = "";
			}
			else {
				curLine += tempLine + " ";
			}
		}
		out.close();
	}

	public static String concatenate(ArrayList<String> ar, int start, int end) {
		String s = "";
		for (int i = start; i <= end; i++) {
			s += ar.get(i);
		}
		return s;
	}

	public static void fileFormtoFlashcard(String txtFile, String newFileName) throws IOException, FileNotFoundException {
		BufferedReader f = new BufferedReader(new FileReader(txtFile));
		PrintWriter out = new PrintWriter(newFileName);
		ArrayList<String> sentences = new ArrayList<String>();
		String paragraph = "";
		String tempText = "";
		boolean bold = false;
		int boldStarted = 0;
		String boldedWord = "";
		while (f.ready()) {
			tempText = f.readLine();
			while (!onlyWhiteSpace(tempText) && f.ready()) {
				sentences.add(tempText);
				paragraph += tempText;
				tempText = f.readLine();
			}
			if (!f.ready()) {
				sentences.add(tempText);
				paragraph += tempText;
			}
			for (int i = 0; i < sentences.size(); i++) {
				for (int j = 0; j < sentences.get(i).length(); j++) {
					if (sentences.get(i).charAt(j) == '*') {
						if (bold) {
							out.println(boldedWord);
							out.println(concatenate(sentences, boldStarted,i));
							if ((boldStarted == 0) && (i == sentences.size() - 1)) {
								out.println(paragraph);
							}
							else if ((boldStarted == 0) && (i == sentences.size() - 2)) {
								out.println(paragraph);
							} 
							else if ((boldStarted == 1) && (i == sentences.size() - 1)) {
								out.println(paragraph);
							} 
							else if (boldStarted == 0) {
								out.println(concatenate(sentences, boldStarted,i+2));
							}
							else if (i == sentences.size() - 1) {
								out.println(concatenate(sentences, boldStarted-2,i));
							}
							else {
								out.println(concatenate(sentences, boldStarted-1,i+1));
							}
							out.println(paragraph);
							out.println(10);
							out.println("True");
							bold = false;
							boldedWord = "";
						}
						else {
							bold = true;
							boldStarted = i;
						}
					}
					else {
						if (bold) {
							boldedWord += sentences.get(i).charAt(j);
						}
					}
				}
			}
			sentences = new ArrayList<String>();
			paragraph = "";
			boldedWord = "";
			boldStarted = 0;
		}
		out.close();
	}

	public static String randString() {
		String s = "";
		for (int i = 0; i < 12; i++) {
			s += (char)(97 + Math.floor(26 * Math.random()));
		}
		return s;
	}

	public static void txtToFlashcard(String txtFile, String newFileName) throws IOException {
		String tempa = randString() + ".txt";
		File f = new File(tempa);
		while ((f.exists() || f.isDirectory())) {
			tempa = randString() + ".txt";
			f = new File(tempa);
		}
		Convert.txtFormToFileForm(txtFile,tempa);
		Convert.fileFormtoFlashcard(tempa, newFileName);
		(new File(tempa)).delete();
	}

	public static void rtfToFlashcard(String rtfFile, String newFileName) throws IOException {
		String tempa = randString() + ".txt";
		File f = new File(tempa);
		while ((f.exists() || f.isDirectory())) {
			tempa = randString() + ".txt";
			f = new File(tempa);
		}
		String tempb = randString() + ".txt";
		f = new File(tempb);
		while ((f.exists() || f.isDirectory())) {
			tempb = randString() + ".txt";
			f = new File(tempb);
		}
		Convert.rtfToTxtForm(rtfFile, tempa);
		Convert.txtFormToFileForm(tempa,tempb);
		Convert.fileFormtoFlashcard(tempb, newFileName);
		(new File(tempa)).delete();
		(new File(tempb)).delete();
	}

	public static String delWordFromDef(String sent, String word) {
		int bStart = -1;
		int bEnd = -1;
		for (int i = 0; i < sent.length(); i++) {
			if (sent.charAt(i) == '*') {
				if (bStart != -1) {
					bEnd = i;
					if (sent.substring(bStart+1, bEnd).contains(word)) {
						return (sent.substring(0,bStart) + sent.substring(bEnd+1,sent.length()));
					}
					bEnd = -1;
					bStart = -1;
				}
				else {
					bStart = i;
				}
			}
		}
		return sent;
	}

	public static void delWords(String fil) throws IOException, FileNotFoundException {
		String word = "";
		String sent = "";
		String tempa = randString() + ".txt";
		File g = new File(tempa);
		while ((g.exists() || g.isDirectory())) {
			tempa = randString() + ".txt";
			g = new File(tempa);
		}
		BufferedReader f = new BufferedReader(new FileReader(fil));
		PrintWriter out = new PrintWriter(tempa);
		while (f.ready()) {
			word = f.readLine();
			out.println(word);
			for (int j = 0; j < 3; j++) {
				out.println(delWordFromDef(f.readLine(), word));
			}
			out.println(f.readLine());
			out.println(f.readLine());
		}
		out.close();
		f = new BufferedReader(new FileReader(tempa));
		out = new PrintWriter(fil);
		while(f.ready()) {
			out.println(f.readLine());
		}
		out.close();
		(new File(tempa)).delete();
	}

}