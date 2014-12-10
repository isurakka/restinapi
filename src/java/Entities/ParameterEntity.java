/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matti
 */
@Entity
@Table(name = "parameter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParameterEntity.findAll", query = "SELECT p FROM ParameterEntity p"),
    @NamedQuery(name = "ParameterEntity.findByRequestId", query = "SELECT p FROM ParameterEntity p WHERE p.requestId = :requestId"),
    @NamedQuery(name = "ParameterEntity.findByParameterId", query = "SELECT p FROM ParameterEntity p WHERE p.parameterId = :parameterId"),
    @NamedQuery(name = "ParameterEntity.findByProjectName", query = "SELECT p FROM ParameterEntity p WHERE p.projectName = :projectName"),
    @NamedQuery(name = "ParameterEntity.findByKey", query = "SELECT p FROM ParameterEntity p WHERE p.key = :key")})
public class ParameterEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "parameter_id")
    private Integer parameterId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "key")
    private String key;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "value")
    private String value;
    @JoinColumn(name = "request_id", referencedColumnName = "project_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private RequestEntity requestId;
    @JoinColumn(name = "project_name", referencedColumnName = "name")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity projectName;

    public ParameterEntity() {
    }

    public ParameterEntity(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public ParameterEntity(Integer parameterId, String key, String value) {
        this.parameterId = parameterId;
        this.key = key;
        this.value = value;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RequestEntity getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestEntity requestId) {
        this.requestId = requestId;
    }

    public ProjectEntity getProjectName() {
        return projectName;
    }

    public void setProjectName(ProjectEntity projectName) {
        this.projectName = projectName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parameterId != null ? parameterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParameterEntity)) {
            return false;
        }
        ParameterEntity other = (ParameterEntity) object;
        if ((this.parameterId == null && other.parameterId != null) || (this.parameterId != null && !this.parameterId.equals(other.parameterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ParameterEntity[ parameterId=" + parameterId + " ]";
    }
    
}
