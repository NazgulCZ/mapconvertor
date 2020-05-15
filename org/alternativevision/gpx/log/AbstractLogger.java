package org.alternativevision.gpx.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractLogger implements ILogger{
	public String logClassName = "";
	
	protected Object log = null;
	protected Class<?> logClass = null;
	
    protected String tag = "GPSParser";
    
    protected void setClassName(String name) {
    	logClassName = name;
    }
    
    protected void init() {
    	try {
			logClass = Class.forName(logClassName);
        	//log = logClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    protected void  callMethodWithTag(String methodName, String message) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, String.class, String.class);
			method.invoke(log,tag, message);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
    protected void  callMethodWithTag(String methodName, String message, Throwable th) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, String.class, String.class, Throwable.class);
			method.invoke(log,tag, message, th);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
    protected void callMethod(String methodName, String message) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, String.class);
			method.invoke(log, message);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
    protected void callMethod(String methodName, String message, Throwable throwable) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, String.class, Throwable.class);
			method.invoke(log, message, throwable);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
    protected void callMethod(Object obj, String methodName, Object message) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, Object.class);
			method.invoke(obj, message);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
    protected void callMethod(Object obj, String methodName, Object message, Throwable throwable) { 
    	Method method = null;
		try {
			method = logClass.getMethod(methodName, String.class, Throwable.class);
			method.invoke(obj, message, throwable);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
    
}
