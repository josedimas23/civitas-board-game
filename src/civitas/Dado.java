/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;

//import java.util.ArrayList;
import java.util.Random;

class Dado {  //vsibilidad de paquete
    
    private static final Dado instance = new Dado();
    
    private Random random = new Random();
    
    private int ultimoResultado;
    
    private boolean debug;
    
    private Dado(){
        
        //el valor de random ya esta dado aleatoriamente
        debug = false;
        ultimoResultado = 1;
    }
    
    static Dado getInstance(){ //vsibilidad de paquete
        
        return instance;
    }
    
    int tirar(){
        
        if(debug){
            
            ultimoResultado = 1;
        }else{
            
            ultimoResultado = random.nextInt(6)+1;
        }
        
        return ultimoResultado;
    }
    
    int quienEmpieza (int n){
        
        return random.nextInt(n);
    }
    
    void setDebug (boolean d){
        
        debug = d;
    }
    
    int getUltimoResultado(){
        
        return ultimoResultado;
    }
    
}
