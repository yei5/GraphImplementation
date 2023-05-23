package model;

public class City {
    private String name;
    public City(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj){
        if (obj instanceof City c){
            return name.equals(c.getName());
        }
        return false;
    }

    public String toString(){
        	return name;
    }
}
