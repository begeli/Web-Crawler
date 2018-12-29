/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @version 24.07.2018
 * @author Berke
 * This class organizes the URL's that have the same host in a multi(more than two) child tree 
 */
package spider;

import java.util.List;
import java.util.ArrayList;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class URITree {

    //properties
    public URITreeNode root;

    //constructors
    /**
     * Initialize the URITree object
     * @param domain - String - domain of the URITree that will become the root
     */
    public URITree(String domain) {
        root = new URITreeNode();
        root.path = getDomain(domain);
        root.key = getDomain(domain);
        root.parent = null;
        root.children = new ArrayList<URITreeNode>();
        root.is_directory = true;

        //if a path exists in the seed uri entered, add the path to the tree as well
        if (!(getPath(domain) == null) && !getPath(root.path).equals("")) {
            addURI(domain);
        }
    }

    //methods
    /*
     * accessor method that returns the root path
     * @return the root path
     */
    public String getRootPath() {
        return root.path;
    }
    
    /**
     * Function that will be called to add a new URI to the tree
     * @param newURI - String - new URI that will be added to the tree
     */
    public void addURI(String newURI) {
        // if the new uri has the same domain with the root domain add it to the tree
        if (root.path.equals(getDomain(newURI))) {
            String queryString = checkForQueryString(newURI);
            
            addURIWithDirectory(getPath(newURI) + queryString);
        }
    }
    
    private String checkForQueryString(String newURI) {
        String queryString = "";
            
        if (newURI.contains("?")) {
            String temp = newURI;
            String[] uriFragments = temp.split("\\?");
            queryString = "?" + uriFragments[uriFragments.length - 1];
        }
            
        return queryString;
    }

    /**
     * create a new node using the information in the parameters and append it to the parent node
     * @param parent_node - URITreeNode - parent of the new node that will be created
     * @param name - String - string that will become the new key of the new node
     * @param uri - String - full path that is needed to take to arrive at this node
     * @param isDirectory - boolean - flag that indicates whether the new node is a directory or a web page
     * returns the new child node that is created
     */
    private URITreeNode appendToNode(URITreeNode parent_node, String name, String uri, boolean isDirectory) {
        URITreeNode new_node = new URITreeNode();
        new_node.key = name;
        new_node.path = uri + (isDirectory ? "/" : "");
        new_node.children = new ArrayList<URITreeNode>();
        new_node.parent = parent_node;
        new_node.is_directory = isDirectory;
        parent_node.children.add(new_node);
        return new_node;
    }

    /**
     * Traverse the children of the current node, if we find the node we are searching return the node otherwise return null
     * @param current - URITreeNode - current node that might potentially carry the key we are looking for
     * @param key - String - directory or the web page in the path
     * return the node that carries the key we are looking for or null if no node carries the key
     */
    private URITreeNode findChildren(URITreeNode current, String key) {
        for (int i = 0; i < current.children.size(); i++) {
            URITreeNode node = current.children.get(i);
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Split the path of a uri, if the splitted part was already a part of one another pages path follow that path, otherwise create a new path to add the new uri 
     * @param uri - String - uri that will be added to the  tree
     */
    private void addURIWithDirectory(String uri) {
        String[] indices = uri.split("/");
        URITreeNode current = root;
        for (int i = 1; i < indices.length; i++) {
            String index = indices[i];
            URITreeNode candidate = findChildren(current, index);
            // if the splitted part was already a part of an existing path set the current to that node in the path
            if (candidate != null) {
                current = candidate;
            } // else create a new path to the uri to be added
            else {
                String[] ilist = Arrays.copyOfRange(indices, 0, i + 1);
                current = appendToNode(current, index, String.join("/", ilist), i != indices.length - 1);
            }
        }
    }
    
    /**
     * Method that will be called to print the complete URI Tree
     */
    public void printTree() {
        printTree(root, 0);
    }
    
    /**
     * Helper method that will be used to print the tree
     * @param currentNode - URITreeNode - node that will be printed
     * @param depth - int - depth of the node that will be printed
     */
    private void printTree(URITreeNode currentNode, int depth) {
        if (currentNode != null) {
            // print the depth of the page or directory leading to a page using ">"
            for (int i = 0; i < depth; i++) {
                System.out.print(">");
            }
            // indicate whether the printed node represents a web page or a directory in the path
            if (currentNode.is_directory) {
                System.out.print("+");
            } else {
                System.out.print("#");
            }
            System.out.println(currentNode.path);

            // print the children of the current node recursively
            for (int i = 0; i < currentNode.children.size(); i++) {
                printTree(currentNode.children.get(i), depth + 1);
            }
        }
    }
    
    /**
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
     * extract the raw path from the url that was entered
     * @param URL - the url of a web page
     * @return the path string or null if the string that was entered in the parameter turned out to be something other than a url
     */
    private String getPath(String URL) {
        try {
            URI uri = new URI(URL);
            return uri.getPath();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static class URITreeNode {

        String key;
        boolean is_directory = false;
        String path;
        URITreeNode parent;
        List<URITreeNode> children;
    }
}