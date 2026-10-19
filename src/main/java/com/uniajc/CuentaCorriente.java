package com.uniajc;

public class CuentaCorriente extends Cuenta {

    protected float sobregiro = 0f; 

    CuentaCorriente(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
    }

    @Override
    public void consignar(float cantidad) {
        validarCantidadConsignacion(cantidad);

        float pagoSobregiro = Math.min(cantidad, sobregiro);
        sobregiro -= pagoSobregiro;
        super.consignar(cantidad - pagoSobregiro);
    }

    @Override
    public void retirar(float cantidad) {
        if (saldo < cantidad) {
            float saldoDisponible = saldo;
            super.retirar(saldoDisponible);
            sobregiro += cantidad - saldoDisponible;
        } else {
            super.retirar(cantidad);
        }
    }

    @Override 
    public void imprimir() {
        super.imprimir();
        System.out.println("Sobregiro: " + sobregiro);
    }

}
