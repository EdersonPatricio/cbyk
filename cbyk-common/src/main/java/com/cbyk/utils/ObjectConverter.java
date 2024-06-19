package com.cbyk.utils;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

/**
 * Classe responsável por converter objetos
 * 
 * @author Ederson Patrício
 *
 */
public class ObjectConverter {

	public static <S, D> D convert( Object object, Class<D> destinationType ) {
		ModelMapper modelMapper = new ModelMapper();

		D convertedObject = null;

		try {
			convertedObject = modelMapper.map( object, destinationType );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return convertedObject;
	}

	@SuppressWarnings( "rawtypes" )
	public static <S, D> List<D> convertList( List objects, Class<D> destinationType ) {
		ModelMapper modelMapper = new ModelMapper();

		List<D> convertedObjects = new ArrayList<D>();

		try {
			for ( Object object : objects ) {
				convertedObjects.add( modelMapper.map( object, destinationType ) );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return convertedObjects;
	}
}
