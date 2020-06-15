package com.trade.markets.marketsignalsGooglealertsRSSFeedReader.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.trade.markets.marketsignalsGooglealertsRSSFeedReader.model.Rssfeednewsarticles;
import com.trade.markets.marketsignalsGooglealertsRSSFeedReader.repository.RssfeedNewsArticleRepository;

@RestController
@RequestMapping("/baseURL")
public class RssfeedNewsArticleParser {

	@Autowired
	private RssfeedNewsArticleRepository rssfeedNewsArticleRepository;

	@RequestMapping(value = "/RssfeedNewsArticleParser/start", method = RequestMethod.GET)
	public boolean RssfeedNewsArticleParser() {

		boolean execution_result = false;

		try {
			File file = new File("src/main/resources/feeds.txt");

			BufferedReader br = new BufferedReader(new FileReader(file));
			MongoClient mongoClient = MongoClients.create(
					"mongodb+srv://marketwinks:L9sS6oOAk1sHL0yi@aws-eu-west1-cluster-tszuq.mongodb.net/marketwinksdbprod?retryWrites=true");
			MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "marketwinksdbprod");

			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {

					String feedURLnumber1 = null;
					String feedURLnumber2 = null;

					Pattern pattern1 = Pattern.compile("-feedNumber1-(.*?)-feedNumber2-", Pattern.DOTALL);
					Matcher matcher1 = pattern1.matcher(line.toString());
					while (matcher1.find()) {
						feedURLnumber1 = matcher1.group(1);
					}

					Pattern pattern2 = Pattern.compile("-feedNumber2-(.*?)-endoffeed", Pattern.DOTALL);
					Matcher matcher2 = pattern2.matcher(line.toString());
					while (matcher2.find()) {
						feedURLnumber2 = matcher2.group(1);
					}

					String feedURLFull = "https://www.google.com/alerts/feeds/" + feedURLnumber1 + "/" + feedURLnumber2;

					URL feedSource = null;
					SyndFeed feed = null;
					try {
						feedSource = new URL(feedURLFull);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SyndFeedInput input = new SyndFeedInput();
					try {
						feed = input.build(new XmlReader(feedSource));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FeedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String title;
					String link;
					String contents;

					String[] parts = feed.getTitle().toString().split("Alert - ");
					String symbol = parts[1].toString().toUpperCase();

					for (int i = 0; i < feed.getEntries().size(); i++) {

						title = StringUtils.substringBetween(feed.getEntries().get(i).toString(),
								"SyndEntryImpl.title=", "SyndEntryImpl.interface=");

						link = StringUtils.substringBetween(feed.getEntries().get(i).toString(), "SyndEntryImpl.link=",
								"SyndEntryImpl.description=");

						contents = StringUtils.substringBetween(feed.getEntries().get(i).toString(),
								"SyndEntryImpl.contents[0].value=", "SyndEntryImpl.links[0].hreflang=");

						Rssfeednewsarticles rssfeedNewsArticle = new Rssfeednewsarticles();

						rssfeedNewsArticle.setSymbol(symbol);
						rssfeedNewsArticle.setTitle(title);
						rssfeedNewsArticle.setUrl(link);
						rssfeedNewsArticle.setContent(contents);
						rssfeedNewsArticle.setTime(java.time.LocalDateTime.now());

						List<Rssfeednewsarticles> queryresult_querytostopduplicate = new ArrayList<>();

						try {

							Query querytostopduplicate = new Query();
							querytostopduplicate.addCriteria(Criteria.where("symbol").is(symbol));

							querytostopduplicate.addCriteria(Criteria.where("title").is(title));
							queryresult_querytostopduplicate = mongoTemplate.find(querytostopduplicate,
									Rssfeednewsarticles.class);
						} catch (Exception e) {
							System.out.println(e);
						}

						if (queryresult_querytostopduplicate.size() == 0) {
							Rssfeednewsarticles saveresult = rssfeedNewsArticleRepository.insert(rssfeedNewsArticle);
						}

					}

					line = br.readLine();
				}

			} finally {
				br.close();
				mongoClient.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		execution_result = true;
		return execution_result;

	}

}