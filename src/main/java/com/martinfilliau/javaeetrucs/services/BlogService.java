package com.martinfilliau.javaeetrucs.services;

import com.martinfilliau.javaeetrucs.data.Category;
import com.martinfilliau.javaeetrucs.data.Post;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import static com.martinfilliau.javaeetrucs.services.utils.QueryParameter.*;

/**
 * Methods to interact with {@link com.martinfilliau.javaeetrucs.data.Post} and {@link com.martinfilliau.javaeetrucs.data.Category}
 * @author martinfilliau
 */
@Stateless
public class BlogService {

    @EJB
    private CrudService crud;

    /**
     * Create a blog post
     * @param p {@link com.martinfilliau.javaeetrucs.data.Post} to create
     * @return entity persisted
     */
    public Post createPost(Post p) {
        return crud.create(p);
    }

    /**
     * Update a blog post
     * @param p {@link com.martinfilliau.javaeetrucs.data.Post} to update
     * @return entity managed
     */
    public Post updatePost(Post p) {
        return crud.update(p);
    }

    /**
     * Get top level categories
     * @return list of categories
     */
    public List<Category> getTopLevelCategories() {
        return crud.findWithNamedQuery(Category.QUERY_GET_TOP_LEVEL_CAT);
    }

    /**
     * Get all posts
     * (Warning: no sort order defined now)
     * @return list of {@link com.martinfilliau.javaeetrucs.data.Post}
     */
    public List<Post> getAllPosts() {
        return crud.getAll(Post.class);
    }

    /**
     * Count all {@link com.martinfilliau.javaeetrucs.data.Post}s
     * @return count
     */
    public Long countAllPosts() {
        return crud.countAll(Post.class);
    }

    /**
     * Get posts for a given category and the children categories of the given category
     * @param categoryId ID of the category
     * @return list of posts
     */
    public List<Post> getPostsForCategoryAndChildren(long categoryId) {
        Category parentCategory = crud.get(Category.class, categoryId);

        List<Category> allCategoriesToRetrieve = getCategoryWithChildren(parentCategory);

        List<Long> ids = new ArrayList<Long>();
        for(Category c : allCategoriesToRetrieve) {
            ids.add(c.getId());
        }

        return crud.findWithNamedQuery(Category.QUERY_GET_POSTS_FOR_CATEGORIES, with("categories", ids));
    }

    /**
     * Get a category with all its children
     * @param c {@link com.martinfilliau.javaeetrucs.data.Category} to retrieve _with_ its children
     * @return list of categories
     */
    private List<Category> getCategoryWithChildren(Category c) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(c);      // adding "top level category"
        for(Category category : c.getChildren()) {
            categories.addAll(getCategoryWithChildren(category));
        }
        return categories;
    }
 
}
