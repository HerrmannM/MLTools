# MLTools
Tooling for some Machine Learning projects at Monash Univeristy, Melbourne, Australia

## Overview
* Some mathematical functions (MathUtils and SUtils **need cleaning**)
* A watch for benchmarking
* Helpers to work with ARFF files

## Building with Ant
The default target is set to create a jar file.
This project relies on third party libraries, which are needed at compile time.
However, those libraries are NOT packaged in the jar we are building.

### Dependencies
* [Apache Common Maths 3 - 3.6.1](https://commons.apache.org/proper/commons-math/)
* [Weka - 3.8.3](https://www.cs.waikato.ac.nz/ml/weka/index.html)

## About Java Unsafe
This code is using raw memory through Java Unsafe.
This may cause some IDEs, such as Eclipse, to complain with a `Access restriction` error, which can be ignore.

In Eclipse, this error can be silenced in the preferences.
Search for `Forbidden reference`, select `Compiler: Errors/Warnings` and set it to `ignore` or `warning`.

## Contributors
* [Dr François Petitjean](https://github.com/fpetitjean)
* [He Zhang](https://github.com/icesky0125)
* [Dr Matthieu Herrmann](https://github.com/HerrmannM)
* [Dr Nayyar Zaidi](https://github.com/nayyarzaidi)

