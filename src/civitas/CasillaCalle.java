package civitas;
import java.util.ArrayList;
/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class CasillaCalle extends Casilla {
    
   private final float precioCompra;
   private float precioEdificar;
   private float precioBaseAlquiler;
   private int numCasas;
   private int numHoteles;
   private static float FACTORALQUILERCALLE=1.0f;
   private static float FACTORALQUILERCASA=1.0f;
   private static float FACTORALQUILERHOTEL=4.0f;
   
   private Jugador propietario;

   public CasillaCalle (String unNombre,float unPrecioCompra, float unPrecioEdificar, float unPrecioAlquilerBase){
        
       super(unNombre);      
        precioCompra=unPrecioCompra;
        precioEdificar=unPrecioEdificar;
        precioBaseAlquiler=unPrecioAlquilerBase;
        numCasas=0;
        numHoteles=0;
        propietario=null;
    }
    
    public int getNumCasas(){
        return numCasas;
    }

    public int getNumHoteles(){
        return numHoteles;
    }

    float getPrecioBaseAlquiler(){
        return precioBaseAlquiler;
    }
    public float getPrecioCompra(){
        return precioCompra;
    }

    public float getPrecioEdificar(){
        return precioEdificar;
    }
    
   public boolean tienePropietario(){
        return (propietario != null);
    }
    
   boolean comprar(Jugador jugador){
        
        return true;
    }
    
    boolean construirCasa(Jugador jugador){
        
        propietario.paga(precioEdificar);
        numCasas++;
    
        return true;
    }
    
    boolean construirHotel(Jugador jugador){
        propietario.paga(precioEdificar);
        numHoteles++;
        return true;
    }
    
    void setPropietario(Jugador jugador){
        
        propietario=jugador;        
    }
    
    
    boolean derruirCasas(int n, Jugador jugador){
        boolean derr = false;
        
        
        if(esEsteElPropietario(jugador) && numCasas >= n){
            numCasas = numCasas - n;
            derr = true;
        }
        
        return derr;
    }
    
    public boolean esEsteElPropietario(Jugador jugador){
        return (propietario == jugador);
    }
    
    public float getPrecioALquilerCompleto(){
        
        return (precioBaseAlquiler * FACTORALQUILERCALLE) * (1+ FACTORALQUILERCASA * numCasas + numHoteles * FACTORALQUILERHOTEL);
        
    }
    
    void recibeJugador(int actual, ArrayList <Jugador> todos){
        
        informe(actual,todos);
        
        Jugador jugador=todos.get(actual);
        
        if(!tienePropietario()){
            jugador.puedeComprarCasilla();
        }
        else{
            tramitarAlquiler(jugador);            
        }
    }
    
    public String toString(){
     
        if(!tienePropietario()){
        return (getNombre() + "\n PRECIOS:  " +
                "\n Compra: " + getPrecioCompra() +
                ";    Edificar: "+getPrecioEdificar() +
                ";    Alquiler completo: " + getPrecioALquilerCompleto() + 
                ";    Casas: " + getNumCasas() + 
                ";    Hoteles: " + getNumHoteles());
        } else{
            return ("Esta calle pertenece al jugador "+ propietario.getNombre());
        
        }
    }
    
    public void tramitarAlquiler(Jugador jugador){
        
        if(tienePropietario() && !esEsteElPropietario(jugador)){
            
            jugador.pagaAlquiler(getPrecioALquilerCompleto());
            propietario.recibe(getPrecioALquilerCompleto());
        }
    }
    
    public int cantidadCasasHoteles(){
        
        return numCasas + numHoteles;
    }
    
    public void actualizaPropietarioPorConversion(JugadorEspeculador jugador){
        propietario = jugador;
    }
    
}
