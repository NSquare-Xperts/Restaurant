package com.nsquare.restaurant;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Production extends Observable implements Parcelable {
    public static final Creator<Production> CREATOR = new Creator<Production>() {
        @Override
        public Production createFromParcel(Parcel source) {
            return new Production(source);
        }

        @Override
        public Production[] newArray(int size) {
            return new Production[size];
        }
    };
    protected String backgroundImageURL;
    protected List<String> castList = new LinkedList();
    protected String coverImageURL;
    protected boolean inWatchlist;
    protected String mpaaRating;
    protected int numReviews;
    protected String productionId;
    protected Date releaseDate;
    protected int runtime;
    protected double starRating;
    protected String title;
    protected String videoURL;

    public Production() {

    }

//    static class C07611 implements Creator<Production> {
//        C07611() {
//        }
//
//        public Production createFromParcel(Parcel in) {
//            return new Production(in);
//        }
//
//        public Production[] newArray(int size) {
//            return new Production[size];
//        }
//    }

    public String getProductionId() {
        return this.productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMpaaRating() {
        return this.mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public double getStarRating() {
        return this.starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public int getNumReviews() {
        return this.numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public int getRuntime() {
        return this.runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public boolean isReleased() {
        return this.releaseDate != null && this.releaseDate.before(new Date());
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImageURL() {
        return this.coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public String getBackgroundImageURL() {
        return this.backgroundImageURL;
    }

    public void setBackgroundImageURL(String backgroundImageURL) {
        this.backgroundImageURL = backgroundImageURL;
    }

    public String getVideoURL() {
        return this.videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public List<String> getCastList() {
        return this.castList;
    }

    public void setCastList(List<String> castList) {
        this.castList = castList;
    }

    public boolean isInWatchlist() {
        return this.inWatchlist;
    }

    public void setInWatchlist(boolean inWatchlist) {
        this.inWatchlist = inWatchlist;
    }

    public void toggleFromWatchlist(boolean inWatchlist) {
        this.inWatchlist = inWatchlist;
        setChanged();
        notifyObservers();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productionId);
        dest.writeString(this.title);
        dest.writeString(this.mpaaRating);
        dest.writeDouble(this.starRating);
        dest.writeInt(this.numReviews);
        dest.writeInt(this.runtime);
        dest.writeSerializable(this.releaseDate);
        dest.writeString(this.coverImageURL);
        dest.writeString(this.backgroundImageURL);
        dest.writeString(this.videoURL);
        dest.writeStringList(this.castList);
        dest.writeInt(this.inWatchlist ? 1 : 0);
    }

    protected Production(Parcel in) {
        this.productionId = in.readString();
        this.title = in.readString();
        this.mpaaRating = in.readString();
        this.starRating = in.readDouble();
        this.numReviews = in.readInt();
        this.runtime = in.readInt();
        this.releaseDate = (Date) in.readSerializable();
        this.coverImageURL = in.readString();
        this.backgroundImageURL = in.readString();
        this.videoURL = in.readString();
        in.readStringList(this.castList);
        this.inWatchlist = in.readInt() == 1;
    }


    public static List<Production> readProductionList(JsonReader reader) throws IOException {
        List<Production> productionList = new LinkedList();
        reader.beginArray();
        while (reader.hasNext()) {
            productionList.add(readProduction(reader));
        }
        reader.endArray();
        return productionList;
    }

    public static Production readProduction(JsonReader reader) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Production production = new Production();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Integer obj = Integer.valueOf(-1);
            switch (name.hashCode()) {
                case -816678056:
                    if (!name.equals("videos")) {
                        break;
                    }
                    obj = Integer.valueOf(10);
                    break;
                case -633418624:
                    if (!name.equals("mpaaRating")) {
                        break;
                    }
                    obj = Integer.valueOf(2);
                    break;
                case -453976817:
                    if (!name.equals("coverImages")) {
                        break;
                    }
                    obj = Integer.valueOf(8);
                    break;
                case -64967209:
                    if (!name.equals("numMFRatings")) {
                        break;
                    }
                    obj = Integer.valueOf(4);
                    break;
                case 3046207:
                    if (!name.equals("cast")) {
                        break;
                    }
                    obj = Integer.valueOf(11);
                    break;
                case 110371416:
                    if (!name.equals("")) {
                        break;
                    }
                    obj = Integer.valueOf(1);
                    break;
                case 212873301:
                    if (!name.equals("releaseDate")) {
                        break;
                    }
                    obj = Integer.valueOf(7);
                    break;
                case 1023651764:
                    if (!name.equals("productionId")) {
                        break;
                    }
                    break;
                case 1324447417:
                    if (!name.equals("metacritic")) {
                        break;
                    }
                    obj = Integer.valueOf(5);
                    break;
                case 1415752006:
                    if (!name.equals("backgroundImages")) {
                        break;
                    }
                    obj = Integer.valueOf(9);
                    break;
                case 1550962648:
                    if (!name.equals("runtime")) {
                        break;
                    }
                    obj = Integer.valueOf(6);
                    break;
                case 1750277775:
                    if (!name.equals("starRating")) {
                        break;
                    }
                    obj = Integer.valueOf(3);
                    break;
                case 1881047624:
                    if (!name.equals("inWatchlist")) {
                        break;
                    }
                    Integer.valueOf(12);
                    break;
                default:
                    break;
            }
        }
        reader.endObject();
        return production;
    }

    private static String readImageValue(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Integer obj = Integer.valueOf(-1);
            switch (name.hashCode()) {
                case 79011047:
                    if (!name.equals("SMALL")) {
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        reader.endObject();
        return null;
    }

    private static List<String> readCastValues(JsonReader reader) throws IOException {
        List<String> castList = new LinkedList();
        reader.beginArray();
        while (reader.hasNext()) {
            castList.add(reader.nextString());
        }
        reader.endArray();
        return castList;
    }

    protected static String readVideoURL(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                Integer obj = Integer.valueOf(-1);
                switch (name.hashCode()) {
                    case 116079:
                        if (!name.equals("nvj")) {
                            break;
                        }
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return null;
    }
}
