package com.example.project.entity;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Section {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String sectionName;
	 
	 @OneToMany(mappedBy = "section",cascade = CascadeType.ALL,orphanRemoval = true)
	 @JsonManagedReference("subsection")
	 private Set<SubSection> subSection  = new HashSet<>();
	 
	 @ManyToOne
	 @JoinColumn(name = "course_id")
	 @JsonBackReference("course-content")
	 private Courses course;
	 
	 
	 
	 public void removeSectionCourse()
	 {
		 this.course.getCourseContent().remove(this);
	 }
     
	 
	 
	
		 
	
}
