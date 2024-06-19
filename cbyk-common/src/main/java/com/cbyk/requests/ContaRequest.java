package com.cbyk.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class ContaRequest {

	@NotBlank
	private String descricao;

	@NotNull( message = "Valor � obrigat�rio" )
	@DecimalMin( value = "0.0", inclusive = false, message = "O valor deve ser maior que 0" )
	private BigDecimal valor;
	
	@NotNull( message = "Situa��o � obrigat�ria" )
	private SituacaoContaEnum situacao;

	@NotNull( message = "Data de vencimento � obrigat�ria" )
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone="GMT-3" )
	private LocalDate dataVencimento;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone="GMT-3" )
	private LocalDate dataPagamento;
}
