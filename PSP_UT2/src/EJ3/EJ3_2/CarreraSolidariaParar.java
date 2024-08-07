/*
    Ejemplo en el que se utiliza una interfaz gráfica y múltiples hilos
    para modificar los componentes de la ventana.

    El programa consistirá en una seríe de etiquetas que se van moviendo
    aleatoriamente en todas direcciones dentro de una ventana.
    Para cada elemento crearemos un hilo

    Cuando trabajamos en una aplicación con interfaz gráfica debemos tener
    en cuenta que los métodos que acceden a modificar la ventana estarán
    sincronizados para evitar inconsistencias debidas a la concurrencia.

    * Para saber como se crea una etiqueta
    * Véase https://www.geeksforgeeks.org/jlabel-java-swing/
*/
package EJ3.EJ3_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Santiago
 */
public class CarreraSolidariaParar {
    List<int[]> relacionesHilos = new ArrayList<>();
    final int SIZE_X = 1200; // Ancho ventana
    final int SIZE_Y = 700; // Alto ventana
    final int W_LABEL = 25 * 3; // Ancho etiqueta
    final int H_LABEL = 25; // Alto etiqueta

    final int SALTO = 15;   // Nº de pixels máximo que avanza en cada movimiento
    final int SEP_Y = 4;    // separación entre etiquetas
    final int SIZE_CARRIL = (H_LABEL + SEP_Y);

    final int MAX_SEPARACION = 50;

    final int DEMORA_BASE = 100; // Milisegundos que esperamos para realizar el siguiente movimiento
    final int VELOCIDAD = 15;

    final boolean ESPERA_ACTIVA = false; // Indica si la demora se hace con espera activa o con sleep

    final int N_HILOS = (int) SIZE_Y / SIZE_CARRIL;  // Nº de hilos y etiquetas que se mostrarán, tantas como caben en vertical

    final Random rnd = new Random();    // Generador de nºs aleatorios.
    // Cuando trabajamos en concurrencia debemos tratar
    // de garantizar que este objeto es accedido en modo exclusivo
    // en otro caso no hay garantía de que los números sean aleatorios

    JFrame frame;   // Ventana principal (marco)
    JPanel panel;   // La ventana principal tiene un marco

    final Hilo hilos[] = new Hilo[N_HILOS];

    //
    // METODOS =========================================================
    //
    // default constructor
    public CarreraSolidariaParar() {
        initComponents();
        ejecutaHilos();
    }

    // main class
    public static void main(String[] args) {
        CarreraSolidariaParar programa = new CarreraSolidariaParar();
    }

