// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.wh.language.js;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class JSElementFactory {

  public static JSProperty createProperty(Project project, String name) {
    final JSFile file = createFile(project, name);
    return (JSProperty) file.getFirstChild();
  }

  public static JSFile createFile(Project project, String text) {
    String name = "dummy.simple";
    return (JSFile) PsiFileFactory.getInstance(project).createFileFromText(name, JSFileType.INSTANCE, text);
  }

  public static JSProperty createProperty(Project project, String name, String value) {
    final JSFile file = createFile(project, name + " = " + value);
    return (JSProperty) file.getFirstChild();
  }

  public static PsiElement createCRLF(Project project) {
    final JSFile file = createFile(project, "\n");
    return file.getFirstChild();
  }

}
