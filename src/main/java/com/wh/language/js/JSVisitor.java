// This is a generated file. Not intended for manual editing.
package com.wh.language.js;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class JSVisitor extends PsiElementVisitor {

  public void visitProperty(@NotNull JSProperty o) {
    visitNamedElement(o);
  }

  public void visitNamedElement(@NotNull JSNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
