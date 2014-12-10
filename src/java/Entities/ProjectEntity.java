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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectEntity.findAll", query = "SELECT p FROM ProjectEntity p"),
    @NamedQuery(name = "ProjectEntity.findByName", query = "SELECT p FROM ProjectEntity p WHERE p.name = :name"),
    @NamedQuery(name = "ProjectEntity.findByBaseUri", query = "SELECT p FROM ProjectEntity p WHERE p.baseUri = :baseUri")})
public class ProjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name")
    private String name;
    @Size(max = 256)
    @Column(name = "base_uri")
    private String baseUri;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectName")
    private List<RequestEntity> requestEntityList;
    @OneToMany(mappedBy = "projectName")
    private List<ParameterEntity> parameterEntityList;
    @JoinColumn(name = "user_name", referencedColumnName = "name")
    @ManyToOne(optional = false)
    private UserEntity userName;
    @JoinColumn(name = "script_id", referencedColumnName = "script_id")
    @ManyToOne
    private ScriptEntity scriptId;

    public ProjectEntity() {
    }

    public ProjectEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    @XmlTransient
    public List<RequestEntity> getRequestEntityList() {
        return requestEntityList;
    }

    public void setRequestEntityList(List<RequestEntity> requestEntityList) {
        this.requestEntityList = requestEntityList;
    }

    @XmlTransient
    public List<ParameterEntity> getParameterEntityList() {
        return parameterEntityList;
    }

    public void setParameterEntityList(List<ParameterEntity> parameterEntityList) {
        this.parameterEntityList = parameterEntityList;
    }

    public UserEntity getUserName() {
        return userName;
    }

    public void setUserName(UserEntity userName) {
        this.userName = userName;
    }

    public ScriptEntity getScriptId() {
        return scriptId;
    }

    public void setScriptId(ScriptEntity scriptId) {
        this.scriptId = scriptId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectEntity)) {
            return false;
        }
        ProjectEntity other = (ProjectEntity) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
