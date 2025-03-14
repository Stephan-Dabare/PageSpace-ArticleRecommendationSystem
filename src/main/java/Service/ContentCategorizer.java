package Service;

import java.util.*;
import java.util.stream.Collectors;

public class ContentCategorizer {
    // Map to store category keywords.
    private static Map<String, Set<String>> categoryKeywords = new HashMap<>();

    // Static block to initialize category keywords.
    static {
        categoryKeywords.put("Sports", new HashSet<>(Arrays.asList(
                "team", "game", "player", "score", "tournament", "championship", "coach",
                "athletic", "sports", "football", "basketball", "soccer", "baseball",
                "olympics", "match", "racing", "fitness", "league", "victory", "competition",
                "team", "game", "player", "score", "tournament", "championship", "coach",
                "athletic", "sports", "football", "basketball", "soccer", "baseball",
                "olympics", "match", "racing", "fitness", "league", "victory", "competition",
                "athlete", "track", "field", "hockey", "tennis", "golf", "marathon",
                "boxing", "cricket", "swimming", "cycling", "volleyball", "gymnastics",
                "wrestling", "surfing", "skating", "skiing", "snowboarding", "climbing",
                "hiking", "camping", "fishing", "hunting", "boating", "sailing", "surfing",
                "diving", "rafting", "kayaking", "canoeing", "rowing", "paddling", "angling"
        )));
        categoryKeywords.put("Entertainment", new HashSet<>(Arrays.asList(
                "movie", "film", "music", "concert", "celebrity", "actor", "actress",
                "director", "show", "television", "series", "performance", "entertainment",
                "dance", "theater", "festival", "album", "award", "stage", "streaming",
                "comedy", "drama", "horror", "thriller", "romance", "action", "documentary",
                "animation", "musical", "pop", "rock", "hip hop", "rap", "country", "jazz",
                "classical", "metal", "reggae", "blues", "folk", "indie", "alternative",
                "pop culture", "celebrity", "gossip", "red carpet", "fashion", "style"
        )));
        categoryKeywords.put("Politics", new HashSet<>(Arrays.asList(
                "government", "election", "president", "congress", "senate", "democrat",
                "republican", "parliament", "political", "policy", "legislation", "vote",
                "campaign", "candidate", "minister", "democracy", "law", "party", "bill",
                "constitution", "debate", "reform", "lobby", "corruption", "scandal",
                "foreign", "affairs", "diplomacy", "embassy", "sanctions", "treaty",
                "negotiation", "summit", "alliance", "coalition", "opposition", "protest",
                "demonstration", "march", "strike", "riot", "revolution", "coup", "uprising"
        )));
        categoryKeywords.put("Science", new HashSet<>(Arrays.asList(
                "research", "scientist", "study", "experiment", "discovery", "laboratory",
                "physics", "chemistry", "biology", "astronomy", "space", "theory",
                "scientific", "molecule", "atom", "quantum", "evolution", "hypothesis",
                "innovation", "data", "analysis", "technology", "engineering", "mathematics",
                "computer", "programming", "algorithm", "artificial", "intelligence", "cars",
                "robots", "automation", "machine", "learning", "neural", "network", "genetics"
        )));
        categoryKeywords.put("Health", new HashSet<>(Arrays.asList(
                "medical", "doctor", "hospital", "patient", "treatment", "disease",
                "health", "medicine", "vaccine", "surgery", "wellness", "therapy",
                "diagnosis", "clinical", "symptoms", "pharmaceutical", "diet",
                "nutrition", "mental", "healthcare", "fitness", "exercise", "lifestyle",
                "wellbeing", "prevention", "recovery", "rehabilitation", "addiction",
                "disorder", "illness", "injury", "cancer", "diabetes", "heart", "stroke",
                "obesity", "depression", "anxiety", "stress", "sleep", "pain", "fatigue",
                "allergy", "infection", "virus", "bacteria", "pandemic", "epidemic", "outbreak",
                "vaccination", "immunity", "quarantine", "isolation", "social distancing",
                "mask", "hygiene", "sanitation", "cleanliness", "exercise", "nutrition",
                "diet", "wellness", "mental health", "fitness", "lifestyle", "wellbeing",
                "prevention", "recovery", "rehabilitation", "addiction", "disorder", "illness",
                "injury", "cancer", "diabetes", "heart", "stroke", "obesity", "depression",
                "anxiety", "stress", "sleep", "pain", "fatigue", "allergy", "infection"
        )));
        categoryKeywords.put("Technology", new HashSet<>(Arrays.asList(
                "software", "hardware", "computer", "internet", "digital", "technology",
                "app", "programming", "code", "algorithm", "artificial", "intelligence",
                "device", "mobile", "network", "cyber", "innovation", "startup",
                "cloud", "data", "big data", "analytics", "machine", "learning",
                "automation", "robotics", "blockchain", "cryptocurrency", "bitcoin",
                "ethereum", "smart", "gadget", "electronics", "gaming", "virtual",
                "reality", "augmented", "reality", "wearable", "tech", "social media",
                "platform", "website", "e-commerce", "online", "digital", "technology",
                "app", "programming", "code", "algorithm", "artificial", "intelligence",
                "device", "mobile", "network", "cyber", "innovation", "startup", "cloud"

        )));
        categoryKeywords.put("Fashion", new HashSet<>(Arrays.asList(
                "fashion", "style", "clothing", "designer", "trend", "collection",
                "model", "runway", "brand", "wear", "dress", "accessory", "luxury",
                "textile", "fabric", "costume", "boutique", "fashion week", "couture",
                "apparel", "garment", "footwear", "jewelry", "handbag", "sunglasses",
                "watch", "hat", "scarf", "glove", "belt", "socks", "tie", "suit",
                "dress", "skirt", "blouse", "shirt", "pants", "jeans", "shorts",
                "jacket", "coat", "sweater", "hoodie", "sweatshirt", "t-shirt", "polo",
                "swimsuit", "lingerie", "pajamas", "athleisure", "streetwear", "vintage",
                "bohemian", "hippie", "goth", "punk", "emo", "grunge", "preppy", "hipster",
                "chic", "elegant", "classy", "casual", "formal", "business", "sporty",
                "athletic", "urban", "rural", "beach", "resort", "mountain"
        )));
        categoryKeywords.put("Finance", new HashSet<>(Arrays.asList(
                "market", "stock", "investment", "bank", "financial", "economy",
                "trade", "business", "money", "fund", "currency", "profit",
                "revenue", "asset", "finance", "credit", "debt", "investor",
                "market", "share", "equity", "bond", "commodity", "derivative",
                "hedge", "fund", "portfolio", "diversification", "liquidity",
                "leverage", "margin", "interest", "rate", "inflation", "deflation",
                "recession", "depression", "boom", "bust", "bubble", "crash",
                "bankruptcy", "default", "foreclosure", "merger", "acquisition",
                "takeover", "ipo", "venture", "capital", "angel", "investor",
                "crowdfunding", "cryptocurrency", "bitcoin", "ethereum", "blockchain",
                "wallet", "exchange", "trading", "mining", "staking", "defi", "nft"
        )));
    }

