/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "script")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScriptEntity.findAll", query = "SELECT s FROM ScriptEntity s"),
    @NamedQuery(name = "ScriptEntity.findByScriptId", query = "SELECT s FROM ScriptEntity s WHERE s.scriptId = :scriptId")})
public class ScriptEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "script_id")
    private Integer scriptId;
    @Lob
    @Size(max = 65535)
    @Column(name = "beforeScript")
    private String beforeScript;
    @Lob
    @Size(max = 65535)
    @Column(name = "afterScript")
    private String afterScript;
    @OneToMany(mappedBy = "scriptId")
    private List<RequestEntity> requestEntityList;
    @OneToMany(mappedBy = "scriptId")
    private List<ProjectEntity> projectEntityList;
    @OneToMany(mappedBy = "scriptId")
    private List<TestcaseEntity> testcaseEntityList;

    public ScriptEntity() {
    }

    public ScriptEntity(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
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

    @XmlTransient
    public List<RequestEntity> getRequestEntityList() {
        return requestEntityList;
    }

    public void setRequestEntityList(List<RequestEntity> requestEntityList) {
        this.requestEntityList = requestEntityList;
    }

    @XmlTransient
    public List<ProjectEntity> getProjectEntityList() {
        return projectEntityList;
    }

    public void setProjectEntityList(List<ProjectEntity> projectEntityList) {
        this.projectEntityList = projectEntityList;
    }

    @XmlTransient
    public List<TestcaseEntity> getTestcaseEntityList() {
        return testcaseEntityList;
    }

    public void setTestcaseEntityList(List<TestcaseEntity> testcaseEntityList) {
        this.testcaseEntityList = testcaseEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scriptId != null ? scriptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScriptEntity)) {
            return false;
        }
        ScriptEntity other = (ScriptEntity) object;
        if ((this.scriptId == null && other.scriptId != null) || (this.scriptId != null && !this.scriptId.equals(other.scriptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ScriptEntity[ scriptId=" + scriptId + " ]";
    }
    
}
