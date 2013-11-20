package com.mquick.start;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.ByteArrayISO8859Writer;
import org.eclipse.jetty.webapp.WebAppContext;

public class DeployListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4600617389312363882L;
	
    private ServletContext _servletContext;
    private ContextHandler _contextHandler;
    @Override
    public void init()
    throws UnavailableException
    {
        _servletContext=getServletContext();
        _contextHandler = initContextHandler(_servletContext);
    }
	
    /**
     * Compute the field _contextHandler.<br/>
     * In the case where the DefaultServlet is deployed on the HttpService it is likely that
     * this method needs to be overwritten to unwrap the ServletContext facade until we reach
     * the original jetty's ContextHandler.
     * @param servletContext The servletContext of this servlet.
     * @return the jetty's ContextHandler for this servletContext.
     */
    protected ContextHandler initContextHandler(ServletContext servletContext)
    {
        ContextHandler.Context scontext=ContextHandler.getCurrentContext();
        if (scontext==null)
        {
            if (servletContext instanceof ContextHandler.Context)
                return ((ContextHandler.Context)servletContext).getContextHandler();
            else
                throw new IllegalArgumentException("The servletContext " + servletContext + " " + 
                    servletContext.getClass().getName() + " is not " + ContextHandler.Context.class.getName());
        }
        else
            return ContextHandler.getCurrentContext().getContextHandler();
    }
	
	
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(1500);
        response.setContentType(MimeTypes.TEXT_XML);

        
        Server server = _contextHandler.getServer();
        Handler[] handlers = server==null?null:server.getChildHandlersByClass(ContextHandler.class);
 
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><handlers>");
        for (int i=0;handlers!=null && i<handlers.length;i++)
        {
            ContextHandler context = (ContextHandler)handlers[i];
            writer.write("<handler ContexPath=\"");
            writer.write(context.getContextPath());
            writer.write("\"");

            writer.write(" Status=\"");
            writer.write(context.isRunning() ? "RUN" : "STOP");
            writer.write("\">");

            writer.write("<Contex value=\"");
            writer.write(context.toString());
            writer.write("\" />");

            
            writer.write("</handler>");
        }
        writer.write("</handlers>");

        writer.flush();
        response.setContentLength(writer.size());
        OutputStream out=response.getOutputStream();
        writer.writeTo(out);
        out.close();
        writer.close();
    }
}
