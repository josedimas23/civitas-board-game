/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;
import java.util.ArrayList;

public class MazoSorpresas {
    private ArrayList<Sorpresa> Sorpresa;
    private boolean barajada;
    private int usadas;
    private boolean debug; //Si esta activado permite que siempre obtengamos las sorpresas en el mmismo orden 
    
    
    private void init(){
        Sorpresa = new ArrayList<>();
        barajada = false;
        usadas=0;
    }
    
    int getUsadas(){
        
        return usadas;
    }
    
    MazoSorpresas (boolean deb){
        debug = deb; 
        init();
        if(debug){
         //se informa a traves del diario 
        }
    }
    
    MazoSorpresas(){
        debug=false;
        init();
    
    }
    
    void alMazo(Sorpresa s){
        if(!barajada){
         Sorpresa.add(s);
        }
    }
    
    Sorpresa siguiente(){
        if(!barajada || usadas == Sorpresa.size()){
            if(!debug){
                usadas=0;
                barajada=true;
            }
        }
        
        usadas++;
        
        Sorpresa pos1 = Sorpresa.get(0);
        Sorpresa.remove(0);
        Sorpresa.add(pos1);
        return pos1;
    }
}
