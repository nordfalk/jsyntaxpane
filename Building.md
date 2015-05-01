#Building JSyntaxPane

# Pre-requsites #

Before building, you need to [checkout the source code](http://code.google.com/p/jsyntaxpane/source/checkout).

For building, you need to install [Maven](http://maven.apache.org/).


# Details #

To build from the command-line, just cd into the checked out folder and issue the command:

```
mvn package
```

The JFlex generated sources will be found in `target/generated-sources`. The binary jar will be built in the `target` folder.

# IDE Configuration #

## Netbeans 6.1 ##

**Tools>Plugins>Available Plugins and choose and install Maven.** After installation, just use the IDE's Open Project option to open JSyntaxPane checkedout project (no need to configure dependencies, source directories, Java platform versions etc.)--Maven projects can be directly be opened in Netbeans.

## Eclipse ##

http://maven.apache.org/eclipse-plugin.html

TODO -- Somebody write this!.