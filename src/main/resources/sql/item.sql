CREATE FULLTEXT INDEX title_search
ON shop.item(title) WITH PARSER ngram;