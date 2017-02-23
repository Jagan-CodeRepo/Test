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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.text.SimpleDateFormat;
import java.util.*;
import weka.core.Instances;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
public class WeatherRetrieval
{
    Calendar cal;
    ArrayList  <String> retrieveDate;
    ArrayList  <String> nretrieveDate;
    ArrayList <WeatherElement> wElement;
    URL url;
    String cdate;
    SimpleDateFormat s,s1;
    ArrayList <String> currDatearr;
    ArrayList <String> curractualDatearr;
    ArrayList <Integer> minHumidity;
    ArrayList <Integer> maxHumidity;
    ArrayList <Integer> rain;
    ArrayList <Integer> maxTemp;
    ArrayList <Integer> minTemp;
    ArrayList <Integer> humidity;
    ArrayList <Integer> temparr;
    ArrayList <Integer> cloud;
    String filePath;
    public WeatherRetrieval(String filePath)
    {
        cal = Calendar.getInstance();
        //cal.set(2014,01, 04);
        retrieveDate=new ArrayList();
        nretrieveDate=new ArrayList();
        wElement=new ArrayList<WeatherElement>();
        currDatearr=new ArrayList();
        curractualDatearr=new ArrayList();
        minHumidity=new ArrayList();
        maxHumidity=new ArrayList();
        rain=new ArrayList();
        maxTemp=new ArrayList();
        minTemp=new ArrayList();
        humidity=new ArrayList();
        temparr=new ArrayList();
        cloud=new ArrayList();
        s=new SimpleDateFormat("yyyy-MM-dd");
        s1=new SimpleDateFormat("dd-MM-yyyy");
        cdate=s.format(cal.getTime()).toString();
        this.filePath=filePath;
    }
    public int retrieveDateSize()
    {
        return retrieveDate.size();
    }
    public int currDateSize()
    {
        return currDatearr.size();
    }
    public String getretrieveDate(int index)
    {
        //Date d=new Date(retrieveDate.get(index));
        //System.out.println("date: "+d);
        return retrieveDate.get(index);
    }
    public int newretrieveDateSize()
    {
        return nretrieveDate.size();
    }
    public String getnewretrieveDate(int index)
    {
        return nretrieveDate.get(index);
    }
    public int getWeatherElementSize()
    {
        return wElement.size();
    }
    public WeatherElement getWeatherElement(int index)
    {
        return wElement.get(index);
    }
    public int findminHumidity(String dat)
    {
        int i,min=100;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(min>we.minHumidity)
                {
                    min=we.minHumidity;
                }
            }
        }
        return min;
    }
    public int findmaxHumidity(String dat)
    {
        int i,max=0;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(max<we.maxHumidity)
                {
                    max=we.maxHumidity;
                }
            }
        }
        return max;
    }
    public int findminTemp(String dat)
    {
        int i,min=100;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(min>we.minTemp)
                {
                    min=we.minTemp;
                }
            }
        }
        return min;
    }
    public int findmaxTemp(String dat)
    {
        int i,max=0;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(max<we.maxTemp)
                {
                    max=we.maxTemp;
                }
            }
        }
        return max;
    }
    public int findmaxRain(String dat)
    {
        int i,max=0;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(max<we.rain)
                {
                    max=we.rain;
                }
            }
        }
        return max;
    }
    public int findmaxCloud(String dat)
    {
        int i,max=0;
        for(i=0;i<getWeatherElementSize();i++)
        {
            WeatherElement we=getWeatherElement(i);
            
            if(we.retrieveDate.equals(dat))
            {
                if(max<we.cloudCover)
                {
                    max=we.cloudCover;
                }
            }
        }
        return max;
    }
    public void findMinMax(int[] a,int ch)
    {
        int smallest = a[0];
        int largetst = a[0];
        for(int i=1; i< a.length; i++)
        {
            if(a[i] > largetst)
                largetst =a[i];
            else if (a[i] < smallest)
                smallest = a[i];
        }
        if(ch==0)
        {
            minHumidity.add(smallest);
            maxHumidity.add(largetst);
        }
        else if(ch==1)
        {
            minTemp.add(smallest);
            maxTemp.add(largetst);
        }
        
    }
    public void assigndate(int NoOfDays)
    {
        
        int i;
        System.out.println(s1.format(cal.getTime()).toString());
        nretrieveDate.add(s1.format(cal.getTime()).toString());
        retrieveDate.add(s.format(cal.getTime()).toString());
        for(i=1;i<=NoOfDays;i++)
        {
            cal.add(Calendar.DATE, 1);
            System.out.println(s1.format(cal.getTime()).toString());
            retrieveDate.add(s.format(cal.getTime()).toString());
            nretrieveDate.add(s1.format(cal.getTime()).toString());
        }
    }
    public void writearff(int ch)
    {
        try
        {
            String s;
            int i;
            BufferedReader br=null;
            if(ch==0)
                 br=new BufferedReader(new FileReader(filePath+"Data/Autotrain12.arff"));
            else
                br=new BufferedReader(new FileReader(filePath+"Data/ClusterAutotrain12.arff"));
            Instances testing_data = new Instances(br);
            br.close();
            testing_data.delete();
            StringBuffer sb=new StringBuffer();
            sb.append(testing_data.toString());
            //sb.append("Rainfall,MinimumTemperature,MaximumTemperture,Cloud,MaximumHumidity,MinimumHumidity,CropStage,Advisory\n");
            // JDBC driver name and database URL
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
            String DB_URL = "jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com/cropml";

            //  Database credentials
            String USER = "jaganroot";
            String PASS = "weather123";
            Connection conn = null;
            Statement stmt = null;
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
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
                System.out.println(rs.getString("stage"));
                for(i=0;i<currDateSize();i++)
                {
                    WeatherElement we=wElement.get(i);
                    s=we.getRain()+","+we.getMaxTemp()+","+we.getMinTemp()+","+we.getCloud()+","+we.getMinHumidity()+","+we.getMaxHumidity()+","+rs.getString("stage") +",?\n";
                    //s=rain.get(i)+","+maxTemp.get(i)+","+minTemp.get(i)+","+cloudCover.get(i)+","+minHumidity.get(i)+","+maxHumidity.get(i)+","+rs.getString("stage") +",?\n";
                    sb.append(s);
                }
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+"Data/TestFinal.arff"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();    
        }
        catch(Exception e)
        {
           System.out.println(e.getMessage()); 
        }
    }
     public void readXml(String loc,int NoOfDays,int ch)
    {
        System.out.println("location:" +loc);
        try
        {
            int i,j=0,k;
            String tmp;
            String temp[];
            int minHumidityval,maxHumidityval,rainval,maxTempval,minTempval,cloudCover;
            String rDate;
            int[] tmparr=new int[3];
            assigndate(NoOfDays);
            String filename=filePath+"XML/"+loc+cdate+".xml";
            File fXmlFile = new File(filename);
            if(fXmlFile.exists())
            {
                FileInputStream file = new FileInputStream(new File(filename));
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder =  builderFactory.newDocumentBuilder();
                Document xmlDocument = builder.parse(file);
                //Document xmlDocument = builder.parse(new URL("http://localhost:8080/WeBFS/Xml/World/"+loc+cdate+".xml").openStream());
                XPath xPath =  XPathFactory.newInstance().newXPath();
                String expression;
                NodeList nodeList;
                for(j=0;j<retrieveDateSize();j++)
                {
                    if(j!=0 && j!=retrieveDateSize()-1)
                    {
                        System.out.println("*************************\nRainfall");
                        expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/precipMM";
                        System.out.println(expression);
                        nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
                        //int max=0,min=100;
                        for (i = 0; i < nodeList.getLength(); i++) 
                        {
                            String tmp1=nodeList.item(i).getFirstChild().getNodeValue();
                            System.out.println(i+"\t"+tmp1); 
                            rainval=(int)Double.parseDouble(tmp1);
                            rain.add(rainval);
                            currDatearr.add(getretrieveDate(j));
                            curractualDatearr.add(getnewretrieveDate(j));
                        }
                    }
                    System.out.println("*************************\nTemperature");
                    if(j==0)
                    {
                        expression="/data/weather[date='"+getretrieveDate(j)+"']/hourly[time=2330]/tempC";
                    }
                    else if(j==retrieveDateSize()-1)
                    {
                        expression="/data/weather[date='"+getretrieveDate(j)+"']/hourly[time=230]/tempC";
                    }
                    else
                    {
                    expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/tempC";
                    }
                    //expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/tempC";
                    System.out.println(expression);
                    nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
                    for ( i = 0; i < nodeList.getLength(); i++) 
                    {
                        String tmp1=nodeList.item(i).getFirstChild().getNodeValue();
                        System.out.println(i+"\t"+tmp1); 
                        int temperature=(int)Double.parseDouble(tmp1);
                        temparr.add(temperature);
                    }
                    System.out.println("*************************\nHumidity");
                    if(j==0)
                    {
                        expression="/data/weather[date='"+getretrieveDate(j)+"']/hourly[time=2330]/humidity";
                    }
                    else if(j==retrieveDateSize()-1)
                    {
                        expression="/data/weather[date='"+getretrieveDate(j)+"']/hourly[time=230]/humidity";
                    }
                    else
                    {
                    expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/humidity";
                    }
                    //expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/humidity";
                    System.out.println(expression);
                    nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
                    for (i = 0; i < nodeList.getLength(); i++) 
                    {
                        String tmp1=nodeList.item(i).getFirstChild().getNodeValue();
                        System.out.println(i+"\t"+tmp1);
                        int humidityval=(int)Double.parseDouble(tmp1);
                        humidity.add(humidityval);
                    }
                    if(j!=0 && j!=retrieveDateSize()-1)
                    {
                        System.out.println("*************************\nCloud");
                        expression = "/data/weather[date='"+getretrieveDate(j)+"']/hourly/cloudcover";
                        System.out.println(expression);
                        nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
                        for ( i = 0; i < nodeList.getLength(); i++) 
                        {
                            int cloudval=(int)Math.round(Double.parseDouble(nodeList.item(i).getFirstChild().getNodeValue())/12.5);
                            System.out.println(i+"\t"+cloudval);
                            cloud.add(cloudval);
                        }
                    }
                    //WeatherElement we=new WeatherElement(getretrieveDate(j),getnewretrieveDate(j),rain,minTemp,maxTemp,minHumidity,maxHumidity,cloudCover);
                    //wElement.add(we);
                }
                System.out.println("*************************\n");
                for(i=1;i<humidity.size()-1;i++)
                {
                    tmparr[0]=humidity.get(i-1);
                    tmparr[1]=humidity.get(i);
                    tmparr[2]=humidity.get(i+1);
                    findMinMax(tmparr,0);
                }
                for(i=1;i<temparr.size()-1;i++)
                {
                    tmparr[0]=temparr.get(i-1);
                    tmparr[1]=temparr.get(i);
                    tmparr[2]=temparr.get(i+1);
                    findMinMax(tmparr,1);
                }
                humidity.removeAll(humidity);
                temparr.removeAll(temparr);
                for( k=0;k<currDatearr.size()&&k<curractualDatearr.size()&&k<rain.size()&&k<maxTemp.size()&&k<minTemp.size()&&k<minHumidity.size()&&k<maxHumidity.size()&&k<cloud.size();k++)
                    {
                        System.out.println("Count: "+k);
                        System.out.println("Date :"+currDatearr.get(k));
                        System.out.println("Date :"+curractualDatearr.get(k));
                        System.out.println("Rain: "+rain.get(k));
                        System.out.println("Max Temp: "+maxTemp.get(k));
                        System.out.println("Min Temp: "+minTemp.get(k));
                        System.out.println("Min Humidity: "+minHumidity.get(k));
                        System.out.println("max Humidity: "+maxHumidity.get(k));
                        System.out.println("Cloud Cover: "+cloud.get(k));
                        System.out.println();
                        WeatherElement we=new WeatherElement(currDatearr.get(k),curractualDatearr.get(k),rain.get(k),minTemp.get(k),maxTemp.get(k),minHumidity.get(k),maxHumidity.get(k),cloud.get(k));
                        wElement.add(we);
                    }
                retrieveDate.remove(retrieveDate.size()-1);
                retrieveDate.remove(0);
                nretrieveDate.remove(nretrieveDate.size()-1);
                nretrieveDate.remove(0);
                writearff(ch);
                file.close();
            }
            else
            {
                //File not exist
            }
            //fXmlFile.delete();
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public int saveXML(String loc,int NoOfDays,int ch) 
    {
        try 
        {
            //save to this filename
            String filename=filePath+"XML/"+loc+cdate+".xml";
            File file = new File(filename);
            if (!file.exists()) 
            {
                // get URL content
                url = new URL("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=9xpbcvejcemnsdd2yjyqrmch&num_of_days="+NoOfDays+"&tp=3&format=xml&cc=no&q="+loc);
                //url = new URL("http://api.openweathermap.org/data/2.5/forecast?mode=xml&q="+loc);
                URLConnection conn = url.openConnection();
 
                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
		file.createNewFile();
                //use FileWriter to write file
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                while ((inputLine = br.readLine()) != null) 
                {
                    bw.write(inputLine);
                    bw.write("\n");
                }
                bw.close();
                br.close();
                fw.close();
                System.out.println("Done");
                readXml(loc,NoOfDays-1,ch);
            }
            else
            {
                readXml(loc,NoOfDays-1,ch);
            }
            return 1;
        }
        catch (MalformedURLException e) 
        {
            e.printStackTrace();
            return 0;
	} 
        catch (IOException e) 
        {
            e.printStackTrace();
            return 0;
	}
    }
    public String saveXMLLatLng(String lat,String lng) 
    {
        URL url;
        Calendar cal= Calendar.getInstance();
        String cdate;
        String loc;
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        cdate=s.format(cal.getTime()).toString();
        try 
        {
            //save to this filename
            String filename=filePath+"XML/latitude.xml";
            File file = new File(filename);
            
            if (!file.exists()) 
            {
                // get URL content
                url = new URL("http://api.openweathermap.org/data/2.5/forecast?mode=xml&lat="+lat+"&lon="+lng);
                //url = new URL("http://api.openweathermap.org/data/2.5/forecast?mode=xml&lat=10.722682672936303&lon=79.0631103515625");
                
                URLConnection conn = url.openConnection();
 
                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
		file.createNewFile();
                //use FileWriter to write file
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                while ((inputLine = br.readLine()) != null) 
                {
                    bw.write(inputLine);
                    bw.write("\n");
                }
                bw.close();
                br.close();
                fw.close();
                System.out.println("Done");
            }
            //System.out.println("location:" +loc);
            File fXmlFile = new File(filename);
            if(fXmlFile.exists())
            {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
 
        	//optional, but recommended
                //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                doc.getDocumentElement().normalize();
 
                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
                NodeList lList = doc.getElementsByTagName("location");
                Node lNode=lList.item(0);
 
                System.out.println("\nCurrent Element :" + lNode.getNodeName());
 
		if (lNode.getNodeType() == Node.ELEMENT_NODE) 
                {
 
                    Element eElement = (Element) lNode;
                    NodeList nameList = doc.getElementsByTagName("name");
                    for (int i = 0; i < nameList.getLength(); i++) 
                    {
                        Node nameNode = nameList.item(i);
                        System.out.println("\nCurrent Element :" + nameNode.getNodeName());
                        if (nameNode.getNodeType() == Node.ELEMENT_NODE) 
                        {
                            Element nameElement = (Element) nameNode;
                            loc=nameElement.getTextContent();
                            System.out.println("latitude and longitude location:"+loc);
                            File newxml=new File(filePath+"XML\\"+loc+cdate+".xml");
                            if(!newxml.exists())
                            {
                                if(fXmlFile.renameTo(newxml))
                                {
                                    System.out.println("Renamed: "+loc);
                                    return loc;
                                }
                            }
                            else
                            {
                                fXmlFile.delete();
                                return loc;
                            }
                        }
                    }
                    
                }
           
        } 
        
        }
        catch (MalformedURLException e) 
        {
            e.printStackTrace();
	} 
        catch (Exception e) 
        {
            e.printStackTrace();
	}
        return null;
    }
}
