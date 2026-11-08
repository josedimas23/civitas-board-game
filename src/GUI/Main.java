package GUI;

import civitas.CivitasJuego;
import controladorCivitas.Controlador;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la versión gráfica.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("apple.awt.application.appearance", "NSAppearanceNameDarkAqua");
        System.setProperty("apple.awt.application.name", "Civitas");

        CivitasView[] vista = new CivitasView[1];
        ArrayList<String> jugadores = new ArrayList<>();
        SwingUtilities.invokeAndWait(() -> {
            vista[0] = new CivitasView();
            jugadores.addAll(new CapturaNombres(vista[0], true).getNombres());
        });

        CivitasJuego juego = new CivitasJuego(jugadores, false);
        SwingUtilities.invokeAndWait(() -> vista[0].setCivitasJuego(juego));

        // El controlador corre en este hilo; la vista bloquea hasta que el jugador elige.
        new Controlador(juego, vista[0]).juega();
        vista[0].mostrarRanking();
    }
}
