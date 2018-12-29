/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.VariableDeclaration;*/

/**
 * @version 24.07.2018
 * @author Berke
 * This class retrieves the JavaScript file in String form and parses it to 
 * find specified JavaScript functions that make the AJAX requests
 */
/*public class JSParser {

    //properties
    private String jsURI;
    private String jsFile;
    private List<String> jsDumpster;
    private List<String> uniqueAjaxReqs;*/

    //constructor
    /**
     * Initialize the JSParser object
     * @param jsURI - String - JavaScript file that will be parsed
     */
    /*public JSParser(String jsURI) throws IOException {
        this.jsURI = jsURI;
        jsDumpster = new LinkedList<String>();
        uniqueAjaxReqs = new LinkedList<String>();
        setJsFile();
    }*/

    //methods 
    /**
     * Receive the contents of the JavaScript file in String format then store it
     */
    /*private void setJsFile() {
        try {
            // create a new URI object that has the URI of the javascript file we will parse
            URL newURI;
            newURI = new URL(jsURI);
            
            // read from the javascript file by opening an input stream
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(newURI.openStream()));

            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder();

            String line;
            
            // convert the javascript file into a String by reading each line from the reader then appending them to the string
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            reader.close();

            jsFile = stringBuilder.toString().trim();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        } catch (Exception e) {
            System.out.println("Oh no!");

        }
    }*/
    
    /**
     * Create a new javascript parser then find every function that makes an ajax request
     */
    /*public void findAJAXRequests() {
        try {
            // create the parser, which is a tree, then visit each node to find the javascript functions that make the ajax requests
            AstNode node = new Parser().parse(jsFile, null, 1);
            node.visit(new Collector());
        }
        catch ( Exception e) {
            System.out.println( "Ooops, something went wrong while finding the ajax requests...");
        }
    }*/
    
    /**
     * Returns the String format of the javascript file
     * @return the javascript file
     */
    /*public String getJSFile() {
        return jsFile;
    }*/

    /**
     * traverse every String that contains the js functions that make the ajax requests, 
     * either replace the string that contains the js function that makes the ajax request 
     * with a smaller String, or if the javascript function that makes the ajax request is 
     * unique add it to the list     
     */
    /*public void fillUniqueAjaxReqs() {        
        //traverse every String that contains the js functions that make the ajax requests
        for (int i = 0; i < jsDumpster.size(); i++) {
            //replace the string that contains the js function that makes the ajax request with a smaller String
            boolean replaced;
            replaced = replaceReqs(jsDumpster.get(i));
            
            // if the javascript function that makes the ajax request is unique add it to the list
            if (!replaced) {
                uniqueAjaxReqs.add(jsDumpster.get(i));
            }
        }
    }*/
    
    /**
     * Print the unique functions that make the AJAX requests
     */
    /*public void printAjaxReqs() {
        for (int i = 0; i < uniqueAjaxReqs.size(); i++) {
            System.out.println(i + 1); 
            System.out.println(uniqueAjaxReqs.get(i));
        }

        if (uniqueAjaxReqs.isEmpty()) {
            System.out.println("There are no ajax requests being made...");
        }
    }*/
    
    /**
     * If a shorter string that contains the whole AJAX request method replace the longer string with the shorter one
     * @param req - String - string that contains the method that makes the AJAX request
     * @return whether the string was replace or not
     */
    /*private boolean replaceReqs(String req) {
        // traverse all the javascript functions that contain the ajax requests
        for (int i = 0; i < uniqueAjaxReqs.size(); i++) {
            // if the req string contains the ajax request and is smaller than the other string that also contains the ajax request 
            // replace the string that contains the js function that makes the ajax request with a smaller String then return true
            if (uniqueAjaxReqs.get(i).contains(req)) {
                uniqueAjaxReqs.set(i, req);
                return true;
            }
        }
        // if the javascript function is unique thus far, return false
        return false;
    }

    class Collector implements NodeVisitor {

        public boolean visit(AstNode node) {
            if ((node.toSource().contains(".ajax(") || node.toSource().contains(".post(") || node.toSource().contains(".open(")) && (node instanceof FunctionCall || node instanceof ExpressionStatement || node instanceof VariableDeclaration)) {//|| node instanceof ExpressionStatement || node instanceof VariableDeclaration)) {//ExpressionStatement || node instanceof VariableDeclaration)) {
                //System.out.println(node.toSource());
                jsDumpster.add(node.toSource());
            }
            //System.out.println( "BREAK");                
            return true;
        }
    }
}*/