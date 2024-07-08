package com.cbyk.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cbyk.entities.Conta;
import com.cbyk.enums.SituacaoContaEnum;
import com.cbyk.repository.ContaRepository;
import com.cbyk.requests.ContaRequest;
import com.cbyk.requests.ContaUpdateRequest;
import com.cbyk.responses.ContaResponse;
import com.cbyk.responses.TotalContasPagasResponse;
import com.cbyk.utils.FileServiceUtil;
import com.cbyk.utils.ObjectConverter;

@Service
@Transactional
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	public ContaResponse save( ContaRequest request ) {
		return salvarConta( request );
	}
	
	public ContaResponse atualizarConta( Long contaId, ContaUpdateRequest request ) {
		ContaResponse result = findPorId( contaId );

		if ( Objects.isNull( result ) ) {
			return null;
		}

		request.setId( result.getId() );
		
		return salvarConta( request );
	}

	public ContaResponse atualizarSituacaoConta( Long contaId, SituacaoContaEnum situacao ) {
		ContaResponse result = findPorId( contaId );

		if ( Objects.isNull( result ) ) {
			return null;
		}

		result.setSituacao( situacao );
		
		return salvarConta( ObjectConverter.convert( result, ContaUpdateRequest.class ) );
	}
	
	private ContaResponse salvarConta( ContaRequest request ) {
		Conta conta = ObjectConverter.convert( request, Conta.class );

		return ObjectConverter.convert( contaRepository.save( conta ), ContaResponse.class );
	}

	public void saveAll( List<ContaRequest> contas ) {
		contaRepository.saveAll( ObjectConverter.convertList( contas, Conta.class ) );
	}

	public ContaResponse findPorId( Long contaId ) {
		Optional<Conta> result = contaRepository.findById( contaId );

		if ( result.isPresent() ) {
			return ObjectConverter.convert( result.get(), ContaResponse.class );
		}

		return null;
	}

	public List<ContaResponse> findAll() {
		List<Conta> result = contaRepository.findAll();

		return ObjectConverter.convertList( result, ContaResponse.class );
	}

	public List<ContaResponse> findPaginado( Pageable pageable ) {
		Page<Conta> result = contaRepository.findAll( pageable );

		return ObjectConverter.convertList( result.getContent(), ContaResponse.class );
	}

	public List<ContaResponse> findContasByDataVencimentoAndDescricao( LocalDate dataVencimento, String descricao ) {
		List<Conta> result = contaRepository.findByDataVencimentoAndDescricaoContainingIgnoreCase( dataVencimento, descricao );

		return ObjectConverter.convertList( result, ContaResponse.class );
	}
	
	public TotalContasPagasResponse calcularTotalPagoPorPeriodo( LocalDate dataInicio, LocalDate dataFim ) {
		TotalContasPagasResponse totalContasPagas = contaRepository.findAll().stream()
	        .filter( conta -> Objects.nonNull( conta.getDataPagamento() ) &&
	                ( conta.getDataPagamento().isEqual( dataInicio ) || conta.getDataPagamento().isAfter( dataInicio ) ) &&
	                ( conta.getDataPagamento().isEqual( dataFim ) || conta.getDataPagamento().isBefore( dataFim ) ) )
	        .collect( Collectors.teeing(
	                Collectors.reducing( BigDecimal.ZERO, Conta::getValor, BigDecimal::add ),
	                Collectors.counting(),
	                TotalContasPagasResponse::new
			) );
		totalContasPagas.setDataInicio( dataInicio );
		totalContasPagas.setDataFim( dataFim );
		
		return totalContasPagas;
	}
	
	public void importarContas( MultipartFile file ) {
		List<ContaRequest> contas = FileServiceUtil.extrairRegistros( file );

		saveAll( contas );
	}

	public void delete( Long contaId ) {
		contaRepository.deleteById( contaId );
	}
}
