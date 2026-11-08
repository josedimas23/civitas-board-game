package civitas;
import java.util.ArrayList;

/**
 *
 *  @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class SorpresaPagarCobrar extends Sorpresa{
    
    private int valor;
    
    SorpresaPagarCobrar(String Tex,int Valor){
        super(Tex,Valor);
       
    }
    
    
    
    public void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
        informe(actual, todos);
        todos.get(actual).modificarSaldo(valor);
    
    }
}
