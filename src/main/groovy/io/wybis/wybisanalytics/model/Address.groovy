package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "country")
@Canonical
@ToString(includeNames = true)
class Address {

    private static final long serialVersionUID = 1L

    public static final String ID_KEY = "addressId"

    @Id
    @Column(name = "id")
    private long id

    @Column(name = "address")
    private String address

    @Column(name = "city_or_town")
    private String cityOrTown

    @Column(name = "postal_code")
    private String postalCode

    @Column(name = "state_code")
    private String stateCode

    @Column(name = "country_code")
    private String countryCode

    // common fields
    @Column(name = "create_time")
    protected Date createTime

    @Column(name = "update_time")
    protected Date updateTime

    @Column(name = "create_by")
    protected long createBy

    @Column(name = "update_by")
    protected long updateBy

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
