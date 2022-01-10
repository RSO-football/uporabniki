package rso.football.uporabniki.dtos;

public class PostavkaRequest {

    public PostavkaRequest() {
    }

    private Integer uporabnikID;
    private float pay;

    public PostavkaRequest(Integer uporabnikID, float pay) {
        this.uporabnikID = uporabnikID;
        this.pay = pay;
    }

    public Integer getUporabnikID() {
        return uporabnikID;
    }

    public void setUporabnikID(Integer uporabnikID) {
        this.uporabnikID = uporabnikID;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }
}
