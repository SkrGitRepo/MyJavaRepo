package com.sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonSimpleWriteExample {

    public static void main(String[] args) {

        JSONObject obj1 = new JSONObject();
        obj1.put("domain", "csm");
        obj1.put("age", new Integer(100));

        JSONArray list1 = new JSONArray();
        list1.add("msg 1");
        list1.add("msg 2");
        list1.add("msg 3");

        obj1.put("messages", list1);
        
        
        JSONObject obj2 = new JSONObject();
        obj2.put("name", "sumkuma2.com");
        obj2.put("age", new Integer(100));

        JSONArray list2 = new JSONArray();
        list2.add("msg 1");
        list2.add("msg 2");
        list2.add("msg 3");
        obj2.put("messages", list2);
        
        JSONArray superlist = new JSONArray();

        superlist.add(obj1);
        superlist.add(obj2);

        
        JSONObject superobj = new JSONObject();
        superobj.put("Employee", superlist);

        try (FileWriter file = new FileWriter("c:\\users/sumkuma2/test.json")) {

            file.write(superobj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(superobj);
        
        
        
        
        JSONObject min = new JSONObject();
        min.put("week", "1");
        min.put("year", "2014");
        
        
        JSONObject min1 = new JSONObject();
        min1.put("week", "1");
        min1.put("year", "2014");

        JSONObject max = new JSONObject();
        max.put("week", "14");
        max.put("year", "2017");

        JSONObject json= new JSONObject();
        json.put("min", min);
        json.put("max", max);
        

        System.out.println(json.toString());

    }

}
