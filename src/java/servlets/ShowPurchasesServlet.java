/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import business.ConnectionPool;
import business.Member;
import business.Purchase;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kmne6
 */
public class ShowPurchasesServlet extends HttpServlet {

    String msg = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String URL = "/MemberScreen.jsp";
        String sql = "";
        //    String msg = "";
        String mo, dy, yr, sqlwhere;

        try {
            Member m = (Member) request.getSession().getAttribute("m");

            mo = request.getParameter("month");
            dy = request.getParameter("day");
            yr = request.getParameter("year");

            // if date fields are empty...
            if (mo.isEmpty() || dy.isEmpty() || yr.isEmpty()) {
                sqlwhere = "";
            } else {
                sqlwhere = yr + "-" + mo + "-" + dy;
            }

            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();

            sql = "SELECT ?, ?, ?, ?, ?, ? FROM ?, ? WHERE ? = ? AND ? = '" + m.getMemid()
                    + "' ";
            if (!sqlwhere.isEmpty()) {
                sql += " AND p.purchasedt >= '" + sqlwhere + "' ";
            }
            //          if (!sqlwhere.isEmpty()) {
            //            sql += " AND ? >= '" + sqlwhere + "' ";
            //      }
            sql += " ORDER BY p.purchasedt";
            //        sql += " ORDER BY ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "tblpurchases.MemID");
            ps.setString(2, "tblpurchases.PurchaseDt");
            ps.setString(3, "tblpurchases.TransType");
            ps.setString(4, "tblpurchases.transCd");
            ps.setString(5, "tblcodes.TransDesc");
            ps.setString(6, "tblpurchases.Amount");
            ps.setString(7, "tblpurchases");
            ps.setString(8, "tblcodes");
            ps.setString(9, "tblpurchases.transcd");
            ps.setString(10, "tblcodes.transcd");
            ps.setString(11, "tblpurchases.memid");
           // ps.setString(12, "tblpurchases.purchasedt");
            //         ps.setString(13, "tblpurchases.purchasedt");

            int rc = ps.executeUpdate();

            // best to replace with prepared statement EC opportunity
            /*           Statement s = conn.createStatement();
            sql = "SELECT p.MemID, p.PurchaseDt, p.TransType, " + "p.transCd, c.TransDesc, p.Amount "
                    + " FROM tblpurchases p, tblcodes c " + " WHERE p.transcd = c.transcd "
                    + "  AND p.memid = '" + m.getMemid() + "' ";
            if (!sqlwhere.isEmpty()) {
                sql += " AND p.purchasedt >= '" + sqlwhere + "' ";
            }
            sql += " ORDER BY p.purchasedt";
             */
            ResultSet r = ps.executeQuery(sql); // was s.executeQuery(sql

            ArrayList<Purchase> pur = new ArrayList<>();

            while (r.next()) {
                Purchase p = new Purchase(
                        r.getString("PurchaseDt"),
                        r.getString("TransType"),
                        r.getString("TransCd"),
                        r.getString("TransDesc"),
                        r.getDouble("Amount")
                );
                pur.add(p);
            }
            r.last();
            msg = "Total records = " + r.getRow() + ".<br>Account balance is " + getBalance(m);

            URL = "/Purchases.jsp";
            request.setAttribute("pur", pur);

            r.close();
            pool.freeConnection(conn);
            conn.close();

//            msg = "Connection pool logic OK.<br>";
        } catch (Exception e) {
            msg = "Connection exception: " + e.getMessage() + "<br>";
        }

        request.setAttribute("msg", msg);

        RequestDispatcher disp = getServletContext().getRequestDispatcher(URL);
        disp.forward(request, response);

    }

    private String getBalance(Member m) {

        double balance = 0;
        String query = "";
        String type = "";
        NumberFormat curr = NumberFormat.getCurrencyInstance();

        try {
            //double balance = 0;
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();

            Statement s = conn.createStatement();

            query = "SELECT TransType, Amount FROM tblpurchases WHERE MemId = '" + m.getMemid() + "';";

            ResultSet r = s.executeQuery(query);
            int count = 0;
            while (r.next()) {
                count++;
                //    String type = "";
                type = r.getString("TransType");
                System.out.println("Transaction Type = " + type + " number of transactions = " + count + " Balance = " + r.getDouble("Amount"));
                if (type.equalsIgnoreCase("D")) {
                    balance += r.getDouble("Amount");
                } else if (type.equalsIgnoreCase("C")) {
                    balance -= r.getDouble("Amount");
                } else {
                    msg = "Unrecognized transaction type<br>";
                }
            }
            return curr.format(balance);
        } catch (Exception e) {
            msg = "Invalid query";
            return msg;
        }

        //    return balance;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
