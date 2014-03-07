Google Guava Showcase
==========

This project contains some showcase for google guava library.

http://code.google.com/p/guava-libraries/

The description of google guava project (copied from above page):"The Guava project contains several of Google's core libraries that we rely on in our Java-based projects: collections, caching, primitives support, concurrency libraries, common annotations, string processing, I/O, and so forth."

I strongly recommend this library to every Java developer.

This project records all the information during I study this library. Hope it will help others who are also interested. 

## How to use this project? 
This project is a standard maven multiple-projects.You can import it as maven project if your IDE supports maven.

Or you can run maven commnad to generate Java project, like

    mvn eclipse:eclipse

A better command is recommended to parpare source and javadoc to help you study:

    mvn eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

For windows & eclipse user, "mvn-eclipse.bat" file is provided to help you. Just double click it.

# project construction
In this showcase project, there are some subprojects:

* guava-showcase-base:         showcase for package com.google.common.base
* guava-showcase-collect:      showcase for package com.google.common.collect
* guava-showcase-concurrent:   showcase for package com.google.common.util.concurrent

# related resource 
+ [guava web site](http://code.google.com/p/guava-libraries/)
+ [guava javadoc](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/index.html)
+ [guava Wiki](http://code.google.com/p/guava-libraries/wiki/GuavaExplained?tm=6)
+ [guava google groups](https://groups.google.com/forum/#!forum/guava-discuss)
