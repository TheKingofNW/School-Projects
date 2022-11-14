# Project3-SampleFileStructure
Demonstration of how to arrange Java files for compiling properly.

In order to compile, use "javac -d bin .\com\mysql\example\Main.java"

Then, to create a JAR, use "jar cvfm main.jar manifest.mf -C bin ."

To run, just use "java -jar main.jar"

All of these commands should be run from the src directory, which is treated as the root of this hierarchy.
Pre-compilation, bin would be empty, and main.jar would not be present.

Ensure you follow Ricky's setup guide for IntelliJ, that your mysqld.exe database is running, and that you have JDK installed and on your PATH.
https://www.oracle.com/java/technologies/downloads/#jdk19-windows
Download, install, and add $installdirectoryhere$/bin to your PATH in environmental variables, in Windows. For other platforms, I have no idea.

If you want to actually compile and run this to see how it looks, make sure you set up the University database, or change the code to whatever database you want.
Also make sure you put in your database username and password, they are currently placeholders.

This guide is subject to change, I made it at 1 AM.
