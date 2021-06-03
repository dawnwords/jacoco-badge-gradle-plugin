package com.github.dawnwords.jacoco.badge;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ReadMeUpdater {

  private final JacocoBadgeGenerateSetting setting;
  private final JacocoResultParser parser;

  private static final Pattern BADGE_PATTERN = Pattern.compile("^!\\[([^]]+)]\\(([^)]+)\\)$");

  ReadMeUpdater(JacocoBadgeGenerateSetting setting) {
    this(setting, new JacocoResultParser(setting));
  }

  ReadMeUpdater(JacocoBadgeGenerateSetting setting, JacocoResultParser parser) {
    this.setting = setting;
    this.parser = parser;
  }

  void updateReadme() throws Exception {
    final Map<String, JacocoBadgePercentageResult> results = parser.getJacocoResults();

    results.values().forEach(JacocoBadgePercentageResult::verifyLimit);

    final Path readmePath = Paths.get(setting.getReadmePath());
    Files.write(readmePath, Files.readAllLines(readmePath).stream()
        .map(l -> {
          Matcher matcher = BADGE_PATTERN.matcher(l);
          if (matcher.find()) {
            String type = matcher.group(1);
            JacocoBadgePercentageResult result = results.get(type);
            if (result != null) {
              return matcher.replaceFirst(String.format("![$1](%s)", result.badgeUrl()));
            }
          }
          return l;
        })
        .collect(Collectors.toList()));
  }

}
