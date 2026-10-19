package com.uniajc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CuentaAhorrosTest {

    private static final float DELTA = 0.001f;

    @Test
    void seCreaActivaCuandoElSaldoEsIgualAlMinimo() {
        CuentaAhorros cuenta = new CuentaAhorros(10_000f, 0.12f);

        assertTrue(cuenta.activa);
    }

    @Test
    void cuentaInactivaNoPermiteConsignacionesNiRetiros() {
        CuentaAhorros cuenta = new CuentaAhorros(9_000f, 0.12f);

        cuenta.consignar(2_000f);
        cuenta.retirar(1_000f);

        assertFalse(cuenta.activa);
        assertEquals(9_000f, cuenta.saldo, DELTA);
        assertEquals(0, cuenta.numeroConsignaciones);
        assertEquals(0, cuenta.numeroRetiros);
    }

    @Test
    void retiroQueDejaSaldoMenorAlMinimoDesactivaLaCuenta() {
        CuentaAhorros cuenta = new CuentaAhorros(11_000f, 0.12f);

        cuenta.retirar(2_000f);

        assertEquals(9_000f, cuenta.saldo, DELTA);
        assertEquals(1, cuenta.numeroRetiros);
        assertFalse(cuenta.activa);
    }

    @Test
    void extractoMensualConRetirosExcedentesCobraComisionYCalculaInteres() {
        CuentaAhorros cuenta = new CuentaAhorros(20_000f, 0.12f);
        for (int i = 0; i < 5; i++) {
            cuenta.retirar(1_000f);
        }

        cuenta.extractoMensual();

        assertEquals(1_000f, cuenta.comisionMensual, DELTA);
        assertEquals(14_140f, cuenta.saldo, DELTA);
        assertTrue(cuenta.activa);
    }

    @Test
    void extractoMensualSinRetirosExcedentesSoloCalculaInteres() {
        CuentaAhorros cuenta = new CuentaAhorros(12_000f, 0.12f);

        cuenta.extractoMensual();

        assertEquals(0f, cuenta.comisionMensual, DELTA);
        assertEquals(12_120f, cuenta.saldo, DELTA);
    }

    @Test
    void consignacionNegativaEnCuentaInactivaLanzaExcepcion() {
        CuentaAhorros cuenta = new CuentaAhorros(9_000f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.consignar(-500f));

        assertEquals(9_000f, cuenta.saldo, DELTA);
        assertEquals(0, cuenta.numeroConsignaciones);
    }
}
