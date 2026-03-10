(function () {
    const horseStyles = {
        "As de Corazones": { color: "#c23861", badge: "?" },
        "As de Diamantes": { color: "#d97706", badge: "?" },
        "As de Treboles": { color: "#2b7a4b", badge: "?" },
        "As de Picas": { color: "#164863", badge: "?" }
    };

    const state = {
        user: null,
        resultado: null,
        currentTurn: 0,
        timerId: null,
        lastBet: null
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
        reiniciarBtn: document.getElementById("reiniciarBtn"),
        registrarBtn: document.getElementById("registrarBtn"),
        apostarBtn: document.getElementById("apostarBtn"),
        comprarBtn: document.getElementById("comprarBtn"),
        nombreUsuario: document.getElementById("nombreUsuario"),
        caballoApuesta: document.getElementById("caballoApuesta"),
        puntosApuesta: document.getElementById("puntosApuesta"),
        paquetesCompra: document.getElementById("paquetesCompra"),
        usuarioNombre: document.getElementById("usuarioNombre"),
        usuarioGrupo: document.getElementById("usuarioGrupo"),
        usuarioPuntos: document.getElementById("usuarioPuntos"),
        mensajeSistema: document.getElementById("mensajeSistema")
    };

    if (!refs.track) {
        return;
    }

    refs.reiniciarBtn.addEventListener("click", repetirApuesta);
    refs.registrarBtn.addEventListener("click", registrarUsuario);
    refs.apostarBtn.addEventListener("click", apostarDesdeFormulario);
    refs.comprarBtn.addEventListener("click", comprarPuntos);
    refs.velocidad.addEventListener("change", () => {
        if (state.resultado) {
            restartAnimation();
        }
    });

    setDefaultRaceState();
    restaurarUsuario();

    async function restaurarUsuario() {
        const userId = Number(localStorage.getItem("caballos.userId"));
        if (!userId) {
            setMessage("Registra un usuario para comenzar.");
            return;
        }

        try {
            const usuario = await apiRequest(`/api/usuarios/${userId}`);
            aplicarUsuario(usuario);
            setMessage("Usuario recuperado. Ya puedes apostar.", "ok");
        } catch (error) {
            localStorage.removeItem("caballos.userId");
            setMessage(error.message, "error");
        }
    }

    async function registrarUsuario() {
        const nombre = refs.nombreUsuario.value.trim();
        if (!nombre) {
            setMessage("Escribe un nombre para registrarte.", "error");
            return;
        }

        try {
            const usuario = await apiRequest("/api/usuarios/registrar", {
                method: "POST",
                body: JSON.stringify({ nombre })
            });

            aplicarUsuario(usuario);
            refs.nombreUsuario.value = "";
            setMessage(`Registro exitoso. Grupo ${usuario.grupo} con ${usuario.puntos} puntos iniciales.`, "ok");
        } catch (error) {
            setMessage(error.message, "error");
        }
    }

    async function comprarPuntos() {
        if (!state.user) {
            setMessage("Debes registrarte antes de comprar puntos.", "error");
            return;
        }

        const paquetes = Number(refs.paquetesCompra.value);
        if (!paquetes || paquetes < 1) {
            setMessage("Indica una cantidad valida de paquetes.", "error");
            return;
        }

        try {
            const compra = await apiRequest(`/api/usuarios/${state.user.id}/comprar`, {
                method: "POST",
                body: JSON.stringify({ paquetes })
            });

            state.user.puntos = compra.saldoActual;
            renderUserPanel();
            setMessage(`Compra aprobada: +${compra.puntosRecibidos} puntos por ${formatPesos(compra.costoPesos)}.`, "ok");
        } catch (error) {
            setMessage(error.message, "error");
        }
    }

    async function apostarDesdeFormulario() {
        const caballo = refs.caballoApuesta.value;
        const puntos = Number(refs.puntosApuesta.value);
        await ejecutarApuesta(caballo, puntos);
    }

    async function repetirApuesta() {
        if (!state.lastBet) {
            setMessage("Primero realiza una apuesta.", "error");
            return;
        }

        await ejecutarApuesta(state.lastBet.caballo, state.lastBet.puntos);
    }

    async function ejecutarApuesta(caballo, puntos) {
        if (!state.user) {
            setMessage("Debes registrarte antes de apostar.", "error");
            return;
        }

        if (!puntos || puntos < 1) {
            setMessage("El valor apostado debe ser mayor a 0.", "error");
            return;
        }

        stopAnimation();
        setLoadingState();

        try {
            const data = await apiRequest("/api/juego/apostar", {
                method: "POST",
                body: JSON.stringify({
                    usuarioId: state.user.id,
                    caballo,
                    puntosApostados: puntos
                })
            });

            state.resultado = data.resultado;
            state.currentTurn = 0;
            state.lastBet = { caballo, puntos };
            state.user.puntos = data.saldoActual;

            renderUserPanel();
            refs.metaTexto.textContent = `${data.resultado.meta} casillas`;
            refs.turnosTotales.textContent = String(data.resultado.turnos.length);
            refs.ganadorTexto.textContent = data.resultado.ganador ? data.resultado.ganador.nombre : "Sin ganador";
            refs.winnerBadge.classList.add("hidden");

            renderInitialTrack(data.resultado);
            renderLog(data.resultado.turnos);
            animateTurns();
            setMessage(data.mensaje + ` Saldo actual: ${data.saldoActual} puntos.`, data.gano ? "ok" : "");
        } catch (error) {
            setDefaultRaceState();
            setMessage(error.message, "error");
        }
    }

    function aplicarUsuario(usuario) {
        state.user = usuario;
        localStorage.setItem("caballos.userId", String(usuario.id));
        renderUserPanel();
    }

    function renderUserPanel() {
        if (!state.user) {
            refs.usuarioNombre.textContent = "Sin registro";
            refs.usuarioGrupo.textContent = "-";
            refs.usuarioPuntos.textContent = "0";
            return;
        }

        refs.usuarioNombre.textContent = state.user.nombre;
        refs.usuarioGrupo.textContent = String(state.user.grupo);
        refs.usuarioPuntos.textContent = String(state.user.puntos);
    }

    function setDefaultRaceState() {
        refs.track.innerHTML = '<div class="glass-card">Aun no hay una carrera activa.</div>';
        refs.log.innerHTML = "";
        refs.turnoActual.textContent = "0";
        refs.cartaActual.textContent = "Esperando...";
        refs.caballoActual.textContent = "Esperando...";
        refs.ganadorTexto.textContent = "Pendiente";
        refs.turnosTotales.textContent = "0";
        refs.metaTexto.textContent = "7 casillas";
        refs.winnerBadge.classList.add("hidden");
        refs.winnerBadge.textContent = "";
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
        refs.track.innerHTML = '<div class="glass-card">Procesando apuesta y preparando carrera...</div>';
        refs.log.innerHTML = "";
        refs.turnoActual.textContent = "0";
        refs.cartaActual.textContent = "Barajando...";
        refs.caballoActual.textContent = "Pendiente";
        refs.ganadorTexto.textContent = "Pendiente";
    }

    function renderInitialTrack(resultado) {
        refs.track.innerHTML = resultado.caballos.map((caballo) => {
            const style = horseStyles[caballo.nombre];
            const cells = [
                '<div class="lane-cell start" data-step="S"><div class="token-slot" data-horse="' +
                caballo.nombre +
                '" data-step="0"></div></div>'
            ];

            for (let step = 1; step <= resultado.meta; step += 1) {
                cells.push(
                    '<div class="lane-cell ' +
                    (step === resultado.meta ? "finish" : "") +
                    '" data-step="' +
                    step +
                    '"><div class="token-slot" data-horse="' +
                    caballo.nombre +
                    '" data-step="' +
                    step +
                    '"></div></div>'
                );
            }

            return (
                '<article class="track-row" id="' +
                slugify(caballo.nombre) +
                '"><div class="track-header"><div class="horse-name"><span class="horse-badge" style="background:' +
                style.color +
                '">' +
                style.badge +
                '</span><span>' +
                caballo.nombre +
                '</span></div><span class="horse-progress-text" id="' +
                slugify(caballo.nombre) +
                '-progress">Posicion 0 de ' +
                resultado.meta +
                '</span></div><div class="lane">' +
                cells.join("") +
                "</div></article>"
            );
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

    function setMessage(text, type) {
        refs.mensajeSistema.textContent = text;
        refs.mensajeSistema.classList.remove("ok", "error");
        if (type === "ok") {
            refs.mensajeSistema.classList.add("ok");
        }
        if (type === "error") {
            refs.mensajeSistema.classList.add("error");
        }
    }

    async function apiRequest(url, options = {}) {
        const response = await fetch(url, {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            ...options
        });

        const payload = await response.json();
        if (!response.ok) {
            throw new Error(payload.error || "Error de servidor");
        }

        return payload;
    }

    function formatPesos(valor) {
        return new Intl.NumberFormat("es-CO", {
            style: "currency",
            currency: "COP",
            maximumFractionDigits: 0
        }).format(valor);
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
