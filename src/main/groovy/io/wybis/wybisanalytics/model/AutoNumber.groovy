package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "auto_number")
@Canonical
@ToString(includeNames = true)
public class AutoNumber {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "val", nullable = false)
    private long value;

    // common fields
    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by", nullable = false)
    protected long createBy;

    @Column(name = "update_by", nullable = false)
    protected long updateBy;

    // persistence operations
    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
    }

    // domain operations
}