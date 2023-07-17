// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.wh.language.js;

import com.intellij.lang.Language;

public class JSLanguage extends Language {

  public static final JSLanguage INSTANCE = new JSLanguage();

  private JSLanguage() {
    super("JS");
  }

}
