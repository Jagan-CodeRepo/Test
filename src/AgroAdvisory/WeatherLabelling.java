/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

import com.cendrillon.clustering.ClusterDocuments;
import java.io.*;
import java.sql.*;
import java.text.BreakIterator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

/**
 *
 * @author jagan
 */
public class WeatherLabelling extends WeatherArray
{
    /*ArrayList<String> rain;
    ArrayList<String> minTemp;
    ArrayList<String> maxTemp;
    ArrayList<String> cloud;
    ArrayList<String> minHumidity;
    ArrayList<String> maxHumidity;*/
    ArrayList<String> cropStage;
    ArrayList<String> advisory;
    ArrayList<String> advisoryLabel;
    ArrayList<String> uniqueAdvisory;
    String filePath;
    // JDBC driver name and database URL
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    String DB_URL = "jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com/cropml";
    //  Database credentials
    String USER = "jaganroot";
    String PASS = "weather123";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs=null;
    public WeatherLabelling(String filePath)
    {
        super();
        /*rain=new ArrayList<String>();
        minTemp=new ArrayList<String>();
        maxTemp=new ArrayList<String>();
        cloud=new ArrayList<String>();
        minHumidity=new ArrayList<String>();
        maxHumidity=new ArrayList<String>();*/
        cropStage=new ArrayList<String>();
        advisory=new ArrayList<String>();
        advisoryLabel=new ArrayList<String>();
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
    public void addRain(String val)
    {
        rain.add(val);
    }
    public void addMinTemp(String val)
    {
        minTemp.add(val);
    }
    public void addMaxTemp(String val)
    {
        maxTemp.add(val);
    }
    public void addCloud(String val)
    {
        cloud.add(val);
    }
    public void addMinHumidity(String val)
    {
        minHumidity.add(val);
    }
    public void addMaxHumidity(String val)
    {
        maxHumidity.add(val);
    }
    public void addCropStage(String val)
    {
        cropStage.add(val);
    }
    public void addAdvisory(String val)
    {
        advisory.add(val);
    }
    public void addAdvisoryLabel(String val)
    {
        advisoryLabel.add(val);
    }
    public String getRain(int index)
    {
        return rain.get(index);
    }
    public String getMinTemp(int index)
    {
        return minTemp.get(index);
    }
    public String getMaxTemp(int index)
    {
        return maxTemp.get(index);
    }
    public String getCloud(int index)
    {
        return cloud.get(index);
    }
    public String getMinHumidity(int index)
    {
        return minHumidity.get(index);
    }
    public String getMaxHumidity(int index)
    {
        return maxHumidity.get(index);
    }
    public String getCropStage(int index)
    {
        return cropStage.get(index);
    }
    public String getAdvisory(int index)
    {
        return advisory.get(index);
    }
    public String getUniqueAdvisory(int index)
    {
        return uniqueAdvisory.get(index);
    }
    public String getAdvisoryLabel(int index)
    {
        return advisoryLabel.get(index);
    }
    public int advisoryLabelSize()
    {
        return advisoryLabel.size();
    }
    public int uniqueAdvisorySize()
    {
        return uniqueAdvisory.size();
    }
    public void writeCSV(int ch)
    {
        BufferedWriter writer = null;
        try 
        {
            int i;
            StringBuffer sb=new StringBuffer();
            sb.append("Rainfall,MinimumTemperature,MaximumTemperture,Cloud,MinimumHumidity,MaximumHumidity,CropStage,Advisory\n");
            if(ch==1)
            {
				System.out.println("Auto tain Path: "+filePath);
                BufferedReader br=new BufferedReader(new FileReader(filePath+"Data/Autotrain12.arff"));
                Instances training_data = new Instances(br);
                for(i=0;i<training_data.numInstances();i++)
                {
                    sb.append(training_data.instance(i).toString()+"\n");
                }
                br.close();
                /*BufferedReader br = null;
                br=new BufferedReader(new FileReader("I:\\B.E Project\\Data\\AutoTrain.csv"));
                String str;
                str=br.readLine();
                while((str=br.readLine())!=null)
                {
                    sb.append(str+"\n");
                }
                br.close();
                */
            }
            
            for(i=0;i<rain.size()&&i<minTemp.size()&& i<maxTemp.size()&&i<cloud.size()&&i<minHumidity.size()&&i<maxHumidity.size()&&i<cropStage.size()&&i<advisoryLabel.size();i++)
            {
                sb.append(rain.get(i)+","+maxTemp.get(i)+","+minTemp.get(i)+","+cloud.get(i)+","+minHumidity.get(i)+","+maxHumidity.get(i)+","+cropStage.get(i)+","+advisoryLabel.get(i)+"\n");
            }
			System.out.println("Auto tain Path: "+filePath);
            writer = new BufferedWriter(new FileWriter(filePath+"Data/Autotrain.csv"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            //new CSV2Arff().CsvtoArff("I:\\B.E Project\\Data\\Autotrain.csv","I:\\B.E Project\\Data\\Autotrain12.arff");
        } catch (IOException ex) {
            System.err.println("Writecsv: "+ex.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
               System.err.println(ex.getMessage());
            }
        }
    }
    public  boolean equalLists(String str1, String str2)
    {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(str1);
        int start = iterator.first();
        ArrayList<String> one=new ArrayList<String>();
        ArrayList<String> two=new ArrayList<String>();
        for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
        {
            one.add(str1.substring(start,end));
        } 
        BreakIterator iterator1 = BreakIterator.getSentenceInstance(Locale.US);
        iterator1.setText(str2);
        start = iterator1.first();
        for (int end = iterator1.next();end != BreakIterator.DONE;start = end, end = iterator1.next()) 
        {
            two.add(str2.substring(start,end));
        } 
        if (one == null && two == null)
        {
            return true;
        }
        if((one == null && two != null)|| one != null && two == null || one.size() != two.size())
        {
        return false;
        }
        Collections.sort(one);
        Collections.sort(two);      
        return one.equals(two);
    }
    public void findUniqueAdvisory()
    {
        uniqueAdvisory= new ArrayList<String>();
        for(String str:advisory)
        {
            if(!uniqueAdvisory.contains(str))
            {
                uniqueAdvisory.add(str);
            }
        }
        for(int i=0;i<uniqueAdvisory.size();i++)
        {
            for(int j=i+1;j<uniqueAdvisory.size();j++)
            {
                if(equalLists(uniqueAdvisory.get(i),uniqueAdvisory.get(j)))
                {
                    uniqueAdvisory.remove(j);
                }
            }
        }
    }
    public void storeLabel()
    {
        BufferedWriter writer = null;
        try 
        {
            StringBuffer sb=new StringBuffer();
            StringBuffer clu =new StringBuffer();
            clu.append("[");
            int j=1;
            System.out.println("Unique Advisory->"+uniqueAdvisory.size());
            PreparedStatement pstmt=null;
            //pstmt=conn.prepareStatement("delete from classadvisory;");
            //pstmt.executeUpdate();
            pstmt=conn.prepareStatement("delete from classcontroladvisory;");
            pstmt.executeUpdate();
            System.out.println("Deleted old table content");
            for(String val : uniqueAdvisory)
            {
                String label="c"+j;
                sb.append(label+" :\n"+val+"\n");
                //pstmt=conn.prepareStatement("insert into classadvisory values(?,?);");
                //pstmt.setString(1, label);
                //pstmt.setString(2, val);
                //pstmt.executeUpdate();
                System.out.println(label);
                BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
                iterator.setText(val);
                int start = iterator.first();
                for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
                {
                    String tmp=val.substring(start,end);
                    System.out.println(tmp);
                    String temp[]=tmp.split("\\$");
                    //System.out.println(temp[0]+"\n"+temp[1]);
                    if(temp.length==2)
                    {
                        pstmt=conn.prepareStatement("insert into classcontroladvisory values(?,?,?);");
                        pstmt.setString(1, label);
                        pstmt.setString(2, temp[0]);
                        pstmt.setString(3, temp[1]);
                        pstmt.executeUpdate();
                        System.out.println("Inserted");
                    }
                }
                clu.append("{\"content\": \""+val+"\", \"id\": "+j+", \"title\": \""+label+"\"}, ");
                j++;
            }
            clu.append("]");
            writer = new BufferedWriter(new FileWriter(filePath+"Data/Autoclasslabel.csv"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            writer = new BufferedWriter(new FileWriter(filePath+"Data/Autoclasslabel.txt"));
            writer.write(clu.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex) 
        {
            System.err.println("Store label: "+ex.getMessage());
        } finally 
        {
            try 
            {
                writer.close();
            } catch (IOException ex) 
            {
               System.err.println(ex.getMessage());
            }
        }
    }
    public void assignLabel()
    {
        int ch=0;
        for(int i=0;i<advisory.size();i++)
        {
            int j=1;
            int status=0;
            for(String str:uniqueAdvisory)
            {
                
                if(equalLists(advisory.get(i),str))
                {
                    String label="c"+j;
                    advisoryLabel.add(label);
                    status=1;
                    break;
                }
                j++;
            }
            if(status==0)
            {
                uniqueAdvisory.add(advisory.get(i));
                String label="c"+j;
                advisoryLabel.add(label);
                ch=1;
                //j++;
            }
        }
        if(ch==1)
        {
            storeLabel();
        }
    }
    public void retrieveLabel()
    {
        uniqueAdvisory= new ArrayList<String>();
        ArrayList<String> label=new ArrayList<String>();
        try
        {
            String sql;
            sql = "SELECT distinct classid FROM classcontroladvisory;";
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                label.add(rs.getString("classid"));
            }
            for(int i=0;i<label.size();i++)
            {
                sql = "SELECT * FROM classcontroladvisory where classid='"+label.get(i) +"';";
                rs = stmt.executeQuery(sql);
                System.out.println(label.get(i));
                String str="";
                while(rs.next())
                {
                    str=str + rs.getString("warning")+" $ "+rs.getString("controls")+"\n";
                    System.out.print(rs.getString("warning")+" $ ");
                    System.out.println(rs.getString("controls"));
                    //uniqueAdvisory.add(rs.getString("advisory"));
                }
                uniqueAdvisory.add(str.replaceAll("\\s+", " "));
                
            }
            //STEP 5: Extract data from result set
            
        }
        catch(Exception e)
        {
           System.out.println("Retrive label: "+e.getMessage()); 
        }
    }
    
}
