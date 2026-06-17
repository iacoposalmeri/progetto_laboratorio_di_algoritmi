#!/bin/bash
echo TEST DI VALIDAZIONE LOGICA (100 righe)
java -cp Catalogo.jar GeneratoreTest test_piccolo.txt 100
java -jar Catalogo.jar test_piccolo.txt output_piccolo.txt
echo "Creati 'test_piccolo.txt' e 'output_piccolo.txt'."

echo "BENCHMARK DI SCALABILITA' ALBERO ROSSO-NERO"
echo ""

SIZES=(10 100 1000 10000 100000 1000000)

for N in "${SIZES[@]}"
do
    echo " STRESS TEST CON $N OPERAZIONI"
    
    echo "Generazione del dataset 'test_$N.txt'..."
    java -cp Catalogo.jar GeneratoreTest test_$N.txt $N
    
    echo ""
    echo "Avvio di 10 esecuzioni per calcolo della media..."
    for i in {1..10}
    do
       java -Xmx4G -jar Catalogo.jar test_$N.txt output_${N}_${i}.txt
       rm output_${N}_*.txt
    done
    
    echo ""
    echo "Cleanup: Rimozione file utilizzati..."
    rm test_$N.txt
    echo ""
done

echo "TUTTI I BENCHMARK SONO STATI COMPLETATI"