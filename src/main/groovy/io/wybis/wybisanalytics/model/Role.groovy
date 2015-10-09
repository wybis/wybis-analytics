package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "role")
@Canonical
@ToString(includeNames = true)
class Role {

    private static final long serialVersionUID = 1L

    public static final String ID_ADMINISTRATOR = "administrator"

    public static final String ID_USER = "user"

    public static final List<String> ROLES = [ID_ADMINISTRATOR, ID_USER]

    @Id
    @Column(name = "id")
    private String id

    @Column(name = "name")
    private String name

    // common fields...
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
