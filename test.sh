#!/bin/bash
# This script runs the anagram program and saves the result in 3 files.
# The results are compared with the expected results, which should 
# have been previously generated (by using create_expected.sh, for example).
# This should be run on your modified version of the anagram program.
#
# The script is run in a shell (Apple Terminal, for example) by saying:
#
# ./test.sh "Mount Holyoke"
# 
# This will create 3 output files:
#    Mount Holyoke_actual.txt - contains the complete output produced by the program
#    Mount Holyoke_actual_candidates.txt - contains just the candidate words
#    Mount Holyoke_actual_anagrams.txt - contains the anagrams in sorted order
#
# To create actual results for some other word or phrase, just use
# that on the command line instead of "Mount Holyoke".  The output files
# will be named based on the word you are taking an anagram of.
#
# The script assumes that the class files are in the bin directory and that the
# dictionary is in a file called words.txt in the current directory.
#
# This has not been tested on Windows.
#
# Run the anagram program saving the results in expected.txt
java -cp bin anagram "$1" 3 words.txt  > "$1_actual.txt"

# Break the output file into 2 parts: candidates and anagrams
awk '{x="File"i} /Anagrams/{x="File"++i;next}{print > x;}' "$1_actual.txt"

# Rename File to be expected_candidates
mv File "$1_actual_candidates.txt"

# Sort File1 and put the sorted values in expected_anagrams.txt
sort File1 > "$1_actual_anagrams.txt"
rm File1

# Compare the candidate files and show any differences.
echo "Comparing candidates"
diff "$1_expected_candidates.txt" "$1_actual_candidates.txt"

# Compare the anagram files and show any differences.
echo "Comparing anagrams"
diff "$1_expected_anagrams.txt" "$1_actual_anagrams.txt"
