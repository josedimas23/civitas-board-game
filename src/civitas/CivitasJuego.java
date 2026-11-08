/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;

import java.util.ArrayList;
import java.util.Collections;

public class CivitasJuego {
    
    private int indiceJugadorActual;
    
    
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private EstadoJuego estado;
    private GestorEstados gestor;
    private MazoSorpresas mazo;
    private Tablero tablero;
    
    
    private void avanzaJugador(){
        
        Jugador jugadorActual = getJugadorActual();
        int posicionActual = jugadorActual.getCasillaActual();
        int tirada = Dado.getInstance().tirar();
        int posicionNueva = tablero.nuevaPosicion(posicionActual, tirada);
        Casilla casilla=tablero.getCasilla(posicionNueva);
        this.contabilizarPasosPorSalida();
        jugadorActual.moverACasilla(posicionNueva);
        casilla.recibeJugador(indiceJugadorActual, jugadores);
    }
    
    public CivitasJuego(ArrayList<String> nombres, boolean debug){
        
        //inicializamos los jugadores 
        for(int i=0; i<nombres.size();i++){
            Jugador temp=new Jugador(nombres.get(i));
            jugadores.add(temp);
            
        }
        
        //creamos el gestor de estados 
        
        gestor = new GestorEstados();
        estado = gestor.estadoInicial();
        
        //Poner el dado en modo debug/no
        
        Dado.getInstance().setDebug(debug);
        
        //Inicializar el indice del jugador actual 
        indiceJugadorActual= Dado.getInstance().quienEmpieza(jugadores.size());
    
        //Inicializamos el valor de mazo sorpresa segun el modo debug 
        mazo = new MazoSorpresas(debug);
        tablero = new Tablero();
    
        //Inicializamos el tablero 
        inicializaTablero(mazo);
        
        //Inicializamos el mazo Sorpresa 
        inicializaMazoSorpresas();
        
    }
    
    public boolean ConstruirCasa(int ip){

       return getJugadorActual().construirCasa(ip);
    } 
    
    public boolean ConstruirHotel(int ip){
       
       return getJugadorActual().construirHotel(ip);
    } 
    
    private void contabilizarPasosPorSalida(){
        if(tablero.computarPasoPorSalida()){
            getJugadorActual().pasaPorSalida();
        }
    }
    
    public boolean finalDelJuego(){
        boolean bancarrota=false;
        for(int i=0;i<jugadores.size();i++){
            if(jugadores.get(i).enBancarrota()){
                bancarrota=true;
            }
        }
        return bancarrota; 
    }
    
    public int getIndiceJugadorActual(){
        return indiceJugadorActual;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    
    public Jugador getJugadorActual(){
        return jugadores.get(indiceJugadorActual);
    }
    
    public ArrayList<Jugador> getJugadores(){
        return jugadores;
    }
    
    private void pasarTurno(){
        if(indiceJugadorActual+1<jugadores.size()){
            indiceJugadorActual++;
        }
        else{
            indiceJugadorActual=0;
        }
    }
    
    public ArrayList<Jugador> ranking(){
        
        Collections.sort(jugadores);
        
        return jugadores;
    }
    
     public void siguientePasoCompletado(OperacionJuego operacion){
         
        estado = gestor.siguienteEstado(getJugadorActual(), estado, operacion);
        
    }
    
    public OperacionJuego siguientePaso(){
        Jugador jugadorActual=getJugadorActual();
        OperacionJuego operacion=gestor.siguienteOperacion(jugadorActual, estado);
        
        if(operacion==OperacionJuego.PASAR_TURNO){
            this.pasarTurno();
            this.siguientePasoCompletado(operacion);
        }
        else if(operacion==OperacionJuego.AVANZAR){
            this.avanzaJugador();
            this.siguientePasoCompletado(operacion);
        }
        
        return operacion;
    }
    
    public boolean comprar(){
       
        Jugador jugadorActual=this.getJugadorActual();
        int numCasillaActual=jugadorActual.getCasillaActual();
        Casilla casilla=tablero.getCasilla(numCasillaActual);
        boolean res=jugadorActual.Comprar((CasillaCalle)casilla);
        
        return res;
        
    } 
    
     private void inicializaTablero(MazoSorpresas mazo){
        
        tablero.añadeCasilla(new Casilla(" Descanso "));
        tablero.añadeCasilla(new CasillaCalle( " albacete ", 60, 20, 50));
        tablero.añadeCasilla(new CasillaCalle( " murcia ", 80, 30, 100));
        tablero.añadeCasilla(new CasillaCalle(" almeria ", 100, 40, 200));
        tablero.añadeCasilla(new CasillaCalle(" granada ", 120, 50, 300));
        tablero.añadeCasilla(new CasillaSorpresa(" Sorpresa ", mazo));
        tablero.añadeCasilla(new CasillaCalle(" madrid ", 140, 60, 400));
        tablero.añadeCasilla(new CasillaCalle(" huelva ", 150, 70, 500));
        tablero.añadeCasilla(new CasillaSorpresa(" Sorpresa ", mazo));
        tablero.añadeCasilla(new CasillaCalle(" cordoba ", 200, 80, 600));
        tablero.añadeCasilla(new Casilla(" Parking "));
        tablero.añadeCasilla(new CasillaCalle(" sevilla ", 220, 90, 700));
        tablero.añadeCasilla(new CasillaCalle(" badajoz ", 240, 100, 800));
        tablero.añadeCasilla(new CasillaCalle(" burgos ", 260, 110, 900));
        tablero.añadeCasilla(new CasillaSorpresa(" Sorpresa ", mazo));
        tablero.añadeCasilla(new CasillaCalle(" avila ", 280, 120, 1000));
        tablero.añadeCasilla(new CasillaCalle(" vigo ", 300, 130, 1100));
        tablero.añadeCasilla(new CasillaSorpresa(" Sorpresa ", mazo));
        tablero.añadeCasilla(new CasillaCalle(" barcelona", 350, 140, 1200));

    }
    
    private void inicializaMazoSorpresas(){
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa Ganas 100", 100));
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa Ganas 600 ", 600));
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa Ganas 1000", 1000));
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa pierdes 100", -100));
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa pierdes 500", -500));
        mazo.alMazo(new SorpresaPagarCobrar("Sorpresa pierdes 1000", -1000));
        mazo.alMazo(new SorpresaPorCasaHotel("Sorpresa Ganas 1100", 1100));
        mazo.alMazo(new SorpresaPorCasaHotel("Sorpresa pierdes 100", -100));
        mazo.alMazo(new SorpresaPorCasaHotel("Sorpresa ganas 2000", 2000));
        mazo.alMazo(new SorpresaPorCasaHotel("Sorpresa no pasa nada", 0));
    }
    
    
}
