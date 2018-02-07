import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class assignment2 {

    static Connection conn;
    static ResultSet rs;
    static Statement stmt;

    public static void main(String[]args){
        Scanner input=new Scanner(System.in);
        String zipcode=askZipCode(input);
        int distance=askDistance(input);
        System.out.println(zipcode+"   "+distance);
        String host="jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user="csc254";
        String password="age126";
        String queryString ="SELECT city,region,country,latitude,longitude FROM cities where region LIKE 'MO' ";

        try {
            conn= DriverManager.getConnection(host,user,password);
            if (conn == null) {
                System.out.println("Connection Fail!");
            }else
                System.out.println("Connection successful");
            stmt=conn.createStatement();
            rs= stmt.executeQuery(queryString);

            ResultSetMetaData rsMetaData=rs.getMetaData();
            int numberOfColums =rsMetaData.getColumnCount();

            System.out.println("Number of column: "+numberOfColums);
            for(int i=1; i<=numberOfColums;i++){
                System.out.printf("Column %2d: %s (%s)\n",i,rsMetaData.getColumnName(i),rsMetaData.getColumnTypeName(i));
            }

            while(rs.next()){
                String country= rs.getString("country");
                String name = rs.getString("city");
                String region=rs.getString("region");
                double lat=rs.getDouble("latitude");
                double lon=rs.getDouble("longitude");
                Place place =new Place(name,region,country,lat,lon);
                System.out.println(place);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public static int askDistance(Scanner input) {
        boolean stop=false;
        int vailddistance=0;
        while(!stop){
            System.out.println("Enter Distacnce: ");
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
    String name;
    String region;
    String country;
    double latitude;
    double longitude;
    double distanceFromOrigin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(getName(), place.getName()) &&
                Objects.equals(getRegion(), place.getRegion());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getRegion());
    }

    public Place(String name, String region, String country, double latitude, double longitude) {
        this.name = name;
        this.region = region;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceFromOrigin=-1.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public double getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distanceFromOrigin=" + distanceFromOrigin +
                '}';
    }
}