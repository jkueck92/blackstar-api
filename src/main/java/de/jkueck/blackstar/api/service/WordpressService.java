package de.jkueck.blackstar.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jkueck.*;
import de.jkueck.blackstar.api.domain.PostStatus;
import de.jkueck.blackstar.api.domain.WordpressCategory;
import de.jkueck.blackstar.api.domain.WordpressTag;
import de.jkueck.blackstar.api.database.repository.PostRepository;
import de.jkueck.blackstar.api.database.entity.Operation;
import de.jkueck.blackstar.api.database.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordpressService {

    private final OperationService operationService;

    private final PostRepository postRepository;

    public List<PostDto> getAll() {
        List<PostDto> list = new ArrayList<>();
        for (Post post : this.postRepository.findAll()) {
            list.add(PostDto.builder()
                    .id(post.getId())
                    .alarmTimestamp(post.getAlarmTimestamp())
                    .keyword(post.getKeyword())
                    .plannedReleaseAt(post.getPlannedReleaseAt())
                    .location(post.getLocation())
                    .status(post.getStatus())
                    .text(post.getText())
                    .title(post.getTitle())
                    .build());
        }
        return list;
    }

    public PostDto getById(long id) {
        Optional<Post> optionalPost = this.postRepository.findById(id);
        if (optionalPost.isPresent()) {
            return PostDto.builder()
                    .id(optionalPost.get().getId())
                    .alarmTimestamp(optionalPost.get().getAlarmTimestamp())
                    .keyword(optionalPost.get().getKeyword())
                    .plannedReleaseAt(optionalPost.get().getPlannedReleaseAt())
                    .location(optionalPost.get().getLocation())
                    .status(optionalPost.get().getStatus())
                    .text(optionalPost.get().getText())
                    .title(optionalPost.get().getTitle())
                    .build();
        }
        return PostDto.builder().build();
    }

    public Post save(WordpressCreatePostDto createPostDto) {

        Optional<Operation> optionalOperation = this.operationService.getRawById(createPostDto.getOperationId());
        return optionalOperation.map(operation -> this.postRepository.save(Post.builder()
                .title(createPostDto.getTitle())
                .text(createPostDto.getText())
                .keyword(createPostDto.getKeyword())
                .alarmTimestamp(createPostDto.getAlarmTimestamp())
                .plannedReleaseAt(createPostDto.getPlannedReleaseAt())
                .location(createPostDto.getLocation())
                .status(PostStatus.NOT_APPROVED.toString())
                .operation(operation)
                .build()
        )).orElse(null);
    }

    public void publishPost(long id) {
        Optional<Post> optionalPost = this.postRepository.findById(id);
        if (optionalPost.isPresent()) {
            optionalPost.get().setStatus(PostStatus.PUBLISHED.toString());
            this.postRepository.save(optionalPost.get());
        }
    }

    public void approvePost(CheckPostDto checkPostDto) {
        Optional<Post> optionalPost = this.postRepository.findById(checkPostDto.getPostId());
        if (optionalPost.isPresent()) {
            optionalPost.get().setStatus(PostStatus.APPROVED.toString());

            if (!checkPostDto.getOldText().equals(checkPostDto.getNewText())) {
                optionalPost.get().setText(checkPostDto.getNewText());
            }

            this.postRepository.save(optionalPost.get());
        }
    }

    public List<PostDto> getNotApprovedPosts() {

        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : this.postRepository.findByStatus(PostStatus.NOT_APPROVED.toString())) {

            postDtos.add(
                    PostDto.builder()
                            .id(post.getId())
                            .alarmTimestamp(post.getAlarmTimestamp())
                            .keyword(post.getKeyword())
                            .plannedReleaseAt(post.getPlannedReleaseAt())
                            .location(post.getLocation())
                            .status(post.getStatus())
                            .text(post.getText())
                            .title(post.getTitle())
                            .build()
            );

        }
        return postDtos;

    }

    public List<PostDto> getFilteredPosts(String filter) {

        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : this.postRepository.findByStatus(filter)) {

            postDtos.add(
                    PostDto.builder()
                            .id(post.getId())
                            .alarmTimestamp(post.getAlarmTimestamp())
                            .keyword(post.getKeyword())
                            .plannedReleaseAt(post.getPlannedReleaseAt())
                            .location(post.getLocation())
                            .status(post.getStatus())
                            .text(post.getText())
                            .title(post.getTitle())
                            .build()
            );

        }
        return postDtos;

    }

    public List<WordpressTagDto> getTags() {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<WordpressTag> wordpressTags = objectMapper.readValue(new URL("https://feuerwehr-ritterhude.de/wp-json/wp/v2/tags"), new TypeReference<>() {
            });

            List<WordpressTagDto> tagDtos = new ArrayList<>();
            for (WordpressTag tag : wordpressTags) {
                tagDtos.add(WordpressTagDto.builder().id(tag.getId()).name(tag.getName()).build());
            }
            return tagDtos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<WordpressCategoryDto> getCategories() {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<WordpressCategory> wordpressCategories = objectMapper.readValue(new URL("https://feuerwehr-ritterhude.de/wp-json/wp/v2/categories"), new TypeReference<>() {
            });

            List<WordpressCategoryDto> categoryDtos = new ArrayList<>();
            for (WordpressCategory category : wordpressCategories) {
                categoryDtos.add(WordpressCategoryDto.builder().id(category.getId()).name(category.getName()).build());
            }
            return categoryDtos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPost(WordpressCreatePostDto createPostDto) {

        try {

            createPostDto.getCategories();

            int i = 0;
            StringBuilder categoryBuilder = new StringBuilder();
            for (WordpressCategoryDto dto : createPostDto.getCategories()) {
                categoryBuilder.append(dto.getId());
                if (i != createPostDto.getCategories().size() - 1) {
                    categoryBuilder.append(",");
                }
                i++;
            }

            StringBuilder tagBuilder = new StringBuilder();
            if (createPostDto.getTags() != null) {
                List<WordpressTag> tags = new ArrayList<>();
                for (WordpressTagDto dto : createPostDto.getTags()) {
                    tags.add(this.createTag(dto.getName()));
                }

                int tagIndex = 0;
                for (WordpressTag tag : tags) {
                    tagBuilder.append(tag.getId());
                    if (tagIndex != tags.size() - 1) {
                        categoryBuilder.append(",");
                    }
                    tagIndex++;
                }
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            CloseableHttpClient httpclient = HttpClientBuilder.create().build();

            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("feuerwehr-ritterhude.de").setPath("/wp-json/wp/v2/posts")
                    .setParameter("title", createPostDto.getTitle())
                    .setParameter("content", this.getContent(createPostDto))
                    .setParameter("date", dateTimeFormatter.format(createPostDto.getPlannedReleaseAt()))
                    .setParameter("status", "future")
                    .setParameter("categories", categoryBuilder.toString());

            if (createPostDto.getTags() != null) {
                builder.setParameter("tags", tagBuilder.toString());
            }

            URI uri = builder.build();

            HttpPost httpPost = new HttpPost(uri);

            httpPost.addHeader(HttpHeaders.AUTHORIZATION, this.getBase64AuthHeader());
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpPost.addHeader(HttpHeaders.ACCEPT_ENCODING, StandardCharsets.UTF_8.toString());

            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            String response = EntityUtils.toString(response2.getEntity());


            System.out.println(response2);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private String getContent(WordpressCreatePostDto post) {

        return "<figure class=\"wp-block-image size-large\"><img class=" + post.getWordpressPostImage().getWpClass() + " src=" + post.getWordpressPostImage().getSrc() + " alt=" + post.getWordpressPostImage().getAlt() + "></figure>\n" +
                "<table style=\"border-collapse: collapse;width: 100%;height: 102px\">\n" +
                "<tbody>\n" +
                "<tr style=\"height: 34px\">\n" +
                "<td style=\"width: 30%;height: 34px\"><strong>Alarmierung:</strong></td>\n" +
                "<td style=\"width: 70%;height: 34px\">" + post.getAlarmTimestamp() + " Uhr</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 34px\">\n" +
                "<td style=\"width: 30%;height: 34px\"><strong>Stichwort:</strong></td>\n" +
                "<td style=\"width: 70%;height: 34px\">Feuer</td>\n" +
                "</tr>\n" +
                "<tr style=\"height: 34px\">\n" +
                "<td style=\"width: 30%;height: 34px\"><strong>Einsatzort:</strong></td>\n" +
                "<td style=\"width: 70%;height: 34px\">" + post.getLocation() + "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<p>\n" + post.getText() + "</p>";

    }

    private String getBase64AuthHeader() {
        String auth = "";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        return "Basic " + new String(encodedAuth);
    }

    private WordpressTag createTag(String tagName) {

        try {


            CloseableHttpClient httpclient = HttpClientBuilder.create().build();

            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("feuerwehr-ritterhude.de").setPath("/wp-json/wp/v2/tags")
                    .setParameter("name", tagName);
            URI uri = builder.build();

            HttpPost httpPost = new HttpPost(uri);

            httpPost.addHeader(HttpHeaders.AUTHORIZATION, this.getBase64AuthHeader());
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpPost.addHeader(HttpHeaders.ACCEPT_ENCODING, StandardCharsets.UTF_8.toString());

            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(EntityUtils.toString(response2.getEntity()), WordpressTag.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
