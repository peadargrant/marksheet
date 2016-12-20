#!/bin/bash

mvn exec:java -Dexec.mainClass="marksheet.MarksheetTool" -Dexec.args="sample.xml sample.xlsx"
