package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    String INDEX = "bookstore";
    String TYPE = "book";

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

    public void addNewBook(Book book) throws IOException {
        Map<String, Object> documentMapper = objectMapper.convertValue(book, Map.class);
        IndexRequest request = new IndexRequest(INDEX, TYPE, String.valueOf(book.getId())).source(documentMapper);
        client.index(request, RequestOptions.DEFAULT);
    }

    public List<Book> searchData(String query) {
        SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(query)
                .analyzeWildcard(true).field("bookName").field("authorName"));
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchResponse);
        } catch (IOException e) {

            e.printStackTrace();
        }

        List<Book> books = getSearchResult(searchResponse);

        return books;
    }

    private List<Book> getSearchResult(SearchResponse response) {
        SearchHit[] searchHits = response.getHits().getHits();
        List<Book> notes = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Book.class));
        }
        System.out.println(notes);
        return notes;
    }

}
