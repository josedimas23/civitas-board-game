package GUI;

import civitas.*;
import controladorCivitas.Respuesta;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Ventana principal. Implementa {@link Vista}: el controlador corre en su propio hilo
 * y cada pregunta se responde con botones dentro de la ventana, sin diálogos emergentes.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class CivitasView extends JFrame implements Vista {

    private record Opcion<T>(String texto, Color color, T valor) {}

    private CivitasJuego juego;
    private final TableroPanel tablero = new TableroPanel();
    private final JugadoresPanel jugadores = new JugadoresPanel();

    private final JLabel accionTitulo = new JLabel(" ");
    private final JLabel accionTexto = new JLabel(" ");
    private final JPanel accionBotones = new JPanel();

    private final DefaultListModel<String> eventos = new DefaultListModel<>();

    private OperacionJuego ultimaOperacion = null;
    private Jugador jugadorTirando;
    private int casillaAntesDeTirar;

    public CivitasView() {
        super("Civitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
        getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);

        JPanel raiz = new JPanel(new BorderLayout(18, 0));
        raiz.setBackground(Tema.FONDO);
        raiz.setBorder(new EmptyBorder(30, 18, 18, 18));
        raiz.add(tablero, BorderLayout.CENTER);
        raiz.add(barraLateral(), BorderLayout.EAST);
        setContentPane(raiz);

        setMinimumSize(new Dimension(1060, 700));
        setSize(1240, 820);
        setLocationRelativeTo(null);
    }

    public void setCivitasJuego(CivitasJuego juego) {
        this.juego = juego;
        tablero.setJuego(juego);
        jugadores.setJuego(juego, tablero);
        evento("Empieza la partida. Sale " + juego.getJugadorActual().getNombre() + ".");
        setVisible(true);
    }

    // ------------------------------------------------------------------ construcción

    private JComponent barraLateral() {
        JPanel lado = new JPanel();
        lado.setOpaque(false);
        lado.setLayout(new BorderLayout(0, 16));
        lado.setPreferredSize(new Dimension(370, 0));

        JPanel arriba = new JPanel();
        arriba.setOpaque(false);
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.add(seccion("JUGADORES"));
        arriba.add(Box.createVerticalStrut(10));
        jugadores.setAlignmentX(LEFT_ALIGNMENT);
        arriba.add(jugadores);
        arriba.add(Box.createVerticalStrut(18));
        arriba.add(tarjetaAccion());
        lado.add(arriba, BorderLayout.NORTH);

        JPanel diario = new JPanel(new BorderLayout(0, 10));
        diario.setOpaque(false);
        diario.add(seccion("DIARIO"), BorderLayout.NORTH);
        JList<String> lista = new JList<>(eventos);
        lista.setBackground(Tema.PANEL);
        lista.setFocusable(false);
        lista.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int a, int b) {}
        });
        lista.setCellRenderer((l, valor, i, sel, foco) -> {
            JLabel et = new JLabel("<html><div style='width:250px'>" + valor + "</div></html>");
            et.setFont(Tema.fuente(Font.PLAIN, 12.5f));
            et.setForeground(i == 0 ? Tema.TEXTO : Tema.TEXTO_SUAVE);
            et.setIcon(new Punto(colorEvento(valor)));
            et.setIconTextGap(10);
            et.setBorder(new EmptyBorder(7, 12, 7, 12));
            return et;
        });
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(Tema.PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        JPanel marco = new Redondeado(Tema.PANEL);
        marco.setLayout(new BorderLayout());
        marco.setBorder(new EmptyBorder(6, 2, 6, 2));
        marco.add(scroll);
        diario.add(marco, BorderLayout.CENTER);
        lado.add(diario, BorderLayout.CENTER);
        return lado;
    }

    private JComponent tarjetaAccion() {
        JPanel t = new Redondeado(Tema.TARJETA);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.setBorder(new EmptyBorder(16, 18, 18, 18));
        t.setAlignmentX(LEFT_ALIGNMENT);
        accionTitulo.setFont(Tema.fuente(Font.BOLD, 18));
        accionTitulo.setForeground(Tema.TEXTO);
        accionTexto.setFont(Tema.fuente(Font.PLAIN, 13));
        accionTexto.setForeground(Tema.TEXTO_SUAVE);
        accionBotones.setOpaque(false);
        accionBotones.setLayout(new GridLayout(0, 1, 0, 8));
        for (JComponent c : new JComponent[]{accionTitulo, accionTexto, accionBotones}) c.setAlignmentX(LEFT_ALIGNMENT);
        t.add(accionTitulo);
        t.add(Box.createVerticalStrut(4));
        t.add(accionTexto);
        t.add(Box.createVerticalStrut(14));
        t.add(accionBotones);
        return t;
    }

    private static JLabel seccion(String s) {
        JLabel l = new JLabel(s);
        l.setFont(Tema.fuente(Font.BOLD, 11));
        l.setForeground(Tema.TEXTO_SUAVE);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // ------------------------------------------------------------------ interacción

    /** Muestra una pregunta con botones y bloquea el hilo del controlador hasta que se elige una opción. */
    @SafeVarargs
    private <T> T preguntar(String titulo, String texto, Opcion<T>... opciones) {
        CompletableFuture<T> respuesta = new CompletableFuture<>();
        SwingUtilities.invokeLater(() -> {
            accion(titulo, texto);
            for (Opcion<T> o : opciones) {
                Boton b = new Boton(o.texto(), o.color());
                b.addActionListener(e -> {
                    accion(" ", " ");
                    respuesta.complete(o.valor());
                });
                accionBotones.add(b);
            }
            accionBotones.revalidate();
            if (accionBotones.getComponentCount() > 0)
                getRootPane().setDefaultButton((JButton) accionBotones.getComponent(0));
        });
        return respuesta.join();
    }

    private void accion(String titulo, String texto) {
        accionTitulo.setText(titulo);
        accionTexto.setText("<html><div style='width:300px'>" + texto + "</div></html>");
        accionBotones.removeAll();
        accionBotones.revalidate();
        accionBotones.repaint();
    }

    private void enEDT(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) r.run();
        else SwingUtilities.invokeLater(r);
    }

    private void evento(String s) {
        enEDT(() -> {
            eventos.add(0, s);
            if (eventos.size() > 200) eventos.remove(eventos.size() - 1);
        });
    }

    private static Color colorEvento(String s) {
        String l = s.toLowerCase();
        if (l.contains("compra")) return Tema.ORO;
        if (l.contains("construye")) return Tema.VERDE;
        if (l.contains("salida")) return Tema.AZUL;
        if (l.contains("no puede") || l.contains("no tiene")) return Tema.ROJO;
        return Tema.TEXTO_SUAVE;
    }

    private String casillaActual() {
        Jugador j = juego.getJugadorActual();
        return Tema.nombreBonito(juego.getTablero().getCasilla(j.getCasillaActual()).getNombre());
    }

    // ------------------------------------------------------------------ Vista

    @Override
    public void actualiza() {
        enEDT(() -> {
            tablero.sincronizar();
            tablero.repaint();
            jugadores.repaint();
        });
    }

    @Override
    public void pausa() {
        // El controlador pausa antes de cada paso; solo esperamos al jugador cuando toca tirar.
        boolean tocaTirar = ultimaOperacion == null || ultimaOperacion == OperacionJuego.PASAR_TURNO;
        if (!tocaTirar) return;
        Jugador j = juego.getJugadorActual();
        jugadorTirando = j;
        casillaAntesDeTirar = j.getCasillaActual();
        preguntar("Turno de " + j.getNombre(),
                "Estás en " + casillaActual() + " con " + Tema.dinero(j.getSaldo()) + ".",
                new Opcion<>("Tirar dado", tablero.colorDe(j), Boolean.TRUE));
    }

    @Override
    public void mostrarSiguienteOperacion(OperacionJuego operacion) {
        ultimaOperacion = operacion;
        if (operacion == OperacionJuego.AVANZAR && jugadorTirando != null) {
            Jugador j = jugadorTirando;
            int desde = casillaAntesDeTirar, hasta = j.getCasillaActual();
            CountDownLatch fin = new CountDownLatch(1);
            enEDT(() -> {
                accion(j.getNombre() + " tira el dado…", " ");
                tablero.animarTirada(j, desde, hasta, fin::countDown);
            });
            try { fin.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            jugadorTirando = null;
        } else if (operacion == OperacionJuego.PASAR_TURNO) {
            evento("Turno de " + juego.getJugadorActual().getNombre() + ".");
        }
        actualiza();
    }

    @Override
    public void mostrarEventos() {
        Diario d = Diario.getInstance();
        while (d.eventosPendientes()) {
            String e = d.leerEvento().replace("\n", " ").trim();
            if (e.isEmpty() || e.startsWith("De:")) continue;   // trazas internas del gestor de estados
            evento(e.replace("Casilla: ", "").replaceAll("(\\d+)\\.0\\b", "$1 €").replaceAll("\\s+", " "));
        }
    }

    @Override
    public Respuesta comprar() {
        Jugador j = juego.getJugadorActual();
        Casilla c = juego.getTablero().getCasilla(j.getCasillaActual());
        String texto = "Has caído en una calle libre.";
        String boton = "Comprar";
        if (c instanceof CasillaCalle cc) {
            texto = "Alquiler base " + Tema.dinero(cc.getPrecioALquilerCompleto())
                    + " · edificar " + Tema.dinero(cc.getPrecioEdificar())
                    + "<br>Tu saldo: " + Tema.dinero(j.getSaldo());
            boton = "Comprar por " + Tema.dinero(cc.getPrecioCompra());
        }
        Respuesta r = preguntar("¿Comprar " + casillaActual() + "?", texto,
                new Opcion<>(boton, Tema.ORO, Respuesta.SI),
                new Opcion<>("No comprar", Tema.TARJETA_ALT, Respuesta.NO));
        actualiza();
        return r;
    }

    @Override
    public OperacionInmobiliaria elegirOperacion() {
        Jugador j = juego.getJugadorActual();
        OperacionInmobiliaria op = preguntar("Gestionar propiedades",
                j.getNombre() + ", ¿quieres construir antes de pasar el turno?",
                new Opcion<>("Construir casa", Tema.VERDE, OperacionInmobiliaria.CONSTRUIR_CASA),
                new Opcion<>("Construir hotel", Tema.ROJO, OperacionInmobiliaria.CONSTRUIR_HOTEL),
                new Opcion<>("Terminar turno", Tema.TARJETA_ALT, OperacionInmobiliaria.TERMINAR));
        return op;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int elegirPropiedad() {
        List<CasillaCalle> props = juego.getJugadorActual().getPropiedades();
        Opcion<Integer>[] ops = new Opcion[props.size()];
        for (int i = 0; i < props.size(); i++) {
            CasillaCalle c = props.get(i);
            ops[i] = new Opcion<>(Tema.nombreBonito(c.getNombre()) + "  ·  " + c.getNumCasas() + " casas, "
                    + c.getNumHoteles() + " hoteles", Tema.TARJETA_ALT, i);
        }
        int elegida = preguntar("¿En qué calle?", "Cada edificio cuesta el precio de edificar de la calle.", ops);
        SwingUtilities.invokeLater(() -> { tablero.repaint(); jugadores.repaint(); });
        return elegida;
    }

    /** Muestra la clasificación final sobre el tablero (llamado al acabar la partida). */
    public void mostrarRanking() {
        mostrarEventos();
        List<Jugador> r = new ArrayList<>(juego.getJugadores());
        r.sort(Comparator.comparingDouble(Jugador::getSaldo).reversed());
        evento("Fin de la partida: gana " + r.get(0).getNombre() + ".");
        enEDT(() -> {
            tablero.mostrarRanking(r);
            jugadores.repaint();
        });
        preguntar("Partida terminada", "Alguien ha entrado en bancarrota. Gana " + r.get(0).getNombre() + ".",
                new Opcion<>("Salir", Tema.ORO, Boolean.TRUE));
        System.exit(0);
    }

    // ------------------------------------------------------------------ piezas pequeñas

    /** Panel con fondo redondeado. */
    static class Redondeado extends JPanel {
        private final Color fondo;
        Redondeado(Color fondo) { this.fondo = fondo; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suave(g);
            g2.setColor(fondo);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            g2.dispose();
        }
        @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, getPreferredSize().height); }
    }

    /** Icono circular para el diario. */
    private record Punto(Color color) implements Icon {
        public int getIconWidth() { return 8; }
        public int getIconHeight() { return 8; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Tema.suave(g);
            g2.setColor(color);
            g2.fillOval(x, y, 8, 8);
            g2.dispose();
        }
    }
}
