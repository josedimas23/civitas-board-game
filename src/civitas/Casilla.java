/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;
import java.util.ArrayList;

public class Casilla {
    
    private String nombre;
    
    public Casilla(String Nombre){
        nombre=Nombre;
    }
    
    public String getNombre(){
        
       return nombre;
    }
    
    void recibeJugador(int iactual, ArrayList <Jugador> todos){
        
        informe(iactual,todos);
   
    }
    
    void informe(int actual, ArrayList<Jugador> todos){
        
        Diario.getInstance().ocurreEvento("\nHa caido en la casilla " 
                + toString() +" el jugador " + todos.get(actual).getNombre());

    }
    
    public String toString(){
      return "Casilla: " + nombre;
    }
}
