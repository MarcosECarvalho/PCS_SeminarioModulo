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
		this.local = local.trim();
	}

	public String getTitulo() {
		return titulo;
	}
	

	public void setTitulo(String titulo) {
		this.titulo = titulo.trim();
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seminario other = (Seminario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Seminario o) 
	{
		return id.compareTo(o.id);
	}
	
	
	@Override
	public String toString() {
		return "Seminario [id=" + id + ", local=" + local + ", titulo=" + titulo + ", data=" + data + ", horaInicio="
				+ horaInicio + ", horaFim=" + horaFim + "]";
	}
}