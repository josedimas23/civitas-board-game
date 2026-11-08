/**
 * Punto de entrada de la versión en modo texto (consola).
 * Uso: java -cp build/classes JuegoTexto [nombre1 nombre2 ...]
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
import civitas.CivitasJuego;
import GUI.VistaTextual;
import controladorCivitas.Controlador;
import java.util.ArrayList;
import java.util.List;

public class JuegoTexto {

    public static void main(String[] args) {
        ArrayList<String> jugadores = new ArrayList<>(
                args.length >= 2 ? List.of(args) : List.of("Jugador 1", "Jugador 2"));

        CivitasJuego juego = new CivitasJuego(jugadores, false);
        VistaTextual vista = new VistaTextual(juego);
        new Controlador(juego, vista).juega();
    }
}
