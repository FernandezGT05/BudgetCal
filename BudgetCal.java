import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.*;
import java.io.*;

class user
{
	String name;
	double bank_bal;
	
	user(String name, double bank_bal){
		this.name=name;
		this.bank_bal=bank_bal;
	}
	@Override
	public String toString(){
		return name +"| Rs."+ bank_bal;
	} 
}
public class BudgetCal
{
	
	void add_mon(ArrayList<user> selected_people, Scanner in ){
		System.out.print("How much? : ");
		double money= in.nextDouble();
		
		for(user p : selected_people) {
			p.bank_bal+=money;
		}
	}
	
	void subtract_mon(ArrayList<user> selected_people,Scanner in) {
		System.out.print("How much? : ");
		double money= in.nextDouble();
		
		for (user p : selected_people) {
			p.bank_bal-=money;
		}
	}
	
	static ArrayList<user> choose(Scanner in,ArrayList<user> loaded_people){
		
		ArrayList <user> selected_people= new ArrayList<>();
		
		System.out.println("1. Gayan");
		System.out.println("2. Tharana");
		System.out.println("3. Chapa");
		System.out.println("4. Ravishan");
		
		while (true)
		{
			System.out.print("How many people to choose? : ");
			int num= in.nextInt();
			if (num <1 || num>4)
			{
				System.out.println("Invalid input. Try again.");
				continue;
			}else
			{
				for(int x=0;x<num;x++)
				{
					System.out.print("Choose a person: ");
					int personIndex= in.nextInt();
					selected_people.add(loaded_people.get(personIndex-1));
				}
			}
			break;
		}
		return selected_people;
	}
	
	static ArrayList <user> load_data(){
		ArrayList<user> people= new ArrayList<>();
		
		Gson gson= new GsonBuilder().setPrettyPrinting().create();
		
		Type listType= new TypeToken<ArrayList<user>>(){}.getType();
		
		try(FileReader reader= new FileReader("budget_data.json")){
			ArrayList<user> loaded = gson.fromJson(reader,listType);
			if (loaded!=null) people=loaded;
		} catch(IOException e){
			e.printStackTrace();
		}
		if (people.isEmpty()) {
			people.add(new user("Gayan", 0));
			people.add(new user("Tharana", 0));
			people.add(new user("Chapa", 0));
			people.add(new user("Ravishan", 0));

			try (FileWriter writer = new FileWriter("budget_data.json")) {
				gson.toJson(people, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return people;
	}
	
	static void save_data(ArrayList <user> selected_people){
		Gson gson= new GsonBuilder().setPrettyPrinting().create();
		
		try(FileWriter writer= new FileWriter("budget_data.json")){
			gson.toJson(selected_people,writer);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args)
	{	
		Scanner in= new Scanner(System.in);
		
		BudgetCal obj= new BudgetCal();
		int person=0;
		
		ArrayList<user> loaded_people= obj.load_data();
		ArrayList<user> selected_people;
		while(true)
		{
			System.out.println("1. Add money");
			System.out.println("2. Subtract money");
			System.out.println("3. Exit");
			System.out.print("Enter what you want to do: ");
			int action=in.nextInt();
			
			if (action==1)
			{
				selected_people = obj.choose(in,loaded_people);
				obj.add_mon(selected_people,in);
				System.out.println(selected_people);
			
			}else if (action==2)
			{
				selected_people = obj.choose(in,loaded_people);
				obj.subtract_mon(selected_people,in);
				System.out.println(selected_people);
				
			}else if (action==3)
			{
				break;
			}
			
		}
		obj.save_data(loaded_people);
		
		
	}
}

/*
add an amount to a person's acc and display it. |||||||||
subtract an amount from a selected number of people.------------ how to choose the people to add or subtract --> add them to an array.
ability to name every transaction.
keep a log of every transaction and their dates.
*/