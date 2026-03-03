# Caballos

Aplicacion web desarrollada con Java y Spring Boot para automatizar el juego de carrera de caballos con cartas. Cada As representa un caballo y la simulacion procesa el mazo turno por turno: se revela una carta, se identifica su palo y solo avanza el caballo correspondiente.

## Estado Del Proyecto

- Interfaz renovada con una presentacion mas profesional.
- Simulacion visual por turnos, sin mover todos los caballos al mismo tiempo.
- API interna para entregar el resultado completo de la carrera.
- Historial animado de cartas, movimientos y ganador.
- Documentacion integrada para sustentar la metodologia.
- Pruebas automaticas pasando.

## Caracteristicas Principales

- Pantalla de inicio con descripcion del proyecto.
- Simulacion visual de la pista en tiempo real.
- Control de velocidad de animacion.
- Boton para generar una nueva carrera.
- Historial de turnos con carta revelada, caballo que avanza y nueva posicion.
- Resumen final con ganador, turnos totales y meta.
- Pagina de documentacion con analisis, alternativas, diseno, implementacion, pruebas y conclusiones.

## Tecnologias

- Java 17+
- Spring Boot
- Maven Wrapper
- JUnit
- HTML, CSS y JavaScript nativo

## Estructura Del Proyecto

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

## Modelo De Datos

Las clases principales del proyecto son:

- `Carta`: representa el valor y el palo de una carta.
- `Caballo`: representa nombre, palo y posicion del caballo.
- `Palo`: enum con corazones, diamantes, treboles y picas.
- `TurnoJuego`: guarda el turno, la carta, el caballo que avanza, su nueva posicion y el estado de todos los caballos en ese momento.
- `ResultadoJuego`: contiene meta, lista de caballos, historial de turnos y ganador.

## Regla De Movimiento

La carrera funciona asi:

1. Se crea un mazo completo de 52 cartas.
2. El mazo se mezcla.
3. Se crean cuatro caballos, uno por cada palo.
4. En cada turno se saca una carta.
5. Solo avanza el caballo del mismo palo de la carta revelada.
6. Se registra el estado completo de la carrera.
7. Gana el primer caballo que llegue a la casilla 7.

Esto corrige el problema de una visualizacion donde parecia que todos se movian al mismo tiempo. Ahora la interfaz representa de forma exacta el movimiento real por carta.

## Requisitos

- Windows, Linux o macOS
- JDK 17 o superior
- Navegador web

## Configuracion De Java En Windows

Si Maven muestra error con `JAVA_HOME`, usa temporalmente el runtime de IntelliJ:

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Si quieres dejarlo permanente:

```powershell
setx JAVA_HOME "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\jbr"
```

Despues cierra y abre la terminal.

## Como Ejecutar El Proyecto

1. Abre una terminal en la carpeta del proyecto.
2. Configura `JAVA_HOME` si hace falta.
3. Ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

4. Abre:

```text
http://localhost:8080/
```

## Rutas De La Aplicacion

- `/` pagina principal
- `/jugar` interfaz principal de la carrera
- `/documentacion` documentacion metodologica
- `/api/juego` resultado JSON de una carrera completa

## Como Probar La Aplicacion

1. Abre `http://localhost:8080/`.
2. Revisa la pantalla principal del proyecto.
3. Entra a `http://localhost:8080/jugar`.
4. Verifica que la carrera se anime paso a paso.
5. Revisa que en cada turno solo cambie la posicion de un caballo.
6. Cambia la velocidad de animacion y vuelve a correr una nueva carrera.
7. Verifica el historial de turnos.
8. Revisa el ganador final.
9. Entra a `http://localhost:8080/documentacion`.

## Como Ejecutar Las Pruebas

```powershell
$env:JAVA_HOME='C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd test
```

Resultado esperado:

- 4 pruebas ejecutadas
- 0 fallos
- 0 errores

## Metodologia De Solucion

### Analisis

Se identificaron los elementos esenciales del juego: mazo, palos, caballos, pista, turnos y ganador.

### Alternativas

Se evaluaron dos opciones:

- aplicacion de consola
- aplicacion web con Spring Boot

Se eligio la aplicacion web porque permite visualizar mejor la logica y facilita el despliegue.

### Diseno

Se definio un modelo orientado a objetos con clases para carta, caballo, turno y resultado. Se usaron listas para almacenar el mazo, los caballos y el historial de la carrera.

### Implementacion

Se separo la solucion en:

- servicio con la logica de simulacion
- controlador HTML para las paginas
- controlador API para la simulacion en JSON
- recursos estaticos para el diseno y la animacion

### Pruebas

Se validan:

- mazo de 52 cartas
- cuatro caballos en posicion inicial cero
- ganador al llegar a la meta
- simulacion consistente

### Conclusiones

El proyecto demuestra de forma clara el uso de modelo de datos, estructuras, operadores y restricciones en una aplicacion funcional con mejor presentacion visual y una simulacion fiel al juego.

## Archivos Importantes

- `src/main/java/com/example/caballos/controller/JuegoController.java`
- `src/main/java/com/example/caballos/controller/JuegoApiController.java`
- `src/main/java/com/example/caballos/service/JuegoService.java`
- `src/main/java/com/example/caballos/model/`
- `src/main/resources/static/app.css`
- `src/main/resources/static/game.js`
- `src/test/java/com/example/caballos/CaballosApplicationTests.java`

## Cambios Recientes

- Rediseno completo de la interfaz.
- Animacion real por turnos.
- Nueva API `/api/juego`.
- Historial con estado de cada paso.
- Documentacion y guias actualizadas.
- Correccion de la percepcion de movimiento simultaneo.
