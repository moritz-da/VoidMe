package de.hdmstuttgart.voidme.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"category", "latitude", "longitude"},
        unique = true)})
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String Title;

    public String Description;

    @ColumnInfo(name = "category")
    public String Category;

    @ColumnInfo(name = "latitude")
    public double Latitude;

    @ColumnInfo(name = "longitude")
    public double Longitude;

    public double Altitude;

    public float Accuracy;

    public int Severity;

    public LocationEntity(String Title, String Description, String Category, double Latitude, double Longitude, double Altitude, float Accuracy, int Severity) {
        this.Title = Title;
        this.Description = Description;
        this.Category = Category;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Altitude = Altitude;
        this.Accuracy = Accuracy;
        this.Severity = Severity;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getCategory() {
        return Category;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getAltitude() {
        return Altitude;
    }

    public float getAccuracy() {
        return Accuracy;
    }

    public int getSeverity() {
        return Severity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Title, Latitude, Longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "LocationEntity{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Category='" + Category + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Altitude='" + Altitude + '\'' +
                ", Accuracy='" + Accuracy + '\'' +
                ", Severity=" + Severity +
                '}';
    }
}