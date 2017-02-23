/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

/**
 *
 * @author jagan
 */
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;

 
public class CSV2Arff {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public void CsvtoArff(String csvFile,String arffFile) {
        try {
            File arff =new File(arffFile);
            File csv=new File(csvFile);
            // load CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(csv);
            Instances data = loader.getDataSet();
            System.out.println(data.numInstances());
            
                
            // save ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(arff);
            //saver.setDestination(arff);
            saver.writeBatch();
            
            
            
            System.out.println("Saved");
        } catch (Exception ex) {
            System.err.println("CSV to Arff: "+ex.getMessage());
        }
        
  }
}