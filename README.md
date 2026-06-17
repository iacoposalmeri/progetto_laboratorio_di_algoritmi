# Progetto: Catalogo Libri con Albero Rosso-Nero (L-LRB)

## Come compilare i file sorgente
Aprire un terminale nella cartella principale del progetto e creare la cartella di output, per poi compilare i sorgenti:

    mkdir bin
    javac -d bin *.java

## Creare l'eseguibile (.jar)

    jar cfe Catalogo.jar Main -C bin .

Questo comando crea un file `Catalogo.jar`

## Esecuzione manuale

    java -Xmx4G -jar Catalogo.jar input.txt output.txt

*Oppure:*

    java -Xmx4G -cp bin Main input.txt output.txt

## Benchmark

**Per ambienti Windows:**
1. Compilare e creare il `.jar`.
2. Fare doppio clic sul file `test.bat` (oppure avviarlo da prompt dei comandi con `./test.bat`).

**Per ambienti Linux / macOS:**
1. Compilare e creare il `.jar`.
2. Fornire i permessi di esecuzione allo script digitando: `chmod +x test.sh`
3. Avviare lo script digitando: `./test.sh`