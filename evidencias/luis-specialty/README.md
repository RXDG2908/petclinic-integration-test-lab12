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

## Capturas manuales pendientes

No se generaron imágenes. La herramienta de captura del entorno no pudo iniciar y devolvió dos veces “trusted Node process exited unexpectedly; kernel reset, rerun your request”.

Para el informe, abrir los archivos reales en IntelliJ IDEA o en un editor y tomar capturas con Recortes de Windows. Mantener visibles el nombre del archivo y los resultados. Evitar otras ventanas y datos personales.

1. Abrir specialty-tests.log al final. Capturar las líneas de ambas clases, el total de 23 y BUILD SUCCESS.
2. Abrir full-tests.log al final. Capturar el total de 50 y BUILD SUCCESS. Identificarla como ejecución anterior de la rama.
3. Abrir compatibility-tests.log al final junto con el apartado de procedencia de este README. Capturar el total de 72 y rotular en el documento “Comprobación conjunta en copia temporal”.
4. Abrir validacion-actual.log al final. Capturar el resultado posterior a la incorporación de cambios remotos.
5. Abrir historial-git.txt y commits-luis.txt. Capturar los commits 5424072 y 55ad1b1 con su autor.
6. Abrir SpecialtyControllerTest.java. Capturar los cuatro métodos pendientes ya implementados y la anotación @Transactional.
7. Abrir SpecialtyControllerMockitoTest.java. Capturar @MockitoBean y los casos de búsqueda, creación, error y borrado.

Las capturas deben mostrar resultados reales completos y legibles. No reconstruir una terminal ni editar los conteos. Guardar las imágenes manuales en esta misma carpeta cuando estén disponibles.
