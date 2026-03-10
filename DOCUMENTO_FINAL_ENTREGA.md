# Automatizacion Del Juego Carrera De Caballos Con Cartas Usando Java Y Spring Boot

**Estudiante:** Juan Pinzon  
**Curso:** [Colocar curso]  
**Asignatura:** [Colocar asignatura]  
**Docente:** [Colocar nombre del docente]  
**Fecha:** [Colocar fecha de entrega]  

---

## 1. Introduccion

En este trabajo se desarrollo una aplicacion web en Java con Spring Boot para automatizar el juego de carrera de caballos con cartas. El objetivo principal fue aplicar los conceptos de modelo de datos, estructuras, operadores y restricciones, tomando como referencia el juego observado en video y transformandolo en una solucion funcional y desplegable.

La aplicacion simula una carrera entre cuatro caballos, donde cada caballo esta representado por un As de la baraja. En cada turno se revela una carta y solamente avanza el caballo cuyo palo coincide con el de la carta obtenida. La carrera termina cuando uno de los caballos llega a la meta.

Ademas de implementar el juego, se documenta el proceso completo siguiendo una metodologia de solucion de problemas compuesta por: analisis, alternativas, diseno, implementacion, pruebas y conclusiones.

---

## 2. Proposito De La Actividad

Construir un juego automatizado que evidencie la comprension del concepto de modelo de datos y su relacion con estructuras, operadores y restricciones, aplicando una metodologia de solucion de problemas y documentando el proceso completo.

---

## 3. Descripcion Del Problema

El problema consiste en automatizar el juego de carrera de caballos con cartas. En este juego:

- participan cuatro caballos
- cada caballo corresponde a un As de un palo distinto
- se usa una baraja francesa
- la pista tiene siete posiciones
- en cada turno se revela una carta
- avanza unicamente el caballo del palo de esa carta
- gana el caballo que llega primero a la meta

La solucion debia permitir ver el juego funcionando, mostrar el resultado de la carrera y dejar evidencia clara de la logica aplicada.

---

## 4. Metodologia De Solucion De Problemas

### 4.1 Analisis

En la etapa de analisis se identificaron los elementos principales del problema y las reglas del juego.

Los datos necesarios para resolver el problema fueron:

- el mazo de cartas
- los palos de la baraja
- los caballos
- la posicion de cada caballo
- el historial de turnos
- el ganador
- la meta de la carrera

Entradas del sistema:

- generacion y mezcla del mazo
- cartas reveladas en cada turno

Salidas del sistema:

- posicion de cada caballo
- historial de movimientos
- ganador final
- visualizacion de la carrera

Del analisis se concluyo que el problema podia modelarse claramente con clases y estructuras sencillas, lo cual hacia viable su implementacion en Java.

### 4.2 Alternativas

Se consideraron dos alternativas de solucion.

**Alternativa 1: aplicacion de consola**

Ventajas:

- implementacion mas simple
- menor complejidad visual

Desventajas:

- poco atractiva visualmente
- menos adecuada para despliegue en la web
- menos clara para una demostracion academica

**Alternativa 2: aplicacion web con Spring Boot**

Ventajas:

- mejor presentacion visual
- mas facil de demostrar al docente
- permite despliegue en la nube
- facilita mostrar documentacion y simulacion en navegador

Desventajas:

- requiere configuracion de Java y entorno
- tiene una complejidad tecnica mayor que consola

Despues de evaluar ambas opciones, se eligio la aplicacion web con Spring Boot porque permite representar mejor la solucion, facilita la comprension del juego y cumple mejor con el requisito de entregar un enlace desplegado.

### 4.3 Diseno

En la etapa de diseno se definio el modelo de datos y la estructura general del sistema.

Se identificaron las clases principales:

- `Carta`
- `Caballo`
- `Palo`
- `TurnoJuego`
- `ResultadoJuego`

Descripcion de cada clase:

**Carta**  
Representa una carta de la baraja y contiene:

- valor
- palo

**Caballo**  
Representa un caballo del juego y contiene:

- nombre
- palo
- posicion

**Palo**  
Es un `enum` que representa los cuatro palos:

- corazones
- diamantes
- treboles
- picas

**TurnoJuego**  
Representa lo ocurrido en un turno:

- numero del turno
- carta revelada
- caballo que avanza
- nueva posicion
- estado de posiciones de todos los caballos

**ResultadoJuego**  
Representa el resultado global de la simulacion:

- meta
- lista de caballos
- historial de turnos
- ganador

Tambien se definio el flujo del algoritmo:

1. Crear el mazo completo de 52 cartas.
2. Mezclar el mazo.
3. Crear los cuatro caballos.
4. Tomar una carta por turno.
5. Identificar el caballo del mismo palo.
6. Avanzar ese caballo una posicion.
7. Registrar el turno.
8. Verificar si el caballo llego a la meta.
9. Si llego a la meta, terminar la carrera y declarar ganador.

