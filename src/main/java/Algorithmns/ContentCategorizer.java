package Algorithmns;

import java.util.*;
import java.util.stream.Collectors;

public class ContentCategorizer {
    private static Map<String, Set<String>> categoryKeywords = new HashMap<>();

    static {
        categoryKeywords.put("Sports", new HashSet<>(Arrays.asList(
                "team", "game", "player", "score", "tournament", "championship", "coach",
                "athletic", "sports", "football", "basketball", "soccer", "baseball",
                "olympics", "match", "racing", "fitness", "league", "victory", "competition"
        )));
        categoryKeywords.put("Entertainment", new HashSet<>(Arrays.asList(
                "movie", "film", "music", "concert", "celebrity", "actor", "actress",
                "director", "show", "television", "series", "performance", "entertainment",
                "dance", "theater", "festival", "album", "award", "stage", "streaming"
        )));
        categoryKeywords.put("Politics", new HashSet<>(Arrays.asList(
                "government", "election", "president", "congress", "senate", "democrat",
                "republican", "parliament", "political", "policy", "legislation", "vote",
                "campaign", "candidate", "minister", "democracy", "law", "party", "bill",
                "constitution"
        )));
        categoryKeywords.put("Science", new HashSet<>(Arrays.asList(
                "research", "scientist", "study", "experiment", "discovery", "laboratory",
                "physics", "chemistry", "biology", "astronomy", "space", "theory",
                "scientific", "molecule", "atom", "quantum", "evolution", "hypothesis",
                "innovation", "data"
        )));
        categoryKeywords.put("Health", new HashSet<>(Arrays.asList(
                "medical", "doctor", "hospital", "patient", "treatment", "disease",
                "health", "medicine", "vaccine", "surgery", "wellness", "therapy",
                "diagnosis", "clinical", "symptoms", "pharmaceutical", "diet",
                "nutrition", "mental", "healthcare"
        )));
        categoryKeywords.put("Technology", new HashSet<>(Arrays.asList(
                "software", "hardware", "computer", "internet", "digital", "technology",
                "app", "programming", "code", "algorithm", "artificial", "intelligence",
                "device", "mobile", "network", "cyber", "innovation", "startup",
                "cloud", "data"
        )));
        categoryKeywords.put("Fashion", new HashSet<>(Arrays.asList(
                "fashion", "style", "clothing", "designer", "trend", "collection",
                "model", "runway", "brand", "wear", "dress", "accessory", "luxury",
                "textile", "fabric", "costume", "boutique", "fashion week", "couture",
                "apparel"
        )));
        categoryKeywords.put("Finance", new HashSet<>(Arrays.asList(
                "market", "stock", "investment", "bank", "financial", "economy",
                "trade", "business", "money", "fund", "currency", "profit",
                "revenue", "asset", "finance", "credit", "debt", "investor",
                "market", "share"
        )));
    }

    public static String categorizeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "Other";
        }

        content = content.toLowerCase();
        String[] words = content.split("\\W+");

        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "and", "or", "but", "in", "on", "at", "to",
                "for", "of", "with", "by"
        ));

        Map<String, Integer> wordFrequency = Arrays.stream(words)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        List<Map.Entry<String, Double>> categoryScores = new ArrayList<>();

        for (Map.Entry<String, Set<String>> category : categoryKeywords.entrySet()) {
            double score = 0.0;
            Set<String> keywords = category.getValue();

            for (Map.Entry<String, Integer> word : wordFrequency.entrySet()) {
                if (keywords.contains(word.getKey())) {
                    score += word.getValue() * 1.0;
                }
            }

            score = score / keywords.size();
            categoryScores.add(new AbstractMap.SimpleEntry<>(category.getKey(), score));
        }

        categoryScores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        double threshold = 0.1;
        if (!categoryScores.isEmpty() && categoryScores.get(0).getValue() > threshold) {
            return categoryScores.get(0).getKey();
        }

        return "Other";
    }
}
