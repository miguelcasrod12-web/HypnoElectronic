package com.hypnoelectronic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Citas</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; padding: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h2>?? Gesti�n de Citas</h2>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Paciente</th><th>Fecha</th><th>Hora</th><th>Estado</th></tr>");
        out.println("<tr><td>1</td><td>Juan P�rez</td><td>2024-01-15</td><td>10:00 AM</td><td>Programada</td></tr>");
        out.println("<tr><td>2</td><td>Mar�a Garc�a</td><td>2024-01-16</td><td>02:30 PM</td><td>Completada</td></tr>");
        out.println("</table>");
        out.println("<br><a href='dashboard.jsp'>Volver al dashboard</a>");
        out.println("</body></html>");
    }
}
