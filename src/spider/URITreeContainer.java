/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * @version 24.07.2018
 * @author Berke
 * This class contains multiple URL trees with different hosts in the root
 */
public class URITreeContainer {

    //properties
    private ArrayList<URITree> treeContainer;

    //constructors
    /**
     * Initialize the URITreeContainer object
     */
    public URITreeContainer() {
        treeContainer = new ArrayList<URITree>();
    }

    //methods
    /** 
     * add a new tree to the container
     * @param tree - URITree - tree to be added to the container
     */
    public void addTree(URITree tree) {        
        treeContainer.add(tree);
    }

    /**
     * if a domain of a webpage is already in the root of one of the trees in the container return the trees index in the container, if it is not in the container return -1
     * @param domain - String - domain of a web page
     * @return index of the tree that contains the domain or -1 if the domain is not in any of the trees
     */
    private int containsDomain(String domain) {
        for (int i = 0; i < treeContainer.size(); i++) {
            if (treeContainer.get(i).getRootPath().equals(getDomain(domain))) {
                return i;
            }
        }

        return -1;
    }
    
    /**
     * get the domain from a URI input
     * @param URI - String - the URI of the domain we want to get 
     * @return return the domain, if there is an exception return null
     */
    private String getDomain(String uriInput) {
        try {
            URI uri = new URI(uriInput);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Add a uri to one of the uri trees if the uri's domain is already a root for one of the trees, else create a new tree and add the uri to that tree
     * @param uri - uri that will be added to one of the trees in the container
     */
    public void addURI(String uri) {
        int indexOfDomain;
        indexOfDomain = containsDomain(uri);

        // if the uri isn't equal to any of the domains in the roots of the trees in the container, create a new tree and add it to the container
        if (indexOfDomain == -1) {
            URITree newTree;
            newTree = new URITree(uri);
            treeContainer.add(newTree);
        } else { // if the domain of the uri is already in a root in one of the trees in the container add the uri and its path that uri tree in container
            treeContainer.get(indexOfDomain).addURI(uri);
        }
    }

    /**
     * prints all of the trees in the container
     */
    public void printTrees() {
        for (int i = 0; i < treeContainer.size(); i++) {
            System.out.println("--------------------------------New page------------------------------");
            treeContainer.get(i).printTree();
            System.out.println("----------------------------------------------------------------------");
        }
    }

}

