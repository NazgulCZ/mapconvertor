/*
 * Log4JLogger.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class Log4JLogger extends AbstractLogger implements ILogger {
	
	public static final String LOG4JCLASSNAME = "org.apache.log4j.Logger";
	private Class factoryClass = null;
	
	Object logger = null;
	
	public Log4JLogger() {
		setClassName(LOG4JCLASSNAME);
		init();
	}
	
	public void setTag(String tag) {
		this.tag = tag;
		initLogger(tag);
		
	}

	public void debug(Object message) {
		callMethod(logger, "debug",message);
	}

	public void info(Object message) {
		callMethod(logger,"info",message);

	}

	public void error(Object message) {
		callMethod(logger,"error",message);

	}

	public void error(Object message, Throwable th) {
		callMethod(logger,"error",message, th);

	}

	public void warn(Object message) {
		callMethod(logger,"warn",message);

	}
	
	protected void initLogger(String tag) {
    	try {
			Method m = logClass.getMethod("getLogger", String.class);
			logger = m.invoke(null, tag);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
}
