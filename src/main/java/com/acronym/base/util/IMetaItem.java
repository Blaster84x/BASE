package com.acronym.base.util; 
 
import java.util.List; 
 
/** 
 * Created by Jared on 6/27/2016. 
 */ 
public interface IMetaItem {

    /**
     * Specifies what metadata this item uses
     * @return Integer list of metadata used
     */
    List<Integer> getMetaData(); 
} 
