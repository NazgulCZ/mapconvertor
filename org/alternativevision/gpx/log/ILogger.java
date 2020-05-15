/*
 * ILogger.java
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

/**
 * 
 * This interface defines the logging methods that are used in the GPXParser API.
 * Each Logging API Wrappers must write their own implementation for those methods.
 *
 */
public interface ILogger {
	
	/**
	 * This method sets the name of the entity that requests to write the message
	 * Equivalent with the tag parameter from Android API, or with classname parameter from Log4J
	 */
	public void setTag(String tag);
	
	/**
	 * This sends a message with level debug to the underlying Logging API 
	 */
    public void debug(Object log);

    /**
	 * This sends a message with level info to the underlying Logging API 
	 */
    public void info(Object log);

    /**
	 * This sends a message with level error to the underlying Logging API 
	 */
    public void error(Object log);
    
    /**
	 * This sends a message with level error to the underlying Logging API 
	 */
    public void error(Object log, Throwable th);

    /**
	 * This sends a message with level warn to the underlying Logging API 
	 */
    public void warn(Object log);
}
