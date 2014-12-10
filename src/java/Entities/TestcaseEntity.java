/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "testcase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestcaseEntity.findAll", query = "SELECT t FROM TestcaseEntity t"),
    @NamedQuery(name = "TestcaseEntity.findByTestcaseId", query = "SELECT t FROM TestcaseEntity t WHERE t.testcaseId = :testcaseId"),
    @NamedQuery(name = "TestcaseEntity.findByName", query = "SELECT t FROM TestcaseEntity t WHERE t.name = :name"),
    @NamedQuery(name = "TestcaseEntity.findByDirty", query = "SELECT t FROM TestcaseEntity t WHERE t.dirty = :dirty")})
public class TestcaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "testcase_id")
    private Integer testcaseId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "beforeScript")
    private String beforeScript;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "afterScript")
    private String afterScript;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dirty")
    private boolean dirty;
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    @ManyToOne(optional = false)
    private RequestEntity requestId;
    @JoinColumn(name = "script_id", referencedColumnName = "script_id")
    @ManyToOne
    private ScriptEntity scriptId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testcaseId")
    private List<TestrunEntity> testrunEntityList;

    public TestcaseEntity() {
    }

    public TestcaseEntity(Integer testcaseId) {
        this.testcaseId = testcaseId;
    }

    public TestcaseEntity(Integer testcaseId, String name, String beforeScript, String afterScript, boolean dirty) {
        this.testcaseId = testcaseId;
        this.name = name;
        this.beforeScript = beforeScript;
        this.afterScript = afterScript;
        this.dirty = dirty;
    }

    public Integer getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(Integer testcaseId) {
        this.testcaseId = testcaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeforeScript() {
        return beforeScript;
    }

    public void setBeforeScript(String beforeScript) {
        this.beforeScript = beforeScript;
    }

    public String getAfterScript() {
        return afterScript;
    }

    public void setAfterScript(String afterScript) {
        this.afterScript = afterScript;
    }

    public boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public RequestEntity getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestEntity requestId) {
        this.requestId = requestId;
    }

    public ScriptEntity getScriptId() {
        return scriptId;
    }

    public void setScriptId(ScriptEntity scriptId) {
        this.scriptId = scriptId;
    }

    @XmlTransient
    public List<TestrunEntity> getTestrunEntityList() {
        return testrunEntityList;
    }

    public void setTestrunEntityList(List<TestrunEntity> testrunEntityList) {
        this.testrunEntityList = testrunEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testcaseId != null ? testcaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestcaseEntity)) {
            return false;
        }
        TestcaseEntity other = (TestcaseEntity) object;
        if ((this.testcaseId == null && other.testcaseId != null) || (this.testcaseId != null && !this.testcaseId.equals(other.testcaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.TestcaseEntity[ testcaseId=" + testcaseId + " ]";
    }
    
}
