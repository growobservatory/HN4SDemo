/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.growexample1.controllers;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.growexample1.Lib.Convertors;
import uk.ac.dundee.computing.aec.growexample1.Lib.Web;

/**
 *
 * @author andycobley
 */
@WebServlet(name = "timeseries", urlPatterns = {"/timeseries","/timeseries/*"})
public class timeseries extends HttpServlet {

    Web w = null;
    private HashMap CommandsMap = new HashMap();

    public void init(ServletConfig config) throws ServletException {
        w = new Web();
        CommandsMap.put("JSON", 1);
        CommandsMap.put("CSV", 2);
        CommandsMap.put("D3", 3);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String timeseries = "http://grow-beta-api.hydronet.com/api/timeseries/get";
        String body = null;
        String body1 = "{\n"
                + "	\"Readers\": [\n"
                + "		{\n"
                + "			\"DataSourceCode\": \"Thingful.Connectors.GROWSensors\",\n"
                + "			\"Settings\": {\n"
                + "				\"LocationCodes\": [\n"
                + "					\"c4ztedvc\"\n"
                + "				],\n"
                + "				\"VariableCodes\": [\n"
                + "					\"Thingful.Connectors.GROWSensors.light\"\n"
                + "				],\n"
                + "				\"StartDate\": \"20170329000000\",\n"
                + "				\"EndDate\": \"20180501000000\",\n"
                + "				\"StructureType\": \"TimeSeries\",\n"
                + "				\"CalculationType\": \"None\"\n"
                + "			}\n"
                + "		}\n"
                + "	],\n";
        String sCSV = "\"Exporter\": {\n"
                + "    \"DataFormatCode\": \"hydronet.csv.simple\",\n"
                + "    \"Settings\": {\n"
                + "      \"Projection\": {\n"
                + "        \"Epsg\": \"3857\"\n"
                + "      }\n"
                + "    }\n"
                + "  },";

        String body2 = "	\"TimeZoneOffset\": \"+0000\"\n"
                + "}";

        //Split the request path into args.  We then use the HASHMAP to look for keywords 
        String args[] = Convertors.SplitRequestPath(request);
        boolean JSON = false;
        boolean CSV = false;
        boolean D3 = false;
        body = body1 + body2;
        for (int i = 0; i < args.length; i++) {
            if (CommandsMap.get(args[i]) != null) {
                switch ((Integer) CommandsMap.get(args[i])) {
                    case 1:
                        JSON = true;
                        body = body1 + body2;
                        break;
                    case 2:
                        CSV = true;
                        body = body1 + sCSV + body2;
                        break;
                    case 3:
                        D3 = true;
                        break;
                    default:
                        body = body1 + body2;
                        break;

                }
            }
        }

        JsonValue obj = null;
        String CSVresponse=null;
        if (w != null) {
            if (CSV !=true){ 
                obj = w.GetJson(timeseries, body);
            }else
                CSVresponse=w.GetString(timeseries, body);
        }

        try {
            PrintWriter out = response.getWriter();
            if (CSV==false)
               out.print(obj);
            else
                out.print(CSVresponse);
        } catch (Exception et) {
            System.out.println("Can not forward " + et);
        }
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
