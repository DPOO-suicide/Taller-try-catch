package uniandes.dpoo.taller1.exceptions;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SomeAuthorsNotFoundException extends Exception {
	 
	private ArrayList<String> authorsFound;
	
	private ArrayList<String> authorsNotFound;
	
    public SomeAuthorsNotFoundException(String message) {
        super(message);
    	authorsFound = new ArrayList<String>();
    	authorsNotFound = new ArrayList<String>();
    }
    
    public void addAuthorFound(String nameAuthor) {
    	authorsFound.add(nameAuthor);
    }
    
    public void addAuthorNotFound(String nameAuthor) {
    	authorsNotFound.add(nameAuthor);
    }
    
    public ArrayList<String> getAuthorsFound(){
    	return authorsFound;
    }
    
    public ArrayList<String> getAuthorsNotFound(){
    	return authorsNotFound;
    }
}