package com.trade.markets.marketsignalsGooglealertsRSSFeedReader.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Rssfeednewsarticles {

	@Id
	public ObjectId _id;

	private String symbol;
	private String title;
	private String content;
	private java.time.LocalDateTime time;
	private String url;

	// ObjectId needs to be converted to string
	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public java.time.LocalDateTime getTime() {
		return time;
	}

	public void setTime(java.time.LocalDateTime time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
