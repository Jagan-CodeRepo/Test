/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

/**
 *
 * @author jagan
 */
public class WeatherElement 
{
    int minHumidity,maxHumidity,rain,maxTemp,minTemp,cloudCover;
    String retrieveDate,actualDate;
    public WeatherElement()
    {}
    public WeatherElement(String date,String adate,int rain,int mintemp,int maxtemp,int minhum,int maxhum,int cloud)
    {
        this.retrieveDate=date;
        this.actualDate=adate;
        this.rain=rain;
        this.minTemp=mintemp;
        this.maxTemp=maxtemp;
        this.minHumidity=minhum;
        this.maxHumidity=maxhum;
        this.cloudCover=cloud;
    }
    public int getRain()
    {
        return rain;
    }
    public int getMinTemp()
    {
        return minTemp;
    }
    public int getMaxTemp()
    {
        return maxTemp;
    }
    public int getMinHumidity()
    {
        return minHumidity;
    }
    public int getMaxHumidity()
    {
        return maxHumidity;
    }
    public int getCloud()
    {
        return cloudCover;
    }
    public String getretrieveDate()
    {
        return retrieveDate;
    }
    public String getactualDate()
    {
        return actualDate;
    }
}
