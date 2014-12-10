/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "testrun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestrunEntity.findAll", query = "SELECT t FROM TestrunEntity t"),
    @NamedQuery(name = "TestrunEntity.findByTestrunId", query = "SELECT t FROM TestrunEntity t WHERE t.testrunId = :testrunId"),
    @NamedQuery(name = "TestrunEntity.findByTime", query = "SELECT t FROM TestrunEntity t WHERE t.time = :time"),
    @NamedQuery(name = "TestrunEntity.findBySuccess", query = "SELECT t FROM TestrunEntity t WHERE t.success = :success"),
    @NamedQuery(name = "TestrunEntity.findByMessage", query = "SELECT t FROM TestrunEntity t WHERE t.message = :message")})
public class TestrunEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "testrun_id")
    private Integer testrunId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Basic(optional = false)
    @NotNull
    @Column(name = "success")
    private boolean success;
    @Size(max = 512)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "testcase_id", referencedColumnName = "testcase_id")
    @ManyToOne(optional = false)
    private TestcaseEntity testcaseId;

    public TestrunEntity() {
    }

    public TestrunEntity(Integer testrunId) {
        this.testrunId = testrunId;
    }

    public TestrunEntity(Integer testrunId, Date time, boolean success) {
        this.testrunId = testrunId;
        this.time = time;
        this.success = success;
    }

    public Integer getTestrunId() {
        return testrunId;
    }

    public void setTestrunId(Integer testrunId) {
        this.testrunId = testrunId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TestcaseEntity getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(TestcaseEntity testcaseId) {
        this.testcaseId = testcaseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testrunId != null ? testrunId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestrunEntity)) {
            return false;
        }
        TestrunEntity other = (TestrunEntity) object;
        if ((this.testrunId == null && other.testrunId != null) || (this.testrunId != null && !this.testrunId.equals(other.testrunId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.TestrunEntity[ testrunId=" + testrunId + " ]";
    }
    
}
