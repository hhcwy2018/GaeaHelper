// This is a generated file. Not intended for manual editing.
package com.wh.language.js;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class JSPropertyImpl extends JSNamedElementImpl implements JSProperty {

  public JSPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JSVisitor visitor) {
    visitor.visitProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JSVisitor) accept((JSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public String getKey() {
    return JSPsiImplUtil.getKey(this);
  }

  @Override
  public String getValue() {
    return JSPsiImplUtil.getValue(this);
  }

  @Override
  public String getName() {
    return JSPsiImplUtil.getName(this);
  }

  @Override
  public PsiElement setName(@NotNull String newName) {
    return JSPsiImplUtil.setName(this, newName);
  }

  @Override
  public PsiElement getNameIdentifier() {
    return JSPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return JSPsiImplUtil.getPresentation(this);
  }

}
