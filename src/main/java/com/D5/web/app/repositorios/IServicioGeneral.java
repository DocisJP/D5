package com.D5.web.app.repositorios;



public interface IServicioGeneral <T>{
	
	/*
	 * Si no entendes esto, es porque te fuiste al grupo de front end. 
	 * 
	 * 
	 * 
	 * 
	 * Ahora que te enojaste, mira: 
	 * 	Declaramos <T> como generico, usando 'Generic Objects T' de Java. Para no tener que realizar 200 CRUD.
	 * 
	 * ¿Como se utiliza?
	 * Googlea.
	 * 
	 * Na, usa ChatGpt
	 * 
	 * 
	 * Ok, la cosa es así: 
	 * 
	 * Cuando llames a los métodos de la interfaz, le tenes que indicar el objecto que le estas pasando utilizando '<>'. 
	 * Por ejemplo: 
	 * 
	 * @Override
	 * public void agregar (Usuario usuario)
	 * {
	 * 		//Se hace el método
	 * }
	 * 
	 * @Override
	 * public void modificar (Proyecto proyecto)
	 * {
	 * 		//Se hace el método
	 * }
	 * 
	 * etc, etc, etc
	 */

	void agregar(T algunaEntidad);
	void modificar(T algunaEntidad);
	void eliminar(T algunaEntidad);
	void cambiarEstado(T algunaEntidad);
	void crear(T algunaEntidad);
	void registrar(T algunaEntidad);
	void valida(T algunError);
	
	void visualizar(T dashBoardoProyectoReunion);
	void accederPerfil(T algunClienteoAgente);
	
	
	
}
