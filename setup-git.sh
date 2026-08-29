#!/bin/bash
# Script guia para inicializar el repositorio con un historial de commits
# progresivo, tal como lo pide la Actividad 5 (Parte 7. Git y GitHub).
#
# Ejecutar UNA sola vez, dentro de la carpeta sistema-tutorias/, despues de
# crear el repositorio vacio en GitHub.
#
# Uso:
#   chmod +x setup-git.sh
#   ./setup-git.sh
#   git remote add origin https://github.com/<tu-usuario>/sistema-tutorias.git
#   git push -u origin main

set -e

git init -b main

git add pom.xml .gitignore
git commit -m "chore: inicializar proyecto Maven"

git add src/main/java/edu/uees/tutorias/domain/Usuario.java \
        src/main/java/edu/uees/tutorias/domain/Estudiante.java \
        src/main/java/edu/uees/tutorias/domain/Docente.java \
        src/main/java/edu/uees/tutorias/domain/Asignatura.java
git commit -m "feat: crear clases iniciales del dominio (Usuario, Estudiante, Docente, Asignatura)"

git add src/main/java/edu/uees/tutorias/domain/HorarioTutoria.java \
        src/main/java/edu/uees/tutorias/domain/EstadoReserva.java \
        src/main/java/edu/uees/tutorias/domain/Reserva.java
git commit -m "feat: implementar modelo de horarios y reservas con su maquina de estados"

git add src/main/java/edu/uees/tutorias/domain/repository/RepositorioReservas.java \
        src/main/java/edu/uees/tutorias/persistence/RepositorioReservasEnMemoria.java
git commit -m "feat: agregar puerto de persistencia RepositorioReservas y su implementacion en memoria"

git add src/main/java/edu/uees/tutorias/notification/
git commit -m "refactor: separar servicio de notificaciones (Notificador, NotificadorEmail, NotificadorConsola)"

git add src/main/java/edu/uees/tutorias/service/
git commit -m "feat: implementar ServicioReservas orquestando reglas del dominio"

git add src/main/java/edu/uees/tutorias/App.java
git commit -m "feat: agregar demo de consola que ensambla las dependencias"

git add src/test/
git commit -m "test: agregar pruebas unitarias de ServicioReservasImpl"

git add docs/
git commit -m "docs: agregar diagrama UML de clases"

git add README.md
git commit -m "docs: actualizar README con decisiones de diseno y guia de ejecucion"

echo "Historial creado. Revisa con: git log --oneline"
