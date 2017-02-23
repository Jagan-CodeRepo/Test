/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

/**
 *
 * @author jagan
 */
import java.io.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import weka.core.Attribute;
import weka.core.converters.CSVLoader;
public class ResultClassifier 
{
    List<String> list;
    List<String> AdvisoryList;
    List<Integer> noAdvisoryList;
    List<String> WarningList;
    List<String> StageList;
    // JDBC driver name and database URL
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    String DB_URL = "jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com/cropml";
    //  Database credentials
    String USER = "jaganroot";
    String PASS = "weather123";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs=null;
   // Instances training_data=null;            
    public ResultClassifier()
    {
        list=new ArrayList<String>();
        AdvisoryList=new ArrayList<String>();
        noAdvisoryList=new ArrayList<Integer>();
        WarningList=new ArrayList<String>();
        StageList=new ArrayList<String>();
        //STEP 2: Register JDBC driver
        try
        {
            
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage()); 
        }
    }
    public void addAdvisory(String Advisory)
    {
        AdvisoryList.add(Advisory);
    }
    public void addWarning(String Warning)
    {
        WarningList.add(Warning);
    }
    public void addStage(String Stage)
    {
        StageList.add(Stage);
    }
    public String getAdvisory(int index)
    {
        if(index<0 ||index>AdvisoryList.size())
        {
            System.out.println("Array out of bound exception arised");
            return null;
        }
        else
        {
            return AdvisoryList.get(index);
        }
    }
    public int getnoAdvisory(int index)
    {
        if(index<0 ||index>noAdvisoryList.size())
        {
            System.out.println("Array out of bound exception arised");
            return -1;
        }
        else
        {
            return noAdvisoryList.get(index);
        }
    }
    public String getWarning(int index)
    {
        if(index<0 ||index>WarningList.size())
        {
            System.out.println("Array out of bound exception arised");
            return null;
        }
        else
        {
            return WarningList.get(index);
        }
    }
    public String getStage(int index)
    {
        if(index<0 ||index>StageList.size())
        {
            System.out.println("Array out of bound exception arised");
            return null;
        }
        else
        {
            return StageList.get(index);
        }
    }
    public int getAdvisorySize()
    {
        return AdvisoryList.size();
    }
    public int getnoAdvisorySize()
    {
        return noAdvisoryList.size();
    }
    public int getWarningSize()
    {
        return WarningList.size();
    }
    public int getStageSize()
    {
        return StageList.size();
    }
    void addToList(String val)
    {
        list.add(val);
    }
    void removeAllfromList()
    {
        list.removeAll(list);
    }
    String MaxFrequency()
    {
        int i,max;
        Set<String> unique = new HashSet<String>(list);
        ArrayList<String> tkey=new ArrayList<String>();
        ArrayList<Integer> tcount=new ArrayList<Integer>();
        for (String key : unique) 
        {
            tkey.add(key);
            System.out.println(key + ": " + Collections.frequency(list, key));
            tcount.add(Collections.frequency(list, key));
        }
        max=0;
        for(i=1;i<tcount.size();i++)
        {
            if(tcount.get(max)<tcount.get(i))
            {
                max=i;
            }
        }
        System.out.println("Maximum Frequency :"+tkey.get(max));
        return tkey.get(max);
    }
    void getAdvisoryFromDB(String val,int ch)
    {
        try
        {
            String sql;
            int tot=0;
            if(ch==0)
                sql = "SELECT warning,controls FROM classcontroladvisory where classid='"+val+"'";
            else
                sql = "SELECT warning,controls FROM classclustercontroladvisory where classid='"+val+"'";
            rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while(rs.next())
            {
                tot++;
                System.out.println(rs.getString("warning"));
                addWarning(rs.getString("warning"));
                System.out.println(rs.getString("controls"));
                addAdvisory(rs.getString("controls"));
            }
            noAdvisoryList.add(tot);
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage()); 
        }
    }
    /*void getAdvisoryFromDB(String val)
    {
        try
        {
            String sql;
            sql = "SELECT advisory FROM classadvisory where classid='"+val+"'";
            rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while(rs.next())
            {
                System.out.println(rs.getString("advisory"));
                addAdvisory(rs.getString("advisory"));
            }
            
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage()); 
        }
    }*/
    void getStageFromDB()
    {
        try
        {
            String sql;
            Date cdate=new Date();
            SimpleDateFormat s1=new SimpleDateFormat("MM");
            int cd=Integer.parseInt(s1.format(cdate));
            System.out.println(cd);
            sql = "SELECT distinct stage FROM cropstage where month='"+cd+"'";
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while(rs.next())
            {
                addStage(rs.getString("stage"));
            }
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage()); 
        }
    }
    protected void finalize() throws Throwable 
    {
        //STEP 6: Clean-up environment
        if(rs!=null)
        {
            rs.close();
        }
        if(stmt!=null)
        {
            stmt.close();
        }
        if(conn!=null)
        {
            conn.close();
        }
    }
    public void getprediction(int NoOfDays,int ch,String filePath)
    {
        try
        {
            String tstage=null;
            BufferedReader br=null;
            /*File csv=new File("I:\\B.E Project\\Data\\AutoTrain.csv");
            CSVLoader loader = new CSVLoader();
            loader.setSource(csv);
            Instances training_data = loader.getDataSet();*/
            if(ch==0)
                br=new BufferedReader(new FileReader(filePath+"Data/Autotrain12.arff"));
            else
                br=new BufferedReader(new FileReader(filePath+"Data/ClusterAutotrain12.arff"));
            Instances training_data = new Instances(br);
            br.close();
            training_data.setClassIndex(training_data.numAttributes() - 1);
           /*File csvtest=new File("I:\\B.E Project\\Data\\TestFinal.csv");
            CSVLoader loader1 = new CSVLoader();
            loader1.setSource(csvtest);
            Instances testing_data = loader1.getDataSet();*/
            br=new BufferedReader(new FileReader(filePath+"Data/TestFinal.arff"));
            Instances testing_data = new Instances(br);
            br.close();
            testing_data.setClassIndex(testing_data.numAttributes() - 1);
            //if(training_data.equalHeaders(testing_data))
            //{
                String summary = training_data.toSummaryString();
                int number_samples = training_data.numInstances();
                int number_attributes_per_sample = training_data.numAttributes();
                System.out.println(testing_data.instance(0));
                System.out.println("Number of attributes in model = " + number_attributes_per_sample);
                System.out.println("Number of samples = " + number_samples);
                System.out.println("Summary: " + summary);
                System.out.println();
        
                // a classifier for decision trees:
                J48 j48 = new J48();
        
                // filter for removing samples:
                //Remove rm = new Remove();
                //rm.setAttributeIndices("1");  // remove 1st attribute
                // filtered classifier
                FilteredClassifier fc = new FilteredClassifier();
                //fc.setFilter(rm);
                fc.setClassifier(j48);
                // train using train.arff:
                fc.buildClassifier(training_data);
                // test using test.arff:
                getStageFromDB();
                System.out.println("Testing instances: "+testing_data.numInstances());
                for (int i = 1; i <= testing_data.numInstances(); i++) 
                {
                    double pred = fc.classifyInstance(testing_data.instance(i-1));
                    String s1=testing_data.classAttribute().value((int) pred);
                    System.out.println(i+" Predicted value: " + s1);
                    if(i%24==0)
                    {
                        String tclass=MaxFrequency();
                        getAdvisoryFromDB(tclass,ch);
                        removeAllfromList();
                        addToList(s1);
                    }
                    else
                    {
                        addToList(s1);
                    }
          
                }
            //}
            /*else
            {
                throw new IllegalStateException("Incompatible train and test set!"); 
            }*/
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e.getMessage()+"Source:");
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
}
