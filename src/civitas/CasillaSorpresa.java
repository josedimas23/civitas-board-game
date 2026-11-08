/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package civitas;
import java.util.ArrayList;

/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class CasillaSorpresa extends Casilla{
    
    private MazoSorpresas mazo=null;
    private Sorpresa sorpresa=null;
    
    CasillaSorpresa(String Nombre,MazoSorpresas Mazo){
        super(Nombre);
        mazo=Mazo;
    }
    
    private void recibeJugador_sorpresa(int actual, ArrayList <Jugador> todos){
        
        Sorpresa sorpresa=mazo.siguiente();
        informe(actual,todos);
        sorpresa.aplicarAJugador(actual, todos);
    
    }
}
