/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @version 24.07.2018
 * @author Berke
 * This class is responsible for crawling all the web pages linked with the seed web page 
 * (as long as the web pages that will be crawled have the same host with the seed web page 
 * or are directly connected with web pages that have the same host as the seed web page)
 */
public class Spider {

    //properties
    //properties with set types are there for easily finding out whether a given file, uri, whatever is in the collection
    private Set<String> pagesVisited;
    private Set<String> uniquePages; // this property holds a single copy of all the unique web pages encountered 
    private Set<String> javaScript; // this property holds a single copy of all the javascript files encountered 
    private Set<String> images; // this property holds a single copy of all the images encountered 
    private Set<String> css; // this property holds a single copy of all the css files encountered 
    private Set<String> blacklist;    
    
    //list types are used to easily traverse the data    
    private List<String> uniquePlusJsPages; // a list that holds all of the unique links and javascript pages
    private List<String> pagesToVisit;
    private List<String> uniqueJs;
    private URITreeContainer container;
    private String domain;
    private String cookieName;
    private String cookieValue;

    //constructor
    /**
     * Initialize the Spider object
     */
    public Spider() {
        javaScript = new HashSet<String>();
        images = new HashSet<String>();
        css = new HashSet<String>();
        pagesVisited = new HashSet<String>();
        uniquePages = new HashSet<String>();
        blacklist = new HashSet<String>();
        
        pagesToVisit = new LinkedList<String>();
        uniquePlusJsPages = new LinkedList<String>();
        uniqueJs = new LinkedList<String>();
        
        container = new URITreeContainer();
        
        domain = null;
        cookieName = null;
        cookieValue = null;
    }

    //methods
    /*
     * @param URL - string - adds the seed url to the pages to visit list then starts printing the connected pages
     */
    public void printPages(String URL, String cookie) {
        pagesToVisit.add(URL);
        uniquePages.add(URL);
        domain = this.getDomain(URL);
        parseCookie(cookie);
        printPagesHelper(URL);

    }

    /**
     * If a page isn't visited and it has the same domain with the seed web page, crawl it, get all the relevant data (css, js, html, image files) from the web page then make a recursive call to crawl pages that will be visited
     * @param URL - the url that will be crawled
     */
    public void printPagesHelper(String URL) {
        // if the URL isn't visited enter into the if statement
        if (!pagesVisited.contains(URL) && domain.equals(getDomain(URL))) {
            SpiderLeg crawly = new SpiderLeg(uniquePages, images, css, javaScript);
            // add the URL to the list of links visited
            pagesVisited.add(URL);
            // crawl through the URL to find the links found in the page
            crawly.crawl(URL, cookieName, cookieValue);
            // since the URL is visited, remove it from the list of pages to visit
            pagesToVisit.remove(0);
            // add all of the newly acquire links from the URL we just visited to the pages to visit and unique pages list
            pagesToVisit.addAll(crawly.getLinks());
            uniquePages.addAll(crawly.getLinks());
            // update the css, image, javascript collections
            css.addAll(crawly.getCSS());
            images.addAll(crawly.getImages());
            javaScript.addAll(crawly.getJS());
            //uniqueJs.addAll(crawly.getJS());
            //update the list containing the unique web pages and javascript files
            uniquePlusJsPages.addAll(crawly.getLinks());
            //uniquePlusJsPages.addAll(crawly.getJS());

            // while there are pages to visit left visit the page at the beginning of the list
            while (!pagesToVisit.isEmpty()) {
                // if a page is already visited remove it from the pages to visit list
                if (pagesVisited.contains(pagesToVisit.get(0))) {
                    pagesToVisit.remove(0);
                    continue;
                }
                // if a web page has a different domain than the seed domain add it to the pages visited list, so it is ignored through rest of the call, then remove it from the pages to visit list
                if (!domain.equals(getDomain(pagesToVisit.get(0))) || blacklist.contains(pagesToVisit.get(0))) {
                    if (blacklist.contains(pagesToVisit.get(0))) {
                        System.out.println("BLACKLISTED " + pagesToVisit.get(0));
                    }
                    
                    pagesVisited.add(pagesToVisit.get(0));
                    pagesToVisit.remove(0);
                    continue;
                }

                // if there are pages left to visit after the possible removals visit the page
                //if (!pagesToVisit.isEmpty()) {
                    printPagesHelper(pagesToVisit.get(0));
                //}
            }
        }
    }

    /**
     * Add all of the unique pages to a container that contains URI trees. A URI tree contains all the web pages with the same domain that were crawled 
     */
    public void fillContainer() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < uniquePlusJsPages.size(); i++) {
            if (getDomain(uniquePlusJsPages.get(i)) != null) {
                container.addURI(uniquePlusJsPages.get(i));
            }
        }
    }
    
    /** 
     * print the web pages with different URLs in an orderly fashion
     */
    public void printContainer() {
        container.printTrees();
    }

    /*
     * extract the host from the url that was entered
     * @param URL - the url of a web page
     * @return the host string or null if the string that was entered in the parameter turned out to be something other than a url
     */
    private String getDomain(String URL) {
        try {
            URI uri = new URI(URL);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    /**
     * Find the cookie name and value if the cookie string isn't null or empty
     * @param cookie - String - String form of both the cookie name and cookie value     
     */
    private void parseCookie( String cookie) {
        if ( cookie != null && !(cookie.equals(""))) {
            int equalSignIndex;
            equalSignIndex = cookie.indexOf("=");
            cookieName = cookie.substring(0, equalSignIndex);
            cookieValue = cookie.substring(equalSignIndex + 1);
        }
    }
    
    /**
     * Adds a user input web page into blacklist of web pages that will not be visited
     * @param blacklistedPage - String - blacklisted web page
     */
    public void addPageToBlackList( String blacklistedPage) {
        System.out.println( "Blacklisted page: " + blacklistedPage);
        blacklist.add(blacklistedPage);
    }    
    
    public void printUniquePages() {
        for ( int i = 0; i < uniquePlusJsPages.size(); i++) {
            System.out.println( "uniquePlusJsPages.get(i) " + uniquePlusJsPages.get(i));
        }
    }
    

}