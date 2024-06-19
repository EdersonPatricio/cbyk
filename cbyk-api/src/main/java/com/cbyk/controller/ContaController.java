package com.cbyk.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbyk.entities.Conta;
import com.cbyk.enums.SituacaoContaEnum;
import com.cbyk.requests.ContaRequest;
import com.cbyk.requests.ContaUpdateRequest;
import com.cbyk.responses.ContaResponse;
import com.cbyk.responses.TotalContasPagasResponse;
import com.cbyk.service.ContaService;
import com.cbyk.utils.FileServiceUtil;
import com.cbyk.utils.ObjectConverter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping( "/cbyk-api" )
@Tag( name = "ContaController" )
public class ContaController {
	
	private static Logger LOG = LoggerFactory.getLogger( ContaController.class );
	
	@Autowired
	private ContaService contaService;
	
	@PostMapping( "/save" )
	@ResponseStatus( HttpStatus.CREATED )
	@Operation( description = "Cadastra uma nova conta" )
	public ResponseEntity<ContaResponse> save( @Valid @RequestBody ContaRequest request ) {
		LOG.info( this.getClass().getName() + " - save - INICIO" );
		
		ContaResponse clientResponse = contaService.save( request );
		
		LOG.info( this.getClass().getName() + " - save - FIM" );
		
		return ResponseEntity.status( HttpStatus.CREATED ).body( clientResponse );
	}
	
	@PutMapping( "/atualizar-conta/{contaId}" )
	@Operation( description = "Atualiza uma conta" )
	public ResponseEntity<ContaResponse> atualizarConta( @PathVariable Long contaId, @Valid @RequestBody ContaUpdateRequest request ) {
		LOG.info( this.getClass().getName() + " - atualizarConta - INICIO" );
		
		ContaResponse result = contaService.findPorId( contaId );
		
		if ( Objects.isNull( result ) ) {
			return ResponseEntity.notFound().build();
		}

		request.setId( result.getId() );
		ContaResponse clientResponse = contaService.update( request );
		
		LOG.info( this.getClass().getName() + " - atualizarConta - FIM" );
		
		return ResponseEntity.ok( clientResponse );
	}
	
	@PutMapping( "/atualizar-situacao/{contaId}" )
	@Operation( description = "Atualiza a situação de uma conta" )
	public ResponseEntity<ContaResponse> atualizarSituacaoConta( @PathVariable Long contaId, @RequestParam SituacaoContaEnum situacao ) {
		LOG.info( this.getClass().getName() + " - atualizarSituacaoConta - INICIO" );
		
		ContaResponse result = contaService.findPorId( contaId );
		
		if ( Objects.isNull( result ) ) {
			return ResponseEntity.notFound().build();
		}

		result.setSituacao( situacao );
		result = contaService.update( ObjectConverter.convert( result, ContaUpdateRequest.class ) );
		
		LOG.info( this.getClass().getName() + " - atualizarSituacaoConta - FIM" );
		
		return ResponseEntity.ok( result );
	}
	
	@GetMapping( path = "/find-paginado", params = { "page", "size" } )
	@Operation( description = "Consulta todos as contas de forma paginada" )
	public ResponseEntity<List<ContaResponse>> findPaginado( @RequestParam int page, @RequestParam int size ) {
		LOG.info( this.getClass().getName() + " - findAllPageable - INICIO" );
		
		List<ContaResponse> contas = contaService.findAllPageable( PageRequest.of( page, size ) );
		
		if ( Objects.nonNull( contas ) ) {
			return ResponseEntity.ok( contas );
		}
		
		LOG.info( this.getClass().getName() + " - findAllPageable - FIM" );
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping( "/find-por-id/{contaId}" )
	@Operation( description = "Consulta uma conta a partir do id" )
	public ResponseEntity<ContaResponse> findPorId( @PathVariable Long contaId ) {
		LOG.info( this.getClass().getName() + " - findById - INICIO" );
		
		ContaResponse conta = contaService.findPorId( contaId );
		
		if ( Objects.nonNull( conta ) ) {
			return ResponseEntity.ok( conta );
		}
		
		LOG.info( this.getClass().getName() + " - findById - FIM" );
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping( "/find-contas-a-pagar" )
	@Operation( description = "Consulta lista de contas a pagar com filtro de data de vencimento e descrição" )
	public ResponseEntity<List<ContaResponse>> findContasByDataVencimentoAndDescricao( @RequestParam LocalDate dataVencimento, @RequestParam String descricao ) {
		LOG.info( this.getClass().getName() + " - findContasByDataVencimentoAndDescricao - INICIO" );
		
		List<ContaResponse> contas = contaService.findContasByDataVencimentoAndDescricao( dataVencimento, descricao );
		
		if ( Objects.nonNull( contas ) ) {
			return ResponseEntity.ok( contas );
		}
		
		LOG.info( this.getClass().getName() + " - findContasByDataVencimentoAndDescricao - FIM" );
		
		return ResponseEntity.noContent().build();
	}

	@GetMapping( "/total-pago-por-periodo" )
	@Operation( description = "Obtem o valor total pago por período" )
	public ResponseEntity<TotalContasPagasResponse> calcularTotalPagoPorPeriodo( @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim ) {
		LOG.info( this.getClass().getName() + " - calcularTotalPagoPorPeriodo - INICIO" );
		
		TotalContasPagasResponse totalContasPagasResponse = contaService.calcularTotalPagoPorPeriodo( dataInicio, dataFim );
		
		LOG.info( this.getClass().getName() + " - calcularTotalPagoPorPeriodo - FIM" );
		
		return ResponseEntity.ok( totalContasPagasResponse );
	}
	
	@PostMapping( path = "/importar-contas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
	@Operation( description = "Realiza importação de contas a pagar via arquivo CSV" )
	public ResponseEntity<Void> importarContas( @RequestParam( "file" ) MultipartFile file ) {
		LOG.info( this.getClass().getName() + " - importarContas - INICIO" );
		
		List<Conta> contas = FileServiceUtil.extrairRegistros( file );
		
		contaService.saveAll( contas );
		
		LOG.info( this.getClass().getName() + " - importarContas - FIM" );
		
		return ResponseEntity.ok().build();
    }
	
	@DeleteMapping( "/deletar/{contaId}" )
	@Operation( summary = "Deleta uma conta" )
	public ResponseEntity<Void> deletar( @Valid @PathVariable Long contaId ) {
		LOG.info( this.getClass().getName() + " - deletar - INICIO" );
		
		ContaResponse result = contaService.findPorId( contaId );
		
		if ( Objects.isNull( result ) ) {
			return ResponseEntity.notFound().build();
		}
		
		contaService.delete( contaId );
		
		LOG.info( this.getClass().getName() + " - deletar - FIM" );
		
		return ResponseEntity.noContent().build();
	}
}
