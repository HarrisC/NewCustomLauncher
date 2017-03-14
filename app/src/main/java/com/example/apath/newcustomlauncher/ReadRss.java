package com.example.apath.newcustomlauncher;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadRss extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayList<FeedItem>feedItems;
    String realTime = "http://news.mingpao.com/rss/ins/s00001.xml";
    URL url;
    ProgressDialog progressDialog;


    public ArrayList<FeedItem> getFeedItems() {
        return feedItems;
    }

    public ReadRss(Context context){
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

    }
    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        NewsReader.setFeedItems(feedItems);
    }

    @Override
    protected Void doInBackground(Void... params) {
        ProcessXML(Getdata());
        return null;
    }

    private void ProcessXML(Document data) {
        if (data != null) {
            feedItems=new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node currentchild = items.item(i);
                if (currentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = currentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node current = itemchilds.item(j);
                        if (current.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("description")) {
                            item.setDescription(current.getTextContent());
                        }
                    }
                    feedItems.add(item);
                    //Log.d("itemTitle", item.getTitle());
                    //Log.d("itemDescription", item.getDescription());
                }
            }

        }
    }
    public Document Getdata(){
        try {
            url = new URL(realTime);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=builderFactory.newDocumentBuilder();
            Document rss = builder.parse(inputStream);
            return rss;
        }catch (Exception e){
            e.printStackTrace();
        } return null;
    }
}
