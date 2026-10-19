package com.uniajc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CuentaCorrienteTest {

    private static final float DELTA = 0.001f;

    @Test
    void retiroSuperiorAlSaldoGeneraSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente(10_000f, 0.12f);

        cuenta.retirar(12_000f);

        assertEquals(0f, cuenta.saldo, DELTA);
        assertEquals(2_000f, cuenta.sobregiro, DELTA);
        assertEquals(1, cuenta.numeroRetiros);
    }

    @Test
    void consignacionParcialPagaSobregiroYCuentaLaOperacion() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.sobregiro = 10_000f;

        cuenta.consignar(4_000f);

        assertEquals(0f, cuenta.saldo, DELTA);
        assertEquals(6_000f, cuenta.sobregiro, DELTA);
        assertEquals(1, cuenta.numeroConsignaciones);
    }

    @Test
    void consignacionSuperiorAlSobregiroPagaDeudaYAumentaSaldo() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.sobregiro = 10_000f;

        cuenta.consignar(15_000f);

        assertEquals(5_000f, cuenta.saldo, DELTA);
        assertEquals(0f, cuenta.sobregiro, DELTA);
        assertEquals(1, cuenta.numeroConsignaciones);
    }

    @Test
    void retirosPosterioresConSobregiroPendienteAcumulanLaDeuda() {
        CuentaCorriente cuenta = new CuentaCorriente(1_000f, 0.12f);
        cuenta.retirar(1_500f);

        cuenta.retirar(300f);

        assertEquals(0f, cuenta.saldo, DELTA);
        assertEquals(800f, cuenta.sobregiro, DELTA);
        assertEquals(2, cuenta.numeroRetiros);
    }

    @Test
    void extractoMensualAplicaComisionEInteres() {
        CuentaCorriente cuenta = new CuentaCorriente(10_000f, 0.12f);
        cuenta.comisionMensual = 50f;

        cuenta.extractoMensual();

        assertEquals(10_049.5f, cuenta.saldo, DELTA);
    }

    @Test
    void consignacionNegativaConSobregiroLanzaExcepcionYSinCambiarDeuda() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.sobregiro = 1_000f;

        assertThrows(IllegalArgumentException.class, () -> cuenta.consignar(-500f));

        assertEquals(0f, cuenta.saldo, DELTA);
        assertEquals(1_000f, cuenta.sobregiro, DELTA);
        assertEquals(0, cuenta.numeroConsignaciones);
    }
}
