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

                String name = rs.getString("city");
                String state=rs.getString("state");
                double lat=rs.getDouble("lat");
                double lon=rs.getDouble("long");
                Place place =new Place(name,state,lat,lon);
                System.out.println(place);
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
    String name;
    String state;
    double latitude;
    double longitude;
    double distanceFromOrigin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(getName(), place.getName()) &&
                Objects.equals(getState(), place.getState());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getState());
    }

    public Place(String name, String state,  double latitude, double longitude) {
        this.name = name;
        this.state = state;

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

    public double getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    @Override
    public String toString() {
        return
                 name + '\'' +
                         state + '\'' +
                         latitude + '\'' +
                         longitude + '\'';
    }
}