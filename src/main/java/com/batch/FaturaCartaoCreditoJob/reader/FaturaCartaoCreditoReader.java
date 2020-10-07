package com.batch.FaturaCartaoCreditoJob.reader;

import com.batch.FaturaCartaoCreditoJob.dominio.FaturaCartaoCredito;
import com.batch.FaturaCartaoCreditoJob.dominio.Transacao;
import org.springframework.batch.item.*;

public class FaturaCartaoCreditoReader implements ItemStreamReader<FaturaCartaoCredito> {

    private ItemStreamReader<Transacao> delegate;
    private Transacao transacaoAtual;

    public FaturaCartaoCreditoReader(ItemStreamReader<Transacao> delegate) {
        this.delegate = delegate;
    }

    @Override
    public FaturaCartaoCredito read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (transacaoAtual == null)
            transacaoAtual = delegate.read();

        FaturaCartaoCredito faturaCartaoCredito = null;
        Transacao transacao = transacaoAtual;
        transacaoAtual = null;

        if (transacao != null) {
            faturaCartaoCredito = new FaturaCartaoCredito();
            faturaCartaoCredito.setCartaoCredito(transacao.getCartaoDeCredito());
            faturaCartaoCredito.setCliente(transacao.getCartaoDeCredito().getCliente());
            faturaCartaoCredito.getTransacoes().add(transacao);

            while (isTransacaoRelacionada(transacao))
                faturaCartaoCredito.getTransacoes().add(transacaoAtual);
        }
        return faturaCartaoCredito;
    }

    private boolean isTransacaoRelacionada(Transacao transacao) throws Exception {
        return peek() != null && transacao.getCartaoDeCredito().getNumeroCartaoCredito() == transacaoAtual.getCartaoDeCredito().getNumeroCartaoCredito();
    }

    private Transacao peek() throws Exception {
        transacaoAtual = delegate.read();
        return transacaoAtual;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
