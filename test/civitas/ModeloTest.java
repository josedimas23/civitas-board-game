package civitas;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests de las reglas del modelo. Sin dependencias: cada método {@code test*}
 * se ejecuta por reflexión y falla lanzando una excepción.
 * Ejecutar con {@code ./test.sh}.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class ModeloTest {

    // ------------------------------------------------------------------ utilidades

    private static void comprueba(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void igual(float esperado, float real, String mensaje) {
        if (Math.abs(esperado - real) > 0.001f)
            throw new AssertionError(mensaje + " (esperado " + esperado + ", obtenido " + real + ")");
    }

    private static CasillaCalle calle(float compra, float edificar, float alquiler) {
        return new CasillaCalle("calle de prueba", compra, edificar, alquiler);
    }

    private static Jugador compraCalle(Jugador j, CasillaCalle c) {
        j.puedeComprarCasilla();
        j.Comprar(c);
        return j;
    }

    // ------------------------------------------------------------------ jugador y compras

    void testJugadorEmpiezaCon7500() {
        igual(7500, new Jugador("Ana").getSaldo(), "saldo inicial");
    }

    void testComprarRestaElPrecioYAsignaPropietario() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        igual(7400, ana.getSaldo(), "saldo tras comprar");
        comprueba(c.esEsteElPropietario(ana), "Ana debería ser la propietaria");
        comprueba(ana.getPropiedades().contains(c), "la calle debería estar entre sus propiedades");
    }

    void testNoSePuedeComprarSinSaldo() {
        Jugador ana = new Jugador("Ana");
        ana.setSaldo(50);
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        comprueba(!c.tienePropietario(), "la calle no debería tener dueño");
        igual(50, ana.getSaldo(), "el saldo no debería cambiar");
    }

    // ------------------------------------------------------------------ alquiler

    void testCaerEnCalleAjenaPagaAlquiler() {
        Jugador ana = new Jugador("Ana"), luis = new Jugador("Luis");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        ArrayList<Jugador> todos = new ArrayList<>(List.of(ana, luis));
        c.recibeJugador(1, todos);
        igual(7300, luis.getSaldo(), "Luis paga el alquiler base");
        igual(7600, ana.getSaldo(), "Ana cobra el alquiler");
    }

    void testElDuenoNoSePagaASiMismo() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        c.recibeJugador(0, new ArrayList<>(List.of(ana)));
        igual(7400, ana.getSaldo(), "caer en tu propia calle no cuesta nada");
    }

    void testElAlquilerCreceConCasasYHoteles() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        ana.construirCasa(0);
        ana.construirCasa(0);
        igual(200 * (1 + 2), c.getPrecioALquilerCompleto(), "alquiler con 2 casas");
    }

    // ------------------------------------------------------------------ construcción

    void testConstruirCasaCuestaElPrecioDeEdificar() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        comprueba(ana.construirCasa(0), "debería poder construir");
        igual(1, c.getNumCasas(), "número de casas");
        igual(7360, ana.getSaldo(), "saldo tras construir");
    }

    void testMaximoCuatroCasas() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        for (int i = 0; i < 4; i++) ana.construirCasa(0);
        comprueba(!ana.construirCasa(0), "la quinta casa no debería permitirse");
        igual(4, c.getNumCasas(), "número de casas");
    }

    void testHotelNecesitaCuatroCasasYLasSustituye() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        comprueba(!ana.construirHotel(0), "sin casas no se puede construir un hotel");
        for (int i = 0; i < 4; i++) ana.construirCasa(0);
        comprueba(ana.construirHotel(0), "con 4 casas sí se puede");
        igual(1, c.getNumHoteles(), "número de hoteles");
        igual(0, c.getNumCasas(), "las casas se derriban al construir el hotel");
    }

    // ------------------------------------------------------------------ tablero y salida

    void testPasarPorLaSalidaSeDetectaYSeCobra() {
        Tablero t = new Tablero();
        for (int i = 0; i < 9; i++) t.añadeCasilla(new Casilla("casilla " + i));
        comprueba(t.nuevaPosicion(8, 4) == 2, "la posición da la vuelta al tablero");
        comprueba(t.computarPasoPorSalida(), "debería detectar el paso por la salida");
        comprueba(!t.computarPasoPorSalida(), "el aviso se consume una sola vez");
        Jugador ana = new Jugador("Ana");
        ana.pasaPorSalida();
        igual(8500, ana.getSaldo(), "premio por pasar por la salida");
    }

    // ------------------------------------------------------------------ sorpresas

    void testCasillaSorpresaAplicaLaCarta() {
        MazoSorpresas mazo = new MazoSorpresas(true);
        mazo.alMazo(new SorpresaPagarCobrar("ganas 500", 500));
        CasillaSorpresa casilla = new CasillaSorpresa("Sorpresa", mazo);
        Jugador ana = new Jugador("Ana");
        casilla.recibeJugador(0, new ArrayList<>(List.of(ana)));
        igual(8000, ana.getSaldo(), "la sorpresa debería sumar 500");
    }

    void testSorpresaPorCasaHotelMultiplicaPorEdificios() {
        Jugador ana = new Jugador("Ana");
        CasillaCalle c = calle(100, 40, 200);
        compraCalle(ana, c);
        ana.construirCasa(0);
        ana.construirCasa(0);
        float antes = ana.getSaldo();
        new SorpresaPorCasaHotel("pagas 100 por edificio", -100).aplicarAJugador(0, new ArrayList<>(List.of(ana)));
        igual(antes - 200, ana.getSaldo(), "2 casas x -100");
    }

    // ------------------------------------------------------------------ partida

    void testLaPartidaAcabaConUnaBancarrota() {
        CivitasJuego juego = new CivitasJuego(new ArrayList<>(List.of("Ana", "Luis")), true);
        comprueba(!juego.finalDelJuego(), "al empezar nadie está en bancarrota");
        juego.getJugadores().get(1).setSaldo(0);
        comprueba(juego.finalDelJuego(), "con saldo 0 la partida termina");
    }

    void testElPrimerPasoDelTurnoEsAvanzar() {
        CivitasJuego juego = new CivitasJuego(new ArrayList<>(List.of("Ana", "Luis")), true);
        comprueba(juego.siguientePaso() == OperacionJuego.AVANZAR, "el turno empieza tirando el dado");
    }

    // ------------------------------------------------------------------ ejecución

    public static void main(String[] args) throws Exception {
        int ok = 0, fallos = 0;
        for (Method m : ModeloTest.class.getDeclaredMethods()) {
            if (!m.getName().startsWith("test")) continue;
            m.setAccessible(true);
            try {
                m.invoke(new ModeloTest());
                System.out.println("  ✓ " + m.getName());
                ok++;
            } catch (java.lang.reflect.InvocationTargetException e) {
                System.out.println("  ✗ " + m.getName() + ": " + e.getCause().getMessage());
                fallos++;
            }
        }
        System.out.println("\n" + ok + " tests correctos, " + fallos + " fallidos");
        if (fallos > 0) System.exit(1);
    }
}
