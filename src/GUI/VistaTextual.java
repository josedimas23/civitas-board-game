/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package GUI;

import civitas.Casilla;
import civitas.CivitasJuego;
import civitas.Diario;
import civitas.OperacionJuego;
import controladorCivitas.Respuesta;
import civitas.OperacionInmobiliaria;
import civitas.Jugador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;



public class VistaTextual implements Vista {
  
    
  private static String separador = "=====================";
  
  private Scanner in;
  
  CivitasJuego juegoModel;
  
  public VistaTextual (CivitasJuego juegoModel) {
    in = new Scanner (System.in);
    this.juegoModel=juegoModel;
  }
  
  public void actualiza(){
      System.out.println("\nEl Jugador "+juegoModel.getJugadorActual().toString());
      
      if(juegoModel.finalDelJuego()){
       
          for(int i=0;i<juegoModel.ranking().size();i++){
             System.out.print(juegoModel.ranking().get(i).toString());
         }
      }
  }
  
           
 public  void pausa() {
    System.out.print ("\nPulsa una tecla");
    in.nextLine();
  }

  int leeEntero (int max, String msg1, String msg2) {
    Boolean ok;
    String cadena;
    int numero = -1;
    do {
      System.out.print (msg1);
      cadena = in.nextLine();
      try {  
        numero = Integer.parseInt(cadena);
        ok = true;
      } catch (NumberFormatException e) { // No se ha introducido un entero
        System.out.println (msg2);
        ok = false;  
      }
      if (ok && (numero < 0 || numero >= max)) {
        System.out.println (msg2);
        ok = false;
      }
    } while (!ok);

    return numero;
  }

  int menu (String titulo, ArrayList<String> lista) {
    String tab = "  ";
    int opcion;
    System.out.println (titulo);
    for (int i = 0; i < lista.size(); i++) {
      System.out.println (tab+i+"-"+lista.get(i));
    }

    opcion = leeEntero(lista.size(),
                          "\n"+tab+"Elige una opción: ",
                          tab+"Valor erróneo");
    return opcion;
  }
  


    public Respuesta comprar(){
        ArrayList<String> lista = new ArrayList(2);
        lista.add("SI");
        lista.add("NO");
        int opcion = menu(" ¿Desea comprar la casilla?", lista);
        return (Respuesta.values()[opcion]);
    }

    public OperacionInmobiliaria elegirOperacion(){
        ArrayList<String> lista = new ArrayList();
        OperacionInmobiliaria operacion;

        for(int i = 0; i<OperacionInmobiliaria.values().length; i++){
            lista.add(OperacionInmobiliaria.values()[i].name());
        }

        int opcion = menu(" ¿Qué gestión inmobiliaria quiere elegir?", lista);
        
        if(opcion == 0)
            operacion = OperacionInmobiliaria.CONSTRUIR_CASA;
        else if(opcion == 1)
            operacion = OperacionInmobiliaria.CONSTRUIR_HOTEL;
        else
            operacion = OperacionInmobiliaria.TERMINAR;

        return operacion;
    }

    public int elegirPropiedad(){
        ArrayList<String> propiedades = new ArrayList();
        for(int i = 0; i < juegoModel.getJugadorActual().getPropiedades().size(); i++)
            propiedades.add(i, juegoModel.getJugadorActual().getPropiedades().get(i).toString());
 
        return  menu(" Propiedad sobre la que se desea hacer la gestión: ",propiedades );
    }

    public void mostrarSiguienteOperacion(OperacionJuego operacion){
        System.out.print(" La siguiente operacion a realizar " + operacion);   
    }
    
    public void mostrarEventos(){
        while (Diario.getInstance().eventosPendientes()){
            System.out.print(Diario.getInstance().leerEvento()); 
        }
    }

}