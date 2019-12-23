package com.github.dawnwords.jacoco.badge;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap;
import org.gradle.internal.impldep.com.google.common.io.Files;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JacocoBadgeGenerateSettingTest {

  @Mock
  private Project project;

  @BeforeMethod
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSetterGetter() {
    JacocoBadgeGenerateSetting setting = new JacocoBadgeGenerateSetting();
    setting.setJacocoReportPath("jacoco-report");
    setting.setReadmePath("readme");
    setting.setLimit(ImmutableMap.of("branch", 90));
    assertEquals(setting.getJacocoReportPath(), "jacoco-report");
    assertEquals(setting.getReadmePath(), "readme");
    assertEquals(setting.getLimit(), ImmutableMap.of("branch", 90));
  }

  @Test
  public void testSetDefault() {
    new JacocoBadgeGenerateSetting()
        .setJacocoReportPath("jacoco-report")
        .setReadmePath("readme")
        .setDefault(project);
  }

  @Test
  public void testUseDefaultJacocoPath() throws IOException {
    File buildDir = Files.createTempDir();
    File reportPath = new File(buildDir, "reports/jacoco/test/jacocoTestReport.xml");
    reportPath.getParentFile().mkdirs();
    reportPath.createNewFile();
    when(project.getBuildDir()).thenReturn(buildDir);
    assertEquals(reportPath.getAbsolutePath(), new JacocoBadgeGenerateSetting()
        .setReadmePath("readme")
        .setDefault(project)
        .getJacocoReportPath());
  }

  @Test
  public void testUseDefaultReadme() throws IOException {
    File projectDir = Files.createTempDir();
    File readmePath = new File(projectDir, "README.md");
    readmePath.getParentFile().mkdirs();
    readmePath.createNewFile();
    when(project.getProjectDir()).thenReturn(projectDir);
    assertEquals(readmePath.getAbsolutePath(), new JacocoBadgeGenerateSetting()
        .setJacocoReportPath("jacoco-report")
        .setDefault(project)
        .getReadmePath());
  }

  @Test(expectedExceptions = GradleException.class, expectedExceptionsMessageRegExp =
      "no default jacocoTestReport found, please specified it in jacocoBadgeGenSetting")
  public void testCheckOnNoJacocoReportPath() {
    when(project.getBuildDir()).thenReturn(Files.createTempDir());
    new JacocoBadgeGenerateSetting().setDefault(project);
  }

  @Test(expectedExceptions = GradleException.class, expectedExceptionsMessageRegExp =
      "no default readmePath found, please specified it in jacocoBadgeGenSetting")
  public void testCheckOnNoReadMePath() {
    when(project.getProjectDir()).thenReturn(Files.createTempDir());
    new JacocoBadgeGenerateSetting().setJacocoReportPath("jacoco-report").setDefault(project);
  }

  @Test(expectedExceptions = GradleException.class, expectedExceptionsMessageRegExp =
      "no default readmePath found, please specified it in jacocoBadgeGenSetting")
  public void testCheckOnEmptyReadMePath() {
    when(project.getProjectDir()).thenReturn(Files.createTempDir());
    new JacocoBadgeGenerateSetting().setJacocoReportPath("jacoco-report").setReadmePath("")
        .setDefault(project);
  }

}
