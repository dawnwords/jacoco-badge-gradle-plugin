package com.github.dawnwords.jacoco.badge;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JacocoBadgePluginTest {

  @Mock
  private Project project;
  @Mock
  private ExtensionContainer extension;

  @BeforeMethod
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
    when(project.getExtensions()).thenReturn(extension);
  }

  @Test
  public void testApply() {
    new JacocoBadgePlugin().apply(project);
    verify(extension).create("jacocoBadgeGenSetting", JacocoBadgeGenerateSetting.class);

    verify(project).task(argThat(map -> {
      assertEquals(map.get("type"), JacocoBadgeGenerate.class);
      assertEquals(map.get("description"), JacocoBadgeGenerate.DESCRIPTION);
      assertEquals(map.get("group"), "jacoco-badge");
      return true;
    }), eq(JacocoBadgeGenerate.TASK_NAME));
  }
}
