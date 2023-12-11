#MAKEFILE PARA JAVA
all: default run clean

JFLAGS = -g
JC = javac
JVM = java
T1 = SopaDeLetrasSearch
T2 = SopaDeLetrasSearchF
T3 = SopaDeLetrasSearchT

.SUFFIXES: .java .class

.java.class: 
	$(JC) $(JFLAGS) $<

CLASSES = \
	SopaDeLetrasSearch.java \
	SopaDeLetrasSearchF.java \
	SopaDeLetrasSearchT.java

default: classes

classes: $(CLASSES:.java=.class)

run: $(T1).class $(T2).class $(T3).class
	$(JVM) $(T1)
	$(JVM) $(T2)
	$(JVM) $(T3)

clean: 
	$(RM) *.class