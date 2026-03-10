package com.example.caballos.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JuegoController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String inicio() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Caballos | Carrera de cartas</title>
                    <link rel="stylesheet" href="/app.css">
                </head>
                <body class="landing-body">
                    <div class="page-shell">
                        <header class="hero hero-home">
                            <div class="hero-copy">
                                <span class="eyebrow">Simulador academico en Java + Spring Boot</span>
                                <h1>Caballos</h1>
                                <p class="lead">Una carrera automatizada donde cada carta define exactamente que caballo avanza. El objetivo de la app es evidenciar modelo de datos, estructuras, operadores y restricciones dentro de una simulacion web clara y verificable.</p>
                                <div class="hero-actions">
                                    <a class="button button-primary" href="/jugar">Iniciar carrera</a>
                                    <a class="button button-secondary" href="/documentacion">Ver metodologia</a>
                                </div>
                            </div>
                            <div class="hero-panel hero-preview">
                                <div class="preview-card">
                                    <span class="mini-label">Mecanica del juego</span>
                                    <ul class="clean-list">
                                        <li>4 caballos: un As por cada palo.</li>
                                        <li>1 carta revelada por turno.</li>
                                        <li>Solo avanza el caballo del mismo palo.</li>
                                        <li>La meta esta en la posicion 7.</li>
                                    </ul>
                                </div>
                            </div>
                        </header>

                        <main class="content-grid">
                            <section class="glass-card">
                                <span class="mini-label">Modelo de datos</span>
                                <h2>Clases del problema</h2>
                                <p>La solucion se apoya en las clases <strong>Carta</strong>, <strong>Caballo</strong>, <strong>TurnoJuego</strong> y <strong>ResultadoJuego</strong>. Cada una representa un fragmento concreto del dominio.</p>
                            </section>
                            <section class="glass-card">
                                <span class="mini-label">Logica</span>
                                <h2>Movimiento por carta real</h2>
                                <p>La simulacion no mueve todos los caballos al mismo tiempo. En cada turno se toma una carta, se identifica su palo y se actualiza exclusivamente el caballo correspondiente.</p>
                            </section>
                            <section class="glass-card">
                                <span class="mini-label">Entrega</span>
                                <h2>Listo para demostrar</h2>
                                <p>La aplicacion incluye interfaz, historial completo de turnos, resumen de reglas, pruebas automatizadas y documentacion para el profesor.</p>
                            </section>
                        </main>
                    </div>
                </body>
                </html>
                """;
    }

    @GetMapping(value = "/jugar", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String jugar() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Caballos | Simulacion</title>
                    <link rel="stylesheet" href="/app.css">
                </head>
                <body class="game-body">
                    <div class="page-shell">
                        <header class="topbar">
                            <a class="brand" href="/">Caballos</a>
                            <nav class="topbar-actions">
                                <a class="button button-ghost" href="/documentacion">Documentacion</a>
                                <button class="button button-primary" id="reiniciarBtn" type="button">Apostar de nuevo</button>
                            </nav>
                        </header>

                        <main class="game-layout">
                            <section class="hero hero-game">
                                <div class="hero-copy">
                                    <span class="eyebrow">Modo multiusuario</span>
                                    <h1>Carrera de caballos con apuestas</h1>
                                    <p class="lead">Registra usuarios con 1000 puntos iniciales, apuesta un valor variable y gana 5x cuando aciertes el caballo ganador.</p>
                                </div>
                                <div class="hero-panel status-panel">
                                    <div class="metric">
                                        <span class="metric-label">Usuario</span>
                                        <strong id="usuarioNombre">Sin registro</strong>
                                    </div>
                                    <div class="metric">
                                        <span class="metric-label">Grupo</span>
                                        <strong id="usuarioGrupo">-</strong>
                                    </div>
                                    <div class="metric">
                                        <span class="metric-label">Puntos</span>
                                        <strong id="usuarioPuntos">0</strong>
                                    </div>
                                </div>
                            </section>

                            <section class="dashboard-grid">
                                <article class="glass-card race-card">
                                    <div class="section-head">
                                        <div>
                                            <span class="mini-label">Registro y apuesta</span>
                                            <h2>Control de jugador</h2>
                                        </div>
                                    </div>

                                    <div class="control-grid">
                                        <label for="nombreUsuario">Nombre de usuario</label>
                                        <div class="inline-actions">
                                            <input id="nombreUsuario" type="text" maxlength="60" placeholder="Ejemplo: AnaPerez">
                                            <button class="button button-secondary" id="registrarBtn" type="button">Registrarse</button>
                                        </div>

                                        <label for="caballoApuesta">Caballo</label>
                                        <select id="caballoApuesta">
                                            <option>As de Corazones</option>
                                            <option>As de Diamantes</option>
                                            <option>As de Treboles</option>
                                            <option>As de Picas</option>
                                        </select>

                                        <label for="puntosApuesta">Puntos a apostar</label>
                                        <input id="puntosApuesta" type="number" min="1" step="1" value="100">

                                        <label for="paquetesCompra">Comprar puntos (1000 por 10.000 COP)</label>
                                        <div class="inline-actions">
                                            <input id="paquetesCompra" type="number" min="1" step="1" value="1">
                                            <button class="button button-ghost" id="comprarBtn" type="button">Comprar</button>
                                        </div>

                                        <div class="inline-actions">
                                            <button class="button button-primary" id="apostarBtn" type="button">Iniciar apuesta</button>
                                        </div>
                                    </div>

                                    <p id="mensajeSistema" class="system-message">Registra un usuario para comenzar.</p>

                                    <div class="section-head">
                                        <div>
                                            <span class="mini-label">Animacion</span>
                                            <h2>Carrera en vivo</h2>
                                        </div>
                                        <div class="speed-control">
                                            <label for="velocidad">Velocidad</label>
                                            <select id="velocidad">
                                                <option value="1200">Lenta</option>
                                                <option value="800" selected>Media</option>
                                                <option value="450">Rapida</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div id="track" class="track-board"></div>
                                </article>

                                <article class="glass-card summary-card">
                                    <span class="mini-label">Resumen</span>
                                    <h2>Estado de la carrera</h2>
                                    <div class="summary-stack">
                                        <div class="summary-row">
                                            <span>Turno actual</span>
                                            <strong id="turnoActual">0</strong>
                                        </div>
                                        <div class="summary-row">
                                            <span>Carta activa</span>
                                            <strong id="cartaActual">Esperando...</strong>
                                        </div>
                                        <div class="summary-row">
                                            <span>Caballo en movimiento</span>
                                            <strong id="caballoActual">Esperando...</strong>
                                        </div>
                                        <div class="summary-row">
                                            <span>Meta</span>
                                            <strong id="metaTexto">7 casillas</strong>
                                        </div>
                                        <div class="summary-row">
                                            <span>Turnos totales</span>
                                            <strong id="turnosTotales">0</strong>
                                        </div>
                                        <div class="summary-row">
                                            <span>Ganador</span>
                                            <strong id="ganadorTexto">Pendiente</strong>
                                        </div>
                                    </div>
                                    <div id="winnerBadge" class="winner-badge hidden"></div>
                                </article>
                            </section>

                            <section class="dashboard-grid bottom-grid">
                                <article class="glass-card">
                                    <span class="mini-label">Ultimos movimientos</span>
                                    <h2>Historial paso a paso</h2>
                                    <div id="log" class="log-list"></div>
                                </article>

                                <article class="glass-card">
                                    <span class="mini-label">Reglas de negocio</span>
                                    <h2>Condiciones nuevas</h2>
                                    <ul class="clean-list clean-list-wide">
                                        <li>Registro de usuario con 1000 puntos iniciales.</li>
                                        <li>Plataforma: maximo 4 grupos, 4 usuarios por grupo.</li>
                                        <li>Apuesta variable por cada carrera.</li>
                                        <li>Si aciertas: premio = puntos apostados x5.</li>
                                        <li>Compra de puntos: 1000 puntos por 10.000 COP, sin limite.</li>
                                    </ul>
                                </article>
                            </section>
                        </main>
                    </div>
                    <script src="/game.js"></script>
                </body>
                </html>
                """;
    }

    @GetMapping(value = "/documentacion", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String documentacion() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Caballos | Documentacion</title>
                    <link rel="stylesheet" href="/app.css">
                </head>
                <body class="docs-body">
                    <div class="page-shell">
                        <header class="topbar">
                            <a class="brand" href="/">Caballos</a>
                            <nav class="topbar-actions">
                                <a class="button button-ghost" href="/jugar">Abrir simulacion</a>
                            </nav>
                        </header>

                        <main class="docs-layout">
                            <section class="hero hero-docs">
                                <div class="hero-copy">
                                    <span class="eyebrow">Metodologia de solucion de problemas</span>
                                    <h1>Documentacion del proyecto</h1>
                                    <p class="lead">La aplicacion fue construida para demostrar comprension de modelo de datos, estructuras, operadores y restricciones a traves de una solucion automatizada del juego de carrera de caballos con cartas.</p>
                                </div>
                            </section>

                            <section class="docs-grid">
                                <article class="glass-card">
                                    <span class="mini-label">Analisis</span>
                                    <h2>Comprension del problema</h2>
                                    <p>Se identificaron los datos esenciales: mazo de cartas, caballos asociados a los Ases, pista de 7 posiciones, turnos de juego e identificacion del ganador.</p>
                                </article>
                                <article class="glass-card">
                                    <span class="mini-label">Alternativas</span>
                                    <h2>Opciones consideradas</h2>
                                    <p>Se comparo una aplicacion de consola frente a una aplicacion web. Se eligio Spring Boot por su capacidad de presentar la simulacion de forma visual, accesible y desplegable.</p>
                                </article>
                                <article class="glass-card">
                                    <span class="mini-label">Diseno</span>
                                    <h2>Modelo y estructuras</h2>
                                    <p>El dominio se organizo con clases dedicadas para carta, caballo, turno y resultado. Se usaron listas para almacenar el mazo, los caballos y el historial de movimientos.</p>
                                </article>
                                <article class="glass-card">
                                    <span class="mini-label">Implementacion</span>
                                    <h2>Construccion de la solucion</h2>
                                    <p>La simulacion mezcla un mazo completo y procesa las cartas una a una. La interfaz web consume el resultado y representa visualmente la carrera por turnos.</p>
                                </article>
                                <article class="glass-card">
                                    <span class="mini-label">Pruebas</span>
                                    <h2>Validacion tecnica</h2>
                                    <p>Se validan el mazo de 52 cartas, la creacion de 4 caballos en posicion cero y la obtencion de un ganador cuando se alcanza la meta.</p>
                                </article>
                                <article class="glass-card">
                                    <span class="mini-label">Conclusiones</span>
                                    <h2>Resultado del trabajo</h2>
                                    <p>El proyecto demuestra como un modelo de datos claro permite traducir reglas reales del problema a una aplicacion funcional, verificable y presentable.</p>
                                </article>
                            </section>
                        </main>
                    </div>
                </body>
                </html>
                """;
    }
}
