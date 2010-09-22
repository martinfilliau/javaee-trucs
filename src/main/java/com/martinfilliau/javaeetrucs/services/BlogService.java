package com.martinfilliau.javaeetrucs.services;

import com.martinfilliau.javaeetrucs.data.Category;
import com.martinfilliau.javaeetrucs.data.Post;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Methods to interact with {@link com.martinfilliau.javaeetrucs.data.Post} and {@link com.martinfilliau.javaeetrucs.data.Category}
 * @author martinfilliau
 */
@Stateless
public class BlogService {

    @EJB
    private CrudService crud;

    public Post createPost(Post p) {
        return crud.create(p);
    }

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
 
}
