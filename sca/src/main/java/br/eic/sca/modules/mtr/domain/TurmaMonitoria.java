package br.eic.sca.modules.mtr.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="mtr_turma_monitoria")
@Entity(name="TurmaMonitoria")
public class TurmaMonitoria implements Comparable<TurmaMonitoria>
{
	//
	// Atributos Mapeados
	//	
	@Id    
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column
	private String local;
	
	@Column
	private String disciplina;
	
	@Enumerated
	@Column(name="dia_da_semana")
	private DayOfWeek diaDaSemana = DayOfWeek.MONDAY;
	
	@Column(name="hora_inicio")
	private LocalTime horaInicio;	
	
	@Column(name="hora_fim")
	private LocalTime horaFim;
	
	//
	// Métodos de Acesso
	//
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
	public String getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}
	public DayOfWeek getDiaDaSemana() {
		return diaDaSemana;
	}
	public String getDiaDaSemanaPtBr() {
		return diaDaSemana.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("Pt-BR")).split("-")[0];
	}
	public void setDiaDaSemana(DayOfWeek diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
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
	
	//
	// Métodos Auxiliares
	//
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
		TurmaMonitoria other = (TurmaMonitoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(TurmaMonitoria o) 
	{
		return id.compareTo(o.id);
	}
	
	@Override
	public String toString() {
		return "TurmaMonitoria [id=" + id + ", local=" + local + ", disciplina=" + disciplina + ", diaDaSemana="
				+ diaDaSemana + ", horaInicio=" + horaInicio + ", horaFim=" + horaFim + "]";
	}
}








