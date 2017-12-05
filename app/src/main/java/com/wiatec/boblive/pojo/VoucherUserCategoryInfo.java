package com.wiatec.boblive.pojo;

public class VoucherUserCategoryInfo {

    private int id;
    private String category;
    private int level;
    private float price;

    //开始计算bonus的起始月数
    private int startMonth;
    private int bonus;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "VoucherUserCategoryInfo{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", level=" + level +
                ", price=" + price +
                ", startMonth=" + startMonth +
                ", bonus=" + bonus +
                ", description='" + description + '\'' +
                '}';
    }
}
