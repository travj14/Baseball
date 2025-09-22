JAVAC = javac
SRC = $(wildcard *.java)
CLASSES = $(SRC:.java=.class)

compile: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

run: Client.class
	java Client

clean:
	rm -f *.class