package com.javier.forecast.Cities;

/**
 * Created by javi0 on 20/12/2016.
 */

public class Cities {
    City [] cities;
    public Cities (){
        cities = new City[6];
        cities[0]= new City("Madrid","40.4167019","-3.7037783000000672");
        cities[1]= new City("Tokio","35.6892463","139.69185530000004");
        cities[2]= new City("Los Angeles","34.0521468","-118.24375700000002");
        cities[3]= new City("Seul","37.566535","126.97796900000003");
        cities[4]= new City("Atenas","37.9836452","23.727501599999982");
        cities[5]= new City("Madrid","-33.8683525","151.20925590000002");
    }

    public City[] getCities() {
        return cities;
    }

    public void setCities(City[] cities) {
        this.cities = cities;
    }
}
