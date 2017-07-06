# spring-elastic-search
Current Spring doesnot support latest Elasticsearch. We will use the Elasticsearch Java client.

## Design Highlight

1. Hide the details to generate the document for index. 
   Use Object instead of using doc directly.
2. Hide the details to generate the complex queries.
   From programming point of view we only need to know the inputs of the queries. The SearchRequest entity will hide the way how we build the queries and then pass to elastic client.
3. Simplified the business logic to query the search engine.