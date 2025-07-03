# SQL Anywhere to Oracle Converter

This is a simple Java command-line utility for converting basic SQL Anywhere
syntax to Oracle Database syntax. It performs a few common text replacements
and is intended only as a starting point. The converter reads SQL from a file
whose path is provided as an argument or from standard input if no file is
given.

## Building

```
javac SqlAnywhereToOracle.java
```

## Usage

```
# From a file
java SqlAnywhereToOracle path/to/input.sql

# Or from standard input
cat input.sql | java SqlAnywhereToOracle
```

The converted SQL is written to standard output.
