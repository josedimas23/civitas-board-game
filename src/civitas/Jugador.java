/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;
import java.util.ArrayList;

public class Jugador implements Comparable <Jugador> {
    
    protected static int CasasMax=4;
    protected static int CasasPorHotel=4;
    private int casillaActual;
    protected static int HotelesMax=4;
    private String nombre;
    protected static float PasoPorSalida=1000;
    private boolean puedeComprar;
    private float saldo;
    private float SaldoInicial=7500;
    
    private boolean especulador = false;
    
    private ArrayList<CasillaCalle> propiedades= new ArrayList<>();
    
    int cantidadCasasHoteles(){
        int edificaciones=0;
        for (int i=0;i<propiedades.size();i++){
            edificaciones += propiedades.get(i).cantidadCasasHoteles();
        }
        return edificaciones;
    }
    
    public int compareTo(Jugador otro){
        return Float.compare(this.saldo, otro.saldo);
    }
    
    public void setSaldo(float s){
        saldo=s;
    }
    
    boolean enBancarrota(){
        return saldo<=0;
    }
    
    public float GetSaldo(){
        return saldo ;
    }
    
    private boolean existeLaPropiedad(int ip){
        return (0<=ip && ip<=propiedades.size());
    }
    
    private static int getCasasMax(){
        return CasasMax;
    }
    
    static int getCasasPorHotel(){
        return CasasPorHotel;
    }
    
    public int getCasillaActual(){
        return casillaActual;
    }
    
    private static int getHotelesMax(){
        return HotelesMax;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    private float getPremioPasoSalida(){
        return PasoPorSalida;
    }
    
    public ArrayList<CasillaCalle> getPropiedades(){
        return propiedades;
    }
    
    boolean getPuedeComprar(){
        return puedeComprar;
    }
    
    public float getSaldo(){
        return saldo;
    }
    
    Jugador(String Nombre){
        nombre=Nombre;
        casillaActual=1;
        puedeComprar=true;
        saldo = SaldoInicial;
    }
    
    public Jugador(Jugador otro){
        nombre=otro.nombre;
        casillaActual=otro.getCasillaActual();
        puedeComprar=otro.getPuedeComprar();
        saldo=otro.getSaldo();
        propiedades=otro.getPropiedades();
    }
    
    boolean modificarSaldo(float cantidad){
        saldo=saldo+cantidad;
        //Diario.getInstance().ocurreEvento("El saldo de "+ this.getNombre()+ "ahora es "+this.getSaldo());
        return true;
    }
    
    boolean moverACasilla(int numCasilla){
        casillaActual=numCasilla;
        puedeComprar=false;
        Diario.getInstance().ocurreEvento("\nEl jugador "+this.getNombre()+" esta ahora en la posiciom "+this.casillaActual);
        return true;
    }
    
    boolean paga(float cantidad){
        return modificarSaldo(cantidad*(-1));
    }
    
    boolean pagaAlquiler(float cantidad){
        return paga(cantidad);
    }
    
    boolean recibe(float cantidad){
        return modificarSaldo(cantidad);
    }
    
    boolean pasaPorSalida(){
        
        recibe(PasoPorSalida);
        Diario.getInstance().ocurreEvento("\nEl jugador " + nombre + " ha recibido "+ PasoPorSalida + " por pasar por la salida");
        return true;
    }
    
    boolean puedeComprarCasilla(){
        puedeComprar=true;
        return puedeComprar;
    }
    
    private boolean puedoGastar(float precio){
        return saldo >= precio;
    } 
    
    private boolean puedoEdificarCasa(CasillaCalle propiedad){
        float precioEdificar=propiedad.getPrecioEdificar();
        boolean puedoEdificarCasa=false;
        if(this.puedoGastar(precioEdificar) && propiedad.getNumCasas() < CasasMax){
            puedoEdificarCasa=true;
        }        
        return puedoEdificarCasa;
        
    }
    
    private boolean puedoEdificarHotel(CasillaCalle propiedad){
        boolean puedoEdificarHotel=false;
        float precio=propiedad.getPrecioEdificar();
        
        if(this.puedoGastar(precio) && 
                propiedad.getNumHoteles()< getHotelesMax() &&
                propiedad.getNumCasas() >= getCasasPorHotel()){
            
            puedoEdificarHotel=true;
        }
        
        return puedoEdificarHotel;
    }
    
    boolean tieneAlgoQueGestionar(){
        return propiedades.size()>0;
    }
    
   public String toString(){
        return ("  " + getNombre() + 
                " se encuentra en la casilla " + (getCasillaActual()) +
                " y tiene un saldo de " + getSaldo());      
    }
    
    public boolean Comprar(CasillaCalle titulo){
        boolean result=false;
        
        if(this.puedeComprar){
            
            float precio=titulo.getPrecioCompra();
            
            if(this.puedoGastar(precio)){
                result=titulo.comprar(this);
                titulo.setPropietario(this);
                this.paga(titulo.getPrecioCompra());
                propiedades.add(titulo);
                Diario.getInstance().ocurreEvento("\nEl jugador "+this.getNombre()+" compra la propiedad "+titulo.getNombre());
                puedeComprar=false;
            }           
            else{
                Diario.getInstance().ocurreEvento("\nEl jugador "+this.getNombre()+" no tiene saldo para comprar la propiedad "+titulo.getNombre());
            }
        
        }
        
        return result;
    }
    
    public boolean construirHotel(int ip){
        boolean result=false;
        
        if(this.existeLaPropiedad(ip)){
            CasillaCalle propiedad=propiedades.get(ip);
            boolean puedoEdificarHotel=this.puedoEdificarHotel(propiedad);
            
            if(puedoEdificarHotel){
                result=propiedad.construirHotel(this);
                propiedad.derruirCasas(CasasPorHotel, this);
                Diario.getInstance().ocurreEvento(" \nEl jugador "+this.getNombre()+" construye un hotel en la propiedad "+propiedades.get(ip).getNombre());
            }else{
                
                Diario.getInstance().ocurreEvento("\nEl jugador "+this.getNombre()+" no puede construir un hotel en la propiedad "+propiedades.get(ip).getNombre());
            }   
        }
        
        return result;
    }
    
    public boolean construirCasa(int ip){
        boolean result=false;
        boolean existe = this.existeLaPropiedad(ip);
        
        if(existe){
            CasillaCalle propiedad=propiedades.get(ip);
            boolean puedoEdificar= this.puedoEdificarCasa(propiedad);
            
            if(puedoEdificar){
                result=propiedad.construirCasa(this);
                Diario.getInstance().ocurreEvento(" \nEl jugador "+this.getNombre()+" construye una casa en la propiedad "+propiedades.get(ip).getNombre());
            }else{
                
                Diario.getInstance().ocurreEvento(" \nEl jugador "+this.getNombre()+" no puede construir una casa en la propiedad "+propiedades.get(ip).getNombre());
            }    
        }
        
       return result;
    }
    
    
    public JugadorEspeculador convertir(){
        JugadorEspeculador esp=new JugadorEspeculador(this);
        especulador=true;
        return esp;
    }
    
    public boolean Especulador(){
        return especulador;
    }
    
    
}
