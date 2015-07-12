# Final Project 11.1

## What the code does:

The code makes flashcards based upon both txt and rtf files, tests the user on those flashcards, and makes rtf files of the flashcards which the user can print out to test themselves on. 


## How it works:

Everything is terminal-based for out project. To run the code just compile and then run the driver class. Our project will give you three choices of what to do. Option 1 allows you to make the flashcard files. When you choose options 2,3 you should enter in the name of the file that you made using option 1. All other instructions will be given to you as you go along.


## How it comes up with the flashcard:

The code parses through files and makes flashcards based upon the bolded words (or in the case of txts the words surrounded by ’*’s). Each flashcard comes with three separate definitions. The short definition is simply the sentence which contains the word. The medium definition is the three sentences which immediately surround the word. The long definition is the paragraph which contains the word. For our purposes a new paragraph begins whenever we encounter a line which contains nothing but white space.


## Possible Bugs:

1. If you don't follow the formatting exactly as described the flashcards and their definitions could end up being weird.

2. Along the same lines as the above, if you accidentally make an empty line bolded in the rtf format, you will get a blank flashcard

3. Since we're converting back and forth between txt files, if you use certain characters you will get a ‘?’

4. If you open the rtf file containing the flashcards (the one made by the program) in TextEdit one of the titles to the chart will not appear on the chart. If you open it in word, it should be correct.

5. If you use any odd formatting in the rtf file or include any tables or charts there may be a handful of random unrelated words in the definition. This error stems from how rtf files work; to fix this error we would have to sift through a list of every single destination (which there are a lot of) and how they work and none of us have the time to do that. That being said we included the most common ones in our code.

6. Not really a bug, but more a stupid thing the user should not do. If you make the name of the file you are creating the name of an existing file it will rewrite that file. The code adds the file endings for you so you can't rewrite the java files or class files, but you can rewrite other files.

7. If you pick option 3, there is a possibility that a few weird characters could turn up if your original file contained non-ASCII characters. I believe this error stems from differences amongst the possible character sets one could be using.