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
import java.sql.ResultSet;
import java.sql.Statement;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String URL = "/MemberScreen.jsp";
        String sql = "";
        String msg = "";
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

            // best to replace with prepared statement EC opportunity
            Statement s = conn.createStatement();
            sql = "SELECT p.MemID, p.PurchaseDt, p.TransType, " + "p.transCd, c.TransDesc, p.Amount "
                    + " FROM tblpurchases p, tblcodes c " + " WHERE p.transcd = c.transcd "
                    + "  AND p.memid = '" + m.getMemid() + "' ";
            if (!sqlwhere.isEmpty()) {
                sql += " AND p.purchasedt >= '" + sqlwhere + "' ";
            }
            sql += " ORDER BY p.purchasedt";

            ResultSet r = s.executeQuery(sql);

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
            msg = "Total records = " + r.getRow();

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
