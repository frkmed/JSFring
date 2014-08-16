package fr.pinguet62.dictionary.model;

// Generated 16 ao�t 2014 18:49:09 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Description generated by hbm2java
 */
@Entity
@Table(name = "description", catalog = "dictionary")
public class Description implements java.io.Serializable {

    private int id;
    private Language language;
    private Keyword keyword;
    private String title;
    private String content;

    public Description() {
    }

    public Description(int id, Language language, String title, String content) {
        this.id = id;
        this.language = language;
        this.title = title;
        this.content = content;
    }

    public Description(int id, Language language, Keyword keyword,
            String title, String content) {
        this.id = id;
        this.language = language;
        this.keyword = keyword;
        this.title = title;
        this.content = content;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANGUAGE", nullable = false)
    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KEYWORD")
    public Keyword getKeyword() {
        return this.keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    @Column(name = "TITLE", nullable = false, length = 50)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "CONTENT", nullable = false, length = 999)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}