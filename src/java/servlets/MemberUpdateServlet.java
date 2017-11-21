package servlets;

import business.Member;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kmne6
 */
@WebServlet(name = "MemberUpdateServlet", urlPatterns = {"/MemberUpdate"})
public class MemberUpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String sql = "", msg = "", URL = "/MemberScreen.jsp";
        String fdata = "";
        long newpwd = 0;
        Member m;
        Member n = new Member();

        String dbURL = "jdbc:mysql://localhost:3306/club";
        String dbUser = "root";
        String dbPwd = "sesame";

        try {
            m = (Member) request.getSession().getAttribute("m");
            n.setMemid(m.getMemid());   // n is set on session, and m cannot operate on same memory location
            n.setMemdt(m.getMemdt());
            n.setStatus(m.getStatus());
            n.setPassattempt(m.getPassattempt());
            
            
            // allow changes to last, first and middle name
            // obtain form elements for all updatable fields...
            // get last name
            try {
                fdata = request.getParameter("lastname");
                if (!fdata.isEmpty()) {
                    n.setLastnm(fdata);
                } else {
                    msg += "Last name is empty.<br>";
                }
            } catch (Exception e) {
                msg += "Last name exception<br>";
            }
            
            // get first name
            try {
                fdata = request.getParameter("firstname");
                if (!fdata.isEmpty()) {
                    n.setFirstnm(fdata);
                } else {
                    msg += "First name is empty<br>";
                }
            } catch (Exception e) {
                msg += "First name exception<br>";
            }
            
            
            // get middle name
            try {
                fdata = request.getParameter("middlename");
                if (!fdata.isEmpty()) {
                    n.setMiddlenm(fdata);
                } else {
                    msg += "Middle name is empty.<br>";
                }
            } catch (Exception e) {
                msg += "Middle name exception<br>";
            }
            
            // get status
     /*       try {
                fdata = request.getParameter("status");
                if(!fdata.isEmpty()) {
                    n.setStatus(fdata);
                } else {
                    msg += "Member status is missing.<br>";
                }
            } catch (Exception e) {
                msg += "Status exception<br>";
            }
    */        
            // get member date
            try {
                fdata = request.getParameter("memdt");
                if(!fdata.isEmpty()) {
                    n.setStatus(fdata);
                } else {
                    msg += "Member join date is missing.<br>";
                }
            } catch (Exception e) {
                msg += "Join date exception.<br>";
            }
            
            // get password
            try {
               newpwd = Long.parseLong(request.getParameter("psswd"));
               if(newpwd > 0) {
                   n.setPassword(newpwd);
               } else {
                   msg += "Password illegal<br>";
               }                
            } catch (NumberFormatException e) {
                msg += "Missing/bad password<br>";
            }
           
            if(msg.isEmpty()) {
                n.setLastnm(m.getLastnm());
                n.setFirstnm(m.getFirstnm());
            }
            // continue for other fields
            
            
            if (msg.isEmpty()) {
                // update database...
                Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPwd);

                sql = "UPDATE tblmembers SET " +
                        " LastName = ?, " +
                        " FirstName = ?, " +
                        " MiddleName = ?, " +
                        " Status = ?, " +
                        " MemDt = ?, " +
                        " Password = ? " +
                        " WHERE MemID = ? ";

                PreparedStatement ps = conn.prepareStatement(sql);
                // statements start from 1 (counting number of question marks in the query
                ps.setString(1, n.getLastnm());
                ps.setString(2, n.getFirstnm());
                ps.setString(3, n.getMiddlenm());
                ps.setString(4, n.getStatus());
                ps.setString(5, n.getMemdt());
                ps.setLong(6, n.getPassword());
                ps.setString(7, n.getMemid());
                int rc = ps.executeUpdate();
                if (rc == 0) {
                    msg += "Update failed: no records changed.<br>";
                } else if (rc == 1) {
                    msg += "Member updated!<br>";
                    m = n;
                    request.getSession().setAttribute("m", m);
                } else {
                    msg += "Unexpected update of " + rc + "records.<br>";
                }
            }

        } catch (SQLException e) {
            msg += "SQL exception: " + sql + " " + e.getMessage();
        } catch (Exception e) {
            msg += "General error: " + e.getMessage();
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
