# DefDiff
A framework for identifying non-essential changes in software patches.

## Requirements:
- Joern needs to be installed and available on the system's path.

## Input:
- A path to a file or directory representing the pre-patch revision
- A path to a file or directory representing the post-patch revision
- A path to a file containing the definitions in DefLang separated by semicolons

The framework will identify the changes in the provided patch that match the provided definitions and will output the results in the console.
It will also start a server, and if you click on the link, it will open a web page with the diff without non-essential changes. 
If more than one file is involved in a patch, it will open a directory viewer first. Please select the "monaco" diff option.