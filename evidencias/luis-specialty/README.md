# Evidencias de Luis Abad — Specialty — Laboratorio 12

Rama feature/abad-specialty. PR existente https://github.com/RXDG2908/petclinic-integration-test-lab12/pull/3.

## Resultados reales conservados

| Archivo | Procedencia | Resultado |
|---|---|---|
| specialty-tests.log | Ejecución de Specialty en la rama de Luis, con los cambios de 5424072 y 55ad1b1 | 23 pruebas, 0 fallos, 0 errores, 0 omitidas, BUILD SUCCESS |
| full-tests.log | Suite completa de la rama de Luis antes de incorporar los cambios remotos posteriores | 50 pruebas, 0 fallos, 0 errores, 0 omitidas, BUILD SUCCESS |
| compatibility-tests.log | Comprobación conjunta en una copia temporal, descrita abajo | 72 pruebas, 0 fallos, 0 errores, 0 omitidas, BUILD SUCCESS |
| reportes-50/ | Reportes de texto originales de Surefire de la corrida de 50 pruebas | Desglose por clase |
| reportes-72-copia-temporal/ | Reportes de texto originales de Surefire de la comprobación conjunta | Incluye Owner, Vet y Visit |
| validacion-actual.log | Nueva ejecución después de incorporar los cambios remotos en esta sesión | 65 pruebas, 0 fallos, 0 errores, 0 omitidas, BUILD SUCCESS |
| commit-validacion-actual.txt | Commit utilizado por validacion-actual.log | Identifica la versión comprobada |
| historial-git.txt | Salida real de git log antes del commit de evidencias | Historial de ramas y merges |
| commits-luis.txt | Salida real de git show de los dos commits de implementación | Autoría y mensajes |
| escenarios-implementados.diff | Diff real del commit 55ad1b1 | Casos H2, Mockito y aislamiento |

Los logs y reportes originales se copiaron sin editar sus resultados. Los archivos de historial y diff se obtuvieron directamente de Git. No se incluyen carpetas target ni bases de datos.

## Comandos utilizados

Desde la raíz del repositorio, en PowerShell.

Pruebas de Specialty (14 H2 y 9 Mockito).

    .\mvnw.cmd -B "-Dtest=SpecialtyControllerTest,SpecialtyControllerMockitoTest" "-Dspring.profiles.active=h2" "-Djavax.net.ssl.trustStoreType=Windows-ROOT" "-Djavax.net.ssl.trustStore=NONE" test

Suite completa, tanto en la corrida original de 50 pruebas como en la validación posterior.

    .\mvnw.cmd -B clean test "-Dspring.profiles.active=h2" "-Djavax.net.ssl.trustStoreType=Windows-ROOT" "-Djavax.net.ssl.trustStore=NONE"

Las opciones de trustStore permiten usar el almacén de certificados de Windows para descargar dependencias.

Historial y autoría.

    git log --all --graph --oneline --decorate
    git show --format=fuller --no-patch 5424072 55ad1b1

## Alcance de la comprobación conjunta de 72 pruebas

La corrida se realizó en una copia temporal denominada lab12-compatibility-a9875d1f-bd1e-4734-bd9d-fbcf1ed66633, fuera del clon de trabajo.

Se exportaron con git archive las ramas de Renzo (ac68f68), David (417b6fe) y Specialty (354ba26). Se superpusieron los nueve archivos de Specialty implementados por Luis, equivalentes a los commits 5424072 y 55ad1b1, y se ejecutó el comando completo de Maven con H2.

Las 72 pruebas corresponden al conjunto de código de los integrantes. Se realizó una comprobación en archivos temporales, sin fusionar los PR para esa corrida. El resultado de 50 pruebas corresponde a la rama de Luis en el estado anterior a los cambios remotos posteriores.

Al publicar, Git detectó nuevos commits remotos en Specialty, que incorporaban Vet y Visit desde main y una resolución del README. Se conservaron esos commits mediante el merge local 578683d. La nueva corrida validacion-actual.log documenta ese estado posterior.

## Escenarios de Specialty

Se conservan las siete pruebas originales. Se añadieron testFindByName, testFindByOffice, testCreateSpecialty_InvalidSchedule y testCreateSpecialty_InvalidHourRange, además de tres pruebas para persistencia del horario, filtro combinado y horario incompleto.

SpecialtyControllerMockitoTest implementa los nueve escenarios exigidos por TASKS_INTEGRATION.md con @MockitoBean sobre SpecialtyService. SpecialtyControllerTest usa H2 real y @Transactional para revertir los datos creados en cada prueba.

Los horarios son opcionales para mantener las peticiones anteriores que solo enviaban el nombre. Al enviarlos se exigen ambas horas, valores de 0 a 23 y apertura menor que cierre.

## Capturas originales aportadas por Luis Abad

Las tres imágenes PNG fueron proporcionadas y verificadas por Luis. Se copiaron desde Lab12/Evidencias al directorio evidencias ya existente en la raíz del repositorio, conservando sus nombres descriptivos. Se verificó que los hashes SHA-256 del original y de la copia coinciden. Las imágenes se publican sin modificaciones.

| Captura | Qué demuestra |
|---|---|
| [01-pr-fusionado.png](../01-pr-fusionado.png) | GitHub muestra el PR #3 con estado Merged y su integración en main. La captura corresponde al estado fusionado de ese PR, anterior a la publicación posterior de los commits de Luis. |
| [02-commits-luis.png](../02-commits-luis.png) | Historial de feature/abad-specialty con Itssmann como autor de los commits de implementación 5424072 y 55ad1b1 y del commit de evidencias 3c0f0e2. |
| [03-pruebas-specialty.png](../03-pruebas-specialty.png) | Ejecución real de SpecialtyControllerTest con 14 pruebas, 0 fallos, 0 errores, 0 omitidas y BUILD SUCCESS. La captura muestra una duración total de 16.656 s y finalización a las 23:09:30 del 6 de noviembre de 2026, hora de Lima. |

Comando de la ejecución H2 documentada por Luis.

    .\mvnw.cmd test "-Dtest=SpecialtyControllerTest" "-Dspring.profiles.active=h2"

## Diferencia entre las ejecuciones

- Las 14 pruebas de la captura corresponden únicamente a SpecialtyControllerTest, con H2.
- Las 23 pruebas del reporte specialty-tests.log suman 14 H2 y 9 Mockito de Specialty.
- Las 50 pruebas de full-tests.log corresponden a la suite completa de la rama antes de incorporar los cambios remotos posteriores.
- Las 65 pruebas de validacion-actual.log corresponden a la suite completa después de incorporar Vet y Visit desde la rama remota.
- Las 72 pruebas de compatibility-tests.log corresponden a la comprobación conjunta en una copia temporal con Owner, Vet, Visit y Specialty.

Todos esos reportes registran cero fallos, cero errores, cero omitidas y BUILD SUCCESS. Son ejecuciones de distinto alcance.

reintento-specialty.log conserva otra ejecución real del comando H2 de 14 pruebas, realizada antes de recibir las capturas. Su duración fue 16.369 s. El log y la captura de 16.656 s pertenecen a corridas distintas.

Los intentos automáticos anteriores de captura fallaron por “windows sandbox failed: registered runtime ownership is missing”. Las tres imágenes de esta sección proceden de Luis y ya están disponibles como evidencias originales.