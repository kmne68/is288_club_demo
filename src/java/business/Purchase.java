/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author kmne6
 */
public class Purchase {

    private String purchdt, purchtype, transcd, transdesc;
    private double amount;
    private NumberFormat curr = NumberFormat.getCurrencyInstance();

    public Purchase() {
        this.purchdt = "";
        this.purchtype = "";
        this.transcd = "";
        this.transdesc = "";
        this.amount = 0;
    }

    public Purchase(String pdt, String ptype, String tcd, String tdesc, double amt) {
        this.purchdt = pdt;
        this.purchtype = ptype;
        this.transcd = tcd;
        this.transdesc = tdesc;
        this.amount = amt;
    }

    public String getPurchdt() {
        return purchdt;
    }

    public void setPurchdt(String purchdt) {
        this.purchdt = purchdt;
    }

    public String getPurchtype() {
        return purchtype;
    }

    public void setPurchtype(String purchtype) {
        this.purchtype = purchtype;
    }

    public String getTranscd() {
        return transcd;
    }

    public void setTranscd(String transcd) {
        this.transcd = transcd;
    }

    public String getTransdesc() {
        return transdesc;
    }

    public void setTransdesc(String transdesc) {
        this.transdesc = transdesc;
    }

    public String getAmount() {
        return curr.format(amount);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
