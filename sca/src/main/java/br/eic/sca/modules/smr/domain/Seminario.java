package br.eic.sca.modules.smr.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="smr_seminario")
@Entity(name="Seminario")
public class Seminario implements Comparable<Seminario>
{
	
	@Id    
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column
	private String local;
	
	@Column
	private String titulo;
	
	@Column(name="data")
	private LocalDate data;
	
	@Column(name="hora_inicio")
	private LocalTime horaInicio;	
	
	@Column(name="hora_fim")
	private LocalTime horaFim;
	
	
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setDisciplina(String titulo) {
		this.titulo = titulo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

	@Override
	public int compareTo(Seminario o) 
	{
		return id.compareTo(o.id);
	}
	
	@Override
	public String toString() {
		return null;
	}
}