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
public class CropSeason 
{
    String season;
    ArrayList<String> stage;
    ArrayList<String> advisory;
    public CropSeason()
    {
        
    }
    public CropSeason(String season)
    {
        this.season=season;
        stage=new ArrayList<String>();
        advisory=new ArrayList<String>();
    }
    public void addStage(String stage)
    {
        this.stage.add(stage);
    }
    public void addadvisory(String advisory)
    {
        this.advisory.add(advisory);
    }
    public void displayStage()
    {
        System.out.println(season+"->");
        for(int i=0;i<stage.size();i++)
        {
            System.out.println("\t"+stage.get(i));
        }
        System.out.println("Advisory->"+advisory.size());
        for(int i=0;i<advisory.size();i++)
        {
            System.out.println("\t"+advisory.get(i));
        }
    }
}
