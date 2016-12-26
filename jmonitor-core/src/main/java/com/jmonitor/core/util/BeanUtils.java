package com.jmonitor.core.util;

import java.lang.reflect.*;

public class BeanUtils {
	public static <T> T instanceClass(Class<T> clazz){
		if(!clazz.isInterface()){
			try{
				return clazz.newInstance();
			}catch(InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		}
		return null;
	}

	public static <T> T instanceClass(Constructor<T> ctor,Object...arg)
		throws IllegalArgumentException,InstantiationException,
		IllegalAccessException,InvocationTargetException{
		makeAccessible(ctor);
		return ctor.newInstance(arg);
	}

	public static Method findMethod(Class<?> clazz,String methodName,Class<?>...paramTypes){
		try{
			return clazz.getMethod(methodName, paramTypes);
		}catch(NoSuchMethodException e){
			return findDeclaredMethod(clazz,methodName,paramTypes);
		}
	}

	public static Method findDeclaredMethod(Class<?> clazz,String methodName,Class<?>[] paramTypes){
		try{
			return clazz.getDeclaredMethod(methodName, paramTypes);
		}catch(NoSuchMethodException e){
			if(clazz.getSuperclass()!=null){
				return findDeclaredMethod(clazz.getSuperclass(),methodName,paramTypes);
			}
			return null;
		}
	}

	public static Method[] findDeclaredMethods(Class<?> clazz){
		return clazz.getDeclaredMethods();
	}

	public static void makeAccessible(Constructor<?> ctor ){
		if((!Modifier.isPublic(ctor.getModifiers()))
				|| !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())
				&&!ctor.isAccessible()){
			ctor.setAccessible(true);
		}
	}

	public static Field[] findDeclaredField(Class<?> clazz){
		return clazz.getDeclaredFields();
	}
}
