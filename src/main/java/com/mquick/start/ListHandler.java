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

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
        if (response.isCommitted() || baseRequest.isHandled())
            return;
        baseRequest.setHandled(true);
		
		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(1500);
        response.setContentType(MimeTypes.TEXT_HTML);

        writer.write("<HTML><HEAD><TITLE>Hello");
        writer.write("</TITLE><BODY>");
        
        Server server = getServer();
        Handler[] handlers = server==null?null:server.getChildHandlersByClass(ContextHandler.class);
 
        for (int i=0;handlers!=null && i<handlers.length;i++)
        {
            ContextHandler context = (ContextHandler)handlers[i];
            if (context.isRunning())
            {
                writer.write("<li><a href=\"");
                writer.write(context.getContextPath());
                if (context.getContextPath().length()>1 && context.getContextPath().endsWith("/"))
                    writer.write("/");
                writer.write("\">");
                writer.write(context.getContextPath());
                if (context.getVirtualHosts()!=null && context.getVirtualHosts().length>0)
                    writer.write("&nbsp;@&nbsp;"+context.getVirtualHosts()[0]+":"+request.getLocalPort());
                writer.write("&nbsp;--->&nbsp;");
                writer.write(context.toString());
                writer.write("</a></li>");
            }
            else
            {
                writer.write("<li>");
                writer.write(context.getContextPath());
                if (context.getVirtualHosts()!=null && context.getVirtualHosts().length>0)
                    writer.write("&nbsp;@&nbsp;"+context.getVirtualHosts()[0]+":"+request.getLocalPort());
                writer.write("&nbsp;--->&nbsp;");
                writer.write(context.toString());
                if (context.isFailed())
                    writer.write(" [failed]");
                if (context.isStopped())
                    writer.write(" [stopped]");
                writer.write("</li>\n");
            }
        }
    		
        writer.write("\n</BODY>\n</HTML>\n");
        writer.flush();
        response.setContentLength(writer.size());
        OutputStream out=response.getOutputStream();
        writer.writeTo(out);
        out.close();
        writer.close();
	}

}
