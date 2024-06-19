
package com.cbyk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.cbyk.entities.Conta;
import com.cbyk.enums.SituacaoContaEnum;

public class FileServiceUtil {
	
	private final static String PATTERN_DD_MM_YYYY = "dd/MM/yyyy";

	public static List<Conta> extrairRegistros( MultipartFile file ) {
		List<Conta> contas = new ArrayList<Conta>();
		
		try {
			InputStreamReader inputStreamReader = new InputStreamReader( file.getInputStream(), StandardCharsets.UTF_8 );
			BufferedReader br = new BufferedReader( inputStreamReader );
			String linha = br.readLine();
			
			if ( Objects.isNull( linha ) ) {
				return contas;
			}
			if ( !linha.contains( ";" ) ) {
				throw new IllegalArgumentException( "Layout do arquivo de contas está fora do padrão! Favor verificar!" );
			}

			linha = br.readLine();
			
			while ( Objects.nonNull( linha ) ) {
				String[] objects = linha.split( ";" );
				String descricao = objects[0];
				BigDecimal valor = new BigDecimal( objects[1] );
				SituacaoContaEnum situacao = SituacaoContaEnum.valueOf( objects[2] );
				LocalDate dataVencimento = LocalDate.parse( objects[3], DateTimeFormatter.ofPattern( PATTERN_DD_MM_YYYY ) );
				
				LocalDate dataPagamento = null;
				if ( objects.length > 4 && !objects[4].isEmpty() ) {
					dataPagamento = LocalDate.parse( objects[4], DateTimeFormatter.ofPattern( PATTERN_DD_MM_YYYY ) );
                }
				
				contas.add( new Conta( null, descricao, valor, situacao, dataVencimento, dataPagamento ) );
				
				linha = br.readLine();
			}
			br.close();
			inputStreamReader.close();
		} catch ( IOException e ) {
			throw new IllegalArgumentException( "Erro na abertura do arquivo: " + e.getMessage(), e );
		}
		
		return contas;
	}
}
