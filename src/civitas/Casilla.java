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
    
    /*
    private float precioCompra;
    private float precioEdificar;
    private float precioBaseAlquiler;
    private int numCasas;
    private int numHoteles;
    private static float FACTORALQUILERCALLE=1.0f;
    private static float FACTORALQUILERCASA=1.0f;
    private static float FACTORALQUILERHOTEL=4.0f;
    
    private MazoSorpresas Mazo;
    
    private Sorpresa sorpresa;
    
    private Jugador propietario;
    
    public int cantidadCasasHoteles(){
        
        return numCasas + numHoteles;
    }
    
    Casilla (TipoCasilla unTipo, String unNombre,float unPrecioCompra, float unPrecioEdificar, float unPrecioAlquilerBase){
        
        init();
        
        tipo=unTipo.CALLE;
        nombre=unNombre;
        precioCompra=unPrecioCompra;
        precioEdificar=unPrecioEdificar;
        precioBaseAlquiler=unPrecioAlquilerBase;
        numCasas=0;
        numHoteles=0;
    }
    
    Casilla (TipoCasilla unTipo, String unNombre){
        
        init();
        
        nombre = unNombre;
        tipo=unTipo.DESCANSO;
    }
    
    Casilla (TipoCasilla unTipo, String unNombre, MazoSorpresas mazo){
        
        init();
        
        nombre = unNombre;
        tipo=unTipo.SORPRESA;
        Mazo = mazo;
        
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
    */   

    /*
    int getNumCasas(){
        
        return numCasas;
    }
    
    int getNumHoteles(){
        
        return numHoteles;
    }
    
    public float getPrecioALquilerCompleto(){
        
        return (precioBaseAlquiler * FACTORALQUILERCALLE) * (1+ FACTORALQUILERCASA * numCasas + numHoteles * FACTORALQUILERHOTEL);
        
    }
    
    float getPrecioCompra(){
        
        return precioCompra;
    }
    
    float getPrecioEdificar(){
        
        return precioEdificar;
    }
    
    float getPrecioBaseAlquiler(){
        
        return precioBaseAlquiler;
    }
    
    */

    /*
    private void init(){
        
        nombre = "null";
        precioCompra = 0;
        precioEdificar = 0;
        precioBaseAlquiler = 0;
        numCasas = 0;
        numHoteles = 0;
    }
    
    */

    /*
    private void recibeJugador_calle(int actual, ArrayList <Jugador> todos){
        
        informe(actual,todos);
        
        Jugador jugador=todos.get(actual);
        
        if(!tienePropietario()){
            jugador.puedeComprarCasilla();
        }
        else{
            tramitarAlquiler(jugador);            
        }
    }
    
    private void recibeJugador_sorpresa(int actual, ArrayList <Jugador> todos){
        
        Sorpresa sorpresa=Mazo.siguiente();
        informe(actual,todos);
        sorpresa.aplicarAJugador(actual, todos);
    
    }
      
    public boolean tienePropietario(){
        
        return (propietario != null);
    }
    
    /*public String toString(){
        if(!tienePropietario()){
            return "calle "+ nombre + " Cuyos precios son => Compra :"+ precioCompra + " Edificar : "+precioEdificar+ 
                " Alquiler base : "+ precioBaseAlquiler + " Casa : "+numCasas + " Hoteles : "+ numHoteles;
            
        }else{
            return "calle "+ nombre + " cuyo dueño es " + propietario.getNombre();
            
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
    */
   
}
