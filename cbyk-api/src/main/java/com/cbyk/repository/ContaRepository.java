package com.cbyk.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbyk.entities.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

	@Query( "SELECT c FROM Conta c WHERE c.dataVencimento = :dataVencimento AND LOWER(c.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY c.descricao" )
	List<Conta> findByDataVencimentoAndDescricaoContainingIgnoreCase( @Param( "dataVencimento" ) LocalDate dataVencimento, @Param( "descricao" ) String descricao );
}
