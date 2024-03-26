package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ROLE_MANAGEMENT", name = "ACCESS_MODULE")
public class AccessModule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	
	@JsonProperty("access_header_ID")
	@Column(insertable=false, updatable=false, name = "access_header_ID")
    private Integer accessHeaderID;
	
    private String module;
    
    @JsonProperty("module_description")
    @Column(name = "module_description")
    private String moduleDescription;
    
    @JsonProperty("module_icon_path")
    @Column(name = "module_icon_path")
    private String moduleIconPath;
    
    @JsonProperty("module_route")
    @Column(name = "module_route")
    private String moduleRoute;
    
    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;
    
    @JsonProperty("created_date")
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @JsonProperty("updated_by")
    @Column(name = "updated_by")
    private String updatedBy;
    
    @JsonProperty("updated_date")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @JsonProperty("is_active")
    @Column(name = "is_active")
    private Boolean isActive;
    
    @OneToMany(mappedBy = "accessPage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AccessPage>  accessPage = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "access_header_ID")
    @JsonBackReference
    private AccessHeader accessHeader;


 
    
    
}
