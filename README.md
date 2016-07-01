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
* if compare comparsion fault. It will decompile the java to class and compare the content.
* if some of the java package has version number it will ignore the package version number.
* after it compare. It will show the numer of same,different and missing file for java and non-java class.
* output two folder and one RESULT file.




