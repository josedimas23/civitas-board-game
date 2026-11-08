package civitas;
import java.util.ArrayList;

/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class SorpresaConvertirme extends Sorpresa{
    
    SorpresaConvertirme(String text,int Val){
        super(text,Val);
    }

    public void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        informe(actual, todos);
        todos.set(actual, todos.get(actual).convertir());
    }

}
