package com.martinfilliau.javaeetrucs.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@NamedQueries({
    @NamedQuery(name=Category.QUERY_GET_TOP_LEVEL_CAT, query="SELECT c FROM Category AS c WHERE c.parent = NULL"),
    @NamedQuery(name=Category.QUERY_GET_POSTS_FOR_CATEGORIES, query="SELECT DISTINCT p FROM Post p, IN(p.categories) c WHERE c.id IN (:categories) ORDER BY p.publishedAt DESC")
})
/**
 * JPA entity representing a category
 */
public class Category extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private List<Category> children;

    @ManyToMany(mappedBy = "categories")
    @JoinTable(name="post_categories", joinColumns=@JoinColumn(name="post_id"),
        inverseJoinColumns=@JoinColumn(name="category_id"))
    private List<Post> posts;


    /**
     * Add a new category as child of the current category
     * @param c category to add as a child
     */
    public void addCategoryChild(Category c) {
        this.getChildren().add(c);
        c.setParent(this);
    }

    /**
     * Check if this category is root (has no parent)
     * @return true if it is root (no parent) else false
     */
    public boolean isRoot() {
        if(this.parent == null) {
            return true;
        }
        return false;
    }


    /* Queries */

    /**
     * Get all top level categories
     */
    public static final String QUERY_GET_TOP_LEVEL_CAT = "Category.getTopLevel";

    /**
     * Get all posts corresponding to categories
     * Will DISTINCT on posts
     */
    public static final String QUERY_GET_POSTS_FOR_CATEGORIES = "Category.getPostsForCategories";


    /* GETTERs and SETTERs */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get parent category
     * @return the parent or null if it has no parent
     */
    public Category getParent() {
        return parent;
    }

    /**
     * Set parent category
     * @param parent the parent to set
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * Get children of the current category
     * @return children
     */
    public List<Category> getChildren() {
        return children;
    }

    /**
     * Set children of the current category
     * @param children the children to set
     */
    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    

    /* Overriden methods */

    @Override
    public final int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public final boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "data.Category[id=" + id + "]";
    }
}
