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
@Table(schema = "dbo", name = "RESOURCE_SERVER_RESOURCE")
public class ResourceServerResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ICON_URI")
    private String iconUri;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "RESOURCE_SERVER_ID")
    private String resourceServerId;

    @Column(name = "OWNER_MANAGED_ACCESS")
    private boolean ownerManagedAccess;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

}
