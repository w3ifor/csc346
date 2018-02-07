import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class assignment2 {

    static Connection conn;
    static ResultSet rs;
    static Statement stmt;

    public static void main(String[]args){
        Scanner input=new Scanner(System.in);
        String vaildzipcode=askZipCode(input);
        double distance=askDistance(input)*1.60934;
        input.close();
        System.out.println(vaildzipcode+"   "+distance);
        String host="jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user="csc254";
        String password="age126";
        String originquery=String.format("SELECT zipcode, city,state, lat, `long`,estimatedpopulation FROM zips2 " +
                "WHERE (zipcode = %s) and locationtype = \"PRIMARY\"",vaildzipcode);

        try {
            conn= DriverManager.getConnection(host,user,password);
            if (conn == null) {
                System.out.println("Connection Fail!");
            }else
                System.out.println("Connection successful");
            stmt=conn.createStatement();
            rs= stmt.executeQuery(originquery);

            ResultSetMetaData rsMetaData=rs.getMetaData();

            while(rs.next()){
                String zipcode=rs.getString("zipcode");
                String city = rs.getString("city");
                String state=rs.getString("state");
                double lat=rs.getDouble("lat");
                double lon=rs.getDouble("long");
                int population=rs.getInt("estimatedpopulation");
                Place original =new Place(zipcode,city,state,lat,lon,population);

                System.out.println("Original place:\n"+original);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public static double askDistance(Scanner input) {
        boolean stop=false;
        double vailddistance=0;
        while(!stop){
            System.out.println("Enter Distacnce: (miles)");
            int distance=input.nextInt();
            if(distance<0){
                System.out.println("Invalid distance, please enter again! ");
            }
            else{
                stop=true;
                vailddistance= distance;
            }
        }


        return vailddistance;
    }

    public static String askZipCode(Scanner input){
        boolean stop=false;
        String vaildzipcode="";
        while(!stop){
            System.out.println("Enter zip code: ");
            String zipCode=input.next();
        if(!zipCode.matches("[0-9]+")||!(zipCode.length()==5)){
            System.out.println("Invalid zip code, please enter again! ");
        }
        else{
            stop=true;
            vaildzipcode=zipCode;
            }
        }
        return vaildzipcode;
    }
}
class Place {
    String zipcode;
    String city;
    String state;
    double latitude;
    double longitude;
    int population;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(getCity(), place.getCity()) &&
                Objects.equals(getState(), place.getState());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCity(), getState());
    }

    public Place(String zippcode,String city, String state,  double latitude, double longitude,int population) {
        this.zipcode=zippcode;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population=population;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }



    @Override
    public String toString() {
        return String.format("Zipcode  | City          | State | Lat    | Lon     | Population |\n" +
                        "%-10s %-15s %-7s %-3.2f    %-3.2f     %-8d",
                        zipcode,city,state,latitude,longitude,population);
    }
}