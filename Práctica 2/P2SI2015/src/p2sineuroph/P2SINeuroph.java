/*
 * P2 Sistemas Inteligentes. Curso 2015/2016
 * DCCIA. Universidad de Alicante
 */

package p2sineuroph;

import java.io.IOException;



/**
 *
 * @author fidel
 */
public class P2SINeuroph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Creo una NN de 2 neuronas en la capa oculta (784 en entrada y 10 salida)
        NN nn = new NN(28*28,30,10);
        
        //Cargo los primeros 60000 valores del train y 10000 de test
        nn.loadMNISTData(60000, 10000);
        
        //Entreno la red 
        double learningRate = 0.005;
        int maxIterations = 10;
        nn.train(learningRate,maxIterations);
        
        
    }


    
}
