package com.example.brainwashing.onlinebookingclinic.Models;

/**
 * Created by Brainwashing on 3/2/2018.
 */

public class FilterModel {
    public boolean openNow;
    public float distance;
    public String specialist;

    public FilterModel(boolean opennow,float dis,String specialist){
        openNow = opennow;
        distance = dis;
        this.specialist = specialist;
    }

}
