A precompiled JAR of this submission is included, which allows it to be run without compiling.
This is run with the following command:

	java --module-path {JAVAFX-SDK} --add-modules javafx.controls -jar .\increment1.jar

To set up the database, you need to run the following command with the CSV files in the appropriate directory:

	java -jar .\appSetupDatabase.jar

Before running, install SQLite by following these instructions:
Step 1 − Go to SQLite download page, and download precompiled binaries from Windows section.
Step 2 − Download sqlite-shell-win32-*.zip and sqlite-dll-win32-*.zip zipped files.
Step 3 − Create a folder C:\>sqlite and unzip above two zipped files in this folder, which will give you sqlite3.def, sqlite3.dll and sqlite3.exe files.
Step 4 − Add C:\>sqlite in your PATH environment variable and finally go to the command prompt and issue sqlite3 command, which should display the following result.