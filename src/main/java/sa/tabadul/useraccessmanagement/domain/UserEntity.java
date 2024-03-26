package sa.tabadul.useraccessmanagement.domain;


import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "USER_ENTITY", schema="dbo")
public class UserEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMAIL_CONSTRAINT")
    private String emailConstraint;

    @Column(name = "EMAIL_VERIFIED")
    private boolean emailVerified;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "FEDERATION_LINK")
    private String federationLink;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "REALM_ID")
    private String realmId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CREATED_TIMESTAMP")
    private Long createdTimestamp;

    @Column(name = "SERVICE_ACCOUNT_CLIENT_LINK")
    private String serviceAccountClientLink;

    @Column(name = "NOT_BEFORE")
    private Integer notBefore;


}
