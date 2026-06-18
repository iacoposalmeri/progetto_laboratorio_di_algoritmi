@echo off

echo TEST DI VALIDAZIONE LOGICA (100 righe)
java -cp Catalogo.jar GeneratoreTest test.txt 100 true
java -jar Catalogo.jar test.txt output_test.txt
echo Creati 'test.txt' e 'output_test.txt'.

echo BENCHMARK DI SCALABILITA' ALBERO ROSSO-NERO
echo.

FOR %%N IN (10 100 1000 10000 100000 1000000) DO (
    
    echo STRESS TEST CON %%N OPERAZIONI
    
    echo Generazione del dataset 'test_%%N.txt'...
    java -cp Catalogo.jar GeneratoreTest test_%%N.txt %%N false
    
    echo.
    echo Avvio di 10 esecuzioni per calcolo della media...
    FOR /L %%i IN (1,1,10) DO (
        java -Xmx4G -jar Catalogo.jar test_%%N.txt output_%%N_%%i.txt
        del output_%%N_*.txt
    )
    
    echo.
    echo Cleanup: Rimozione file utilizzati...
    del test_%%N.txt
    echo.
)

echo TUTTI I BENCHMARK SONO STATI COMPLETATI
pause