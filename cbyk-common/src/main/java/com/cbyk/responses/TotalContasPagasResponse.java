package com.cbyk.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalContasPagasResponse {

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone = "GMT-3" )
	private LocalDate dataInicio;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt", timezone = "GMT-3" )
	private LocalDate dataFim;

	private BigDecimal valorTotal;

	private Long quantidadeContasPagas;

	public TotalContasPagasResponse( BigDecimal valorTotal, Long quantidadeContasPagas ) {
		this.valorTotal = valorTotal;
		this.quantidadeContasPagas = quantidadeContasPagas;
	}
}
