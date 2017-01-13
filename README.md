# Java parser example

This project is an example of the use of javaparser library. 
It scans a project directory searching for java patterns in .java files.

## Initial Steps

Clone the project into a directory in your local machine

```
git clone https://github.com/ivan-gayol/javaparser.git
```

Use maven assembly plugin in order to generate the application executable jar file 

```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```

You can find now in the target directory the generated jar file

```
javaparser-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
 

### Execution Instructions

Execute the jar as a normal jar file including as parameter the path to the directory of the code you want to analyze

```
 java -jar javaparser-0.0.1-SNAPSHOT-jar-with-dependencies.jar [[PATH_TO_YOUR_CODE_DIR]]
```

This should return the results in the console

    Total number of methods with:
    =============================
    One call to addCallback   ->            6
    Two calls to addCallback  ->            6
    Three or more calls to addCallback  ->  7
    Total number of methods with:
    =============================
    50 or less lines of code          ->            367
    Between 50 and 150 lines of code  ->            16
    Over 150 lines of code            ->            9
    
    Total number of methods with:
    =============================
    3 or less todo comments       ->            16
    Between 4 and 7 todo comments ->            11
    Over 7 lines of todo comments ->            4

## Author

* **Iván Gayol** - *Initial work* - [JavaParser](https://github.com/ivan-gayol/javaparser)