/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AgroAdvisory;

import java.util.ArrayList;

/**
 *
 * @author jagan
 */
public class WeatherArray 
{
    ArrayList<String> rain;
    ArrayList<String> minTemp;
    ArrayList<String> maxTemp;
    ArrayList<String> cloud;
    ArrayList<String> minHumidity;
    ArrayList<String> maxHumidity;
    public WeatherArray()
    {
        rain=new ArrayList<String>();
        minTemp=new ArrayList<String>();
        maxTemp=new ArrayList<String>();
        cloud=new ArrayList<String>();
        minHumidity=new ArrayList<String>();
        maxHumidity=new ArrayList<String>();
    }
    
}
