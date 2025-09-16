package com.example.demo.exception;

public class ExceptionMessage extends Exception {

	private String errorMessage;

	// Constructor que recibe el mensaje de error
	public ExceptionMessage(String errorMessage) {
        super(errorMessage); 
        this.errorMessage = errorMessage;
    }

	// Método para obtener el mensaje de error
	@Override
	public String getMessage() {
		return this.errorMessage; 
	}
}
