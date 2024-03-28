
package com.base.api.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString
//@Data
@Table(name = "categories")
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1510773756756964739L;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @JsonProperty("category")
    @ManyToOne()
    @Setter
    @Getter
    @JsonBackReference
    private Category categoryTable;

    @Transient
    @OneToMany(mappedBy = "categoryTable")
    @Setter
    @JsonManagedReference
    public List<Category> children;

//    // returns direct children
    public List<Category> getChildren() {
        return children;
    }
//
//    // returns all children to any level
//    public List<Category> getAllChildren() {
//        return getAllChildren(this);
//    }
//
//    // recursive function to walk the tree
//    public List<Category> getAllChildren(Category parent) {
//        List<Category> allChildren = new ArrayList<Category>();
//
//        for (Category child : parent.getChildren()) {
//            allChildren.add(child);
//            allChildren.addAll(getAllChildren(child));
//        }
//
//        return allChildren;
//    }

    @Column(name = "status")
    @Getter
    @Setter
    private String status;

    @OneToMany(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private List<Translatable> translations = new ArrayList<Translatable>();

    public Stream<Category> flattened() {
        return Stream.concat(Stream.of(this), children.stream().flatMap(Category::flattened));
    }

    public Category(UUID id, String name, String status, LocalDateTime createdAt) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdDate = createdAt;
    }

    public Category(UUID id, String name, String status, LocalDateTime createdAt, List<Category> category) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdDate = createdAt;
        this.children = category;
    }
}