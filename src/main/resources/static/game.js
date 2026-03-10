(function () {
    const horseStyles = {
        "As de Corazones": { color: "#c23861", badge: "\u2665" },
        "As de Diamantes": { color: "#d97706", badge: "\u2666" },
        "As de Treboles": { color: "#2b7a4b", badge: "\u2663" },
        "As de Picas": { color: "#164863", badge: "\u2660" }
    };

    const state = {
        token: null,
        user: null,
        resultado: null,
        currentTurn: 0,
        timerId: null,
        lastBet: null,
        busy: false
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
        logoutBtn: document.getElementById("logoutBtn"),
        registrarBtn: document.getElementById("registrarBtn"),
        loginBtn: document.getElementById("loginBtn"),
        apostarBtn: document.getElementById("apostarBtn"),
        comprarBtn: document.getElementById("comprarBtn"),
        registroNombre: document.getElementById("registroNombre"),
        registroPassword: document.getElementById("registroPassword"),
        loginNombre: document.getElementById("loginNombre"),
        loginPassword: document.getElementById("loginPassword"),
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
    refs.logoutBtn.addEventListener("click", logout);
    refs.registrarBtn.addEventListener("click", registrarUsuario);
    refs.loginBtn.addEventListener("click", loginUsuario);
    refs.apostarBtn.addEventListener("click", apostarDesdeFormulario);
    refs.comprarBtn.addEventListener("click", comprarPuntos);
    refs.velocidad.addEventListener("change", () => {
        if (state.resultado) {
            restartAnimation();
        }
    });

    setDefaultRaceState();
    renderUserPanel();
    restoreSession();

    async function restoreSession() {
        const token = localStorage.getItem("caballos.token");
        if (!token) {
            setMessage("Registra o inicia sesion para comenzar.");
            return;
        }

        state.token = token;
        try {
            const user = await apiRequest("/api/auth/me", { auth: true });
            applyUser(user);
            setMessage("Sesion recuperada correctamente.", "ok");
        } catch (error) {
            clearSession();
            setMessage("Tu sesion expiro. Inicia sesion nuevamente.", "error");
        }
    }

    async function registrarUsuario() {
        if (state.busy) {
            return;
        }

        const nombre = refs.registroNombre.value.trim();
        const password = refs.registroPassword.value;

        if (!nombre || !password) {
            setMessage("Debes ingresar nombre y contrasena para registrar.", "error");
            return;
        }

        if (password.length < 6) {
            setMessage("La contrasena debe tener minimo 6 caracteres.", "error");
            return;
        }

        setBusy(true);
        try {
            const auth = await apiRequest("/api/auth/registro", {
                method: "POST",
                body: JSON.stringify({ nombre, password })
            });

            applyAuth(auth);
            refs.registroNombre.value = "";
            refs.registroPassword.value = "";
            setMessage(`Registro exitoso. Grupo ${auth.usuario.grupo} con 1000 puntos iniciales.`, "ok");
        } catch (error) {
            setMessage(error.message, "error");
        } finally {
            setBusy(false);
        }
    }

    async function loginUsuario() {
        if (state.busy) {
            return;
        }

        const nombre = refs.loginNombre.value.trim();
        const password = refs.loginPassword.value;

        if (!nombre || !password) {
            setMessage("Debes ingresar nombre y contrasena para iniciar sesion.", "error");
            return;
        }

        setBusy(true);
        try {
            const auth = await apiRequest("/api/auth/login", {
                method: "POST",
                body: JSON.stringify({ nombre, password })
            });

            applyAuth(auth);
            refs.loginPassword.value = "";
            setMessage("Inicio de sesion exitoso.", "ok");
        } catch (error) {
            setMessage(error.message, "error");
        } finally {
            setBusy(false);
        }
    }

    async function logout() {
        if (!state.token || state.busy) {
            clearSession();
            return;
        }

        setBusy(true);
        try {
            await apiRequest("/api/auth/logout", { method: "POST", auth: true });
        } catch (_) {
            // no-op: logout local should still happen even if backend fails
        } finally {
            clearSession();
            setBusy(false);
        }

        setMessage("Sesion cerrada.", "ok");
    }

    async function comprarPuntos() {
        if (!state.user) {
            setMessage("Debes iniciar sesion antes de comprar puntos.", "error");
            return;
        }

        const paquetes = Number(refs.paquetesCompra.value);
        if (!paquetes || paquetes < 1) {
            setMessage("Indica una cantidad valida de paquetes.", "error");
            return;
        }

        if (state.busy) {
            return;
        }

        setBusy(true);
        try {
            const compra = await apiRequest("/api/usuarios/me/comprar", {
                method: "POST",
                auth: true,
                body: JSON.stringify({ paquetes })
            });

            state.user.puntos = compra.saldoActual;
            renderUserPanel();
            setMessage(`Compra aprobada: +${compra.puntosRecibidos} puntos por ${formatPesos(compra.costoPesos)}.`, "ok");
        } catch (error) {
            handleAuthErrorIfNeeded(error);
            setMessage(error.message, "error");
        } finally {
            setBusy(false);
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
            setMessage("Debes iniciar sesion antes de apostar.", "error");
            return;
        }

        if (!puntos || puntos < 10) {
            setMessage("La apuesta minima es de 10 puntos.", "error");
            return;
        }

        if (state.busy) {
            return;
        }

        stopAnimation();
        setLoadingState();
        setBusy(true);

        try {
            const data = await apiRequest("/api/juego/apostar", {
                method: "POST",
                auth: true,
                body: JSON.stringify({ caballo, puntosApostados: puntos })
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
            handleAuthErrorIfNeeded(error);
            setDefaultRaceState();
            setMessage(error.message, "error");
        } finally {
            setBusy(false);
        }
    }

    function applyAuth(auth) {
        state.token = auth.token;
        localStorage.setItem("caballos.token", state.token);
        applyUser(auth.usuario);
    }

    function applyUser(user) {
        state.user = user;
        renderUserPanel();
    }

    function clearSession() {
        stopAnimation();
        state.token = null;
        state.user = null;
        state.resultado = null;
        state.lastBet = null;
        localStorage.removeItem("caballos.token");
        renderUserPanel();
        setDefaultRaceState();
    }

    function handleAuthErrorIfNeeded(error) {
        if (error.status === 401) {
            clearSession();
        }
    }

    function renderUserPanel() {
        if (!state.user) {
            refs.usuarioNombre.textContent = "Sin sesion";
            refs.usuarioGrupo.textContent = "-";
            refs.usuarioPuntos.textContent = "0";
            refs.logoutBtn.classList.add("hidden");
            return;
        }

        refs.usuarioNombre.textContent = state.user.nombre;
        refs.usuarioGrupo.textContent = String(state.user.grupo);
        refs.usuarioPuntos.textContent = String(state.user.puntos);
        refs.logoutBtn.classList.remove("hidden");
    }

    function setBusy(isBusy) {
        state.busy = isBusy;
        const disabled = isBusy;
        refs.registrarBtn.disabled = disabled;
        refs.loginBtn.disabled = disabled;
        refs.apostarBtn.disabled = disabled;
        refs.comprarBtn.disabled = disabled;
        refs.reiniciarBtn.disabled = disabled;
        refs.logoutBtn.disabled = disabled;
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
        const headers = {
            "Content-Type": "application/json",
            Accept: "application/json",
            ...(options.headers || {})
        };

        if (options.auth && state.token) {
            headers.Authorization = `Bearer ${state.token}`;
        }

        const response = await fetch(url, {
            method: options.method || "GET",
            headers,
            body: options.body
        });

        let payload = {};
        try {
            payload = await response.json();
        } catch (_) {
            payload = {};
        }

        if (!response.ok) {
            const error = new Error(payload.error || "Error de servidor");
            error.status = response.status;
            throw error;
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
