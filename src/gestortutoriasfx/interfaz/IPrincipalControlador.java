package gestortutoriasfx.interfaz;

import gestortutoriasfx.modelo.pojo.Usuario;

/**
 * Nombre de la Clase: IPrincipalControlador
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Interfaz encargada de obligar a inicializar la informacion de los distintos utuarios que existan en
 * la base de datos.
 */

public interface IPrincipalControlador {
    void inicializarInformacion(Usuario usuario);
}