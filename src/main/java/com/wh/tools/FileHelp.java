package com.wh.tools;

import com.intellij.openapi.project.ProjectManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public abstract class FileHelp {

    public interface CopyFilter {
        boolean filter(File srcFile, File destFile);
    }

    public interface OverWrite {
        boolean before(File sourceFile, File destFile) throws Exception;

        void after(File sourceFile, File destFile) throws Exception;
    }

    public interface IDelDir {
        boolean prepareDeleteFile(File file);

        boolean deletedFile(File file, boolean isok);
    }

    public interface ReplaceCopyFilter extends CopyFilter {
        boolean filter(File srcFile, AtomicReference<File> destFile);

        default boolean filter(File srcFile, File destFile) {
            return true;
        }
    }

    public static String removeExt(String name) {
        int index = name.indexOf(".");
        if (index != -1)
            name = name.substring(0, index);
        return name;
    }

    public static String changeExt(String filename, String newext) {
        for (int i = filename.length() - 1; i >= 0; i--) {
            if (filename.charAt(i) == '.') {
                filename = filename.substring(0, i + 1);
                return filename + newext;
            }

        }
        return filename + "." + newext;
    }

    public static String getExt(String filename) {
        for (int i = filename.length() - 1; i >= 0; i--) {
            if (filename.charAt(i) == '.') {
                String ext = filename.substring(i + 1);
                return ext;
            }

        }
        return null;
    }

    public static File getRootDir() {
        return new File(ProjectManager.getInstance().getOpenProjects()[0].getBasePath());
    }

    public static File getFile(String... names) {
        return getFile(getRootDir(), names);
    }

    public static File getFile(File basePath, String... names) {
        if (names == null || names.length == 0)
            return basePath;

        for (String name : names) {
            if (name == null || name.isEmpty())
                continue;
            basePath = new File(basePath, name);
        }
        return basePath;
    }

    public static void delDir(File dir) throws Exception {
        delDir(dir, new IDelDir() {

            @Override
            public boolean prepareDeleteFile(File file) {
                return true;
            }

            @Override
            public boolean deletedFile(File file, boolean isok) {
                return isok;
            }
        });
    }

    protected static void fireDir(File file, IDelDir iDelDir) throws Exception {
        if (iDelDir.prepareDeleteFile(file)) {
            if (!file.delete()) {
                if (!iDelDir.deletedFile(file, false))
                    throw new IOException("delete[" + file.getAbsolutePath() + "] failed!");
            } else {
                if (!iDelDir.deletedFile(file, true))
                    throw new Exception("user abort operation!");
            }
        } else {
            throw new Exception("user abort operation!");
        }
    }

    public static void delDir(File dir, IDelDir iDelDir) throws Exception {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null)
            return;

        if (!iDelDir.prepareDeleteFile(dir)) {
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                fireDir(file, iDelDir);
            } else if (file.isDirectory()) {
                delDir(file);
            }
        }

        fireDir(dir, iDelDir);
    }

    public static void copyFileTo(File srcFile, File destFile) throws IOException {
        copyFileTo(srcFile, destFile, null);
    }

    public static void copyFileTo(File srcFile, File destFile, OverWrite overWrite) throws IOException {
        if (srcFile.isDirectory() || destFile.isDirectory())
            throw new IOException("源或者目的不是文件！");

        try {
            if (overWrite != null && !overWrite.before(srcFile, destFile) && destFile.exists())
                return;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        try (FileInputStream fis = new FileInputStream(srcFile);
             FileOutputStream fos = new FileOutputStream(destFile);) {
            int readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = fis.read(buf)) != -1) {
                fos.write(buf, 0, readLen);
            }
            fos.flush();
        }

        if (overWrite != null)
            try {
                overWrite.after(srcFile, destFile);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException(e);
            }

    }

    public static void copyFilesTo(File srcDir, File destDir) throws IOException {
        copyFilesTo(srcDir, destDir, null, null);
    }

    public static void copyFilesTo(File srcDir, File destDir, CopyFilter copyFilter) throws IOException {
        copyFilesTo(srcDir, destDir, copyFilter, null);
    }

    public static void copyFilesTo(File srcDir, File destDir, OverWrite overWrite)
            throws IOException {
        copyFilesTo(srcDir, destDir, null, overWrite);
    }

    public static void copyFilesTo(File srcDir, File destDir,
                                   CopyFilter copyFilter, OverWrite overWrite)
            throws IOException {
        if (!srcDir.exists())
            throw new IOException("dir[" + srcDir.getAbsolutePath() + "] not exists!");

        if (!srcDir.isDirectory())
            throw new IOException("[" + srcDir + "] not dir!");

        if (destDir == null) {
            throw new IOException("set dir please first!");
        }
        if (!destDir.exists())
            if (!destDir.mkdirs())
                throw new IOException("make dir[" + destDir.getAbsolutePath() + "] failed!");

        File[] srcFiles = srcDir.listFiles();
        if (srcFiles == null || srcFiles.length == 0)
            return;

        for (File srcFile : srcFiles) {
            if (srcFile.isFile()) {
                File destFile = new File(destDir, srcFile.getName());
                if (copyFilter != null) {
                    if (copyFilter instanceof ReplaceCopyFilter) {
                        AtomicReference<File> desAtomicReference = new AtomicReference<>(destFile);
                        if (((ReplaceCopyFilter) copyFilter).filter(srcFile, desAtomicReference)) {
                            destFile = desAtomicReference.get();
                        } else {
                            continue;
                        }
                    } else if (!copyFilter.filter(srcFile, destFile))
                        continue;
                }
                copyFileTo(srcFile, destFile, overWrite);
            } else if (srcFile.isDirectory()) {
                File theDestDir = new File(destDir, srcFile.getName());
                copyFilesTo(srcFile, theDestDir, copyFilter, overWrite);
            }
        }
    }

    public static BasicFileAttributes getFileAttr(File file) throws IOException {
        Path path = file.toPath();
        BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class,
                LinkOption.NOFOLLOW_LINKS);
        return basicview.readAttributes();
    }

    public static Date getFileCreateTime(File file) throws IOException {
        return new Date(getFileAttr(file).creationTime().toMillis());
    }

    public static Date getFileLastModifyTime(File file) {
        return new Date(file.lastModified());
    }

}