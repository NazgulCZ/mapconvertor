/*
 * FacadeLogger.java
 * 
 * Copyright (c) 2013, AlternativeVision. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.alternativevision.gpx.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * In order to be mode flexible, an additional logging layer was created. This layer makes logging abstract
 * so multiple logging APIs can be used. Each logging API must have a class that implements the {@link ILogger} interface. 
 * </br></br>
 * This class initializes the logging system based on the properties set in gpxparser.properties. 
 * The property <b>logClass</b> needs to point to a fully qualified class name that implement the {@link ILogger} interface.
 * Three classes are provided with the mechanism: {@link DummyLogger}, {@link AndroidLogger} and {@link Log4JLogger}.
 * </br>
 * </br>
 * You can change the default setting by changing the <b>logClass</b> property value in the gpxparser.properties:</br>
 * <code>
 * logClass=com.yourDomain.yourApplication.logging.yourLoggerAPIwrapperClass
 * </code>
 * </br>
 * </br>
 */
public class FacadeLogger {
	
	public static final String DEFAULT_LOGGER = DummyLogger.class.getName();
	
	public static final String GPX_PROPERTY_FILE = "gpxparser.properties";
	
	public static final String LOG_CLASS_KEY = "logClass";

	
	
	private static String loggerClass = null;

	private static final FacadeLogger instance = new FacadeLogger();
	
	public static FacadeLogger getInstance() {
		return instance;
	}
	
	private FacadeLogger() {
		try {
		init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Properties props = new Properties();
		File propertiesFile = new File(GPX_PROPERTY_FILE);
		if(propertiesFile.exists()) {
			try {
				props.load(new FileInputStream(propertiesFile));
				if(props.containsKey(LOG_CLASS_KEY)) {
					loggerClass = props.getProperty(LOG_CLASS_KEY);
				}
			} catch (IOException ex) {
				
			}
		} else {
			//we have no properties file, assuming the default values
			loggerClass = DEFAULT_LOGGER;
		}
		
	}
	
	public ILogger getLogger(String tag) {
		ILogger _logger = null;
		try {
			
			_logger = (ILogger) Class.forName(loggerClass).getDeclaredConstructor().newInstance();
			_logger.setTag(tag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _logger;
	}
}
