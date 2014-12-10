/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Matti
 */
@Entity
@Table(name = "request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestEntity.findAll", query = "SELECT r FROM RequestEntity r"),
    @NamedQuery(name = "RequestEntity.findByProjectId", query = "SELECT r FROM RequestEntity r WHERE r.projectId = :projectId"),
    @NamedQuery(name = "RequestEntity.findByRelativeUri", query = "SELECT r FROM RequestEntity r WHERE r.relativeUri = :relativeUri"),
    @NamedQuery(name = "RequestEntity.findByMethod", query = "SELECT r FROM RequestEntity r WHERE r.method = :method")})
public class RequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "project_id")
    private Integer projectId;
    
    @Size(max = 256)
    @Column(name = "relative_uri")
    private String relativeUri;
    
    @Size(max = 64)
    @Column(name = "method")
    private String method;
    
    @OneToMany(mappedBy = "requestId", fetch = FetchType.LAZY)
    private Collection<ParameterEntity> parameterEntityCollection;
    
    @JoinColumn(name = "script_id", referencedColumnName = "script_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ScriptEntity scriptId;
    
    @JoinColumn(name = "project_name", referencedColumnName = "name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProjectEntity projectName;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId", fetch = FetchType.LAZY)
    private Collection<TestcaseEntity> testcaseEntityCollection;

    public RequestEntity() {
    }

    public RequestEntity(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getRelativeUri() {
        return relativeUri;
    }

    public void setRelativeUri(String relativeUri) {
        this.relativeUri = relativeUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @XmlTransient
    public Collection<ParameterEntity> getParameterEntityCollection() {
        return parameterEntityCollection;
    }

    public void setParameterEntityCollection(Collection<ParameterEntity> parameterEntityCollection) {
        this.parameterEntityCollection = parameterEntityCollection;
    }

    public ScriptEntity getScriptId() {
        return scriptId;
    }

    public void setScriptId(ScriptEntity scriptId) {
        this.scriptId = scriptId;
    }

    public ProjectEntity getProjectName() {
        return projectName;
    }

    public void setProjectName(ProjectEntity projectName) {
        this.projectName = projectName;
    }

    @XmlTransient
    public Collection<TestcaseEntity> getTestcaseEntityCollection() {
        return testcaseEntityCollection;
    }

    public void setTestcaseEntityCollection(Collection<TestcaseEntity> testcaseEntityCollection) {
        this.testcaseEntityCollection = testcaseEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectId != null ? projectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.projectId == null && other.projectId != null) || (this.projectId != null && !this.projectId.equals(other.projectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getRelativeUri() + " - " + getMethod();
        //return "Entities.RequestEntity[ projectId=" + projectId + " ]";
    }
    
}
