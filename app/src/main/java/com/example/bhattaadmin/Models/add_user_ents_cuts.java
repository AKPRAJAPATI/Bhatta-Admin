package com.example.bhattaadmin.Models;

public class add_user_ents_cuts {
    int bharai_rate;
    int cuts_of_pachhisa;
    int total_ents;
    int kati_gayi_ents;
    int money;
    boolean showbtn;

    public add_user_ents_cuts() {
    }

    public add_user_ents_cuts(int bharai_rate, int cuts_of_pachhisa, int total_ents, int kati_gayi_ents, int money, boolean showbtn) {
        this.bharai_rate = bharai_rate;
        this.cuts_of_pachhisa = cuts_of_pachhisa;
        this.total_ents = total_ents;
        this.kati_gayi_ents = kati_gayi_ents;
        this.money = money;
        this.showbtn = showbtn;
    }

    public int getBharai_rate() {
        return bharai_rate;
    }

    public void setBharai_rate(int bharai_rate) {
        this.bharai_rate = bharai_rate;
    }

    public int getCuts_of_pachhisa() {
        return cuts_of_pachhisa;
    }

    public void setCuts_of_pachhisa(int cuts_of_pachhisa) {
        this.cuts_of_pachhisa = cuts_of_pachhisa;
    }

    public int getTotal_ents() {
        return total_ents;
    }

    public void setTotal_ents(int total_ents) {
        this.total_ents = total_ents;
    }

    public int getKati_gayi_ents() {
        return kati_gayi_ents;
    }

    public void setKati_gayi_ents(int kati_gayi_ents) {
        this.kati_gayi_ents = kati_gayi_ents;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isShowbtn() {
        return showbtn;
    }

    public void setShowbtn(boolean showbtn) {
        this.showbtn = showbtn;
    }
}
