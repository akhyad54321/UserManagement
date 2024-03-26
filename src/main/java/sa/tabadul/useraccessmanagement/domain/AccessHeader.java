package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(schema = "ROLE_MANAGEMENT", name = "ACCESS_HEADER")
public class AccessHeader {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String header;
    
    @JsonProperty("header_ar")
    @Column(name = "header_ar")
    private String headerAr;
    
    @JsonProperty("header_description")
    @Column(name = "header_description")
    private String headerDescription;
    
    @JsonProperty("header_icon_path")
    @Column(name = "header_icon_path")
    private String headerIconPath;
    
    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;
    
    @JsonProperty("created_date")
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @JsonProperty("head_sequence_no")
    @Column(name = "head_sequence_no")
    private Integer headSequenceNo;
    
    @Transient
    @JsonProperty("updated_by")
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Transient
    @JsonProperty("updated_date")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @JsonProperty("is_active")
    @Column(name = "is_active")
    private Boolean isActive;
	
    
    
}
