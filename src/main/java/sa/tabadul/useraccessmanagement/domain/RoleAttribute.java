package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;


@Data
@Entity
@Table(name="ROLE_ATTRIBUTE" , schema = "dbo")
public class RoleAttribute {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	
	@Column(name="NAME")
	private String name;
	
	@NotNull
	@Column(name="VALUE")
	private String value;
	

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "ROLE_ID")
    @JsonBackReference
    private  KeyClockRole keyClockRole;

}
