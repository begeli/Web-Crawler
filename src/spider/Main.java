
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @version 24.07.2018
 * @author Berke
 * This class is called from the terminal to Spider a designated web page
 */
package spider;

public class Main {

    public static void main(String[] args) {
        System.out.println("If there are no cookies, just type \"\"");
        
        if ( args.length >= 4) { 
            System.out.println("args.length " + args.length);
            if (!args[0].equals("uri--")){  
                throw new IllegalArgumentException("Expected uri-- found " + args[0]);
            }
            if (!args[2].equals("cookie--")) {  
                throw new IllegalArgumentException("Expected cookie-- found " + args[2]);
            }
            
            
            Spider spidey = new Spider();  
                        
            if ( args.length >= 6) {
                if (!args[4].equals("blocked--")) {
                    throw new IllegalArgumentException("Expected blocked-- found " + args[4]);
                }
                for ( int i = 5; i < args.length; i++) {
                    System.out.println("args[i] "+ args[i]);
                    spidey.addPageToBlackList( args[i]);
                }
            }
            
            spidey.printPages(args[1], args[3]);
            spidey.fillContainer();
            spidey.printContainer();
        }
        else {
            System.out.println( "Incorrect number of arguments were entered...");
        }
    
    }
}