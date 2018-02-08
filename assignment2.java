import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class assignment2 {

    static Connection conn;
    static ResultSet rs;
    static Statement stmt;

    public static void main(String[]args){
        Scanner input=new Scanner(System.in);
        String vaildzipcode=askZipCode(input);
        double distanceInMeters=askDistance(input);
        input.close();

        String host="jdbc:mysql://turing.cs.missouriwestern.edu:3306/misc";
        String user="csc254";
        String password="age126";


        try {
            conn= DriverManager.getConnection(host,user,password);
            if (conn == null) {
                System.out.println("Connection Fail!");
            }else
                System.out.println("Connection successful");
            stmt=conn.createStatement();
            String originquery=String.format("SELECT zip_code, city,state_prefix, lat, lon,population,housingunits FROM zips " +
                    "WHERE (zip_code = %s) ",vaildzipcode);
            rs= stmt.executeQuery(originquery);

            ResultSetMetaData rsMetaData=rs.getMetaData();
            Place original=null;
            ArrayList<Place> disasterplaces=new ArrayList<>();
            while(rs.next()){
                String zipcode=rs.getString("zip_code");
                String city = rs.getString("city");
                String state=rs.getString("state_prefix");
                double lat=rs.getDouble("lat");
                double lon=rs.getDouble("lon");
                int population=rs.getInt("population");
                int housingunits=rs.getInt("housingunits");
                original =new Place(zipcode,city,state,lat,lon,population,housingunits);

                System.out.println("Original place:\n"+original);
            }
            if(original==null){
                System.out.println("Invaild Original place");
            }
            else
            {
                String disasterquery=String.format("SELECT zip_code, city,state_prefix, lat,lon,population,housingunits FROM zips");
                rs = stmt.executeQuery(disasterquery);
                while(rs.next()){
                    String zipcode=rs.getString("zip_code");
                    String city = rs.getString("city");
                    String state=rs.getString("state_prefix");
                    double lat=rs.getDouble("lat");
                    double lon=rs.getDouble("lon");
                    int population=rs.getInt("population");
                    int housingunits=rs.getInt("housingunits");
                    double distance=haversine(original.getLatitude(),original.getLongitude(),lat,lon);

                    if(distance<distanceInMeters && zipcode !=original.getZipcode()){
                        disasterplaces.add(new Place(zipcode,city,state,lat,lon,population,housingunits));
                    }

                }

                for(int i=0;i<disasterplaces.size();i++){
                    for(int j=i+1;j<disasterplaces.size();j++){
                        if(disasterplaces.get(i).getCity().equals(disasterplaces.get(j).getCity())&&disasterplaces.get(i).getState().equals(disasterplaces.get(j).getState())){
                            disasterplaces.get(i).setPopulation(disasterplaces.get(i).getPopulation()+disasterplaces.get(j).getPopulation());
                            disasterplaces.remove(disasterplaces.get(j));
                            j--;
                        }

                    }

                }
                System.out.println("Avoid cities:\nZipcode  | City          | State | Lat    | Lon     | Population | Housingunits |");

                for(Place places : disasterplaces){
                    System.out.println(places);
                }
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
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371;

        double Lat = Math.toRadians(lat2 - lat1);
        double Lon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(Lat / 2.0),2.0) + Math.cos(lat1) * Math.cos(lat2)* Math.pow(Math.sin(Lon/2.0),2.0);
        double c = 2.0 * (Math.atan2(Math.sqrt(a),Math.sqrt(1.0-a)));
        return R * c;

    }
}
class Place {
    String zipcode;
    String city;
    String state;
    double latitude;
    double longitude;
    int population;
    int housingunits;

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

    public Place(String zipcode,String city, String state,  double latitude, double longitude,int population, int housingunits) {
        this.zipcode=zipcode;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population=population;
        this.housingunits=housingunits;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
    public int getHousingunits() {
        return housingunits;
    }

    public void setHousingunits(int housingunits) {
        this.housingunits = housingunits;
    }



    @Override
    public String toString() {
        return String.format(
                        "%-10s %-15s %-7s %-3.2f    %-3.2f     %-8d    %-8d",
                        zipcode,city,state,latitude,longitude,population,housingunits);
    }
}