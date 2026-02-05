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
	
	static void display_data(ArrayList<user> loaded_people){
		System.out.println(" ");
		for (user p : loaded_people){
			System.out.println(p.name+" -- "+p.bank_bal);
		}
		System.out.println(" ");
	}
	
	static ArrayList<user> choose(Scanner in,ArrayList<user> loaded_people){
		
		ArrayList <user> selected_people= new ArrayList<>();
		int count=1;
		for (user p : loaded_people){
			System.out.println(count+". "+p.name);
			count++;
		}
		
		while (true)
		{
			System.out.print("How many people to choose? : ");
			int num= in.nextInt();
			if (num <1 || num>loaded_people.size())
			{
				System.out.println("Invalid input. Try again.");
				continue;
			}else
			{
				for(int x=0;x<num;x++)
				{
					System.out.print("Choose a person: ");
					int personIndex= in.nextInt();
					if (personIndex>loaded_people.size()){
						x--;
						System.out.println("Invalid. Choose in the range!");
					}
					selected_people.add(loaded_people.get(personIndex-1));
				}
			}
			break;
		}
		return selected_people;
	}
	
	static ArrayList <user> load_data(Scanner in){
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
			int num_of_people;
			while (true){
				System.out.print("How many users do you want to add? : ");
				num_of_people=in.nextInt();
				if (num_of_people>100){
					System.out.println("Too many users. Ask the developer to edit the code to accommodate that many users.");
				}else if (num_of_people<2){
					System.out.println("Needs at least 2 users. Enter again.(Or ask the developer to edit the code to accommodate to a single person if you want to track just your own balance)");
				}else{
					break;
				}
			}
			in.nextLine();
			for(int x=0;x<num_of_people;x++){
				System.out.print("Enter the user's name: ");
				String uname= in.nextLine();
				people.add(new user (uname, 0));
			}
			
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
	
	static void add_user(Scanner in,ArrayList <user> loaded_people){
		in.nextLine();
		System.out.print("Enter the user's name: ");
		String uname= in.nextLine();
		
		loaded_people.add(new user(uname,0));
	}
	
	static void del_user(Scanner in,ArrayList <user> loaded_people){
		while(true){
			in.nextLine();
			System.out.print("Who do you want to delete: ");
			String del_name= in.nextLine();
			boolean found= false;
			for(int i=loaded_people.size()-1;i>=0;i--){
				if (loaded_people.get(i).name.equals(del_name)){
					loaded_people.remove(i);
					found=true;
					break;
				}
			}
			if (found==false){
				System.out.println("Invalid name.Try again.(Spell check correctly!)");
			}else{
				System.out.println("User deleted successfully.");
				break;
			}
		}
	}
	
	public static void main(String[]args)
	{	
		Scanner in= new Scanner(System.in);
		
		BudgetCal obj= new BudgetCal();
		int person=0;
		
		ArrayList<user> loaded_people= obj.load_data(in);
		ArrayList<user> selected_people;
		while(true)
		{
			obj.display_data(loaded_people);
			System.out.println("1. Add money");
			System.out.println("2. Subtract money");
			System.out.println("3. Add a user");
			System.out.println("4. Delete a user");
			System.out.println("5. Exit");
			System.out.print("Enter what you want to do: ");
			int action=in.nextInt();
			
			if (action==1)
			{
				selected_people = obj.choose(in,loaded_people);
				obj.add_mon(selected_people,in);
				System.out.println(selected_people);
				obj.save_data(loaded_people);
			
			}else if (action==2)
			{
				selected_people = obj.choose(in,loaded_people);
				obj.subtract_mon(selected_people,in);
				System.out.println(selected_people);
				obj.save_data(loaded_people);
				
			}else if (action==3)
			{
				obj.add_user(in,loaded_people);
				obj.save_data(loaded_people);
				
			}else if(action==4)
			{
				obj.del_user(in,loaded_people);
				obj.save_data(loaded_people);
				
			}else if(action==5)
			{
				break;
			}
			
		}
		obj.save_data(loaded_people);
		
		
	}
}

/*
add an amount to a person's acc and display it. ✅
subtract an amount from a selected number of people.✅
ability to name every transaction.
keep a log of every transaction and their dates.

1. add people method,delete people method✅

2. log_data
- amount
- wheather its an adddition or a subtraction
- date&time
- custom msg
*/