package com.wh.tools;

import com.intellij.core.CoreBundle;
import com.intellij.model.ModelBranchUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.util.io.BufferExposingByteArrayInputStream;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.util.io.OSAgnosticPathUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.StringUtilRt;
import com.intellij.openapi.vfs.*;
import com.intellij.util.PathUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.DistinctRootsCollection;
import com.intellij.util.io.URLUtil;
import com.intellij.util.text.CharArrayUtil;
import org.apache.xml.resolver.helpers.FileURL;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class VfsUtilCore {

   /**
     * @return a {@link File} for a given {@link VirtualFile},
     * the created file may not exist or may not make sense.
     * <br />
     * It could be better and more reliably to use the {@link VirtualFile#toNioPath()}
     * <br />
     * @implNote it takes the part after ://, trims !/ at the end and turns it into a File path
     *
     * @see VirtualFile#toNioPath()
     */
    public static @NotNull File virtualToIoFile(@NotNull VirtualFile file) {
        return file.toNioPath().toFile();
//        return new File(PathUtil.toPresentableUrl(file.getUrl()));
    }

    public static @Nullable VirtualFile fileToVirtualFile(@NotNull File file) throws Exception {
        Path path = Path.of(file.toURI());
        return VirtualFileManager.getInstance().findFileByNioPath(path);
    }
  }

