package com.example.bhattaadmin.Models;

public class entsDetail {
    int rate_of_bharai;
    int cuts_of_pachhisa;

    public entsDetail() {
    }

    public entsDetail(int rate_of_bharai, int cuts_of_pachhisa) {
        this.rate_of_bharai = rate_of_bharai;
        this.cuts_of_pachhisa = cuts_of_pachhisa;
    }

    public int getRate_of_bharai() {
        return rate_of_bharai;
    }

    public void setRate_of_bharai(int rate_of_bharai) {
        this.rate_of_bharai = rate_of_bharai;
    }

    public int getCuts_of_pachhisa() {
        return cuts_of_pachhisa;
    }

    public void setCuts_of_pachhisa(int cuts_of_pachhisa) {
        this.cuts_of_pachhisa = cuts_of_pachhisa;
    }
}
