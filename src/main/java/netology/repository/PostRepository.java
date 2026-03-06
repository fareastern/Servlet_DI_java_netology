package netology.repository;

import netology.exception.NotFoundException;
import netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {

    // Для потокобезопасности использую ConcurrentHashMap, AtomicLong
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        // Создаем новый пост, когда ID = 0
        if (post.getId() == 0) {
            long id = nextId.getAndIncrement();
            post.setId(id);
            posts.put(id, post);
            return post;
        }

        // Если запрашиваемого поста нет, возвращаем ошибку 404
        if (!posts.containsKey(post.getId())) {
            throw new NotFoundException("Post with id " + post.getId() + " not found");
        }

        // Если пост есть
        posts.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        if (posts.remove(id) == null) {
            throw new NotFoundException("Post with id " + id + " not found");
        }
    }
}
