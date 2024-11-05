package Controlador;

import Excepciones.CitasException;
import Excepciones.ClientesExcepciones;
import Excepciones.UserNotFound;
import Modelo.DAO.daoCitas;
import Modelo.DAO.daoDiagnosticos;
import Modelo.DAO.daoRecepcion;
import Modelo.DAO.daoVeterinarios;
import Modelo.VO.Cita;
import Modelo.VO.Diagnostico;
import Modelo.VO.Recepcion;
import Modelo.VO.Veterinario;
import Vista.VistaResumen;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ControladorResumen {

    static VistaResumen vistaRes = new VistaResumen();
    static int totalCitas = 0;
    static double totalPrecioCitas = 0;
    static double totalPersonal = 0;
    static String citaSoli = "";

    public static void mostrarVentana() throws SQLException, ClientesExcepciones, UserNotFound, CitasException {
        vistaRes = new VistaResumen();
        vistaRes.setVisible(true);
        calcular();
        calcularUtilidad();
    }

    public static void reiniciar() {
        totalCitas = 0;
        totalPersonal = 0;
        totalPrecioCitas = 0;
        citaSoli = "";
    }

    public static void calcular() throws SQLException, ClientesExcepciones, UserNotFound, CitasException {
        calcularCitaSolicitada();
        calcularPagoPersonal();
        calcularPrecioCitas();
        calcularTotalCitas();
        vistaRes.getTxt_dineroCitas().setText("$" + totalPrecioCitas + "");
        vistaRes.getTxt_pagos().setText("$" + totalPersonal + "");
        vistaRes.getTxt_totalCitas().setText(totalCitas + "");
        vistaRes.getTxt_tipo().setText(citaSoli);
    }

    public static void calcularUtilidad() {
        double otros = (double) vistaRes.getSpin_gastos().getValue();
        double utilidad = totalPrecioCitas - (totalPersonal + otros);
        vistaRes.getTxt_utilidad().setText("$" + utilidad);
        if (utilidad < 0) {
            vistaRes.getTxt_utilidad().setForeground(Color.red);
        } else {
            vistaRes.getTxt_utilidad().setForeground(Color.GREEN);
        }
    }

    public static void regresar() {
        reiniciar();
        vistaRes.setVisible(false);
        ControladorMenuAdmi.mostrarVentana();
        vistaRes.dispose();
    }

    public static void calcularTotalCitas() throws SQLException, ClientesExcepciones, UserNotFound {
        ArrayList<Cita> lista = daoCitas.listaCitas();

        totalCitas = lista.size();
    }

    public static void calcularCitaSolicitada() throws SQLException, ClientesExcepciones, UserNotFound {
        ArrayList<Cita> lista = daoCitas.listaCitas();
        int rev, estetico, vac, estere;
        rev = estetico = vac = estere = 0;
        for (Cita cita : lista) {
            String tipo = cita.getTipo();
            switch (tipo) {
                case "Revision":
                    rev++;
                case "Estetico":
                    estetico++;
                case "Vacunacion":
                    vac++;
                case "Esterelizacion":
                    estere++;
            }
        }
        int[] tipos = {rev, estetico, vac, estere};
        Arrays.sort(tipos);
        int mas = tipos[3];
        boolean revision = mas == rev;
        boolean estetica = mas == estetico;
        boolean vacuna = mas == vac;
        boolean esterelizar = mas == estere;

        if (revision) {
            citaSoli = "Revision";
        } else if (estetica) {
            citaSoli = "Estetico";
        } else if (vacuna) {
            citaSoli = "Vacunacion";
        } else if (esterelizar) {
            citaSoli = "Esterelizacion";
        }

    }

    public static void calcularPrecioCitas() throws SQLException, CitasException, UserNotFound, ClientesExcepciones {
        ArrayList<Diagnostico> lista = daoDiagnosticos.getLista();
        for (Diagnostico diagnostico : lista) {
            totalPrecioCitas += diagnostico.getPrecio();
        }
    }

    public static void calcularPagoPersonal() throws SQLException {
        ArrayList<Veterinario> listaVet = daoVeterinarios.getVeterinarios();
        ArrayList<Recepcion> listaRecep = daoRecepcion.lista();
        for (Veterinario vet : listaVet) {
            totalPersonal += vet.getSueldo();
        }
        for (Recepcion recep : listaRecep) {
            totalPersonal += recep.getSueldo();
        }
    }

}
