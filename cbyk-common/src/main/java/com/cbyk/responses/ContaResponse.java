package com.cbyk.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cbyk.enums.SituacaoContaEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContaResponse {

	private Long id;

	private String descricao;

	private BigDecimal valor;
	
	private SituacaoContaEnum situacao;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone="GMT-3" )
	private LocalDate dataVencimento;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone="GMT-3" )
	private LocalDate dataPagamento;
}
