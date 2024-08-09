package com.myblog7.service.impl;

import com.myblog7.entity.Post;
import com.myblog7.exception.ResourceNotFound;
import com.myblog7.payload.PostDto;
import com.myblog7.payload.PostResponse;
import com.myblog7.repository.PostRepository;
import com.myblog7.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {

        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postRepository.save(post);

        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Post not found with id : " + id)
        );

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);

        PostDto dto = mapToDto(updatedPost);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with id : " + id)
        );
        PostDto dto = mapToDto(post);
        return dto;
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pagePost = postRepository.findAll(pageable);
        List<Post> posts = pagePost.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }


    Post mapToEntity( PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
        // Post post = new Post();
       // post.setTitle(postDto.getContent());
       // post.setDescription(postDto.getDescription());
       // post.setContent(postDto.getContent());
        return post;
    }

   PostDto mapToDto(Post post){

       PostDto dto = modelMapper.map(post, PostDto.class);
       // PostDto dto = new PostDto();
       // dto.setId(post.getId());
       // dto.setTitle(post.getTitle());
       // dto.setDescription(post.getDescription());
       // dto.setContent(post.getContent());
        return dto;
    }
}
