package com.shuangsetoolsserver.base;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chai
 *
 * Class: BaseAction, all controller must extend this abstract class to 
 *         implement the execute method; 
 */
public abstract class BaseAction extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private HttpServletRequest request = null;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        this.request = request;
        execute(request, response);        
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        this.request = request;
        execute(request, response);
    }
    
    protected abstract void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException;
    
    /*
     * return the URL of this site, 
     * e.g. "http://135.240.131.117:port/LiveVideoServer" 
     */
    public String getBasePath() {
        String path = request.getContextPath(); 
        String basePath = request.getScheme() + "://" + 
                    request.getServerName() + ":" +
                    request.getServerPort() + path;
        return basePath;
    }
    
    /*
     * Return the real path of this website in system.
     * e.g. "/var/lib/tomcat6/webapps/LiveVideoServer/"
     */
    public String getRootRealPathOfWebSite() {
        ServletContext application = request.getServletContext();
        String realPath = application.getRealPath("/");
        return realPath;
    }
}
