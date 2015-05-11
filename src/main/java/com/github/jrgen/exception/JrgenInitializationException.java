package com.github.jrgen.exception;

import com.github.jrgen.context.JrgenContext;

/***
 * <p>
 * JrgenInitializationException is thrown when the initialize() method had not
 * been called prior to any attempts to generate a class instance in a given
 * {@link JrgenContext} instance.
 * </p>
 * 
 * @author Allan J. Shoulders
 * @version 1.0
 * @since 1.0.0
 *
 */
public class JrgenInitializationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Override of the default constructor of super class 
	 * {@link RuntimeException}.
	 */
	public JrgenInitializationException() {
		super();
	}
	
	/**
	 * Override of the default constructor of super class 
	 * {@link RuntimeException}.
	 * 
	 * @param message the message presented to the developer retrievable 
	 * by an instance of this Exception.
	 */
	public JrgenInitializationException (String message) {
		super(message);
	}
	
	/**
	 * Override of the default constructor of super class 
	 * {@link RuntimeException}.
	 * 
	 * @param cause the cause which can be another {@link Exception} wrapped
	 * into an instance of this class. It is retrievable for later use.
	 */
	public JrgenInitializationException (Throwable cause) {
		super(cause);
	}
	
	/**
	 * Override of the default constructor of super class 
	 * {@link RuntimeException}.
	 * 
	 * @param message the message presented to the developer retrievable 
	 * by an instance of this Exception.
	 * @param cause the cause which can be another {@link Exception} wrapped
	 * into an instance of this class. It is retrievable for later use.
	 */
	public JrgenInitializationException (String message, Throwable cause) {
		super(message, cause);
	}

}
