package com.TourAccount.model;

/**
 * User: User
 * Date: 29.06.15
 * Time: 14:02
 */
public class ItemTourist {
    private int tourItem;
    private int touristId;
    private float touristAmount;
    private int currAmount;

    public int getCurrAmount() {
        return currAmount;
    }

    public void setCurrAmount(int currAmount) {
        this.currAmount = currAmount;
    }

    public int getTourItem() {
        return tourItem;
    }

    public void setTourItem(int tourItem) {
        this.tourItem = tourItem;
    }

    public int getTouristId() {
        return touristId;
    }

    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    public float getTouristAmount() {
        return touristAmount;
    }

    public void setTouristAmount(float touristAmount) {
        this.touristAmount = touristAmount;
    }

    @Override
    public String toString() {
        return "touristid = " + touristId + "; amount = " + touristAmount+ "; cuur_id = " + currAmount;
    }

}
