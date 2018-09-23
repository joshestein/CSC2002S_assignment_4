SRCDIR = ./treeGrow/
BINDIR = ./bin/

#JFLAGS = -g -d $(BINDIR)
JFLAGS = -g
JC = javac
JVM = java

FILES = $(wildcard $(SRCDIR)*.java)

classes: $(subst $(SRCDIR), $(BINDIR), $(FILES:.java=.class))

#$(addprefix $(BINDIR), $(classes))
all: $(classes)

$(BINDIR)%.class: $(SRCDIR)%.java
	$(JC) $(JFLAGS) $<

run: 
	$(JVM) -cp ./ treeGrow.TreeGrow ./sample_input.txt
	#$(JVM) -cp ./treeGrow/ bin.treeGrow.TreeGrow sample_input.txt

docs:
	javadoc -sourcepath $(SRCDIR)*.java -d ./docs/    

clean:
	rm -rf  $(SRCDIR)*.class 

