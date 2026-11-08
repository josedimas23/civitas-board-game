/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package civitas;

public class Civitas {

   /* public static void main(String[] args) {
        
        int jugador1 = 0, jugador2 = 0, jugador3 = 0, jugador4 = 0;
        int cont = 0; 
        
        //ejer1
        
        for(int i=0; i<100; i++){
        
            cont = Dado.getInstance().quienEmpieza(4);
            
            if(cont == 1)
                
                jugador1++;
                else if(cont == 2)
                    
                    jugador2++;
                    else if(cont == 3)
                        
                        jugador3++;
                        else
                            jugador4++;   
        }
        
        System.out.println("Numero de veces repetidas:");
        System.out.println("Jugador 1 = " + jugador1);
        System.out.println("Jugador 2 = " + jugador2);
        System.out.println("Jugador 3 = " + jugador3);
        System.out.println("Jugador 4 = " + jugador4); 
        
        //ejer2
        
        int salida;
        
        Dado.getInstance().setDebug(false);
        System.out.println("\nFALSE");
        for(int i = 0; i<5; i++){
            
            salida = Dado.getInstance().tirar();
            System.out.println(salida);
        }

        Dado.getInstance().setDebug(true);
        System.out.println("TRUE");
        for(int i = 0; i<5; i++){
            
            salida = Dado.getInstance().tirar();
            System.out.println(salida);
        }        
        
        //ejer3
        
        salida = Dado.getInstance().getUltimoResultado();
        
        System.out.println("\nEl ultimo resultado es = " + salida);
        
        //ejer4
        
        //System.out.println(TipoCasilla.CALLE);
        System.out.println("\n" + TipoCasilla.DESCANSO);
        //System.out.println(TipoCasilla.SORPRESA);
        System.out.println(TipoSorpresa.PORCASAHOTEL);
        //System.out.println(TipoSorpresa.PAGARCOBRAR);
        System.out.println(EstadoJuego.DESPUES_AVANZAR);
        
        //ejer5
        
        Tablero tablero = new Tablero();
        
        
        Casilla casilla1=new Casilla(TipoCasilla.CALLE,"albacete",1500,1000,500 );
        Casilla casilla2=new Casilla(TipoCasilla.CALLE,"murcia",1000,1000,500 );
        Casilla casilla3=new Casilla(TipoCasilla.CALLE,"cuenca",2000,1000,500 );
        
        tablero.añadeCasilla(casilla1);
        tablero.añadeCasilla(casilla2);
        tablero.añadeCasilla(casilla3);
        
        Casilla casilla21=tablero.getCasilla(1);
        Casilla casilla22=tablero.getCasilla(2);
        Casilla casilla23=tablero.getCasilla(3);
        
        System.out.println("\n" + casilla21.toString());
        System.out.println(casilla22.toString());
        System.out.println(casilla23.toString());
        
        //ejer6
        
        String maxi = "",mini = "";
        float max=0,min=5000;
        float medio=0;
        
        //Empezamos en la casilla 1 por que la 0 es la salida 
        
        for(int j = 1; j <= 3 /*tablero.getNumCasillas(); j++){ //Ponemos 3 por que si usamos la funcion tablero.getNumCasillas() nos sale un error por el size
               
            if(max < tablero.getCasilla(j).getPrecioCompra()){
                max=tablero.getCasilla(j).getPrecioCompra();
                maxi=tablero.getCasilla(j).getNombre();
            }
            
            if(min > tablero.getCasilla(j).getPrecioCompra()){
                min=tablero.getCasilla(j).getPrecioCompra();
                mini=tablero.getCasilla(j).getNombre();
            }
            
            medio+=tablero.getCasilla(j).getPrecioCompra();
        }
        
        medio/=3 /*tablero.getNumCasillas();
        
        System.out.println("\nLa calle mas cara es "+ maxi + " y vale : "+ max);
        System.out.println("La calle mas barata es "+ mini + " y vale : "+ min);
        System.out.println("El precio medio es : "+ medio);
        
        //ejer7
        
        Diario.getInstance().ocurreEvento("Probando el diario ");
        Diario.getInstance().ocurreEvento("Segunda prueba");
        Diario.getInstance().ocurreEvento("Tercera prueba ");

        do {
            System.out.println("\n" + Diario.getInstance().leerEvento());
        } while (Diario.getInstance().eventosPendientes());
        
        
        //ejer8
        
        Dado.getInstance().setDebug(false);
        
        int tiradas;
        int pos=0;
        
        for(int i=0;i<10;i++){
            tiradas=Dado.getInstance().tirar();
            pos=tablero.nuevaPosicion(pos, tiradas);
            System.out.println("\nHas sacado un : "+tiradas);
            System.out.println("\nTe quedas en al posicion "+pos);
        }
            

    }*/
}

