JAR=target/marksheet-2.0-SNAPSHOT.jar

$(JAR): $(wildcard src/**/*)
	mvn package

index.html: index.org sample.txt sample.xml
	emacs --visit `pwd`/$< -f org-table-iterate-buffer-tables -f org-html-export-to-html --kill

sample_from_txt.xlsx: $(JAR) sample.txt
	mvn exec:java -Dexec.mainClass="marksheet.MarksheetTool" -Dexec.args="sample.txt sample_from_txt.xlsx"

sample_from_xml.xlsx: $(JAR) sample.xml
	mvn exec:java -Dexec.mainClass="marksheet.MarksheetTool" -Dexec.args="sample.xml sample_from_xml.xlsx"

PUBLISHABLE_FILES=index.html sample_from_txt.xlsx sample_from_xml.xlsx sample.xml sample.txt $(JAR)

publishable: $(PUBLISHABLE_FILES)
	python3 /Users/peadar/Documents/tools/linkcrawler/linkcrawler.py index.html

sync_files.txt: Makefile
	./sync_files.sh $(PUBLISHABLE_FILES) > $@

.PHONY: publish
publish: publishable sync_files.txt
	rsync -vur --delete --delete-excluded --include="target/" --include-from=sync_files.txt --exclude="*" ./ "peadar@peadargrant.com:/home/peadar/www/pub/marksheet/"
