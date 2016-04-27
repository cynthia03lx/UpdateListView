package com.example.devine_it.updatelistview;

public class Item {
    private String name;
    private String adress;


    public Item(){

    }

    public Item(String name, String adress){
        this.name = name;
        this.adress = adress;

    }

    public String getName() {
        return name;
    }

    public void setNumber(String details) {
        this.name = details;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String name) {
        this.adress = name;
    }



}
