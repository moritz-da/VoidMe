package de.hdmstuttgart.voidme.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String Title;

    public String Description;

    public String Category;

    public String Latitude;

    public String Longitude;

    public String Altitude;

    public String Accuracy;

    public String Address;

    public int Severity;

    public LocationEntity(String Title, String Description, String Category, String Latitude, String Longitude, String Altitude, String Accuracy, String Address, int Severity) {
        this.Title = Title;
        this.Description = Description;
        this.Category = Category;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Altitude = Altitude;
        this.Accuracy = Accuracy;
        this.Address = Address;
        this.Severity = Severity;
    }


    public String getAltitude() {
        return Altitude;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getDescription() {
        return Description;
    }

    public String getAccuracy() {
        return Accuracy;
    }

    public String getAddress() {
        return Address;
    }

    public int getSeverity() {
        return Severity;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity location = (LocationEntity) o;
        return Title.equals(location.Title) && Latitude.equals(location.Latitude) && Longitude.equals(location.Longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Title, Latitude, Longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Category='" + Category + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Altitude='" + Altitude + '\'' +
                ", Accuracy='" + Accuracy + '\'' +
                ", Address='" + Address + '\'' +
                ", Severity=" + Severity +
                '}';
    }
}