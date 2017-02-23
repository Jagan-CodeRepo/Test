/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;
import java.io.*;
import java.text.BreakIterator;
import java.util.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/**
 *
 * @author jagan
 */
public class DocumentExtraction 
{
    public int checkKeywords(String filePath,String text)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/keywords.lst"));
            String ip=br.readLine();
            while(ip!=null)
            {
                if(text.contains(ip))
                {
                    status=1;
                    return status;
                }
                else
                {
                    ip=br.readLine();
                }
            }
            br.close();
        } catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
        } finally 
        {
            try 
            {
                br.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }finally 
            {
                try 
                {
                    br.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return status;
    }
    public int checkRemoveKeywords(String filePath,String text)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/removekeywords.lst"));
            String ip=br.readLine();
            while(ip!=null)
            {
                if(text.contains(ip))
                {
                    status=1;
                    return status;
                }
                else
                {
                    ip=br.readLine();
                }
            }
            br.close();
        } catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
        } finally 
        {
            try 
            {
                br.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return status;
    }
    public WeatherLabelling extractFromFile(String filePath,int ch)
    {
        int count=1;
        StringBuffer sb=null;
        WeatherLabelling wl=new WeatherLabelling(filePath);
        String path;
        if(ch==0)
        {
            path = filePath+"Doc/"; 
        }
        else
        {
            path = filePath+"UploadData/"; 
        }
 
        String files;
        String file1[];
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles(); 
        int tablecount=0;
        ArrayList<WeatherAdvisory> wa=new ArrayList<WeatherAdvisory>();
        for (int i = 0; i < listOfFiles.length; i++) 
        {
            if (listOfFiles[i].isFile()) 
            {
                files = listOfFiles[i].getName();
                if (files.endsWith(".doc") || files.endsWith(".DOC") || files.endsWith(".rtf") || files.endsWith(".RTF"))
                {
                    ArrayList<CropSeason> CropSeasonList=new ArrayList<CropSeason>();
                    tablecount=0;
                    file1=files.split(".doc");
                    System.out.println(file1[0]);
                    WeatherAdvisory wadvisory=new WeatherAdvisory(file1[0],filePath);
                    InputStream fis = null;
                    try 
                    {
                        fis = new FileInputStream(listOfFiles[i]);
                        POIFSFileSystem fs = null;
                        fs = new POIFSFileSystem(fis);
                        HWPFDocument doc1 = null;
                        doc1 = new HWPFDocument(fs);
                        Range range = doc1.getRange();
                        for (int j=0; j<range.numParagraphs(); j++) 
                        {
                            Paragraph par = range.getParagraph(j);
                            if(par.text().trim().contains("Parameters") && par.isInTable())
                            {
                                tablecount++;
                                if(tablecount==2)
                                {
                                    j++;
                                    
                                    par = range.getParagraph(j);
                                    while(!par.text().trim().contains("Rainfall(mm)") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
                                    if(par.text().trim().contains("Rainfall(mm)") && par.isInTable())
                                    {
										System.out.println("Rainfall :");
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
											System.out.println(par.text());
                                            wadvisory.addRain(par.text().trim());
                                        }
                                    }
									while(!par.text().trim().contains("Maximum temperature") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
									if(par.text().trim().contains("Maximum temperature") && par.isInTable())
                                    {
										System.out.println("Max Temperature");
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
											System.out.println(par.text());
                                            wadvisory.addMaxTemp(par.text().trim());
                                        }
                                    }
									while(!par.text().trim().contains("Minimum temperature") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
                                    if(par.text().trim().contains("Minimum temperature") && par.isInTable())
                                    {
										System.out.println("Min Temperature");
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
											System.out.println(par.text());
                                            wadvisory.addMinTemp(par.text().trim());
                                        }
                                    }
									while(!par.text().trim().contains("Total cloud cover") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
                                    if(par.text().trim().contains("Total cloud cover") && par.isInTable())
                                    {
										System.out.println("Cloud");
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
											System.out.println(par.text());
                                            wadvisory.addCloud(par.text().trim());
                                        }
                                    }
									while(!par.text().trim().contains("Relative humidity") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
                                    if(par.text().trim().contains("Relative humidity") && par.isInTable())
                                    {
										System.out.println("Humidity");
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
                                            String hum[]=par.text().trim().split("-");
                                            wadvisory.addMinHumidity(hum[0]);
                                            wadvisory.addMaxHumidity(hum[1]);
											System.out.println(par.text());
                                            //wadvisory.addMaxHumidity(par.text().trim().replaceAll("-", ","));
                                        }
                                    }
									while(!par.text().trim().contains("Wind") && par.isInTable())
                                    {
                                        j++;
                                        par = range.getParagraph(j);
                                    }
                                    if(par.text().trim().contains("Wind") && par.isInTable())
                                    {
                                        for(int k=j+1;k<=j+5;k++)
                                        {
                                            par = range.getParagraph(k);
                                        }
                                    }
                                    
                                }
                            }
                            
                            String seasonwords[]={"Samba","Thaladi","Kuruvai"};
                            String stagewords[]={"nursery","transplanting","tillering","panicle-initiation","flowering","maturity","harvest"};
            
                            if(par.text().trim().contains("Stage") && !par.isInList() && !par.isInTable())
                            {
                                String word = par.text().trim();
                                String season[]=word.split(";");
                                for(int m=0;m<season.length;m++)
                                {
                                    for(int k=0;k<seasonwords.length;k++)
                                    {
                                        BufferedReader br=new BufferedReader(new FileReader(filePath+"pattern/"+seasonwords[k]+".lst"));
                                        String ip=br.readLine();
                                        while(ip!=null)
                                        {
                                            if(season[m].contains(ip))
                                            {
                                                CropSeason cs=new CropSeason(seasonwords[k]);
                                                String stage[]=season[m].split(" ");
                                                for(int o=0;o<stage.length;o++)
                                                {
                                                    for(int l=0;l<stagewords.length;l++)
                                                    {
                                                        BufferedReader br1=new BufferedReader(new FileReader(filePath+"pattern/"+stagewords[l]+".lst"));
                                                        String ip1=br1.readLine();
                                                        while(ip1!=null)
                                                        {
                                                            if(stage[o].contains(ip1))
                                                            {
                                                                cs.addStage(stagewords[l]);
                                                                ip1=null;
                                                                break;
                                                            }
                                                            else
                                                            {
                                                                ip1=br1.readLine();
                                                            }
                                                        }
                                                        br1.close();
                                                    }
                                                }
                                                CropSeasonList.add(cs);
                                                wadvisory.addCropSeason(cs);
                                                ip=null;
                                                break;
                                            }
                                            else
                                            {
                                                ip=br.readLine();
                                            }
                                        }
                                        br.close();
                                        
                                    }
                               }
           
           
                            }
                            if(par.isInList())
                            {
                                String list=par.text().replaceAll("\\s+", " ");
                                for(int p=0;p<CropSeasonList.size();p++)
                                {
                                    CropSeason cs1=CropSeasonList.get(p);
                                    BufferedReader br=new BufferedReader(new FileReader(filePath+"pattern/"+cs1.season+".lst"));
                                    String ip=br.readLine();
                                    while(ip!=null)
                                    {
                                    if(list.contains(ip))
                                    {
                                        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
                                        iterator.setText(list);
                                        int start = iterator.first();
                                        for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
                                        {
                                            String listadvisory=list.substring(start,end);
                                            listadvisory=listadvisory.trim();
                                        if(checkKeywords(filePath,listadvisory)==1 &&checkRemoveKeywords(filePath,listadvisory)==0)
                                        {
                                            if(listadvisory.charAt(listadvisory.length()-1)!='.')
                                            {
                                                listadvisory+='.';
                                            }
                                            cs1.addadvisory(listadvisory);
                                        }
                                        }
                                        ip=null;
                                        break;
                                    }
                                    else
                                    {
                                        ip=br.readLine();
                                    }
                                    }
                                    br.close();
                                }
                                
                            }
                        }
                    } catch (Exception ex) 
                    {
                        System.out.println(ex.getMessage());
                    }
                    
                    for(int p=0;p<CropSeasonList.size();p++)
                    {
                        CropSeasonList.get(p).displayStage();
                    }
                    //*/
                    //wa.add(wadvisory);
                    wl=wadvisory.generateAgroAdvisory(wl);
                    
                }
            }
            if(ch!=0)
            {
                if(listOfFiles[i].exists())
                listOfFiles[i].delete();
            }
        }
        if(ch==0)
        {
            wl.findUniqueAdvisory();
            wl.storeLabel();
            wl.assignLabel();
            wl.writeCSV(0);
        }
        else
        {
            wl.retrieveLabel();
            wl.assignLabel();
            wl.writeCSV(1);
        }
                    
   return wl;
   }
}
