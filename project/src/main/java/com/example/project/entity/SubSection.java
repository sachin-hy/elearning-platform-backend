package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubSection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String timeDuration;
	private String description;
	private String vedioUrl;
	private String additionalUrl;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	@JsonBackReference("subsection")
	private Section section;

	
	public void addSection(Section section)
	{
		this.section = section;
		section.getSubSection().add(this);
	}
	
   public void removeSection()
   {
	   this.section.getSubSection().remove(this);
   }
	
	
}
