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
 * @author Administrator
 */
@Entity
@Table(name = "request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestEntity.findAll", query = "SELECT r FROM RequestEntity r"),
    @NamedQuery(name = "RequestEntity.findByRequestId", query = "SELECT r FROM RequestEntity r WHERE r.requestId = :requestId"),
    @NamedQuery(name = "RequestEntity.findByProject", query = "SELECT r FROM RequestEntity r WHERE r.projectName = :projectName"),
    @NamedQuery(name = "RequestEntity.findByRelativeUri", query = "SELECT r FROM RequestEntity r WHERE r.relativeUri = :relativeUri"),
    @NamedQuery(name = "RequestEntity.findByMethod", query = "SELECT r FROM RequestEntity r WHERE r.method = :method")})
public class RequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "request_id")
    private Integer requestId;
    @Size(max = 256)
    @Column(name = "relative_uri")
    private String relativeUri;
    @Size(max = 64)
    @Column(name = "method")
    private String method;
    @JoinColumn(name = "script_id", referencedColumnName = "script_id")
    @ManyToOne
    private ScriptEntity scriptId;
    @JoinColumn(name = "project_name", referencedColumnName = "name")
    @ManyToOne(optional = false)
    private ProjectEntity projectName;
    @OneToMany(mappedBy = "requestId")
    private List<ParameterEntity> parameterEntityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId")
    private List<TestcaseEntity> testcaseEntityList;

    public RequestEntity() {
    }

    public RequestEntity(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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
    public List<ParameterEntity> getParameterEntityList() {
        return parameterEntityList;
    }

    public void setParameterEntityList(List<ParameterEntity> parameterEntityList) {
        this.parameterEntityList = parameterEntityList;
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
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.relativeUri + " | " + this.method;
    }
    
}
