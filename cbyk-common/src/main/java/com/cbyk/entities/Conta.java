package com.cbyk.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.cbyk.enums.SituacaoContaEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table( name = "CONTA" )
public class Conta implements Serializable {

	private static final long serialVersionUID = 738950144481495231L;

	@Id
	@Column( name = "ID" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@Column( name = "DC_DESCRICAO", nullable = false )
	private String descricao;

	@Column( name = "VL_VALOR", nullable = false )
	private BigDecimal valor;

	@Enumerated( EnumType.STRING )
	@Column( name = "DC_SITUACAO" )
	private SituacaoContaEnum situacao;

	@Column( name = "DATA_VENCIMENTO", nullable = false )
	private LocalDate dataVencimento;

	@Column( name = "DATA_PAGAMENTO" )
	private LocalDate dataPagamento;

}
