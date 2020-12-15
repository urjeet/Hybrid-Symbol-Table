# Hybrid Symbol Table
***
An implementation of a hybrid symbol table that provides hash table access, indexed integer access, and sequential access (using an iterator). This implementation was used to model an array data type in the PHP Language. The PHP array, unlike that of Java, is a hybrid of a hash table and linked list. I completed this project for a University assignment.  

**Do not use any of the source code to submit as your own work or idea.** 

*Program Written in October 2020*  
*Pushed to Github in December 2020*

***

## Source Files and Their Roles

`MainProgram.java` is the main file that creates a hybrid symbol table and calls methods that pass in data into it. Apart from inputting data, this file also calls methods that modify and delete the said data. Modifying includes sorting the data. Finally, this file is the main driver of printing the subject headings to the console.

`HybridSymbolTable.java` is the actual implementation of the PHP array data structure in Java. It holds a generic Java class that implements `Iterable`. It works as a symbol table, storing key and value pairs where the keys are of Java `String` type and the values are of parameter type `V`. The underlying storage mechanism uses a linear probing hash table. This allows methods such as `put()`, `get()`, and `unset()` to be essentially done in `O(1)` time. The hash table size is resized to twice the size when the table is at least half-full. The linked list is used to keep track of the items in sequential order. This will be based on how they are inserted into the hybrid symbol table. 

***

## Compiling, Executing, and Understanding the Output

**COMPILATION:**
> javac MainProgram.java

**EXECUTION:**
> java MainProgram

When compiling and executing `MainProgram.java`, please make sure that the hybrid symbol table, `HybridSymbolTable.java`, is located in the same directory. The output is clearly labeled with headings. The data is printed through the `Iterable` interface. Using the `each()` method all of the non-null contents of the hybrid symbol table are printed. Next, the raw hash table contents are printed. After the all the data has been printed, data modification begins. There is searching, adding, deleting, sorting and more printing of the modified data. The array can also be 'flipped' where the key and value pairs are switched amongst themselves. Ultimately, the output is simple to understand in that the functions of the hybrid symbol table are demonstrated and the design is showcased. 

