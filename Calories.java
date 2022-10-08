import java.util.*;
import java.io.*;

class FoodItem {
	double price;
	int[] nutrition;
	
	FoodItem(double price, int[] nutrition) {
		this.price = price;
		this.nutrition = nutrition;
	}

	@Override
	public String toString() {
		return "Price: " + price + ", \tProtein: " + nutrition[0] + ",\tIron: " + nutrition[1] + ", Starch: " + nutrition[2]; 
	}
}

public class Calories {
	private static ArrayList<ArrayList<FoodItem>> successful_permutations = new ArrayList<>();

	// this was for debugging
	private static void print_permutation(ArrayList<FoodItem> permutation) {
		System.out.println("[");
		for (FoodItem food: permutation)
			System.out.println("\t" + food);
		System.out.println("]");
	}

	// checks if all nutrition requirements are
	// met, given the test case
	private static boolean contains_req_nutrition(final int[] required_nutrition, ArrayList<FoodItem> purchased) {
		for (int i = 0; i < 3; i++) {
			// total of the nutrition type in the list
			int total = 0;
			for (FoodItem food: purchased)
				total += food.nutrition[i];
			// if its not enough, return false
			if (required_nutrition[i] > total)
				return false;
		}
		return true;
	}

	// This is the actual recursive
	// algorithim behind this solution
	@SuppressWarnings("unchecked") // this is because of the .clone casting -- https://stackoverflow.com/questions/4388054/java-how-to-fix-the-unchecked-cast-warning
	private static void permute(final int[] required_nutrition, ArrayList<FoodItem> purchased, ArrayList<FoodItem> left) {
		// if the current list of purchased
		// items is sufficient, end recursion
		// and add it to the solution list (success)
		if (contains_req_nutrition(required_nutrition, purchased)) {
			successful_permutations.add(purchased);
			return;
		}

		// if we are out of new options,
		// end recursion (failure)
		if (left.isEmpty())
			return;

		// remove the immediate next item from
		// our options, regardless of what next
		// choice we make
		FoodItem next = left.remove(0);

		// dont purchase the item
		permute(required_nutrition, purchased, (ArrayList<FoodItem>) left.clone());

		// purchase the item
		ArrayList<FoodItem> perm = (ArrayList<FoodItem>) purchased.clone();
		perm.add(next);
		permute(required_nutrition, perm, (ArrayList<FoodItem>) left.clone());
	}

	private static void solve(final int[] required_nutrition, final ArrayList<FoodItem> grocery) {
		// clear the previous solution list in case
		// this is a new test case
		successful_permutations.clear();
		// initial recursive call
		permute(required_nutrition, new ArrayList<FoodItem>(), grocery);

		// if theres no possible solutions
		if (successful_permutations.isEmpty())
			System.out.println("not possible");
		else {
			// otherwise, loop through and get
			// the cheapest cost
			double cheapest_cost = Double.MAX_VALUE;
			for (ArrayList<FoodItem> perm: successful_permutations) {
				double total_cost = 0.0;
				for (FoodItem food: perm)
					total_cost += food.price;
				cheapest_cost = Math.min(total_cost, cheapest_cost);
			}
			System.out.printf("%.2f\n", cheapest_cost);
		}
	}

	// Main for input
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("calories.dat"));
		// looping through test cases
		int test_cases = in.nextInt();
		for (int test_case = 0; test_case < test_cases; test_case++) {
			int line_count = in.nextInt();
			// storing any nutrition information in
			// an array so that its really easy to
			// keep track of that information
			int[] required_nutrition = new int[] {
				in.nextInt(), // Protein
				in.nextInt(), // Iron
				in.nextInt()  // Starch
			};
			ArrayList<FoodItem> grocery = new ArrayList<>();
			for (int line_index = 0; line_index < line_count; line_index++)
				grocery.add(new FoodItem(in.nextDouble(), new int[] {
					in.nextInt(), // Protein
					in.nextInt(), // Iron
					in.nextInt()  // Starch
				}));

			solve(required_nutrition, grocery);
		}
	}
}