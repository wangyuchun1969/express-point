package com.mquick.start;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.mquick.server.websocket.DeployWebsocket;

public class ExpressPoint {
    
    static private class MyDeploymentManager extends DeploymentManager {
    	@Override
    	public void addApp(App app) {
    		super.addApp(app);
    		System.out.println(app.getContextPath());
    	}
    }
    
	public static void main(String[] args) throws Exception {
        

		Server server = new Server(8081);
		HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setServer(server);
        

		ProtectionDomain domain = ExpressPoint.class.getProtectionDomain();
		URL location = domain.getCodeSource().getLocation();
		String war;
		if (!location.toExternalForm().endsWith("WEB-INF/classes/")) {
			war = location.toExternalForm();
		} else {
			// For start from maven exec:java
			int length = location.toExternalForm().length();
			war = location.toExternalForm().substring(0, length - 17);
		}
        
		WebAppContext webapp = new WebAppContext(handlers, war, "/start");
		handlers.addHandler(contexts);

        ServletContextHandler listContext = new ServletContextHandler(contexts, "/list");
		listContext.addServlet(new ServletHolder(new DeployListServlet()), "/");
        server.setHandler(handlers);
		server.start();
		
		// ---------------------------------- jetty-deploy.xml
		// https://github.com/btpka3/btpka3.github.com/blob/master/java/jetty/first-exec-war/src/execWar/java/Main.java
		DeploymentManager deployer = new MyDeploymentManager();
		deployer.setContexts(contexts);
//		deployer.setContextAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/servlet-api-[^/]*\\.jar$");
		WebAppProvider webappProvider = new WebAppProvider();
		webappProvider.setTempDir(new File("./tmp"));
		webappProvider.setMonitoredDirName("./webapps");
		webappProvider.setScanInterval(1);
		webappProvider.setExtractWars(true);
		// webappProvider.setConfigurationManager(new
		// PropertiesConfigurationManager());
		deployer.addAppProvider(webappProvider);
		server.addBean(deployer);


		// NOTE: it works!
		try {
			MyLocalLauncher.browse("http://localhost:8081/start/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
			new DeployWebsocket();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		server.join();
	}

	private static class MyLocalLauncher {

		/**
		 * A browser launcher that uses JDK 1.6 Desktop.browse support.
		 */
		private static class Jdk16Launcher extends ReflectiveLauncher {

			/**
			 * Create a Jdk16Launcher if supported.
			 * 
			 * @throws UnsupportedOperationException
			 *             if not supported
			 */
			public Jdk16Launcher() throws UnsupportedOperationException {
				try {
					Class<?> desktopClass = Class.forName("java.awt.Desktop");
					browseMethod = desktopClass.getMethod("browse", URI.class);
					Method factory = desktopClass.getMethod("getDesktop");
					browseObject = factory.invoke(null);
					return;
				} catch (ClassNotFoundException e) {
					// not on JDK 1.6, try other methods
				} catch (NoSuchMethodException e) {
					// not on JDK 1.6, try other methods
				} catch (SecurityException e) {
					// ignore, try other methods
				} catch (IllegalArgumentException e) {
					// ignore, try other methods
				} catch (IllegalAccessException e) {
					// ignore, try other methods
				} catch (InvocationTargetException e) {
					// ignore, try other methods
				}
				throw new UnsupportedOperationException(
						"no JDK 1.6 Desktop.browse");
			}

			@Override
			protected Object convertUrl(String url) throws URISyntaxException,
					MalformedURLException {
				return new URL(url).toURI();
			}
		}

		private interface Launcher {
			void browse(String url) throws IOException, URISyntaxException;
		}

		/**
		 * Interface for launching a URL in a browser, which uses reflection.
		 * 
		 * <p>
		 * Subclass must set browseObject and browseMethod appropriately.
		 */
		private abstract static class ReflectiveLauncher implements Launcher {

			protected Object browseObject;
			protected Method browseMethod;

			public void browse(String url) throws IOException,
					URISyntaxException {
				Object arg = convertUrl(url);
				Throwable caught = null;
				try {
					browseMethod.invoke(browseObject, arg);
					return;
				} catch (InvocationTargetException e) {
					Throwable cause = e.getCause();
					if (cause instanceof IOException) {
						throw (IOException) cause;
					}
					caught = e;
				} catch (IllegalAccessException e) {
					caught = e;
				}
				throw new RuntimeException("Unexpected exception", caught);
			}

			/**
			 * Convert the URL into another form if required. The default
			 * implementation simply returns the unmodified string.
			 * 
			 * @param url
			 *            URL in string form
			 * @return the URL in the form needed for browseMethod
			 * @throws URISyntaxException
			 * @throws MalformedURLException
			 */
			protected Object convertUrl(String url) throws URISyntaxException,
					MalformedURLException {
				return url;
			}
		}

		/**
		 * Launch a browser by searching for a browser executable on the path.
		 */
		private static class UnixExecBrowserLauncher implements Launcher {

			private static final String[] browsers = { "firefox", "opera",
					"konqueror", "chrome", "chromium", "epiphany", "seamonkey",
					"mozilla", "netscape", "galeon", "kazehakase", };

			private String browserExecutable;

			/**
			 * Creates a launcher by searching for a suitable browser
			 * executable. Assumes the presence of the "which" command.
			 * 
			 * @throws UnsupportedOperationException
			 *             if no suitable browser can be found.
			 */
			public UnixExecBrowserLauncher()
					throws UnsupportedOperationException {
				for (String browser : browsers) {
					try {
						Process process = Runtime.getRuntime().exec(
								new String[] { "which", browser });
						if (process.waitFor() == 0) {
							browserExecutable = browser;
							return;
						}
					} catch (IOException e) {
						// ignore, try next one
					} catch (InterruptedException e) {
						// ignore, try next one
					}
				}
				throw new UnsupportedOperationException(
						"no suitable browser found");
			}

			public void browse(String url) throws IOException {
				Runtime.getRuntime().exec(
						new String[] { browserExecutable, url });
				// TODO(jat): do we need to wait for it to exit and check exit
				// status?
				// That would be best for Firefox, but bad for some of the other
				// browsers.
			}
		}

		/**
		 * Launch the default browser on Windows via the URL protocol handler.
		 */
		private static class WindowsLauncher implements Launcher {

			public void browse(String url) throws IOException {
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
				// TODO(jat): do we need to wait for it to exit and check exit
				// status?
			}
		}

		private static Launcher launcher;

		/**
		 * Browse to a given URI.
		 * 
		 * @param url
		 * @throws IOException
		 * @throws URISyntaxException
		 */
		public static void browse(String url) throws IOException,
				URISyntaxException {
			if (launcher == null) {
				findLauncher();
			}
			launcher.browse(url);
		}

		/**
		 * Initialize launcher to an appropriate one for the current
		 * platform/JDK.
		 */
		private static void findLauncher() {
			try {
				launcher = new Jdk16Launcher();
				return;
			} catch (UnsupportedOperationException e) {
				// ignore and try other methods
			}
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				launcher = new WindowsLauncher();
			} else {
				launcher = new UnixExecBrowserLauncher();
				// let UnsupportedOperationException escape to caller
			}
		}
	}

}
