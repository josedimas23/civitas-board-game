package civitas;
import java.util.ArrayList;

/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class CasillaSorpresa extends Casilla{

    private MazoSorpresas mazo=null;

    CasillaSorpresa(String Nombre,MazoSorpresas Mazo){
        super(Nombre);
        mazo=Mazo;
    }

    @Override
    void recibeJugador(int actual, ArrayList <Jugador> todos){

        Sorpresa sorpresa=mazo.siguiente();
        informe(actual,todos);
        sorpresa.aplicarAJugador(actual, todos);

    }
}
