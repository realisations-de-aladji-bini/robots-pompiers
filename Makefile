# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testInvader testLecture testLecteurDonneesKO testAffichageCarte testScenario0 testScenario1 testDijkstra testDijkstraFilter testRunSimulation docs

testInvader:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestInvader.java

testLecture:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestLecteurDonnees.java

testLecteurDonneesKO:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestLecteurDonneesKO.java

testAffichageCarte:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestAffichageCarte.java

testScenario0:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestScenario0.java

testScenario1:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestScenario1.java

testDijkstra:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestDijkstra.java
	
testDijkstraFilter:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestDijkstraFilter.java

testRunSimulation:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestRunSimulation.java

# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:lib/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeInvader: 
	java -classpath bin:lib/gui.jar tests/TestInvader

exeLecture: testLecture
	java -classpath bin:lib/gui.jar tests/TestLecteurDonnees cartes/carteSujet.map

exeLecteurDonneesKO:  testLecteurDonneesKO
	java -classpath bin:lib/gui.jar tests/TestLecteurDonneesKO

exeAffichageCarte: testAffichageCarte
	java -classpath bin:lib/gui.jar tests/TestAffichageCarte cartes/carteSujet.map

exeScenario0: testScenario0
	java -classpath bin:lib/gui.jar tests/TestScenario0

exeScenario1: testScenario1
	java -classpath bin:lib/gui.jar tests/TestScenario1

exeDijkstra: testDijkstra
	java -classpath bin:lib/gui.jar tests/TestDijkstra
	
exeDijkstraFilter: testDijkstraFilter 
	java -classpath bin:lib/gui.jar tests/TestDijkstraFilter

exeRunSimulation: testRunSimulation
	java -classpath bin:lib/gui.jar tests/TestRunSimulation carte=$(carte) strategie=$(strategie)

docs:
	find src -type f -name "*.java" | xargs javadoc -d docs -sourcepath src -classpath lib/gui.jar

clean:
	rm -rf bin/* docs
