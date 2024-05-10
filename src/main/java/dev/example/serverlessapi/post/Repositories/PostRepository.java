package dev.example.serverlessapi.post.Repositories;

import dev.example.serverlessapi.post.Entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    // Custom queries can be defined here
}