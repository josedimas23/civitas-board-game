/**
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
package GUI;


import civitas.OperacionInmobiliaria;
import civitas.OperacionJuego;
import controladorCivitas.Respuesta;

public interface Vista {

    public void actualiza();

    public void pausa();

    public Respuesta comprar();

    public OperacionInmobiliaria elegirOperacion();

    public int elegirPropiedad();

    public void mostrarSiguienteOperacion(OperacionJuego operación);

    public void mostrarEventos();

}
