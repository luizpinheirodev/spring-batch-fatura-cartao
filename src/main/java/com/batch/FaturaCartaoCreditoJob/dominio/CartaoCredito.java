package com.batch.FaturaCartaoCreditoJob.dominio;

public class CartaoCredito {
    private int numeroCartaoCredito;
    private Cliente cliente;

    public CartaoCredito() {
    }

    public int getNumeroCartaoCredito() {
        return numeroCartaoCredito;
    }

    public void setNumeroCartaoCredito(int numeroCartaoCredito) {
        this.numeroCartaoCredito = numeroCartaoCredito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
