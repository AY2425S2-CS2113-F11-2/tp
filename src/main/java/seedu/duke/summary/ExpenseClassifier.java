package seedu.duke.summary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.duke.expense.Expense;
import seedu.duke.storage.DataStorage;

public class ExpenseClassifier {
    private Map<Categories, List<String>> categoryKeywords;

    public ExpenseClassifier() {
        initializeCategoryKeywords();
    }

    private void initializeCategoryKeywords() {
        categoryKeywords = new HashMap<>();
        categoryKeywords.put(Categories.Food, Arrays.asList(
                "food", "restaurant", "groceries", "dining", "snack", "lunch", "dinner",
                "breakfast", "brunch", "cafe", "bistro", "barbecue", "fast food", "dessert", "bakery"));
        categoryKeywords.put(Categories.Shopping, Arrays.asList(
                "clothes", "electronics", "mall", "fashion", "accessory",
                "shoes", "jewelry", "home decor", "furniture", "beauty", "cosmetics", "boutique", "outlet"));
        categoryKeywords.put(Categories.Travel, Arrays.asList(
                "flight", "hotel", "transport", "train", "taxi",
                "bus", "cruise", "rental car", "itinerary", "tour", "excursion", "travel agent", "reservation",
                "airport", "visa", "backpacker"));
        categoryKeywords.put(Categories.Entertainment, Arrays.asList(
                "movie", "concert", "game", "party", "music",
                "theater", "comedy", "festival", "show", "art", "exhibition", "opera", "dance", "amusement",
                 "performance", "arcade"));
    }

    public Map<Categories, Double> calculateCategoryTotals(List<Expense> expenses) {
        Map<Categories, Double> categoryTotals = new HashMap<>();
        for (Categories category : Categories.values()) {
            categoryTotals.put(category, 0.0);
        }

        for (Expense expense : expenses) {
            Categories category = classifyExpense(expense);
            categoryTotals.put(category, categoryTotals.get(category) + expense.getAmount());
        }

        return categoryTotals;
    }

    public Map<Categories, Integer> calculateCategoryCounts(List<Expense> expenses) {
        Map<Categories, Integer> categoryCounts = new HashMap<>();
        for (Categories category : Categories.values()) {
            categoryCounts.put(category, 0);
        }

        for (Expense expense : expenses) {
            Categories category = classifyExpense(expense);
            categoryCounts.put(category, categoryCounts.get(category) + 1);
        }

        return categoryCounts;
    }

    private Categories classifyExpense(Expense expense) {
        String description = expense.getDescription().toLowerCase();

        for (Map.Entry<Categories, List<String>> entry : categoryKeywords.entrySet()) {
            Categories category = entry.getKey();
            List<String> keywords = entry.getValue();

            for (String keyword : keywords) {
                if (description.contains(keyword)) {
                    return category;
                }
            }
        }

        return Categories.Miscellaneous;
    }

    public void calculateCategoryProportions() {
        List<Expense> expenses = DataStorage.loadExpenses();
        Map<Categories, Integer> categoryCount = new HashMap<>();

        for (Categories category : Categories.values()) {
            categoryCount.put(category, 0);
        }

        for (Expense expense : expenses) {
            String title = expense.getTitle();
            String description = expense.getDescription().toLowerCase();
            boolean categorized = false;

            for (Map.Entry<Categories, List<String>> entry : categoryKeywords.entrySet()) {
                for (String keyword : entry.getValue()) {
                    if (title.contains(keyword) || description.contains(keyword)) {
                        categoryCount.put(entry.getKey(), categoryCount.get(entry.getKey()) + 1);
                        categorized = true;
                        break;
                    }
                }
                if (categorized) {
                    break;
                }
            }

            if (!categorized) {
                categoryCount.put(Categories.Miscellaneous, categoryCount.get(Categories.Miscellaneous) + 1);
            }
        }

        int totalExpenses = expenses.size();
        if (totalExpenses == 0) {
            System.out.println("No expenses to calculate proportions.");
            return;
        }

        System.out.println("Category Proportions (" + expenses.size() + "):");
        for (Map.Entry<Categories, Integer> entry : categoryCount.entrySet()) {
            double percentage = (entry.getValue() / (double) totalExpenses) * 100;
            System.out.printf("%s: %.2f%%\n", entry.getKey(), percentage);
        }

        // Add data visualization stuff here
    }
}
