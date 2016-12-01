/*
 * P2 Sistemas Inteligentes. Curso 2015/2016
 * DCCIA. Universidad de Alicante
 */

package p2sineuroph;

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
 * @author Fidel
 */
public class NN implements LearningEventListener {
    
    public NN(int inputSize, int hiddenLayerSize, int outputSize){
       _mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputSize, hiddenLayerSize, outputSize);
       _bp = new BackPropagation();
       
       //Para recibir el evento en cada iteración de BP
       _bp.addListener(this);
    }
    
    
    public void loadMNISTData(int trainSamples, int testSamples) {
        
        System.out.println("Loading MNIST data...");
        
        if(trainSamples>0 && trainSamples<=60000 && testSamples>0 && testSamples<=10000){
            try {
            _trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, trainSamples);
            _testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, testSamples);
            test(_testSet);
            //Imprime las imagenes
           // print(_trainSet);
            } catch (IOException ex) {
                Logger.getLogger(NN.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("Error in loadMNISTData: fuera de rango. trainSamples debe estar entre 1 y 60000; testSamples entre 1 y 10000");
        }

    }
    
    //Imprime el DataSet que se le pasa
    public void print(DataSet set){
        for(DataSetRow datasr : set.getRows()){
            double[] dd=datasr.getInput();
            for(int j=0;j<28;j++){
                for(int k=0;k<28;k++){
                    int value=(int)(long) (dd[j*28+k]*255);
                    if(value>0)
                        System.out.print("ｱ");
                    else{
                        System.out.print(" ");}
                }
                System.out.println();
            }
        }
    }
    
    public void randomize(){
        _mlp.randomizeWeights(); 
    }
    
    public void train(double learningRate, int maxIterations) {
        
        System.out.println("Training network...");
        _bp.setMaxIterations(maxIterations);
        _bp.setLearningRate(learningRate);
        _mlp.learn(_trainSet,_bp);
        test(_testSet);
    }
    
    
    
    @Override
    public void handleLearningEvent(LearningEvent le) {
        BackPropagation bp = (BackPropagation) le.getSource();
        System.out.println(bp.getCurrentIteration() + ") Network error: " + bp.getTotalNetworkError());
        System.out.print("Train: ");
        test(_trainSet);
        System.out.print("Test: ");
        test(_testSet);
    }
    
    public void test(DataSet ds){
        int i=1;
        boolean[] aciertos= new boolean[ds.size()];
       // System.out.println("");
        for(DataSetRow datasr : ds.getRows()){
            _mlp.setInput(datasr.getInput());//cogemos una imagen y la metemos a _mlp
            _mlp.calculate();//Calculamos
            double teorica[]= datasr.getDesiredOutput();//Lo que debería ser
            double error=_mlp.getLearningRule().getTotalNetworkError();
            double salida[]= _mlp.getOutput();//La salida de la red
    //        System.out.println(i+") Network error: "+error);
  //          System.out.println("Salida deseada: "+ getMaxPerceptron(teorica));//lo que debería salir
//            System.out.println("Perceptrón máximo:" + getMaxPerceptron(salida));//lo que sale
           
            //Me guarda en un array de booleans si se ha detectado bien o no
            if(getMaxPerceptron(teorica)==getMaxPerceptron(salida)){
                aciertos[i-1]=true;
            }
            else{
                aciertos[i-1]=false;
            }
            //Imprime el la imagen (número) por consola
            /*
            double[] dd=datasr.getInput();
            for(int j=0;j<28;j++){
                for(int k=0;k<28;k++){
                    int value=(int)(long) (dd[j*28+k]*255);
                    if(value>0)
                        System.out.print("ｱ");
                    else{
                        System.out.print(" ");}
                }
                System.out.println();
            }*/
            i++;
          
        }
        double count =0;
        for(int j=0;j<aciertos.length;j++){
            if(aciertos[j]){
                count++;
            }
        }
        double rate= (count*100)/aciertos.length;//la división es entera ....
        System.out.println("% de acierto: "+rate +" ("+ count +" ok;"+(aciertos.length-count)+" error)");
    }
    
    public int getMaxPerceptron(double salida[]){
        int perc=-1;
        double max=0;
        for(int i=0;i<salida.length;i++){
            if(salida[i]>max){
               // System.out.println(salida[i]);//Para ver la salida 
                perc=i;
                max=salida[i];
            }
        }
        return perc;
    }
    
    
    public DataSet _trainSet;
    public DataSet _testSet;
    public MultiLayerPerceptron _mlp;
    public BackPropagation _bp;
}
