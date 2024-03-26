package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESOURCE_SERVER_POLICY", schema = "dbo")
public class ResourceServerPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private String id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "DECISION_STRATEGY")
	private String decisionStrategy;

	@Column(name = "LOGIC")
	private String logic;

	@Column(name = "RESOURCE_SERVER_ID")
	// @ManyToOne
    // @JoinColumn(referencedColumnName = "id", name = "RESOURCE_SERVER_ID")
    // @JsonBackReference
    private String resourceServerId;

	@Column(name = "OWNER")
	private String owner;

}
