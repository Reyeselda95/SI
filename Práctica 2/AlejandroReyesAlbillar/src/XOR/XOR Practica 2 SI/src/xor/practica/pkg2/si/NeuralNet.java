/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xor.practica.pkg2.si;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.convolution.MNISTDataSet;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author ALEANDRO REYES ALBILLAR 
 */
public class NeuralNet implements LearningEventListener {
    public NeuralNet(int inputSize, int hiddenLayerSize, int outputSize){
       _mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputSize, hiddenLayerSize, outputSize);
       _bp = new BackPropagation();
       //Para recibir el evento en cada iteración de BP
       _bp.addListener(this);
    }
    
    public void randomize(){
        _mlp.randomizeWeights(); 
    }
    
    public void train(double learningRate, int maxIterations, DataSet trainingSet) {
        
        _trainSet=trainingSet;
        
        System.out.println("Training network...");
        _bp.setMaxIterations(maxIterations);
        _bp.setLearningRate(learningRate);
        _mlp.learn(_trainSet,_bp);
        
    }
    
    @Override
    public void handleLearningEvent(LearningEvent le) {
        BackPropagation bp = (BackPropagation) le.getSource();
        System.out.println(bp.getCurrentIteration() + ") Network error: " + bp.getTotalNetworkError());
        test(_trainSet);
    }
    
    
    public void test(DataSet ds){
       int i=1;
      // System.out.println("");   
       _mlp.setInput(ds.getRowAt(i).getInput());//cogemos una imagen y la metemos a _mlp
       _mlp.calculate();//Calculamos
       Double pesos[]= _mlp.getWeights();//Array de pesos

       System.out.print("[");
       for(int j=0;j<pesos.length;j++){
            if(j==0)
                System.out.print(pesos[j]);
            else
                System.out.print(","+pesos[j]);
        }
       System.out.println("]");
       i++;
   }
     
    public DataSet _trainSet;
    public MultiLayerPerceptron _mlp;
    public BackPropagation _bp;
}
