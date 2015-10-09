package io.wybis.wybisanalytics.model

import groovy.transform.Canonical

import javax.persistence.*

@Entity
@Table(name = "blob_data")
@Canonical
@groovy.transform.ToString(includeNames = true)
class BlobData {
    private static final long serialVersionUID = 1L

    public static final String ID_KEY = "blobDataId"

    @Id
    @Column(name = "id")
    private long id

    @Column(name = "name")
    private String name

    @Column(name = "type")
    private String type

    @Column(name = "content")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content

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
