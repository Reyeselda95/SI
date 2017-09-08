/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author Sistemas Inteligentes
 * @author ALEJANDRO REYES ALBILLAR  ara65@alu.ua.es
 */
public class Jugador {

    /**
     * Clase Casilla Guarda un valor, la fila, la columna y el dominio de la
     * casilla
     */
    public class Casilla {

        private int valor;
        private final int fil;
        private final int col;
        private final int[] dom = new int[10];

        /**
         * Constructor por defecto de la clase casilla, se le pasa la posicion y
         * el valor
         *
         * @param fil fila de la casilla
         * @param col columna de la casilla
         * @param val valor de la casilla
         */
        public Casilla(int fil, int col, int val) {
            this.col = col;
            this.fil = fil;
            dom[0] = 0;
            if (val != 0) {
                for (int i = 1; i < 10; i++) {
                    dom[i] = 0;
                }
                dom[val] = 1;
            } else {
                for (int i = 1; i < 10; i++) {
                    dom[i] = 1;
                }
            }
            valor = val;
        }

        /**
         * Setter de valor
         *
         * @param val es el valor a asignar a la casilla
         */
        public void setValor(int val) {
            valor = val;
            if (val > 0) {
                for (int i = 1; i < 10; i++) {
                    dom[i] = 0;
                }
                dom[val] = 1;
            }
        }

        /**
         * Getter de valor
         *
         * @return devuelve el valor de la casilla
         */
        public int getValor() {
            return valor;
        }

        /**
         * Setter del dominio, copia el dominio valor a valor
         *
         * @param domin es el dominio a copiar
         */
        public void setDom(int[] domin) {
            System.arraycopy(domin, 1, dom, 1, 9);
        }

        /**
         * Getter de valor
         *
         * @return devuelve el array de int con el dominio
         */
        public int[] getDom() {
            return dom;
        }

        /**
         * Getter de fila
         *
         * @return devuelve la fila
         */
        public int getFil() {
            return fil;
        }

        /**
         * Getter de columna
         *
         * @return devuelve la columna
         */
        public int getCol() {
            return col;
        }

    }

    private boolean buscadoAC3 = false;
    
    private Casilla[] casillas = new Casilla[81];

