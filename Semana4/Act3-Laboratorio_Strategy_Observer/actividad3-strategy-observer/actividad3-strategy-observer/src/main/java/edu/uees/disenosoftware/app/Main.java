package edu.uees.disenosoftware.app;

import edu.uees.disenosoftware.domain.Reserva;
import edu.uees.disenosoftware.observer.*;
import edu.uees.disenosoftware.strategy.*;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PASO 1: Probar Observer con Reserva ===");
        Reserva reserva1 = new Reserva("R-001", LocalDateTime.now().plusHours(5));
        reserva1.agregarObserver(new EmailObserver());
        reserva1.agregarObserver(new CalendarioObserver());
        reserva1.agregarObserver(new PanelEstudianteObserver());
        reserva1.agregarObserver(new PanelDocenteObserver());

        System.out.println("--> Confirmando reserva R-001:");
        reserva1.confirmar();

        System.out.println("\n=== PASO 2: Probar Strategy con ServicioCancelacion ===");
        ServicioCancelacion servicio = new ServicioCancelacion(new CancelacionNormal());
        System.out.println("--> Cancelando R-001 con Política Normal:");
        servicio.cancelar(reserva1);

        System.out.println("\n=== PASO 3: Probar Estrategias de Reto (Grupal y Emergencia) ===");
        Reserva reservaGrupal = new Reserva("R-002", LocalDateTime.now().plusHours(30));
        reservaGrupal.agregarObserver(new EmailObserver());
        reservaGrupal.agregarObserver(new PanelEstudianteObserver());

        ServicioCancelacion servicioGrupal = new ServicioCancelacion(new CancelacionGrupal());
        System.out.println("--> Cancelando R-002 con Política Grupal:");
        servicioGrupal.cancelar(reservaGrupal);

        Reserva reservaEmergencia = new Reserva("R-003", LocalDateTime.now().plusMinutes(30));
        reservaEmergencia.agregarObserver(new EmailObserver());
        reservaEmergencia.agregarObserver(new PanelDocenteObserver());

        ServicioCancelacion servicioEmergencia = new ServicioCancelacion(
                new CancelacionEmergencia("Corte de suministro eléctrico")
        );
        System.out.println("--> Cancelando R-003 con Política de Emergencia:");
        servicioEmergencia.cancelar(reservaEmergencia);
    }
}