### 4.4 Implementacion

La implementacion se desarrollo en Java usando Spring Boot.

Componentes principales del sistema:

- servicio de logica del juego
- controlador para las paginas web
- controlador API para la simulacion en formato JSON
- estilos CSS para la presentacion
- JavaScript para animar la carrera por turnos

La logica principal se implemento en `JuegoService`, donde se:

- crea el mazo
- mezcla el mazo
- se crean los caballos
- se ejecutan los turnos
- se identifica el ganador

La interfaz web se implemento con:

- una pagina principal
- una pagina de simulacion
- una pagina de documentacion

Durante la implementacion tambien se mejoro el diseno visual para que la aplicacion se viera mas profesional. Se corrigio un problema inicial en el que parecia que todos los caballos se movian al mismo tiempo. La solucion fue registrar y mostrar el estado exacto de cada turno, haciendo que la interfaz representara con fidelidad el movimiento real: una carta, un caballo, un avance.

### 4.5 Pruebas

Se realizaron pruebas para verificar el funcionamiento correcto del sistema.

**Prueba 1: creacion del mazo**  
Objetivo: verificar que el mazo tuviera 52 cartas.  
Resultado esperado: 52 cartas creadas correctamente.  
Resultado obtenido: correcto.

**Prueba 2: creacion de caballos**  
Objetivo: verificar que se crearan 4 caballos y que todos iniciaran en posicion 0.  
Resultado esperado: 4 caballos en posicion inicial 0.  
Resultado obtenido: correcto.

**Prueba 3: simulacion del juego**  
Objetivo: verificar que la carrera terminara con un ganador valido.  
Resultado esperado: un caballo alcanza la meta y se declara ganador.  
Resultado obtenido: correcto.

**Prueba 4: historial de turnos**  
Objetivo: verificar que se registre cada turno con carta, caballo y posicion.  
Resultado esperado: historial completo y consistente.  
Resultado obtenido: correcto.

Adicionalmente se verifico la aplicacion desplegada en Railway, comprobando que:

- la pagina principal cargara correctamente
- la simulacion funcionara en la nube
- los caballos fueran visibles desde la salida
- la carrera mostrara ganador e historial

### 4.6 Conclusiones

El desarrollo de este proyecto permitio comprender como representar un problema real mediante un modelo de datos bien definido. Las clases creadas facilitaron la organizacion de la informacion y permitieron aplicar estructuras, operadores y restricciones de forma clara.

Se concluye que:

- el modelo de datos fue fundamental para representar cartas, caballos y turnos
- las estructuras como listas permitieron almacenar y recorrer los elementos del juego
- los operadores hicieron posible comparar palos, avanzar posiciones y validar la meta
- las restricciones garantizaron que el sistema respetara las reglas del juego
- Spring Boot fue una buena eleccion para ofrecer una solucion visual, funcional y desplegable

Como mejora futura, podria agregarse una interfaz mas interactiva, estadisticas de varias carreras o un modo manual paso a paso.

---

## 5. Relacion Con Modelo De Datos, Estructuras, Operadores Y Restricciones

### 5.1 Modelo De Datos

El modelo de datos define como se representan los elementos del problema dentro del sistema. En este proyecto se representaron mediante clases.

Ejemplos:

- una carta tiene valor y palo
- un caballo tiene nombre, palo y posicion
- un turno tiene carta revelada, caballo que avanza y posiciones actualizadas
- el resultado final contiene ganador, historial y estado general de la carrera

### 5.2 Estructuras

Se utilizaron diferentes estructuras:

- `List<Carta>` para almacenar el mazo
- `List<Caballo>` para almacenar los caballos
- `List<TurnoJuego>` para el historial de la carrera
- `Map<String, Integer>` para registrar posiciones por turno
- `enum` para representar los palos

Estas estructuras facilitaron el manejo de la informacion y permitieron recorrer, actualizar y consultar el estado del juego.

### 5.3 Operadores

En el proyecto se usaron operadores como:

- asignacion: `=`
- comparacion: `==`, `>=`
- incremento y aritmetica: `+`
- operadores logicos cuando corresponde
- estructuras de control como `if` y `for`

Estos operadores permitieron:

- comparar palos
- avanzar posiciones
- validar si un caballo llego a la meta
- recorrer el mazo y los caballos

### 5.4 Restricciones

El sistema se construyo respetando reglas fijas:

- solo existen 4 caballos
- cada caballo inicia en posicion 0
- la meta esta en la posicion 7
- solo avanza el caballo del palo correcto
- el juego termina cuando un caballo alcanza la meta

Estas restricciones garantizan que la simulacion respete el comportamiento esperado del juego.

---

