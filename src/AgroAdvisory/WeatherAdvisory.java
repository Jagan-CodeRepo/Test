/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/**
 *
 * @author jagan
 */
public class WeatherAdvisory extends WeatherArray
{
    String rdate;
    /*ArrayList<String> rain;
    ArrayList<String> minTemp;
    ArrayList<String> maxTemp;
    ArrayList<String> cloud;
    ArrayList<String> minHumidity;
    ArrayList<String> maxHumidity;*/
    ArrayList<CropSeason> cropSeasonList;
    String filePath;
    public WeatherAdvisory()
    {
    }
    public WeatherAdvisory(String rdate,String filePath)
    {
        super();
        this.rdate=rdate;
        /*rain=new ArrayList<String>();
        minTemp=new ArrayList<String>();
        maxTemp=new ArrayList<String>();
        cloud=new ArrayList<String>();
        minHumidity=new ArrayList<String>();
        maxHumidity=new ArrayList<String>();*/
        cropSeasonList=new ArrayList<CropSeason>();
        this.filePath=filePath;
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
    public void addCropSeason(CropSeason cs)
    {
        cropSeasonList.add(cs);
        
    }
    public int checkNurseryTransplant(CropSeason cs)
    {
        int status=0;
        for(int i=0;i<cs.stage.size();i++)
        {
            if(cs.stage.get(i).equals("nursery"))
            {status++;}
           if(cs.stage.get(i).equals("transplanting"))
            {status++;} 
        }
        return status;
    }
    public int checkNurseryTransplantKey(String val,String text)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/"+val+"keywords.lst"));
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
    public int checkPostpone(String val,String text)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/"+val+".lst"));
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
    public String checkPest(String text)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/pest.lst"));
            String ip=br.readLine();
            while(ip!=null)
            {
                if(text.contains(ip))
                {
                    return ip;
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
        return null;
    }
    public String removeWords(String val)
    {
        BufferedReader br = null;
        int status=0;
        try 
        {
            
            br = new BufferedReader(new FileReader(filePath+"pattern/removewords.lst"));
            String ip=br.readLine();
            while(ip!=null)
            {
                String regex = "\\s*\\b"+ip+"\\b\\s*";
                val=val.replaceAll(regex," ");
                ip=br.readLine();
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
        return val;
    }
    public boolean contains(ArrayList<String> list, String val)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).equalsIgnoreCase(val))
                return true;
        }
        return false;
    }
    public boolean checkDressing(ArrayList<String> advisory)
    {
        for(String str:advisory)
        {
            if(checkPostpone("postponelist",str)==1)
            {
                return true;
            }
        }
        return false;
    }
    public boolean checkPloughing(ArrayList<String> advisory)
    {
        for(String str:advisory)
        {
            if(checkPostpone("ploughinglist",str)==1)
            {
                return true;
            }
        }
        return false;
    }
    public WeatherLabelling generateAgroAdvisory(WeatherLabelling wl)
    {
		for(int i=0;i<rain.size()&&i<minTemp.size()&& i<maxTemp.size()&&i<cloud.size()&&i<minHumidity.size()&&i<maxHumidity.size();i++)
        {
		    for(int j=0;j<cropSeasonList.size();j++)
            {
               CropSeason cs=cropSeasonList.get(j);
               for(int k=0;k<cs.stage.size();k++) 
               {
                  StringBuffer advi=new StringBuffer();
                  int raintmp=Integer.parseInt(rain.get(i));
				  if(!checkDressing(cs.advisory)&&!cs.stage.get(k).equals("nursery")&&!cs.stage.get(k).equals("harvest")&&!cs.stage.get(k).equals("maturity"))
                  {
                     if(raintmp>=0 && raintmp<8)
                     {
                         if(cs.stage.get(k).equals("transplanting"))
                          {
                              advi.append("Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              advi.append("Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                          //advi.append("Keywords1 $ Application of basal fertilizers, nitrogen fertilizers and spraying of pesticides can be done. ");
                     }
                     else if(raintmp>=8 && raintmp<25)
                     {
                          advi.append("Keywords3 $ Top dressing of fertilizers can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                     }
                     else if(raintmp>=25)
                     {
                          advi.append("Keywords4 $ Top dressing of fertilizers can be postponed, irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                     }
                  }
                  /*if(cs.stage.get(k).equals("nursery") && !checkPloughing(cs.advisory))
                  {
                     if(raintmp==0)
                     {
                          advi.append("Keywords0 $ Ploughing of paddy fields and sowing green manure crops through irrigation.");
                     }
                     else if(raintmp>=1 && raintmp<8)
                     {
                          advi.append("Keywords2 $ Ploughing of paddy fields may be taken up using the expected rainfall.");
                     }
                     else if(raintmp>=8 && raintmp<25)
                     {
                          advi.append("Keywords3 $ Ploughing of paddy fields may be taken up using the expected rainfall and irrigation to paddy field can be postponed.");
                     }
                     else if(raintmp>=25)
                     {
                          advi.append("Keywords4 $ Irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                     }
                  }*/
                  for(int l=0;l<cs.advisory.size();l++)
                  {
                      if(!cs.advisory.get(l).contains("pulses"))
                      {
                      String pest=checkPest(cs.advisory.get(l));
                      if(pest!=null)
                      {
                          cs.advisory.remove(l);
                          pest=pest.substring(0, 1).toUpperCase() + pest.substring(1);
                          if(pest.equalsIgnoreCase("Insect and nematode attack"))
                          {
                              cs.advisory.add(l,pest+" $ Apply 100 kg of neem cake. ");
                          }
                          else
                          {
                          cs.advisory.add(l,pest+" $ ");
						  }
                      }
                      if((cs.advisory.get(l).contains("Harvest")&&cs.advisory.get(l).contains("stem"))&&(cs.stage.get(k).equals("maturity")||cs.stage.get(k).equals("harvest")))
                      {
                          continue;
                      }
                      if(raintmp==0)
                      {
                          if(cs.advisory.get(l).contains("rainfall")||cs.advisory.get(l).contains("rainwater")||cs.advisory.get(l).contains("rain"))
                          {
                          if(cs.stage.get(k).equals("maturity"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords0 $ Harvesting of the matured paddy can be taken up by using the dry weather. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords0 $ Harvesting of the matured paddy can be taken up by using the dry weather. ");
                          
                          }
                          else if(cs.stage.get(k).equals("nursery"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords0 $ Ploughing of paddy fields and sowing green manure crops through irrigation. ");
                          }
                          else if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==1)
                          {
                            if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                              //cs.advisory.remove(l);
                              //cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers, nitrogen fertilizers and spraying of pesticides can be done.");
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==0)
                          {
                              if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                              //cs.advisory.remove(l);
                              //cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers, nitrogen fertilizers and spraying of pesticides can be done.");
                          }
                          else if(cs.advisory.get(l).contains("matured")&&(cs.stage.get(k).equals("harvest")||cs.stage.get(k).equals("maturity")))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords0 $ Harvesting of the matured paddy can be taken up by using the dry weather. ");
                          }
                      }
                      else if(raintmp>0&& raintmp<8)
                      {
                          if(cs.advisory.get(l).contains("rainfall")||cs.advisory.get(l).contains("rainwater")||cs.advisory.get(l).contains("rain")){
                          if(cs.stage.get(k).equals("nursery")){
                          cs.advisory.remove(l);
						  cs.advisory.add(l, "Keywords2 $ Ploughing of paddy fields may be taken up using the expected rainfall. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords2 $ Harvesting of the matured paddy can be taken up with precaution. ");
                          }
                          else if(cs.stage.get(k).equals("maturity")){
                          cs.advisory.remove(l);
                          cs.advisory.add(l, "Keywords2 $ Harvesting of the matured paddy can be taken up with precaution. ");
                          }
                          else if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==1)
                          {
                              if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                              //cs.advisory.remove(l);
                              //cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers, nitrogen fertilizers and spraying of pesticides can be done.");
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==0)
                          {
                              if(cs.stage.get(k).equals("transplanting"))
                          {
                              cs.advisory.remove(l);
							  cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                              //cs.advisory.remove(l);
                              //cs.advisory.add(l, "Keywords1 $ Application of basal fertilizers, nitrogen fertilizers and spraying of pesticides can be done.");
                          }
                          else if((cs.advisory.get(l).contains("matured")||(cs.advisory.get(l).contains("Harvesting")))&&(cs.stage.get(k).equals("harvest")||cs.stage.get(k).equals("maturity")))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords2 $ Harvesting of the matured paddy can be taken up with precaution. ");
                          }
                          
                      }
                      else if(raintmp>7&& raintmp<25)
                      {
                          if(cs.advisory.get(l).contains("rainfall")||cs.advisory.get(l).contains("rainwater")||cs.advisory.get(l).contains("rain"))
                          {
                          if(cs.stage.get(k).equals("nursery")){
                          cs.advisory.remove(l);
						  cs.advisory.add(l, "Keywords3 $ Ploughing of paddy fields may be taken up using the expected rainfall and irrigation to paddy field can be postponed. ");
                          }
                          else if(cs.stage.get(k).equals("harvest")){
                          cs.advisory.remove(l);
						  cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else if(cs.stage.get(k).equals("maturity")){
                          cs.advisory.remove(l);
                          cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords3 $ Top dressing of fertilizers can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==1)
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords3 $ Top dressing of fertilizers can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==0)
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords3 $ Top dressing of fertilizers can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(cs.advisory.get(l).contains("matured")&&(cs.stage.get(k).equals("harvest")||cs.stage.get(k).equals("maturity")))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          
                      }
                      else if(raintmp>24)
                      {
                          if(cs.advisory.get(l).contains("rainfall")||cs.advisory.get(l).contains("rainwater")||cs.advisory.get(l).contains("rain"))
                          {
                          if(cs.stage.get(k).equals("nursery")){
                          cs.advisory.remove(l);
                          cs.advisory.add(l, "Keywords4 $ Irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(cs.stage.get(k).equals("harvest")){
                          cs.advisory.remove(l);
                          cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else if(cs.stage.get(k).equals("maturity")){
                          cs.advisory.remove(l);
                          cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords4 $ Top dressing of fertilizers can be postponed, irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==1)
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords4 $ Top dressing of fertilizers can be postponed, irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(checkPostpone("postponelist",cs.advisory.get(l))==1&&checkPostpone("postpone",cs.advisory.get(l))==0)
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords4 $ Top dressing of fertilizers can be postponed, irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(cs.advisory.get(l).contains("matured")&&(cs.stage.get(k).equals("harvest")||cs.stage.get(k).equals("maturity")))
                          {
                              cs.advisory.remove(l);
                              cs.advisory.add(l, "Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                      }
                      if(checkNurseryTransplant(cs)==2)
                      {
                         if(checkNurseryTransplantKey(cs.stage.get(k),cs.advisory.get(l))==1)
                         {
                             advi.append(cs.advisory.get(l)+" ");
                         }
                         
                      }
                      else
                      {
                          advi.append(cs.advisory.get(l)+" ");
                      }
                  }
               }
                  if(cs.advisory.isEmpty())
                  {
                      if(raintmp==0)
                      {
                          if(cs.stage.get(k).equals("maturity"))
                          {
                              advi.append("Keywords0 $ Harvesting of the matured paddy can be taken up by using the dry weather. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              advi.append("Keywords0 $ Harvesting of the matured paddy can be taken up by using the dry weather. ");
                          
                          }
                          else if(cs.stage.get(k).equals("nursery"))
                          {
                              advi.append("Keywords0 $ Ploughing of paddy fields and sowing green manure crops through irrigation. ");
                          }
                          else if(cs.stage.get(k).equals("transplanting"))
                          {
                              advi.append("Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              advi.append("Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                          
                      }
                      else if(raintmp>0&& raintmp<8)
                      {
                          if(cs.stage.get(k).equals("nursery"))
                          {
                              advi.append("Keywords2 $ Ploughing of paddy fields may be taken up using the expected rainfall. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              advi.append("Keywords2 $ Harvesting of the matured paddy can be taken up with precaution. ");
                          }
                          else if(cs.stage.get(k).equals("maturity"))
                          {
                              advi.append("Keywords2 $ Harvesting of the matured paddy can be taken up with precaution. ");
                          }
                          else if(cs.stage.get(k).equals("transplanting"))
                          {
                              advi.append("Keywords1 $ Application of basal fertilizers can be done. ");
                          }
                          else if(cs.stage.get(k).equals("tillering"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else if(cs.stage.get(k).equals("panicle-initiation"))
                          {
                              advi.append("Keywords1 $ Application of nitrogen fertilizers and spraying of pesticides can be done. ");
                          }
                          else
                          {
                              advi.append("Keywords1 $ Foliar spray of 2 % DAP (4kg of DAP in 200lit of water) during flowering is recommended to increase the number of filled grains and grain yield. ");
                          }
                          
                      }
                      else if(raintmp>7&& raintmp<25)
                      {
                          if(cs.stage.get(k).equals("nursery"))
                          {
                              advi.append("Keywords3 $ Ploughing of paddy fields may be taken up using the expected rainfall and irrigation to paddy field can be postponed. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              advi.append("Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else if(cs.stage.get(k).equals("maturity"))
                          {
                              advi.append("Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else
                          {
                              advi.append("Keywords3 $ Top dressing of fertilizers can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                      }
                      else if(raintmp>24)
                      {
                          if(cs.stage.get(k).equals("nursery"))
                          {
                              advi.append("Keywords4 $ Irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          else if(cs.stage.get(k).equals("harvest"))
                          {
                              advi.append("Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else if(cs.stage.get(k).equals("maturity"))
                          {
                              advi.append("Keywords5 $ Proper drainage may be arranged to drain excess of water in fields and plan harvest accordingly. ");
                          }
                          else
                          {
                              advi.append("Keywords4 $ Top dressing of fertilizers can be postponed, irrigation to paddy field can be postponed and proper drainage may be arranged to drain excess of water in fields. ");
                          }
                          
                      }
                  }
                  if(advi.length()>0){
                  String val=advi.toString().replaceAll("\\s+", " ");
                  ArrayList<String> valList=new ArrayList<String>();
                  BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
                  iterator.setText(val);
                  int start = iterator.first();
                  for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
                  {
                      if(!contains(valList,val.substring(start,end)))
                        valList.add(val.substring(start,end));
                  }
                  advi=new StringBuffer();
                  for(int f=0;f<valList.size();f++)
                  {
                      advi.append(valList.get(f));
                  }
                  String strAdvi=removeWords(advi.toString());
                  strAdvi=strAdvi.replaceAll("\\bKeywords0\\b", "Dry weather");
                  strAdvi=strAdvi.replaceAll("\\bKeywords1\\b", "No to Light rainfall");
                  strAdvi=strAdvi.replaceAll("\\bKeywords2\\b", "Light rainfall");
                  strAdvi=strAdvi.replaceAll("\\bKeywords3\\b", "Moderate rainfall");
                  strAdvi=strAdvi.replaceAll("\\bKeywords4\\b", "Heavy rainfall");
                  strAdvi=strAdvi.replaceAll("\\bKeywords5\\b", "Moderate to Heavy rainfall");
                  wl.addRain(rain.get(i));
                  wl.addMinTemp(maxTemp.get(i));
                  wl.addMaxTemp(minTemp.get(i));
                  wl.addCloud(cloud.get(i));
                  wl.addMinHumidity(minHumidity.get(i));
                  wl.addMaxHumidity(maxHumidity.get(i));
                  wl.addCropStage(cs.stage.get(k));
                  wl.addAdvisory(strAdvi);
				  System.out.println(cs.stage.get(k)+"->\n"+strAdvi);
                  }
               }
            }
        }
        return wl;
    }
    
}

