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
public class ClusterSampleList 
{
    String name;
    ArrayList<String> clusterClass;
    public ClusterSampleList(String name)
    {
        this.name=name;
        clusterClass=new ArrayList<String>();
    }
    public String getClusterName()
    {
        return name;
    }
    public void addClusterClass(String val)
    {
        clusterClass.add(val);
    }
    public String getClusterClass(int index)
    {
        return clusterClass.get(index);
    }
    public int clusterSize()
    {
        return clusterClass.size();
    }
}
