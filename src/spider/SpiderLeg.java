/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @version 24.07.2018
 * @author Berke
 * The class that crawls a single web page, collecting the JavaScript files and links
 */
public class SpiderLeg {

    //properties
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links;
    private List<String> picsInThisPage;
    private List<String> jsFilesInThisPage;
    private List<String> cssInThisPage;
    private Document htmlDocument;
    private Set<String> uniquePages;
    private Set<String> images;
    private Set<String> jsFiles;
    private Set<String> cssFiles;

    //constructor
    /**
     * Initialize the SpiderLeg object
     * @param uniquePages - Set<String> - the set that contains a unique copy of the web pages that were already visited
     * @param images - Set<String> - the set that contains a unique copy of the images that were already obtained
     * @param css - Set<String> - the set that contains a unique copy of the CSS files that were already obtained
     * @param javaScript - Set<String> - the set that contains a unique copy of the JavaScript files that were already obtained
     */
    public SpiderLeg(Set<String> uniquePages, Set<String> images, Set<String> css, Set<String> javaScript) {
        links = new LinkedList<String>();
        jsFilesInThisPage = new LinkedList<String>();
        picsInThisPage = new LinkedList<String>();
        cssInThisPage = new LinkedList<String>();
        htmlDocument = null;
        this.uniquePages = uniquePages;
        this.images = images;
        cssFiles = css;
        jsFiles = javaScript;
    }

    //methods
    /**
     * If the link that is being crawled is an html page continue to crawl 
     * it to collect the jacascript files and the web page links found in the current link
     * @param currentURL - String - url that is being crawled
     * @param cookieName - String - cookie name that might be being used as the session cookie
     * @param cookieValue - String - value of the cookie
     * @return whether the crawling was successful or not
     */
    public boolean crawl(String currentURL, String cookieName, String cookieValue) {
        try {
            // create the connection
            Connection connection = Jsoup.connect(currentURL).userAgent(USER_AGENT);
            if ( cookieName != null && !cookieName.equals("")) {
                connection.cookie(cookieName, cookieValue);
            }
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            this.htmlDocument.setBaseUri(currentURL);

            // if the connection is successful let the user know
            if (connection.response().statusCode() == 200) {
                System.out.println("Currently visiting " + currentURL);
            }

            // if something other than an html file was received let the user know and return false
            if (!connection.response().contentType().contains("text/html")) { //text/
                System.out.println("**Failure** Retrieved something other than HTML");
                //System.out.println( connection.response().contentType());  
                return false;
            }

            // parse all the elements that contain script in them and put it into a list
            Elements scriptElems = htmlDocument.select("script");

            // for every element in the list, if the javascript file/link was not already found in a previous crawl add it into the list of unique javascript file/link list             
            for (Element scriptElem : scriptElems) {
                if (!jsFiles.contains(scriptElem.absUrl("src")) && !scriptElem.absUrl("src").equals("")) {
                    this.jsFilesInThisPage.add(scriptElem.absUrl("src"));
                    this.jsFiles.add(scriptElem.absUrl("src"));
                    //System.out.println(scriptElem.absUrl("src"));
                    //System.out.println("****************************");
                }
            }

            //System.out.println("Found (" + jsFilesInThisPage.size() + ") new js files");
            // parse all the elements that contain img in them and put it into a list
            Elements pics = htmlDocument.select("img");

            // for every element in the list, if the image was not already found in a previous crawl add it into the list of unique image list
            for (Element pic : pics) {
                if (!images.contains(pic.absUrl("src")) && !pic.absUrl("src").equals("")) {
                    this.picsInThisPage.add(pic.absUrl("src"));
                    this.images.add(pic.absUrl("src"));
                    //System.out.println( pic.absUrl("src"));
                }
            }

            //System.out.println("Found (" + picsInThisPage.size() + ") new images");
            // parse all the elements that contain link in them and put it into a list
            Elements css = htmlDocument.select("link");

            // for every element in the list, if the css file was not already found in a previous crawl add it into the list of unique css files list
            for (Element cascadingStyleSheet : css) {
                if (!cssFiles.contains(cascadingStyleSheet.absUrl("href")) && !cascadingStyleSheet.absUrl("href").equals("")) {
                    this.cssInThisPage.add(cascadingStyleSheet.absUrl("href"));
                    this.cssFiles.add(cascadingStyleSheet.absUrl("href"));
                    //System.out.println( cascadingStyleSheet.absUrl("href"));
                }
            }

            //System.out.println("Found (" + cssInThisPage.size() + ") new css files");
            // parse all the elements that contain a[href] in them and put it into a list
            Elements linksOnPage = htmlDocument.select("a[href]");

            // for every element in the list, if the link was not already found in a previous crawl add it into the list of unique links list
            for (Element link : linksOnPage) {
                //if ( !this.links.contains( link.absUrl( "href")) && !pagesVisited.contains(link.absUrl( "href")) && !uniquePages.contains(link.absUrl( "href"))) {
                if (!uniquePages.contains(link.absUrl("href")) && !link.absUrl("href").equals("")) {
                    this.links.add(link.absUrl("href"));
                    this.uniquePages.add(link.absUrl("href"));
                    System.out.println(link.absUrl("href"));
                }
            }

            //System.out.println("Found (" + links.size() + ") new links");
            return true;
        } catch (IOException ioe) {
            return false;
        }

    }
    
    /**
     * An accessor method that returns all the links that were obtained as a result of crawling
     * @return a list of links that were obtained as a result of crawling
     */
    public List<String> getLinks() {
        return links;
    }
    
    /**
     * An accessor method that returns all the css files that were obtained as a result of crawling
     * @return a list of css files that were obtained as a result of crawling
     */
    public List<String> getCSS() {
        return cssInThisPage;
    }
    
    /**
     * An accessor method that returns all the javascript files that were obtained as a result of crawling
     * @return a list of javascript files that were obtained as a result of crawling
     */
    public List<String> getJS() {
        return jsFilesInThisPage;
    }
    
    /**
     * An accessor method that returns all the images that were obtained as a result of crawling
     * @return a list of images that were obtained as a result of crawling
     */
    public List<String> getImages() {
        return picsInThisPage;
    }
    
    /**
     * An accessor method that returns all the unique links that were obtained as a result of crawling
     * @return a list of unique links that were obtained as a result of crawling
     */
    public Set<String> getUniquePages() {
        return this.uniquePages;
    }
}