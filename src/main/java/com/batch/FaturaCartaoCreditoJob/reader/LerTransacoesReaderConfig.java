package com.batch.FaturaCartaoCreditoJob.reader;

import com.batch.FaturaCartaoCreditoJob.dominio.CartaoCredito;
import com.batch.FaturaCartaoCreditoJob.dominio.Cliente;
import com.batch.FaturaCartaoCreditoJob.dominio.Transacao;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class LerTransacoesReaderConfig {

    @Bean
    public JdbcCursorItemReader<Transacao> lerTransacoesReader(@Qualifier("appDataSource") DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transacao>()
                .name("lerTransacoesReader")
                .dataSource(dataSource)
                .sql("select * from transacao join cartao_credito using (numero_cartao_credito) order by numero_cartao_credito")
                .rowMapper(rowMapperTransacao())
                .build();
    }

    private RowMapper<Transacao> rowMapperTransacao() {
        return new RowMapper<Transacao>() {
            @Override
            public Transacao mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("cliente"));

                CartaoCredito cartaoCredito = new CartaoCredito();
                cartaoCredito.setNumeroCartaoCredito(resultSet.getInt("numero_cartao_credito"));
                cartaoCredito.setCliente(cliente);

                Transacao transacao = new Transacao();
                transacao.setId(resultSet.getInt("id"));
                transacao.setCartaoDeCredito(cartaoCredito);
                transacao.setData(resultSet.getDate("data"));
                transacao.setValor(resultSet.getDouble("valor"));
                transacao.setDescricao(resultSet.getString("descricao"));

                return transacao;
            }
        };
    }
}
