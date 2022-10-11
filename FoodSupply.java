import java.util.*;
import java.io.*;

public class FoodSupply {
	private static ArrayList<Integer> best_combination;

	private static int total(ArrayList<Integer> barrels) {
		return barrels.stream().mapToInt(Integer::intValue).sum();
	}

	private static void permute_combination(final int min_calories, ArrayList<Integer> selected, ArrayList<Integer> barrels) {
		if (total(selected) >= min_calories) {
			if (best_combination == null || total(selected) < total(best_combination))
				best_combination = selected;
			return;
		}

		for (int i = 0; i < barrels.size(); i++) {
			Integer next_barrel = barrels.get(i);

			ArrayList<Integer> clone = (ArrayList<Integer>) barrels.clone();
			clone.remove(next_barrel);
			permute_combination(min_calories, (ArrayList<Integer>) selected.clone(), clone);

			selected.add(next_barrel);
			permute_combination(min_calories, (ArrayList<Integer>) selected.clone(), clone);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("food_supply.dat"));
		while (in.hasNextLine()) {
			int[] tokens = Arrays.stream(in.nextLine().split(",")).mapToInt(Integer::valueOf).toArray();
			ArrayList<Integer> barrels = new ArrayList<>();
			final int min_calories = tokens[0];
			for (int i = 1; i < tokens.length; i++)
				barrels.add(tokens[i]);
			
			int days = 0;
			while (!barrels.isEmpty()) {
				permute_combination(min_calories, new ArrayList<Integer>(), barrels);
				if (best_combination != null) {
					for (Integer n: best_combination)
						barrels.remove(n);
					days++;
					// System.out.println("Day " + days + ": " + best_combination);
					// System.out.println("Remaining: " + barrels);
				} else break;
				best_combination = null;
			}
			System.out.println(days);
		}
	}
}