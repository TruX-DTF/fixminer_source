# Code of FixMiner

## Build

Clone and install `edu.lu.uni:simple-utils`.

```
git clone https://github.com/SerVal-DTF/simple-utils
mvn install
```

Clone and install the modified version of Gumtree.

```
git clone https://github.com/SerVal-DTF/gumtree
mvn install
```

Clone and install fixminer.

```
git clone https://github.com/SerVal-DTF/fixminer_source
mvn install
```

## Main classes

* `src/main/java/edu/lu/uni/serval/fixminer/Launcher.java` computes the edit scripts from local file pairs and pushes them to a Redis database
* `src/main/java/edu/lu/uni/serval/utils/ClusterToPattern.java` extracts the Rich Edit Script from the Gumtree output from the database
* `src/main/java/edu/lu/uni/serval/utils/CallShell.java`
* `src/main/java/edu/lu/uni/serval/fixminer/TestTreeLoader.java`
* `src/main/java/edu/lu/uni/serval/fixminer/jobs/PatternExtractor.java`
* `src/main/java/edu/lu/uni/serval/fixminer/akka/compare/CompareTrees.java`

