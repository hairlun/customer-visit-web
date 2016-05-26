package com.jude.system;

import com.jude.util.CachedParams;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;

public class SystemLoaderListener extends ContextLoaderListener {
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		servletContext.setAttribute("contextPath", servletContext.getContextPath());

		CachedParams.load();
		super.contextInitialized(event);
	}
}