    /**
     * Este algoritmo comprueba que sea correcto el valor que se ha introducido
     * en el tablero a partir de la posición del valor introducido y el tablero
     *
     * @param tablero es el tablero con el que se va a trabajar
     * @param fil es la columna de la casilla introducida
     * @param col es la fila de la casilla introducida
     * @return devuelve true si la solución introducida es correcta y cumple las restricciones
     */
    public boolean correcto(Tablero tablero, int fil, int col) {
        boolean ok = true;
        int num = tablero.getCasilla(fil, col);

        //Comprueba la fila
        for (int i = 0; i < 9; i++) {
            if (tablero.getCasilla(fil, i) == num && i != col) {
                ok = false;
                return ok;
            }
        }
        //Comprueba la columna
        for (int i = 0; i < 9; i++) {
            if (tablero.getCasilla(i, col) == num && i != fil) {
                ok = false;
                return ok;
            }
        }
        //Comprueba la submatriz
        int indexfil;
        int indexcol;
        //Seleccion de submatriz
        //filas
        if (fil < 3) {
            indexfil = 0;
        } else if (fil < 6) {
            indexfil = 3;
        } else {
            indexfil = 6;
        }
        //columnas
        if (col < 3) {
            indexcol = 0;
        } else if (col < 6) {
            indexcol = 3;
        } else {
            indexcol = 6;
        }

        //Comprobación
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero.getCasilla(indexfil + i, indexcol + j) == num && !(indexfil + i == fil && indexcol + j == col)) {
                    ok = false;
                    return ok;
                }
            }
        }

        return ok;
    }

    /**
     * El algoritmo back ejecuta el algoritmo de backtracking de modo recursivo
     * utilizando un dominio pasado
     *
     * @param tablero es el tablero con el que se va a trabajar
     * @param casillas es el array de casillas
     * @param pos es la posición actual a probar
     * @return devuelve true si se ejecuta correctamente el algoritmo
     */
    public boolean back(Tablero tablero, Casilla[] casillas, int pos) {
        boolean result = false;

        //impresión de las matrices a modo de comprobación
        /*
         for (int i = 0; i < 9; i++) {//Se recorren las filas
         for (int j = 0; j < 9; j++) {//Se recorren las colummnas
         System.out.print(tablero.getCasilla(i, j));
         if (j == 8) {
         System.out.println();
         }
         }
         if (i == 8) {
         System.out.println();
         }
         }
         */
        if (pos == casillas.length) {
            return true;
        }

        for (int i = 1; i <= 9; i++) {

            if (casillas[pos].getValor() == 0) {

                int[] aux = casillas[pos].getDom();

                int[] dom = new int[10];
                dom[0] = 0;
                for (int j = 1; j < 10; j++) {//haciendo copia por valor
                    dom[j] = aux[j];
                }

                tablero.setCasilla(i, casillas[pos].getFil(), casillas[pos].getCol());
                casillas[pos].setValor(i);
                dom[i] = 0;
                if (correcto(tablero, casillas[pos].getFil(), casillas[pos].getCol())) {
                    result = back(tablero, casillas, pos + 1);

                    if (result == true) {
                        return true;
                    }
                }
                dom[i] = 1;
                tablero.setCasilla(0, casillas[pos].getFil(), casillas[pos].getCol());
                casillas[pos].setValor(0);
                casillas[pos].setDom(dom);
            } else {
                result = back(tablero, casillas, pos + 1);
            }
        }
        return result;
    }

    /**
     * Función que comprueba que el fichero cargado cumple las restricciones
     * iniciales para poder resolver el sudoku
     *
     * @param tablero es el tablero con el que se va a trabajar
     * @param casillas es el array de casillas del tablero
     * @return devuelve true si se ha cargado el tablero sin incidencias
     */
    public boolean compruebaCargado(Tablero tablero, Casilla[] casillas) {
        int pos = 0;
        for (int i = 0; i < 9; i++) {//Se recorren las filas
            for (int j = 0; j < 9; j++) {//Se recorren las colummnas
                if (tablero.getCasilla(i, j) != 0) {
                    if (!correcto(tablero, i, j)) {
                        System.out.println("El tablero inicial posee un valor repetido en una fila, columna o submatiz, por lo que no hay solución.");
                        return false;
                    }
                } else {
                    if (dominioVacío(casillas[pos].getDom())) {
                        System.out.println("Existe un dominio vacío, por lo que no hay solución.");
                        return false;
                    }
                }
                pos++;
            }
        }
        return true;
    }

    /**
     * Se llama desde la clase Interfaz para ejecutar BC. Al final de la función
     * la solución del tablero se debe encontrar en tablero
     *
     * @param tablero es el tablero con el que se va a trabajar
     * @return devuelve true si ejecuta bien backtracking
     */
    public boolean ejecutarBC(Tablero tablero) {
        //El booleano ok guardará el valor que devolverá la ejecución del algoritmo
        boolean ok = true;
        if (!buscadoAC3) {
           //Rellena el array de casillas
           init(tablero);
        }
        
        if (!compruebaCargado(tablero, casillas)) {
            return false;
        }
        
        try {
           
            int vacias = 0;
            for (Casilla casilla : casillas) {
                if (casilla.getValor() == 0) {
                    vacias++;
                }
            }
            int it = 0;
            Casilla[] cambiar = new Casilla[vacias];
            for (Casilla casilla : casillas) {
                if (casilla.getValor() == 0) {
                    cambiar[it] = casilla;
                    it++;
                }
            }

            //ejecución del algoritmo de bactracking
            long init;
            long fin;
            long total;
            init = System.nanoTime();
            ok = back(tablero, cambiar, 0);
            fin = System.nanoTime();
            total = fin - init;
            System.out.println("El tiempo tardado en ejecutar Backtracking ha sido: " + total + " ns.");
            buscadoAC3 = false;

        } catch (Exception ex) {
            System.err.println("El algoritmo de Backtracking ha tenido un problema: " + ex);
        }

        return ok;
    }

    /**
     * Comprueba si el dominio está vacío o no
     *
     * @param dom es el dominio a comprobar
     * @return devuelve true si existe un dominio vacío
     */
    public boolean dominioVacío(int[] dom) {
        for (int i = 1; i <= 9; i++) {
            if (dom[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hace la reducción de dominios según el algoritmo AC3, lo guarda en la
     * casilla correspondiente y devuelve el array de casillas con los dominios
     * reducidos.
     *
     * @param tablero es el tablero con el que se va a trabajar
     * @param casillas es el array de casillas a las que se va a reducir el dominio
     * @param pos es la posicion del array a mirar
     * @return devuelve el array de casillas con los dominios reducidos
     */
    public Casilla[] switchBackAC3(Tablero tablero, Casilla[] casillas, int pos) {

        if (pos == casillas.length) {
            return casillas;
        }
        int[] dom = casillas[pos].getDom();

        if (casillas[pos].getValor() == 0) {
            //Vector inicial vacío a rellenar que almacena los posibles valores que puede tomar la casilla inicial.
            //La posición del vector será el valor, como el 0 no es una solución no se utilizará y se pondrá a 0
            int number;
            //Comprueba la fila
            for (int i = 0; i < 9; i++) {
                number = tablero.getCasilla(casillas[pos].getFil(), i);
                if (number != 0) {
                    dom[number] = 0;
                }
            }
            //Comprueba la columna
            for (int i = 0; i < 9; i++) {
                number = tablero.getCasilla(i, casillas[pos].getCol());
                if (number != 0) {
                    dom[number] = 0;
                }
            }

            //Comprueba la submatriz
            int indexfil;
            int indexcol;
            //Seleccion de submatriz
            //filas
            if (casillas[pos].getFil() < 3) {
                indexfil = 0;
            } else if (casillas[pos].getFil() < 6) {
                indexfil = 3;
            } else {
                indexfil = 6;
            }
            //columnas
            if (casillas[pos].getCol() < 3) {
                indexcol = 0;
            } else if (casillas[pos].getCol() < 6) {
                indexcol = 3;
            } else {
                indexcol = 6;
            }

            //Comprobación
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    number = tablero.getCasilla(indexfil + i, indexcol + j);
                    if (number != 0) {
                        dom[number] = 0;
                    }
                }
            }
        }

        casillas[pos].setDom(dom);

        return switchBackAC3(tablero, casillas, pos + 1);
    }

    /**
     * Dado un tablero, rellena el vector de casillas
     * @param tablero es el tablero con el que se va a trabajar
     */
    public void init(Tablero tablero){
        int it = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                casillas[it] = new Casilla(i, j, tablero.getCasilla(i, j));
                it++;
            }
        }
    }
    
    /**
     * Se llama desde la clase Interfaz para ejecutar AC. Al final de la función
     * la solución del tablero se debe encontrar en tablero
     *
     * @param tablero
     * @return devuelve true si ha reducido los tamaños
     */
    public boolean ejecutarAC(Tablero tablero) {
        
        //Rellena el array de casillas
        init(tablero);

        if (!compruebaCargado(tablero, casillas)) {
            return false;
        }
        try {
            long init;
            long fin;
            long total;
            init = System.nanoTime();
            //ejecución de la reduccion de dominios por AC3
            casillas = switchBackAC3(tablero, casillas, 0);
            fin = System.nanoTime();
            total = fin - init;
            System.out.println("El tiempo tardado en ejecutar AC3 ha sido: " + total + " ns.");
            
            int[] initial;
            int count;
            for (Casilla casilla : casillas) {
                count = 0;
                initial = casilla.getDom();
                System.out.print("Dominio de la casilla(" + casilla.getFil() + "," + casilla.getCol() + "): [");
                for (int j = 1; j < 10; j++) {
                    if (initial[j] != 0) {
                        if (count != 0) {
                            System.out.print(",");
                        }
                        System.out.print(j);
                        count++;
                    }
                }
                System.out.print("]");
                System.out.println();
            }
            buscadoAC3 = true;
        } catch (Exception ex) {
            System.err.println("El algoritmo AC3 ha tenido un problema: " + ex);
        }

        return true;
    }
}
