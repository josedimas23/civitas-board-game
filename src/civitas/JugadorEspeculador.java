/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package civitas;

/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class JugadorEspeculador extends Jugador{
    
   static final private int FactorEspeculador=2;
   static final protected int CasasMax = 4 * FactorEspeculador;
   static final protected int CasasPorHotel = 4 * FactorEspeculador;
   static final protected int HotelesMax = 4 * FactorEspeculador;
   
   protected JugadorEspeculador(Jugador jug){
       super(jug);
       actualizaPropiedadesPorConversion();
   }
   
    boolean modificarSaldo(float cantidad){
        float saldo =  cantidad/2;
        setSaldo(saldo);
        Diario.getInstance().ocurreEvento("El saldo de " + this.getNombre()
                              + " pasa a ser" + this.getSaldo());

        return true;
    }
   
   public void actualizaPropiedadesPorConversion(){
       for(int i=0;i<getPropiedades().size();i++){
           getPropiedades().get(i).actualizaPropietarioPorConversion(this);
       }
   }
   
    public String toString(){
     return "Este jugador es especulador, se llama '" + getNombre() + "', está en la casilla "
            + getCasillaActual() + ", y tiene " + getSaldo() + " saldo.";
    } 
}
