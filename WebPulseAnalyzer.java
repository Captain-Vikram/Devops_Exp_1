
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The "Web Pulse Analyzer"
 *
 * This program demonstrates a Polyglot workflow: 1. Java orchestrates a Python
 * subprocess to scrape a website. 2. Python handles the heavy lifting of
 * browser automation (Playwright). 3. Java ingests the raw HTML, parses it, and
 * performs frequency analysis. 4. Results are visualized as an ASCII bar chart
 * in the console.
 */
public class WebPulseAnalyzer {

    // You can change this default URL or pass a URL as a command line argument
    private static final String DEFAULT_URL = "https://en.wikipedia.org/wiki/Artificial_intelligence";

    public static void main(String[] args) {
        String url = (args.length > 0) ? args[0] : DEFAULT_URL;

        System.out.println("🔮 Java Crystal Ball: Analyzing the soul of the web...");
        System.out.println("   Target: " + url + "\n");

        try {
            // STEP 1: Summon the Python Script (The Scout)
            runPythonScraper(url);

            // STEP 2: Ingest the Data
            Path htmlPath = Paths.get("captured_data", "page_source.html");
            if (!Files.exists(htmlPath)) {
                System.err.println("❌ Error: captured_data/page_source.html was not generated.");
                return;
            }

            System.out.println("   [Java] Digesting " + Files.size(htmlPath) + " bytes of raw HTML...");
            String content = Files.readString(htmlPath);

            // STEP 3: Analyze the "Pulse" (The Brain)
            Map<String, Integer> wordCounts = extractAndCountKeywords(content);

            // STEP 4: Visualize (The Artist)
            printAsciiChart(wordCounts);

        } catch (Exception e) {
            System.err.println("💥 An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runPythonScraper(String url) throws IOException, InterruptedException {
        System.out.println("   [Java] Launching Python scraper subprocess...");

        // We assume 'python' is in the system PATH. On some systems this might be 'python3'
        ProcessBuilder pb = new ProcessBuilder("python", "Test.py", url);
        pb.redirectErrorStream(true); // Merge stderr into stdout so we can see everything
        Process process = pb.start();

        // Read output from Python in real-time to keep user entertained
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Formatting the python output to distinguish it
                System.out.println("   \u001B[32m[Python]\u001B[0m " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script failed with exit code " + exitCode);
        }
        System.out.println("   [Java] Python subprocess finished successfully.\n");
    }

    private static Map<String, Integer> extractAndCountKeywords(String html) {
        // 1. Rudimentary HTML stripping (Regex is wild, but sufficient for this demo)
        String text = html.replaceAll("(?s)<script.*?>.*?</script>", " "); // Kill scripts
        text = text.replaceAll("(?s)<style.*?>.*?</style>", " ");   // Kill styles
        text = text.replaceAll("<[^>]+>", " ");                     // Kill tags
        text = text.replace("&nbsp;", " ").replace("&amp;", "&");   // decoding basics

        // 2. Tokenize and Normalize
        String[] words = text.split("[^a-zA-Z]+"); // Split on anything not a letter

        // 3. Define noise words to ignore
        Set<String> stopWords = Set.of(
                "the", "and", "of", "to", "a", "in", "is", "that", "for", "it", "as", "was", "with", "on",
                "by", "at", "an", "be", "this", "which", "or", "from", "but", "not", "are", "have", "has", "he", "she",
                "wikipedia", "edit", "retrieved", "archived", "original", "search", "articles", "article",
                "page", "jump", "main", "content", "about", "contact", "login", "create", "account", "all", "can", "if"
        );

        Map<String, Integer> frequency = new HashMap<>();

        for (String w : words) {
            String lower = w.toLowerCase();
            // Filter: Must be longer than 3 chars and not a stop word
            if (lower.length() > 3 && !stopWords.contains(lower)) {
                frequency.put(lower, frequency.getOrDefault(lower, 0) + 1);
            }
        }

        // 4. Sort by frequency desc and take Top 10
        return frequency.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort Descending
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private static void printAsciiChart(Map<String, Integer> data) {
        System.out.println("\n📊 WEB PULSE REPORT: Top Keywords");
        System.out.println("=============================================");

        if (data.isEmpty()) {
            System.out.println("   (No significant words found - is the page empty?)");
            return;
        }

        int maxVal = data.values().stream().max(Integer::compare).orElse(1);
        final int MAX_BAR_WIDTH = 40;

        // ANSI Color codes for terminal bling
        String RESET = "\u001B[0m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String word = String.format("%-15s", entry.getKey()); // Pad word to 15 chars
            int count = entry.getValue();

            // Calculate relative bar length
            int barLen = (int) ((double) count / maxVal * MAX_BAR_WIDTH);
            String bar = "█".repeat(barLen);

            System.out.println("   " + CYAN + word + RESET + " | " + YELLOW + bar + " " + count + RESET);
        }
        System.out.println("=============================================");
    }
}
