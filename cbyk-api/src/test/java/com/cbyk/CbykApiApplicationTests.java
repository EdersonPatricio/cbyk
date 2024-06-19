package com.cbyk;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cbyk.enums.SituacaoContaEnum;
import com.cbyk.requests.ContaRequest;
import com.cbyk.requests.ContaUpdateRequest;
import com.cbyk.responses.ContaResponse;
import com.cbyk.responses.TotalContasPagasResponse;
import com.cbyk.service.ContaService;
import com.cbyk.utils.ObjectConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
public class CbykApiApplicationTests {
	
	private final String BASE_URL = "/cbyk-api";
	private final String USERNAME = "user";
    private final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaService contaService;

    private ContaRequest contaRequest;
    private ContaResponse contaResponse;
    private ContaUpdateRequest contaUpdateRequest;;
    private TotalContasPagasResponse totalContasPagasResponse;

	@BeforeEach
	void setUp() {
		objectMapper.registerModule( new JavaTimeModule() );
		contaRequest = new ContaRequest( "Conta de Luz", BigDecimal.valueOf( 100.0 ), SituacaoContaEnum.PAGA, LocalDate.now().plusDays( 10 ), LocalDate.now() );
		contaResponse = new ContaResponse( 1L, "Conta de Luz", BigDecimal.valueOf( 100.0 ), SituacaoContaEnum.PAGA, LocalDate.now().plusDays( 10 ), LocalDate.now() );
		contaUpdateRequest = ObjectConverter.convert( contaResponse, ContaUpdateRequest.class );
		totalContasPagasResponse = new TotalContasPagasResponse( LocalDate.now().minusDays( 1 ), LocalDate.now().plusDays( 1 ), BigDecimal.valueOf( 100.0 ), 1L );
	}

	@Test
	void testSave() throws Exception {
		given( contaService.save( any( ContaRequest.class ) ) ).willReturn( contaResponse );

		this.mockMvc.perform( post( BASE_URL + "/save" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON )
			.content( objectMapper.writeValueAsString( contaRequest ) ) )
			.andExpect( status().isCreated() )
			.andExpect( jsonPath( "$.descricao", is( "Conta de Luz" ) ) );
		
		verify( contaService, times( 1 ) ).save( any( ContaRequest.class ) );
    }

	@Test
	void testAtualizarConta() throws Exception {
		given( contaService.findPorId( anyLong() ) ).willReturn( contaResponse );
		given( contaService.update( any( ContaUpdateRequest.class ) ) ).willReturn( contaResponse );

		mockMvc.perform( put( BASE_URL + "/atualizar-conta/1" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON )
			.content( objectMapper.writeValueAsString( contaUpdateRequest ) ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$.descricao", is( "Conta de Luz" ) ) );

		verify( contaService, times( 1 ) ).findPorId( anyLong() );
		verify( contaService, times( 1 ) ).update( any( ContaUpdateRequest.class ) );
	}

	@Test
	void testAtualizarSituacaoConta() throws Exception {
		given( contaService.findPorId( anyLong() ) ).willReturn( contaResponse );
		given( contaService.update( any( ContaUpdateRequest.class ) ) ).willReturn( contaResponse );

		mockMvc.perform( put( BASE_URL + "/atualizar-situacao/1?situacao=" + SituacaoContaEnum.PAGA.name() )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$.situacao", is( SituacaoContaEnum.PAGA.name() ) ) );

		verify( contaService, times( 1 ) ).findPorId( anyLong() );
		verify( contaService, times( 1 ) ).update( any( ContaUpdateRequest.class ) );
	}

	@Test
	void testFindAllPageable() throws Exception {
		given( contaService.findAllPageable( any( PageRequest.class ) ) ).willReturn( Arrays.asList( contaResponse ) );

		mockMvc.perform( get( BASE_URL + "/find-paginado?page=0&size=10" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$[0].descricao", is( "Conta de Luz" ) ) );

		verify( contaService, times( 1 ) ).findAllPageable( any( PageRequest.class ) );
	}

	@Test
	void testFindById() throws Exception {
		given( contaService.findPorId( anyLong() ) ).willReturn( contaResponse );

		mockMvc.perform( get( BASE_URL + "/find-por-id/1" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$.descricao", is( "Conta de Luz" ) ) );

		verify( contaService, times( 1 ) ).findPorId( anyLong() );
	}

	@Test
	void testGetContasByDataVencimentoAndDescricao() throws Exception {
		given( contaService.findContasByDataVencimentoAndDescricao( any( LocalDate.class ), anyString() ) ).willReturn( Arrays.asList( contaResponse ) );

		mockMvc.perform( get( BASE_URL + "/find-contas-a-pagar?dataVencimento=2024-06-27&descricao=Conta de Luz" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$[0].descricao", is( "Conta de Luz" ) ) );

		verify( contaService, times( 1 ) ).findContasByDataVencimentoAndDescricao( any( LocalDate.class ), anyString() );
	}

	@Test
	void testCalcularTotalPagoPorPeriodo() throws Exception {
		given( contaService.calcularTotalPagoPorPeriodo( any( LocalDate.class ), any( LocalDate.class ) ) ).willReturn( totalContasPagasResponse );

		mockMvc.perform( get( BASE_URL + "/total-pago-por-periodo?dataInicio=2024-06-16&dataFim=2024-06-18" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath( "$.valorTotal", is( 100.0 ) ) );

		verify( contaService, times( 1 ) ).calcularTotalPagoPorPeriodo( any( LocalDate.class ), any( LocalDate.class ) );
	}

	void testDeletar() throws Exception {
		given( contaService.findPorId( anyLong() ) ).willReturn( contaResponse );
		doNothing().when( contaService ).delete( anyLong() );

		mockMvc.perform( delete( BASE_URL + "/deletar/1" )
			.with( httpBasic( USERNAME, PASSWORD ) )
			.contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isNoContent() );

		verify( contaService, times( 1 ) ).findPorId( anyLong() );
		verify( contaService, times( 1 ) ).delete( anyLong() );
	}
}
