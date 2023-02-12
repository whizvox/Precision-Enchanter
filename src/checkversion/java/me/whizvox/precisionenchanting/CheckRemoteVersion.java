package me.whizvox.precisionenchanting;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class that compares the versions of the local and remote Gradle projects.
 */
public class CheckRemoteVersion {

  private static final String
      REMOTE_URL = "https://raw.githubusercontent.com/whizvox/Precision-Enchanter/1.18.2/build.gradle";

  private static final Pattern VERSION_LINE_PATTERN = Pattern.compile("^version = '(.*)'$");

  private static String extractProjectVersion(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line;
    while ((line = reader.readLine()) != null) {
      Matcher matcher = VERSION_LINE_PATTERN.matcher(line);
      if (matcher.matches()) {
        return matcher.group(1);
      }
    }
    return null;
  }

  void checkRemoteVersion(boolean throwIfException, boolean throwIfMatch) throws IOException {
    String remoteVersion;
    String localVersion;
    try (InputStream remoteScript = new URL(REMOTE_URL).openStream();
         InputStream localScript = Files.newInputStream(Paths.get("build.gradle"))) {
      remoteVersion = extractProjectVersion(remoteScript);
      localVersion = extractProjectVersion(localScript);
    } catch (IOException e) {
      if (throwIfException) {
        throw e;
      }
      System.err.println("Could not retrieve remote file");
      e.printStackTrace();
      return;
    }
    System.out.println("[CheckRemoteVersion] Local: '" + localVersion + "', Remote: '" + remoteVersion + "'");
    boolean match = Objects.equals(Objects.requireNonNull(localVersion), remoteVersion);
    if (throwIfMatch) {
      if (match) {
        throw new AssertionError("Local and remote versions match");
      }
    } else {
      if (match) {
        System.out.println("[CheckRemoteVersion] Both local and remote versions match!");
      }
    }
  }

  /**
   * Compare the versions of the local and remote Gradle projects. Ideally, this should be automatically executed
   * before a Git commit is made so that each commit is guaranteed to correspond to a unique project version.
   * @param args <code>-e</code> : prevent any internal exceptions from being thrown, and instead will just be logged
   *             to stderr. This does not apply to any potential exceptions that could be thrown when parsing CLI args.
   *             <br>
   *             <code>-a</code> : skip the assertion check on the local and remote versions
   * @throws ParseException If something went wrong parsing the CLI arguments, which shouldn't happen
   * @throws IOException If the <code>-e</code> option was not used and something went wrong trying to either retrieve
   * the contents of the remote file or read the contents of either the local or remote files
   * @throws NullPointerException If the version of the local project could not be read
   * @throws AssertionError If the <code>-a</code> option was not used and the versions of the local and remote
   * projects match.
   */
  public static void main(String[] args) throws Exception {
    Options options = new Options();
    options.addOption("e", false, "Do not rethrow any encountered exceptions");
    options.addOption("a", false, "Skip the assertion check on the local and remote versions");
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    // ignore until repo push
    //new CheckRemoteVersion().checkRemoteVersion(!cmd.hasOption("e"), !cmd.hasOption("a"));
  }

}
