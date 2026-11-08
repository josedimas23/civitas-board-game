/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;

import java.util.ArrayList;

public abstract class Sorpresa {
    
    private String texto;
    private int valor;
    private MazoSorpresas mazo;
    

    public int getValor() {
        return valor;
    }
    

    
    public void informe(int actual, ArrayList<Jugador> todos){
        
        Diario.getInstance().ocurreEvento("\nSe le esta aplicando una sorpresa al jugador "
                + todos.get(actual).getNombre() + this.toString());
        
    }
    
    Sorpresa(String tex,int Valor){
        texto = tex;
        valor=Valor;
    }
    
    public String toString(){
        
        return "\n" + texto;
    }
    
    public abstract void aplicarAJugador(int actual, ArrayList<Jugador> todos);
    
}
