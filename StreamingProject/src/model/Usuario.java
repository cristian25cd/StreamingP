package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Usuario {

	@Id
	private String login;
	
	private String password;
	
	
	@OneToMany
	private List<Video> videos;
	
	@OneToMany
	private List<ListaDeReproduccion> listasR;

	
	public Usuario() {
		videos= new ArrayList<Video>();
		listasR = new ArrayList<ListaDeReproduccion>();
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the videos
	 */
	public List<Video> getVideos() {
		return videos;
	}

	/**
	 * @param videos the videos to set
	 */
	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	/**
	 * @return the listasR
	 */
	public List<ListaDeReproduccion> getListasR() {
		return listasR;
	}

	/**
	 * @param listasR the listasR to set
	 */
	public void setListasR(List<ListaDeReproduccion> listasR) {
		this.listasR = listasR;
	}
	
}