## 6. Herramientas Y Tecnologias Utilizadas

- Java
- Spring Boot
- Maven Wrapper
- JUnit
- HTML
- CSS
- JavaScript
- Git y GitHub
- Railway

---

## 7. Estructura Del Proyecto

```text
src/
  main/
    java/com/example/caballos/
      controller/
      model/
      service/
    resources/
      static/
  test/
    java/com/example/caballos/
```

Archivos importantes:

- `JuegoController.java`
- `JuegoApiController.java`
- `JuegoService.java`
- clases del paquete `model`
- `app.css`
- `game.js`
- `CaballosApplicationTests.java`

---

## 8. Paso A Paso De Construccion Del Proyecto

### Paso 1. Comprension del juego

Primero se observo y analizo el juego base para entender sus reglas. Se identifico que el juego gira en torno a una carrera entre cuatro caballos, donde el movimiento depende del palo de la carta revelada.

### Paso 2. Identificacion de datos

Se definieron los datos necesarios para automatizar la actividad:

- cartas
- palos
- caballos
- posiciones
- historial de turnos
- ganador

### Paso 3. Seleccion de la tecnologia

Se comparo una solucion de consola con una solucion web. Se eligio Java con Spring Boot por ser adecuada para construir logica robusta, interfaz visible en navegador y despliegue sencillo en la nube.

### Paso 4. Diseno del modelo

Se crearon las clases principales del dominio para representar correctamente el problema: `Carta`, `Caballo`, `Palo`, `TurnoJuego` y `ResultadoJuego`.

### Paso 5. Programacion de la logica

Se desarrollo el servicio `JuegoService`, encargado de:

- construir el mazo
- mezclarlo
- crear caballos
- procesar los turnos
- registrar movimientos
- determinar el ganador

### Paso 6. Construccion de la interfaz

Se implemento una interfaz web con:

- portada de presentacion
- vista de simulacion
- documentacion del proceso

Luego se mejoro el diseno visual y se agrego una animacion turno por turno.

### Paso 7. Correccion del movimiento visual

En una version inicial parecia que todos los caballos avanzaban al mismo tiempo. Ese problema se corrigio mostrando el estado exacto de cada turno y agregando una salida visible para todos los caballos desde el inicio.

### Paso 8. Pruebas tecnicas

Se ejecutaron pruebas automaticas para validar:

- mazo de 52 cartas
- 4 caballos en posicion inicial
- simulacion con ganador
- consistencia del historial

### Paso 9. Publicacion del proyecto

El proyecto se subio a GitHub y luego se desplego en Railway para generar un enlace publico funcional.

### Paso 10. Documentacion final

Finalmente se organizo la solucion en un documento formal con metodologia, explicacion tecnica, enlaces y pasos de ejecucion.

---

## 9. Instrucciones De Configuracion Y Ejecucion

Requisitos:

- JDK 17 o superior
- Maven Wrapper incluido en el proyecto
- Navegador web

Si aparece error de `JAVA_HOME`, puede usarse temporalmente:

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Para ejecutar el proyecto localmente:

```powershell
.\mvnw.cmd spring-boot:run
```

Luego abrir en el navegador:

```text
http://localhost:8080/
```

Para ejecutar pruebas:

```powershell
.\mvnw.cmd test
```

---

## 10. Enlace Del Repositorio

Repositorio en GitHub:

`https://github.com/juanpinzon2007/Caballos`

---

## 11. Enlace De La Aplicacion Desplegada

Aplicacion desplegada en Railway:

`https://caballos-production.up.railway.app/`

Rutas principales:

- `https://caballos-production.up.railway.app/`
- `https://caballos-production.up.railway.app/jugar`
- `https://caballos-production.up.railway.app/documentacion`

---

## 12. Evidencia De Funcionamiento

La aplicacion desplegada permite comprobar:

- carga de la pagina principal
- simulacion automatica de la carrera
- visualizacion de caballos desde la salida
- avance correcto segun la carta obtenida
- historial de turnos
- declaracion del ganador
- documentacion del proyecto integrada en la aplicacion

---

## 13. Conclusiones Finales

La automatizacion del juego carrera de caballos con cartas fue una experiencia util para aplicar conceptos fundamentales de programacion y desarrollo de software. El proyecto permitio transformar un juego observado en video en una aplicacion web funcional y desplegada, cumpliendo con los requisitos planteados.

Se logro demostrar la relacion entre:

- modelo de datos
- estructuras
- operadores
- restricciones
- metodologia de solucion de problemas

El resultado final fue una aplicacion clara, funcional, visualmente mejorada y disponible en la nube para su revision.

---

## 14. Anexos Sugeridos

Se recomienda agregar al documento final:

1. captura de la pagina principal
2. captura de la simulacion en Railway
3. captura del repositorio en GitHub
4. captura de la pagina de documentacion
