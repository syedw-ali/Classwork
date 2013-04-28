import java.util.ArrayList;
import java.util.Iterator;


public class MultipleChoice extends Question{
	private ArrayList<String> options;
	
	public MultipleChoice(){
		options = new ArrayList<String>();
	}
	
	public MultipleChoice(int numOfOptions){
		options = new ArrayList<String>();
		
		if(numOfOptions > 0){
			createOptions();
		}
	}
	
	/**
	 * Wrapper create method
	 */
	public void create(){
		createOptions();
	}
	
	/**
	 * Creates options for this multiple choice question.
	 * Adds options to the 'options' ArrayList
	 */
	public void createOptions(){
		boolean addAnother = true;
		
		while(addAnother){
			System.out.println("Enter choice text: (type '\\done' when done)");
			String ans = rd.readLine();
			
			// is user finished entering choices
			if(ans.equals("\\done")){ // yes
				addAnother = false;
			} else{ // no
				this.addOption(rd.readLine());
			}
		} // while (addAnother)
	} // createOptions
	
	/**
	 * Add a string option text to the ArrayList 'options'
	 * @param optionStr string of the option value
	 */
	public void addOption(String optionStr){
		options.add(optionStr);
	}
	
	/**
	 * Get the number of options available for this multiple choice
	 * question
	 * @return number of options
	 */
	public int getNumberOfOptions(){
		return options.size();
	}
	
	/**
	 * Neatly display the question along
	 * with the multiple choices
	 */
	public void displayQuestion(){
		System.out.println("");
		System.out.println(this.getPrompt());
		displayOptions();
		System.out.println("");
	}
	
	/**
	 * Neatly displays all the options
	 * in a numbered list type order.
	 */
	public void displayOptions(){
		int optionNum = 1;
		
		Iterator<String> it = options.iterator();
		while(it.hasNext()){
			System.out.println(" ("+ optionNum + ") " + it.next());
			optionNum = optionNum + 1;
		}
	}
}