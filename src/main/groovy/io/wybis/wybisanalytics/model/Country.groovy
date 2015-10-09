package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "country")
@Canonical
@ToString(includeNames = true)
class Country {

    private static final long serialVersionUID = 1L

    @Id
    @Column(name = "code")
    private String id

    @Column(name = "code3")
    private String threeLetterCode

    @Column(name = "nbr")
    private int numericCode

    @Column(name = "name")
    private String name

    // common fields
    @Column(name = "create_time")
    protected Date createTime

    @Column(name = "update_time")
    protected Date updateTime

    @Column(name = "create_by")
    protected String createBy

    @Column(name = "update_by")
    protected String updateBy

    // persistence operations
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
