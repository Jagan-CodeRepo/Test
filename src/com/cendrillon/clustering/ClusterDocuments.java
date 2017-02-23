package com.cendrillon.clustering;

import AgroAdvisory.ClusterSampleList;
import java.io.*;
import java.sql.*;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

/**
 * This class implements clustering of
 * text documents using Cosine or Jaccard distance between the feature vectors of the documents
 * together with k means clustering. The number of clusters is adapted so that the ratio of the
 * intracluster to intercluster distance is below a specified threshold.
 */
public class ClusterDocuments 
{
	private static final int CLUSTERING_ITERATIONS = 50;
	private static final double CLUSTERING_THRESHOLD = 0.1;
	private static final int NUM_FEATURES = 1000;
        // JDBC driver name and database URL
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com/cropml";
        //  Database credentials
        String USER = "jaganroot";
        String PASS = "weather123";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs=null;
        Instances training_data;
        ClusterList clusterList;
        String filePath;
        public ClusterDocuments(String filePath)
        {
            this.filePath=filePath;
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
        
        public int getnewCluster(String title)
        {
            for(int i=0;i<clusterList.size();i++)
                {
                    for(int j=0;j<clusterList.get(i).size();j++)
                    {
                        if(title.equals(clusterList.get(i).get(j).getTitle()))
                        {
                            return (i+1);
                        }
                        
                    }
                }
            return -1;
        }
        public ArrayList<ClusterSampleList> getClusterContent()
        {
            ArrayList<ClusterSampleList> cslList=new ArrayList<ClusterSampleList>();
            try 
            {
                PreparedStatement pstmt=null;
                //pstmt=conn.prepareStatement("delete from classadvisory;");
                //pstmt.executeUpdate();
                pstmt=conn.prepareStatement("delete from classclustercontroladvisory;");
                pstmt.executeUpdate();
                System.out.println("Deleted old table content");
                for(int i=0;i<clusterList.size();i++)
                {
                    String label="c"+(i+1);
                    ClusterSampleList csl=new ClusterSampleList(label);
                    System.out.println("Cluster"+(i+1));
                    StringBuffer sb=new StringBuffer();
                    for(int j=0;j<clusterList.get(i).size();j++)
                    {
                        //System.out.print(clusterList.get(i).get(j).getTitle()+"\t");
                        //System.out.println(clusterList.get(i).get(j).getContents());
                        sb.append(clusterList.get(i).get(j).getContents());
                    }
                    ArrayList<String> cList=new ArrayList<String>();
                    BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
                    iterator.setText(sb.toString());
                    int start = iterator.first();
                    for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
                    {
                        cList.add(sb.toString().substring(start, end));
                        System.out.println(sb.toString().substring(start, end));
                    }
                    ArrayList<String> uniqueCluster=new ArrayList<String>();
                    for(String str:cList)
                    {
                        if(!uniqueCluster.contains(str))
                        {
                            uniqueCluster.add(str);
                        }
                    }
                    System.out.println("Unique cluster->");
                    for(String val : uniqueCluster)
                    {
                        System.out.println(val);
                        csl.addClusterClass(val);
                        String temp[]=val.split("\\$");
                        //System.out.println(temp[0]+"\n"+temp[1]);
                        if(temp.length==2)
                        {
                            pstmt=conn.prepareStatement("insert into classclustercontroladvisory values(?,?,?);");
                            pstmt.setString(1, label);
                            pstmt.setString(2, temp[0]);
                            pstmt.setString(3, temp[1]);
                            pstmt.executeUpdate();
                            System.out.println("Inserted");
                        }
                    }
                    System.out.println();
                    cslList.add(csl);
                }
                
            } 
            catch (Exception ex) {
            System.out.println(ex.getMessage());
            }
        return cslList;
        }
        public Instances modifyClassLabel()
        {
            try 
            {
                int i;
                training_data = new Instances(new BufferedReader(new FileReader(filePath+"Data/Autotrain12.arff")));
                //training_data.setClassIndex(training_data.numAttributes()-1);
                StringBuffer sb=new StringBuffer();
                String clu;
                sb.append("Rainfall,MinimumTemperature,MaximumTemperture,Cloud,MinimumHumidity,MaximumHumidity,CropStage,Advisory\n");
                for(i=0;i<training_data.numInstances();i++)
                {
                    //System.out.print(training_data.instance(i).stringValue(training_data.numAttributes()-1)+":");
                    clu="c"+getnewCluster(training_data.instance(i).stringValue(training_data.numAttributes()-1));
                    training_data.instance(i).setValue(training_data.numAttributes()-1, clu);
                    sb.append(training_data.instance(i).toString()+"\n");
                }
                //getClusterContent(clusterList);
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+"Data/ClusterAutotrain12.csv"));
                writer.write(sb.toString());
                writer.flush();
                writer.close();
            } 
            catch (Exception ex) 
            {
                System.err.println(ex.getMessage());
            }
            return training_data;
        }
	/**
	 * Cluster the text documents in the provided file. The clustering process consists of parsing and
	 * encoding documents, and then using Clusterer with a specific Distance measure.
	 */
	public  void textToCluster() 
        {
            try 
            {
                BufferedReader in = new BufferedReader(new FileReader(filePath+"Data/Autoclasslabel.txt"));
                String input = in.readLine();
                in.close();
                DocumentList documentList = new DocumentList(input);
                Encoder encoder = new TfIdfEncoder(NUM_FEATURES);
                encoder.encode(documentList);
                DistanceMetric distance = new CosineDistance();
                Clusterer clusterer = new KMeansClusterer(distance, CLUSTERING_THRESHOLD, CLUSTERING_ITERATIONS);
                clusterList = clusterer.cluster(documentList);
                System.out.println(clusterList);
            
            } 
            catch (Exception ex) 
            {
            System.err.println(ex.getMessage());
            }
        
	}
}