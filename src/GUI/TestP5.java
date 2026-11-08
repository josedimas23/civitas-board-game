
package GUI;

import java.util.*;
import civitas.*;
import controladorCivitas.*;

/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class TestP5 {
    
    public static void main(String[] args){
        
        CivitasView vista = new CivitasView();
        CapturaNombres nombres = new CapturaNombres(vista, true);
        ArrayList<String> jugadores = new ArrayList<>();
        
        jugadores = nombres.getNombres();
        
        Dado.createInstance(vista);
        
        CivitasJuego juego = new CivitasJuego(jugadores, false);
        
        Controlador controlador = new Controlador(juego, vista);
     
        vista.setCivitasJuego(juego);

        controlador.juega();
        
    }
}
