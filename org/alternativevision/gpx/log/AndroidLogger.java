/*
 * AndroidLogger.java
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

public class AndroidLogger extends AbstractLogger implements ILogger {

	public static final String logClassName = "android.util.Log";
	private static final String ANDROIDCLASSNAME = "android.util.Log";
	
    public AndroidLogger() {
    	this.tag = "GPXParser";
    	setClassName(ANDROIDCLASSNAME);
    	init();
    }

    public void debug(Object message) {
        callMethodWithTag("d", message.toString());
    }

    public void info(Object message) {
    	callMethodWithTag("i", message.toString());
    }

    public void error(Object message) {
    	callMethodWithTag("e", message.toString());
    }

    public void error(Object message, Throwable th) {
    	callMethodWithTag("e", message.toString(),th);
    }

    public void warn(Object message) {
    	callMethodWithTag("w", message.toString());
    }

	public void setTag(String tag) {
		this.tag = tag;
	}

}