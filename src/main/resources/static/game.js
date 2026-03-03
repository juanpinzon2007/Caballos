(function () {
    const horseStyles = {
        "As de Corazones": { color: "#c23861", badge: "♥" },
        "As de Diamantes": { color: "#d97706", badge: "♦" },
        "As de Treboles": { color: "#2b7a4b", badge: "♣" },
        "As de Picas": { color: "#164863", badge: "♠" }
    };

    const state = {
        resultado: null,
        currentTurn: 0,
        timerId: null
    };

    const refs = {
        track: document.getElementById("track"),
        log: document.getElementById("log"),
        turnoActual: document.getElementById("turnoActual"),
        cartaActual: document.getElementById("cartaActual"),
        caballoActual: document.getElementById("caballoActual"),
        ganadorTexto: document.getElementById("ganadorTexto"),
        turnosTotales: document.getElementById("turnosTotales"),
        metaTexto: document.getElementById("metaTexto"),
        winnerBadge: document.getElementById("winnerBadge"),
        velocidad: document.getElementById("velocidad"),
        reiniciarBtn: document.getElementById("reiniciarBtn")
    };

    if (!refs.track) {
        return;
    }

    refs.reiniciarBtn.addEventListener("click", loadRace);
    refs.velocidad.addEventListener("change", () => {
        if (state.resultado) {
            restartAnimation();
        }
    });

    loadRace();

    async function loadRace() {
        stopAnimation();
        setLoadingState();

        const response = await fetch("/api/juego", { headers: { Accept: "application/json" } });
        const resultado = await response.json();

        state.resultado = resultado;
        state.currentTurn = 0;

        refs.metaTexto.textContent = `${resultado.meta} casillas`;
        refs.turnosTotales.textContent = String(resultado.turnos.length);
        refs.ganadorTexto.textContent = resultado.ganador ? resultado.ganador.nombre : "Sin ganador";
        refs.winnerBadge.classList.add("hidden");

        renderInitialTrack(resultado);
        renderLog(resultado.turnos);
        animateTurns();
    }

    function restartAnimation() {
        stopAnimation();
        state.currentTurn = 0;
        renderInitialTrack(state.resultado);
        renderLog(state.resultado.turnos);
        animateTurns();
    }

    function stopAnimation() {
        if (state.timerId) {
            clearTimeout(state.timerId);
            state.timerId = null;
        }
    }

    function setLoadingState() {
        refs.track.innerHTML = "<div class=\"glass-card\">Preparando nueva carrera...</div>";
        refs.log.innerHTML = "";
        refs.turnoActual.textContent = "0";
        refs.cartaActual.textContent = "Barajando...";
        refs.caballoActual.textContent = "Pendiente";
        refs.ganadorTexto.textContent = "Pendiente";
    }

    function renderInitialTrack(resultado) {
        refs.track.innerHTML = resultado.caballos.map((caballo) => {
            const style = horseStyles[caballo.nombre];
            const cells = [`
                <div class="lane-cell start" data-step="S">
                    <div class="token-slot" data-horse="${caballo.nombre}" data-step="0"></div>
                </div>
            `];

            for (let step = 1; step <= resultado.meta; step += 1) {
                cells.push(`
                    <div class="lane-cell ${step === resultado.meta ? "finish" : ""}" data-step="${step}">
                        <div class="token-slot" data-horse="${caballo.nombre}" data-step="${step}"></div>
                    </div>
                `);
            }

            return `
                <article class="track-row" id="${slugify(caballo.nombre)}">
                    <div class="track-header">
                        <div class="horse-name">
                            <span class="horse-badge" style="background:${style.color}">${style.badge}</span>
                            <span>${caballo.nombre}</span>
                        </div>
                        <span class="horse-progress-text" id="${slugify(caballo.nombre)}-progress">Posicion 0 de ${resultado.meta}</span>
                    </div>
                    <div class="lane">${cells.join("")}</div>
                </article>
            `;
        }).join("");

        resultado.caballos.forEach((caballo) => {
            renderHorsePosition(caballo.nombre, 0, resultado.meta, false);
        });
    }

    function renderLog(turnos) {
        refs.log.innerHTML = turnos.map((turno) => `
            <article class="log-item" id="turno-${turno.numero}">
                <strong>Turno ${turno.numero}</strong>
                <div class="log-meta">
                    <span>Carta: ${turno.carta.etiqueta}</span>
                    <span>Avanza: ${turno.caballoQueAvanza}</span>
                    <span>Nueva posicion: ${turno.nuevaPosicion}</span>
                </div>
            </article>
        `).join("");
    }

    function animateTurns() {
        if (!state.resultado || state.currentTurn >= state.resultado.turnos.length) {
            finishRace();
            return;
        }

        const turno = state.resultado.turnos[state.currentTurn];
        applyTurn(turno, state.resultado.meta);
        highlightLog(turno.numero);
        state.currentTurn += 1;
        state.timerId = setTimeout(animateTurns, Number(refs.velocidad.value));
    }

    function applyTurn(turno, meta) {
        refs.turnoActual.textContent = String(turno.numero);
        refs.cartaActual.textContent = turno.carta.etiqueta;
        refs.caballoActual.textContent = turno.caballoQueAvanza;

        Object.entries(turno.posiciones).forEach(([nombre, posicion]) => {
            renderHorsePosition(nombre, posicion, meta, nombre === turno.caballoQueAvanza);
        });
    }

    function renderHorsePosition(nombre, posicion, meta, moving) {
        for (let step = 0; step <= meta; step += 1) {
            const slot = document.querySelector(`[data-horse="${cssEscape(nombre)}"][data-step="${step}"]`);
            if (slot) {
                slot.innerHTML = "";
            }
        }

        const visibleStep = posicion <= 0 ? 0 : Math.min(posicion, meta);
        const slot = document.querySelector(`[data-horse="${cssEscape(nombre)}"][data-step="${visibleStep}"]`);
        if (!slot) {
            return;
        }

        const style = horseStyles[nombre];
        slot.innerHTML = `
            <div class="horse-token ${moving ? "moving" : ""}" style="background:${style.color}">
                ${style.badge}
            </div>
        `;

        updateProgressText(nombre, posicion, meta);
    }

    function updateProgressText(nombre, posicion, meta) {
        const text = document.getElementById(`${slugify(nombre)}-progress`);
        if (text) {
            text.textContent = `Posicion ${posicion} de ${meta}`;
        }
    }

    function highlightLog(numero) {
        document.querySelectorAll(".log-item.active").forEach((item) => item.classList.remove("active"));
        const current = document.getElementById(`turno-${numero}`);
        if (current) {
            current.classList.add("active");
            current.scrollIntoView({ block: "nearest", behavior: "smooth" });
        }
    }

    function finishRace() {
        if (!state.resultado || !state.resultado.ganador) {
            return;
        }

        refs.caballoActual.textContent = state.resultado.ganador.nombre;
        refs.ganadorTexto.textContent = state.resultado.ganador.nombre;
        refs.winnerBadge.classList.remove("hidden");
        refs.winnerBadge.textContent = `Ganador oficial: ${state.resultado.ganador.nombre} en ${state.resultado.turnos.length} turnos.`;
    }

    function slugify(text) {
        return text.toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/[^a-z0-9]+/g, "-")
            .replace(/^-|-$/g, "");
    }

    function cssEscape(text) {
        return text.replace(/"/g, '\\"');
    }
})();
