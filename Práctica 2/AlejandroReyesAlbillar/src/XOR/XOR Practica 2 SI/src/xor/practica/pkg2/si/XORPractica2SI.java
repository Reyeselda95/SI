/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xor.practica.pkg2.si;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

/**
 *
 * @author ALEANDRO REYES ALBILLAR 45931406-S
 */
public class XORPractica2SI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    // create new perceptron network
        NeuralNet neuralNetwork = new NeuralNet(2,2,1);
    // create training set
        DataSet trainingSet = new DataSet(2, 1);
    // add training data to training set (logical OR function)
        trainingSet.addRow(new DataSetRow(new double[]{0, 0},new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1},new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0},new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1},new double[]{0}));
        // learn the training set
        
        //Entreno la red 
        double learningRate = 0.00005;
        int maxIterations = 10;
        neuralNetwork.train(learningRate,maxIterations, trainingSet);
    }

}
