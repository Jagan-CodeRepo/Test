/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

/**
 *
 * @author jagan
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import weka.core.Instances;
import weka.core.Utils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.converters.CSVLoader;

/**
 * Performs a single run of cross-validation.
 *
 * Command-line parameters:
 * <ul>
 *    <li>-t filename - the dataset to use</li>
 *    <li>-x int - the number of folds to use</li>
 *    <li>-s int - the seed for the random number generator</li>
 *    <li>-c int - the class index, "first" and "last" are accepted as well;
 *    "last" is used by default</li>
 *    <li>-W classifier - classname and options, enclosed by double quotes; 
 *    the classifier to cross-validate</li>
 * </ul>
 *
 * Example command-line:
 * <pre>
 * java CrossValidationSingleRun -t anneal.arff -c last -x 10 -s 1 -W "weka.classifiers.trees.J48 -C 0.25"
 * </pre>
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CrossValidationSingleRun {

  /**
   * Performs the cross-validation. See Javadoc of class for information
   * on command-line parameters.
   *
   * @param args        the command-line parameters
   * @throws Excecption if something goes wrong
   */
  public Evaluation crossValidate(String filename){
       Evaluation eval=null;
        try {
            BufferedReader br=new BufferedReader(
                              new FileReader(filename));
            // loads data and set class index
            Instances data = new Instances(br);
            br.close();
            /*File csv=new File(filename);
            CSVLoader loader = new CSVLoader();
            loader.setSource(csv);
            Instances data = loader.getDataSet();*/
            data.setClassIndex(data.numAttributes() - 1);

            // classifier
            String[] tmpOptions;
            String classname="weka.classifiers.trees.J48 -C 0.25";
            tmpOptions     = classname.split(" ");
            classname      = "weka.classifiers.trees.J48";
            tmpOptions[0]  = "";
            Classifier cls = (Classifier) Utils.forName(Classifier.class, classname, tmpOptions);

            // other options
            int seed  = 1;
            int folds = 10;

            // randomize data
            Random rand = new Random(seed);
            Instances randData = new Instances(data);
            randData.randomize(rand);
            if (randData.classAttribute().isNominal())
              randData.stratify(folds);

            // perform cross-validation
           eval = new Evaluation(randData);
            for (int n = 0; n < folds; n++) {
              Instances train = randData.trainCV(folds, n);
              Instances test = randData.testCV(folds, n);
              // the above code is used by the StratifiedRemoveFolds filter, the
              // code below by the Explorer/Experimenter:
              // Instances train = randData.trainCV(folds, n, rand);

              // build and evaluate classifier
              Classifier clsCopy = Classifier.makeCopy(cls);
              clsCopy.buildClassifier(train);
              eval.evaluateModel(clsCopy, test);
            }

            // output evaluation
            System.out.println();
            System.out.println("=== Setup ===");
            System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
            System.out.println("Dataset: " + data.relationName());
            System.out.println("Folds: " + folds);
            System.out.println("Seed: " + seed);
            System.out.println();
            System.out.println("Correctly Classified Instances: "+eval.correct());
            System.out.println("Percentage of Correctly Classified Instances: "+eval.pctCorrect());
            System.out.println("InCorrectly Classified Instances: "+eval.incorrect());
            System.out.println("Percentage of InCorrectly Classified Instances: "+eval.pctIncorrect());
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return eval;
  }
}

