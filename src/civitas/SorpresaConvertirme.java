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
public class SorpresaConvertirme extends Sorpresa{
    
    SorpresaConvertirme(String text,int Val){
        super(text,Val);
    }

    public void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        informe(actual, todos);
        todos.set(actual, todos.get(actual).convertir());
    }

}
