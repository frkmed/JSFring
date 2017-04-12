package fr.pinguet62.jsfring.model.sql;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.querydsl.core.annotations.Config;

// Querydsl
@Config(defaultVariableName = "right_")
// JPA
@Entity
@Table(name = "\"RIGHT\"")
public class Right implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "CODE", unique = true, nullable = false, length = 63)
    private String code;

    @ManyToMany
    @JoinTable(name = "profiles_rights", joinColumns = {
            @JoinColumn(name = "\"RIGHT\"", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "profile", nullable = false, updatable = false) })
    private Set<Profile> profiles;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    public Right() {
        // No action
    }

    public Right(String code) {
        this.code = code;
    }

    public Right(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public Right(String code, String title, Set<Profile> profiles) {
        this.code = code;
        this.title = title;
        this.profiles = profiles;
    }

    /**
     * Test equality of object by comparing their {@link #code}.
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!obj.getClass().equals(Right.class))
            return false;
        Right other = (Right) obj;
        return Objects.equals(code, other.code);
    }

    public String getCode() {
        return code;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Get the {@link Object#hashCode() hash} of {@link #code}.
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}