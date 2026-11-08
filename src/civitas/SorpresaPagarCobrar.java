/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
