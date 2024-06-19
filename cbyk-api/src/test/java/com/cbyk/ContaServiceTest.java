package com.cbyk;

import com.cbyk.entities.Conta;
import com.cbyk.enums.SituacaoContaEnum;
import com.cbyk.repository.ContaRepository;
import com.cbyk.requests.ContaRequest;
import com.cbyk.responses.ContaResponse;
import com.cbyk.responses.TotalContasPagasResponse;
import com.cbyk.service.ContaService;
import com.cbyk.utils.ObjectConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class ContaServiceTest {

	@InjectMocks
	private ContaService contaService;

	@Mock
	private ContaRepository contaRepository;

	private Conta conta;

	private ContaRequest contaRequest;

	private ContaResponse contaResponse;

	@BeforeEach
	void setUp() {
		conta = new Conta( 1L, "Conta de Luz", BigDecimal.valueOf( 100.0 ), SituacaoContaEnum.PAGA, LocalDate.now().plusDays( 10 ), LocalDate.now() );
		contaRequest = ObjectConverter.convert( conta, ContaRequest.class );
		contaResponse = ObjectConverter.convert( conta, ContaResponse.class );
	}

	@Test
	void testSave() {
		when( contaRepository.save( any( Conta.class ) ) ).thenReturn( conta );
		ContaResponse response = contaService.save( contaRequest );
		assertNotNull( response );
		assertEquals( contaResponse.getDescricao(), response.getDescricao() );
		verify( contaRepository, times( 1 ) ).save( any( Conta.class ) );
	}

	@Test
	void testSaveAll() {
		List<Conta> contas = Arrays.asList( conta );
		contaService.saveAll( contas );
		verify( contaRepository, times( 1 ) ).saveAll( contas );
	}
	
	@Test
	void testUpdate() {
		when( contaRepository.save( any( Conta.class ) ) ).thenReturn( conta );
		contaRequest.setSituacao( SituacaoContaEnum.PAGA );
		ContaResponse response = contaService.update( contaRequest );
		assertNotNull( response );
		assertEquals( contaResponse.getDescricao(), response.getDescricao() );
		assertEquals( contaResponse.getSituacao(), SituacaoContaEnum.PAGA );
		verify( contaRepository, times( 1 ) ).save( any( Conta.class ) );
	}

	@Test
	void testFindById() {
		when( contaRepository.findById( 1L ) ).thenReturn( Optional.of( conta ) );
		ContaResponse response = contaService.findPorId( 1L );
		assertNotNull( response );
		assertEquals( contaResponse.getDescricao(), response.getDescricao() );
		verify( contaRepository, times( 1 ) ).findById( 1L );
	}

	@Test
	void testFindAll() {
		when( contaRepository.findAll() ).thenReturn( Arrays.asList( conta ) );
		List<ContaResponse> response = contaService.findAll();
		assertNotNull( response );
		assertEquals( 1, response.size() );
		verify( contaRepository, times( 1 ) ).findAll();
	}

	@Test
	void testFindAllPageable() {
		Pageable pageable = PageRequest.of( 0, 10 );
		Page<Conta> page = new PageImpl<>( Arrays.asList( conta ) );
		when( contaRepository.findAll( pageable ) ).thenReturn( page );
		List<ContaResponse> response = contaService.findAllPageable( pageable );
		assertNotNull( response );
		assertEquals( 1, response.size() );
		verify( contaRepository, times( 1 ) ).findAll( pageable );
	}

	@Test
	void testFindByDataVencimentoAndDescricaoContaining() {
		when( contaRepository.findByDataVencimentoAndDescricaoContainingIgnoreCase( any( LocalDate.class ), anyString() ) ).thenReturn( Arrays.asList( conta ) );
		List<ContaResponse> response = contaService.findContasByDataVencimentoAndDescricao( LocalDate.now().plusDays( 10 ), "Conta de Luz" );
		assertNotNull( response );
		assertEquals( 1, response.size() );
		verify( contaRepository, times( 1 ) ).findByDataVencimentoAndDescricaoContainingIgnoreCase( any( LocalDate.class ), anyString() );
	}

	@Test
	void testCalcularTotalPagoPorPeriodo() {
		when( contaRepository.findAll() ).thenReturn( Arrays.asList( conta ) );
		TotalContasPagasResponse response = contaService.calcularTotalPagoPorPeriodo( LocalDate.now().minusDays( 1 ), LocalDate.now().plusDays( 1 ) );
		assertNotNull( response );
		assertEquals( BigDecimal.valueOf( 100.0 ), response.getValorTotal() );
		assertEquals( 1, response.getQuantidadeContasPagas() );
		verify( contaRepository, times( 1 ) ).findAll();
	}

	@Test
	void testDelete() {
		doNothing().when( contaRepository ).deleteById( 1L );
		contaService.delete( 1L );
		verify( contaRepository, times( 1 ) ).deleteById( 1L );
	}
}