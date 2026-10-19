package com.uniajc;

public class CuentaAhorros extends Cuenta {

    protected boolean activa = false;

    private static final float SALDO_MINIMO = 10_000f;
    private static final float COMISION_RETIRO = 1_000f;
    private static final int NUMERO_RETIROS_SIN_COMISION = 4;

    CuentaAhorros(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
        cuentaActivaSaldoMinimo();
    }

    public boolean cuentaActivaSaldoMinimo() {
        activa = saldo >= SALDO_MINIMO;
        return activa;
    }

    @Override
    public void consignar(float cantidad) {
        validarCantidadConsignacion(cantidad);
        if (!activa) {
            return;
        }
        super.consignar(cantidad);
        cuentaActivaSaldoMinimo();
    }

    @Override
    public void retirar(float cantidad) {
        if (activa) {
            super.retirar(cantidad);
            cuentaActivaSaldoMinimo();
        }
    }

    @Override
    public void extractoMensual() {
        if (!activa) {
            return;
        }

        int retirosExcedentes = Math.max(0, numeroRetiros - NUMERO_RETIROS_SIN_COMISION);
        comisionMensual = retirosExcedentes * COMISION_RETIRO;
        super.extractoMensual();
        cuentaActivaSaldoMinimo();
    }

    @Override
    public void imprimir() {
        super.imprimir();
        System.out.println("Cuenta activa: " + activa);
    }
}
