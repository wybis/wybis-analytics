package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "users")
@Canonical
@ToString(includeNames = true)
class User {

    private static final long serialVersionUID = 1L

    public static final String ID_KEY = "userId"

    @Id
    @Column(name = "id")
    private long id

    @Column(name = "userId")
    private String userId

    @Column(name = "password")
    private String password

    @Column(name = "id_number")
    private String idNumber

    @Column(name = "email_id")
    private String emailId

    @Column(name = "first_name")
    private String firstName

    @Column(name = "last_name")
    private String lastName

    @Column(name = "hand_phone_number")
    private String handPhoneNumber

    @Column(name = "land_phone_number")
    private String landPhoneNumber

    @Column(name = "address_id")
    private long addressId

    private transient Address address

    @Column(name = "type")
    private String type

    @Column(name = "status")
    private String status

    @Column(name = "role_id")
    private String roleId

    // @OneToOne
    private transient Role role

    @Column(name = "branch_id")
    private long branchId

    // common fields
    @Column(name = "create_time")
    protected Date createTime

    @Column(name = "update_time")
    protected Date updateTime

    @Column(name = "create_by")
    protected String createBy

    @Column(name = "update_by")
    protected String updateBy

    // persistance operations
    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date()
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date()
        this.createTime = now
        this.updateTime = now
    }

    // domain operations
}
