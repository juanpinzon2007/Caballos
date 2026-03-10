# Caballos

Aplicacion web desarrollada con Java y Spring Boot para automatizar el juego de carrera de caballos con cartas. Cada As representa un caballo y la simulacion procesa el mazo turno por turno: se revela una carta, se identifica su palo y solo avanza el caballo correspondiente.

## Estado Del Proyecto

- Base de datos PostgreSQL integrada con JPA.
- Registro de usuarios con 1000 puntos iniciales.
- Modo multiusuario con limite de 4 grupos de 4 usuarios.
- Apuestas con valor variable y premio x5 cuando el usuario gana.
- Compra de puntos ilimitada: paquete de 1000 puntos por 10.000 COP.
- Simulacion visual de la carrera por turnos con historial completo.

## Caracteristicas Principales

- Registro/login por nombre de usuario.
- Asignacion automatica a grupos con capacidad maxima.
- Saldo inicial de 1000 puntos por usuario.
- Apuesta por caballo con monto variable.
- Pago de premio x5 sobre lo apostado si acierta.
- Compra de paquetes de puntos sin limite.
- Persistencia de usuarios, apuestas y compras en PostgreSQL.

## Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
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
- Docker (opcional, para levantar PostgreSQL rapido)
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
2. Levanta PostgreSQL (opcional con Docker):

```powershell
docker compose up -d
```

3. Configura `JAVA_HOME` si hace falta.
4. Ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Abre:

```text
http://localhost:8080/
```

## Rutas De La Aplicacion

- `/` pagina principal
- `/jugar` interfaz principal multiusuario
- `/documentacion` documentacion metodologica
- `/api/juego` resultado JSON de una carrera completa
- `/api/juego/apostar` ejecutar apuesta de usuario
- `/api/usuarios/registrar` registrar usuario (1000 puntos iniciales)
- `/api/usuarios/{usuarioId}` consultar usuario
- `/api/usuarios/{usuarioId}/comprar` comprar paquetes de puntos

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

## Despliegue En Railway

1. Entra a `https://railway.app/`.
2. Inicia sesion con GitHub.
3. Crea un proyecto nuevo con `Deploy from GitHub repo`.
4. Selecciona el repositorio `juanpinzon2007/Caballos`.
5. Railway detectara el proyecto Maven.
6. Si Railway pide comandos manuales, usa:

```text
Build Command: ./mvnw clean package -DskipTests
Start Command: java -jar target/caballos-0.0.1-SNAPSHOT.jar
```

7. Espera a que termine el build.
8. Abre la URL publica generada por Railway.

Nota:
La aplicacion ya fue configurada para usar `PORT`, que es el puerto que Railway inyecta en produccion.

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

- Persistencia real en PostgreSQL.
- Nuevo dominio de datos para usuarios, grupos, apuestas y compras.
- Reglas multiusuario: 4 grupos, 4 usuarios por grupo.
- Registro con 1000 puntos iniciales.
- Premio x5 por apuesta ganadora.
- Compra ilimitada de paquetes de puntos (1000 por 10.000 COP).
