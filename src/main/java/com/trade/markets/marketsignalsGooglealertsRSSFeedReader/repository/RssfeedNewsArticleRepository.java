package com.trade.markets.marketsignalsGooglealertsRSSFeedReader.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.trade.markets.marketsignalsGooglealertsRSSFeedReader.model.Rssfeednewsarticles;

@Repository
public interface RssfeedNewsArticleRepository extends MongoRepository<Rssfeednewsarticles, String> {
}