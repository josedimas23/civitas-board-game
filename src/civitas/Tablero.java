/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;

import java.util.ArrayList;

public class Tablero {
    
    private ArrayList<Casilla>  casillas = new ArrayList<>();
    private boolean porSalida;
    
    Tablero(){
        Casilla salida = new Casilla( "Salida");
        casillas.add(salida);
        porSalida=false;
    }
    
    private boolean Correcto (int numCasilla){
        if (numCasilla<casillas.size()){
            return true;
        }
        else{
            return false;
        }
    }
    
    
    public int getNumCasillas(){
        return casillas.size();
    }
    
    boolean computarPasoPorSalida(){
        boolean salida=porSalida;
        porSalida=false;
        return salida; 
    }
    
    void añadeCasilla(Casilla casilla){
       casillas.add(casilla);
    }
    
    public Casilla getCasilla(int numCasilla){
        return casillas.get(numCasilla);
    }
    
    int nuevaPosicion(int actual, int tirada){
        int nuevaPosicion;
        nuevaPosicion=actual+tirada; 
        if(nuevaPosicion >= casillas.size()){
            nuevaPosicion= nuevaPosicion % casillas.size();
            porSalida=true;
        }
        return nuevaPosicion;
    }
    
    
            
    
}
