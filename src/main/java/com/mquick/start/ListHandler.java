package com.mquick.start;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.ByteArrayISO8859Writer;

public class ListHandler extends AbstractHandler {

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
        if (response.isCommitted() || baseRequest.isHandled())
            return;
        baseRequest.setHandled(true);
		
		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(1500);
        response.setContentType(MimeTypes.TEXT_XML);

        Server server = getServer();
        Handler[] handlers = server==null?null:server.getChildHandlersByClass(ContextHandler.class);
 
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        for (int i=0;handlers!=null && i<handlers.length;i++)
        {
            ContextHandler context = (ContextHandler)handlers[i];
            writer.write("<handler>");
            writer.write("<ContexPath value=\"");
            writer.write(context.getContextPath());
            writer.write("\" />");

            writer.write("<Contex value=\"");
            writer.write(context.toString());
            writer.write("\" />");

            writer.write("<Status value=\"");
            writer.write(context.isRunning() ? "RUN" : "STOP");
            writer.write("\" />");
            
            writer.write("</handler>");
        }
    		
        writer.flush();
        response.setContentLength(writer.size());
        OutputStream out=response.getOutputStream();
        writer.writeTo(out);
        out.close();
        writer.close();
	}
	
}
