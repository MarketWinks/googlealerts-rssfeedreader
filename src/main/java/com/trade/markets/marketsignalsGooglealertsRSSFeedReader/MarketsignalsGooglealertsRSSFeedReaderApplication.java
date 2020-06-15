package com.trade.markets.marketsignalsGooglealertsRSSFeedReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.trade.markets.marketsignalsGooglealertsRSSFeedReader.repository.RssfeedNewsArticleRepository;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan(basePackages = { "com.*" })
@EnableMongoRepositories(basePackages = "com.trade.markets.marketsignalsGooglealertsRSSFeedReader.repository")
@EnableCaching
public class MarketsignalsGooglealertsRSSFeedReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketsignalsGooglealertsRSSFeedReaderApplication.class, args);
	}

	@Autowired
	RssfeedNewsArticleRepository rssfeedNewsArticleRepository;

}
