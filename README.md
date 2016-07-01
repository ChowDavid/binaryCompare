#Binary Compare
##build
```
mvn clean assembly:asembly
```
##execute
```
java -jar binaryCompare-jar-with-dependencies.jar "Header" "Sourcefolder" "TargetFolder"
```

## feature 1.0.0-SNAPSHOT
* compare two java package folder
* if compare comparison fault. It will decompile the java to class and compare the content.
* if some of the java package has version number it will ignore the package version number.
* after it compare. It will show the number of same,different and missing file for java and non-java class.
* output two folder and one RESULT file.

## New Pending Feature
* modify the target folder last modify date same as original last modify date.
* add source field -DSource= 
* add target folder -DTarget= 
* add message header -DHeader= 
* add last modify date -DlastModify=yyyy-MM-dd, if file before last modify date. the result will separate two.
* add The Java file compare method would be change base on method base not class base and found the number of file same, different or missing. 
* add extra report data about number of line different and missing, from method comparison method. 