    // Method to categorize content based on keywords.
    public static String categorizeContent(String content) {
        // Check if content is null or empty.
        if (content == null || content.trim().isEmpty()) {
            return "Other";
        }

        // Convert content to lowercase and split into words.
        content = content.toLowerCase();
        String[] words = content.split("\\W+");

        // Remove common stop words to filter out.
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with", "by"
        ));

        // Count frequency of words excluding stop words.
        Map<String, Integer> wordFrequency = Arrays.stream(words)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));


        // Map to store category scores.
        Map<String, Double> categoryScores = new HashMap<>();

        // Loop through each category and calculate score.
        for (Map.Entry<String, Set<String>> category : categoryKeywords.entrySet()) {
            // Get category name and keywords.
            String categoryName = category.getKey();
            // Get keywords for the category.
            Set<String> keywords = category.getValue();
            // Initialize match count.
            int matchCount = 0;

            // Loop through each word in the content.
            for (String word : wordFrequency.keySet()) {
                // Check if word is a keyword for the category.
                if (keywords.contains(word)) {
                    matchCount += wordFrequency.get(word);
                }
            }

            // Calculate score for the category.
            if (matchCount > 0) {
                categoryScores.put(categoryName, (double) matchCount / keywords.size());
            }
        }

        // Find category with max score.
        return categoryScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .filter(entry -> entry.getValue() > 0.05)
                .map(Map.Entry::getKey)
                .orElse("Other");
    }

}