    /**
     * Creamos la ventana principal
     */
    public void initComponents() {
        // create a new frame to stor text field and button
        frame = new JFrame("Etiquetas");
        panel = new JPanel(); // create a panel
        panel.setLayout(null);

        // add panel to frame
        //frame.add(panel);
        frame.getContentPane().add(panel);
        frame.setSize(SIZE_X, SIZE_Y); // set the size of frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /**
         * Evento que captura cuando se cierra la ventana. Comunica a los hilos
         * que finalicen
         */
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                finalizaHilos();
            }
        });

        //5. Show it.
        frame.setVisible(true);

        //frame.show();   // Obsoleto, aunque a nosotros no nos preocupa
    }

    /**
     * Crea una etiqueta JLabel y la situa en la posición indicada
     *
     * @param x
     * @param y
     * @return
     */
    JLabel creaEtiqueta(int x, int y, String id) {
        // create a label to display text
        JLabel label = new JLabel();

        // add text to label
        label.setText(id);
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(x, y, W_LABEL, H_LABEL);
        label.setOpaque(true);
        panel.add(label);
        return label;
    }

    /**
     * Lanza los hilos
     */
    public void ejecutaHilos() {
        for (int i = 0; i < hilos.length; i++) {
            JLabel l = creaEtiqueta(0, i * SIZE_CARRIL + SEP_Y / 2, Integer.toString(i + 1));
            hilos[i] = new Hilo(l, (i + 1));
        }
        for (int i = 0; i < hilos.length; i++) {
            hilos[i].start();
        }
    }

    public int posPrimero() {
        int x = 0;
        for (int i = 0; i < hilos.length; i++) {
            Rectangle rectLabel = hilos[i].getLabel().getBounds();
            if (hilos[i].continuarHilo && (int) rectLabel.getX() >= x) {
                x = (int) rectLabel.getX();
            }
        }
        return x;
    }

    public static int minX = 0;

    /*
    Este metodo esta mal implementado, para identificar que etiqueta es la última se tiene que filtrar primero todas las etiquetas descalificadas.
    No tiene sentido y el problema reside cuando el indice idx es cero y no encuentra ninguna iteración, devolviendo un indice que lo mas probable
    es que esté descalificado y posteriormente se calculan las distancias incorrectas, descalificando injustamente otras etiquetas.
     */
    public Hilo ultimo() {
        int idx = 0;
        //Se filtra los hilos descalificados
        List<Hilo> filteredTags = Arrays.stream(hilos).filter(h -> h.continuarHilo ).collect(Collectors.toList());
        for (int i = 0; i < filteredTags.size(); i++) {
            if (filteredTags.get(i).getX() <= filteredTags.get(idx).getX()) { //FIX ERROR AQUI SE TIENE QUE COMPARAR CON LOS TAGS FILTRADOS!!!
                idx = i;
            }
        }
        /*
        En caso de no encontrar candidato, significa que estamos ante la última etiqueta llegando a la meta, con lo cual devolvemos null y
        posteriormente tratamos este caso.
         */
        return filteredTags.size() == 0 ? null : filteredTags.get(idx);
    }

    /**
     * Comunica a todos los hilos que deben finalizar
     */
    public void finalizaHilos() {
        for (int i = 0; i < hilos.length; i++) {
            hilos[i].finaliza();
        }
    }


    /**
     * *********************************************************************
     * CLASE ANIDADA
     * **********************************************************************
     */
    class Hilo extends Thread {

        private int id;
        private JLabel label;
        private String textLabel;
        boolean continuarHilo = true;
        boolean estaDescalificado = false;

        /**
         * Constructor
         *
         * @param label
         */
        public Hilo(JLabel label, int id) {
            this.id = id;
            this.label = label;
            this.textLabel = label.getText();
        }

        /**
         * Se ejecuta indefinidamente hasta que se marca el hilo para finalizar
         */
        @Override
        public void run() {
            try {
                while (continuarHilo) {
                    desplazaEtiqueta();
                    revisarHilosRelacionadosBloqueados(this);

                    int espera = rnd.nextInt(DEMORA_BASE) * rnd.nextInt(VELOCIDAD);
                    if (ESPERA_ACTIVA) {
                        double inicio = System.currentTimeMillis();
                        while (System.currentTimeMillis() < inicio + espera) ;
                    } else {
                        try {
                            Thread.sleep(espera);
                        } catch (Exception Ex) {
                        }
                    }
                }
                System.out.println("\nFinalizado " + getLabel().getText());
            } catch (Exception e) {
                System.out.println("\nFinalizado " + getLabel().getText() + " ANOMALO");
                System.out.println(e.getMessage());
            }
        }

        public int getX() {
            return (int) label.getBounds().getX();
        }

        /**
         * Marca el hilo para que se finalice
         */
        public void finaliza() {
            continuarHilo = false;
        }

        /**
         * Mueve la etiqueta en la dirección calculada y comprueba que no se
         * salga de los límites de la ventana
         */
        private void desplazaEtiqueta() throws Exception {
            Rectangle rectLabel = label.getBounds();
            Rectangle rectPanel = panel.getBounds();

            int newX = ((int) rectLabel.getX());
            int y = ((int) rectLabel.getY());
            int salto = SALTO;
            Hilo ultimo = null;

            // sincronización de la actualización de la posición
            synchronized (this) {
                Object[] objects = actualizarPosicion(rectLabel, rectPanel, newX, salto);
                newX = (int) objects[0];
                rectLabel.setLocation(newX, y);
                ultimo = ultimo();
            }
            label.setBounds(rectLabel);
            /*
            Si es null, se trata del último elemento que ya ha llegado al final y ha terminado su ejecución.
             */
            if (ultimo == null) {
                label.setForeground(Color.BLACK);
                label.setBackground(Color.WHITE);
                return;
            }
            int primeroX = posPrimero();
            int ultimoX = ultimo.getX();
            int separacion = newX - ultimoX;

            if (!ultimo.continuarHilo) {
                throw new Exception("El último no puede estar muerto");
            }

            if (newX == primeroX) {
                label.setForeground(Color.YELLOW);
                label.setBackground(Color.BLUE);
            } else if (newX == ultimoX) {
                label.setForeground(Color.ORANGE);
                label.setBackground(Color.CYAN);
            } else {
                label.setForeground(Color.BLACK);
                label.setBackground(Color.WHITE);
            }
            label.setText(textLabel + "(" + separacion + ")");
            if (separacion > MAX_SEPARACION) {
                // Penalizado
                label.setForeground(Color.RED);
                label.setBackground(Color.GRAY);
                this.label.setText(textLabel + "<" + ultimo.textLabel + ">");

                synchronized (this) {
                    int[] relacion = new int[]{ultimo.id, id};  // Guardar la relación entre los hilos
                    synchronized (relacionesHilos){
                        relacionesHilos.add(relacion);
                    }
                    System.out.println("\n" + this.id + "Bloqueado");
                    this.wait();
                }
            }
        }

        private void revisarHilosRelacionadosBloqueados(Hilo hilo){
            synchronized (relacionesHilos){
                relacionesHilos.stream().filter( r -> r[0] == hilo.id).forEach( rfound -> {
                    if(hilos[rfound[0] - 1].getX() == hilos[rfound[1] - 1].getX()){
                        synchronized (hilos[rfound[1] - 1]) {
                            hilos[rfound[1] - 1].notify();
                        }
                        System.out.println( hilos[rfound[1] - 1].id + "go máquina");
                    }
                });
            }
        }

        public synchronized Object[] actualizarPosicion(
                Rectangle rectLabel,
                Rectangle rectPanel,
                int newX,
                int salto
        ) {
            if (newX + salto < rectPanel.getWidth() - rectLabel.getWidth()) {
                // No llega al final
                newX += salto;
            } else {
                // LLega al final, no sobrepasa
                newX = (int) (rectPanel.getWidth() - rectLabel.getWidth());
                continuarHilo = false;
            }
            return new Object[]{newX, ultimo()};
        }

        /**
         * @return the label
         */
        public JLabel getLabel() {
            return label;
        }
    }
}
