package com.cbyk.exceptionhandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.cbyk.enums.SituacaoContaEnum;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler( HttpMessageNotReadableException.class )
	public ResponseEntity<Object> handleHttpMessageNotReadable( HttpMessageNotReadableException ex, WebRequest request ) {
		String errorMessage = "Erro de formatação JSON: " + ex.getMessage();
		Throwable cause = ex.getCause();
		
		if ( cause instanceof InvalidFormatException ) {
			InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
			return handleInvalidFormatException( invalidFormatException );
		}
		if ( cause instanceof JsonParseException ) {
			errorMessage = "Valor do campo [valor] inválido. Ao informar valor com casa decimal, deve ser utilizado o ponto no lugar da vírgula.";
			return handleMethodArgumentNotValid( "valor", errorMessage, HttpStatus.BAD_REQUEST );
		}

		return new ResponseEntity<>( errorMessage, HttpStatus.BAD_REQUEST );
	}
	
	@ExceptionHandler( MethodArgumentNotValidException.class )
	public ResponseEntity<Map<String, String>> handleValidationExceptions( MethodArgumentNotValidException ex ) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach( error -> 
			errors.put( error.getField(), error.getDefaultMessage() )
		);
		return new ResponseEntity<>( errors, HttpStatus.BAD_REQUEST );
	}
	
	@ExceptionHandler( MethodArgumentTypeMismatchException.class )
	public ResponseEntity<Object> handleTypeMismatch( MethodArgumentTypeMismatchException ex ) {
		return handleException( ex.getPropertyName(), ex.getValue().toString() );
    }

	@ExceptionHandler( InvalidFormatException.class )
	public ResponseEntity<Object> handleInvalidFormatException( InvalidFormatException ex ) {
		return handleException( extrairValorCampo( ex.getPathReference() ), ex.getValue().toString() );
	}
	
	private ResponseEntity<Object> handleException( String campo, String valor ) {
		String errorMessage = null;
		
		if ( campo.equals( "situacao" ) ) {
			errorMessage = "Valor do campo [%s] inválido: %s. O campo deve ter uma das seguintes opções: %s.";
			errorMessage = String.format( errorMessage, campo, valor, Arrays.toString( SituacaoContaEnum.values() ) );
		} else {
			errorMessage = "Formato do campo [%s] inválido: %s. O campo deve possuir o formato 'dd/MM/yyyy'.";
			errorMessage = String.format( errorMessage, campo, valor );
		}
		
		return handleMethodArgumentNotValid( campo, errorMessage, HttpStatus.BAD_REQUEST );
	}
	
	private String extrairValorCampo( String input ) {
		String regex = ".*\\[\"(.*?)\"\\].*";
		Pattern pattern = Pattern.compile( regex );
		Matcher matcher = pattern.matcher( input );

		if ( matcher.matches() ) {
			return matcher.group( 1 );
		}

		return null;
	}
	
	private ResponseEntity<Object> handleMethodArgumentNotValid( String atributo, String mensagem, HttpStatus status ) {
		Problema problem = new Problema( status.value(), "Campo(s) invalido(s). Preencha corretamente e tente novamente.", LocalDateTime.now() );
		problem.setCampo( new Campo( atributo, mensagem ) );
		
		return new ResponseEntity<>( problem, status );
	}
}
