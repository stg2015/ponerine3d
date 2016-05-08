package com.sp.video.yi.view.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.widget.ImageView;



import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BitmapToolkit {
    private final static String TAG                      = "BitmapToolkit";
    public static final  int    DEFAULT_COMPRESS_QUALITY = 80;
    private final static String DIR_ROOT                 = Environment
            .getExternalStorageDirectory().getPath() + "/MoMoShow/";
    public final static String DIR_PHOTO                = DIR_ROOT + "photo/";
    public final static String DIR_SHOW_IMAGE           = DIR_ROOT + "show/image/";
    public final static String DIR_SHOW_AUDIO           = DIR_ROOT + "show/audio/";
    public final static String DIR_SHOW_VIDEO           = DIR_ROOT + "show/video/";

    public final static String DIR_SMALL_AVATAR = DIR_ROOT + "avatar/130/";

    public final static String DIR_IM_PICTURE         = DIR_ROOT + "im/picture/";
    public final static String DIR_IM_AUDIO           = DIR_ROOT + "im/audio/";
    public final static String DIR_IM_MAP             = DIR_ROOT + "im/map/";
    private static final int    MAX_THUMB_LONG         = 120;
    private static final int    MAX_THUMB_SHORT        = 30;
    public final static  int    UPLOAD_IMAGE_MAX_WIDTH = 640;
    public final static  int    CROP_IMAGE_MAX_WIDTH   = 480;
    public final static  int    CROP_IMAGE_MAX_HEIGHT  = 800;

    public final static int SELECT_IMAGE_MIN_SIDE = 120;

    public static void deletePic(long pid, int size) {
        deletePic(pid, size, DIR_ROOT);
    }

    public static void deletePic(long pid, int size, String dir) {
        File dirFile = new File(getFullPath(pid, size, dir));
        if (dirFile.exists()) {
            boolean result = dirFile.delete();
            Log.v(TAG, "deletePic" + getFullPath(pid, size, dir) + "result:"
                    + result);
        }
    }

    private static String getPath(long pid, String dir) {
        String path = DIR_ROOT + dir;// + pid % 64 + "/";
        return path;
    }

    private static String getName(long pid, int size) {
        return pid + "_" + size + ((size < 100) ? ".cache" : ".jpg");// +
        // "_cache";
    }

    private static String getFullPath(long pid, int size, String dir) {
        return getPath(pid, dir) + getName(pid, size);
    }

    public static boolean isExist(long pid, int size) {
        return isExist(pid, size, DIR_ROOT);
    }

    public static boolean isExist(long pid, int size, String dir) {
        File dirFile = new File(getFullPath(pid, size, dir));
        boolean isExit = dirFile.exists() && dirFile.length() > 0;
        Log.v(TAG, "isExit:" + dirFile.length() + "----" + pid + isExit
                + dirFile.getAbsolutePath());
        return isExit;
    }

    /**
     * <br>
     * Description:rotate Bitmap <br>
     * Author:hexy <br>
     * Date:2011-4-1上午10:23:00
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (degree == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Bitmap操作完应该显示的释放
            try {
                bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap = tempBmp;
        } catch (OutOfMemoryError ex) {
            // 建议如果出现了内存不足异常，最好return 原始的bitmap对象。.
        }
        return bitmap;
    }

    public static Bitmap rotateBitmapWithThrowsOOM(Bitmap bitmap, int degree)
            throws OutOfMemoryError {
        if (degree == 0)
            return bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Bitmap操作完应该显示的释放
            bitmap.recycle();
            bitmap = tempBmp;
        } catch (OutOfMemoryError ex) {
            throw ex;
        }
        return bitmap;
    }

    private String mDirectory = DIR_ROOT;

    // file name
    private String mName = "";

    private String mRemoteUrl = "";

    private String mSuffix = "";

    private String mPrefix = "";

    /**
     * <br>
     * Description: 实现图片缓存,下载的类. <br>
     * Author:hexy <br>
     * Date:2011-4-1上午10:30:09
     *
     * @param url
     *            图片远程的url, 读取本地图片无需用此构造函数
     * @param prefix
     *            图片前缀
     * @param suffix
     *            图片后缀
     */
    public BitmapToolkit(String url, String prefix, String suffix) {
        mDirectory = DIR_PHOTO;
        mPrefix = prefix;
        mSuffix = suffix;
        mName = stringToMD5(url);
        mRemoteUrl = url;
        this.mkdirsIfNotExist();
    }

    /**
     * <br>
     * Description: 实现图片缓存,下载的类. <br>
     * Author:hexy <br>
     * Date:2011-4-1上午10:30:09
     *
     * @param dir
     *            缓存文件夹绝对路径
     * @param url
     *            图片远程的url, 读取本地图片无需用此构造函数
     * @param prefix
     *            图片前缀
     * @param suffix
     *            图片后缀
     */
    public BitmapToolkit(String dir, String url, String prefix, String suffix) {
        mDirectory = dir;
        mPrefix = prefix;
        mSuffix = suffix;
        mName = stringToMD5(url);
        mRemoteUrl = url;
        this.mkdirsIfNotExist();
    }

    public String getName() {
        return mName;
    }

    public boolean isExist() {
        File dirFile = new File(getAbsolutePath());
        boolean isExit = dirFile.exists() && dirFile.length() > 0;
        Log.v(TAG, "isExit:" + isExit + " size:" + dirFile.length()
                + " absoulutePath:" + getAbsolutePath());
        return isExit;
    }

    public void deletePic() {
        File dirFile = new File(getAbsolutePath());
        if (dirFile.exists()) {
            boolean result = dirFile.delete();
            Log.v(TAG, "deletePic" + getAbsolutePath() + "result:" + result);
        }
    }

    /**
     * get degree from exif
     */
    public static int getDegree(String filename) {
        int result = 0;
        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(filename);
            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

        } catch (Exception e) {
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                result = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                result = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                result = 270;
                break;
        }

        return result;
    }

    public static Bitmap halfCenterBitmap(Bitmap bitmap, final float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.save();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final int border = 0;
        final Rect rect = new Rect(border, border, output.getWidth() - 2
                * border, output.getHeight() - 2 * border);
        final int left = border + bitmap.getWidth() / 4;
        final int top = border + bitmap.getHeight() / 4;
        final Rect src = new Rect(left, top, output.getWidth() + left - 2
                * border, output.getHeight() + top - 2 * border);
        final Rect dst = new Rect(border, border, output.getWidth() - 2
                * border, output.getHeight() - 2 * border);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        canvas.restore();

        return output;
    }

    public Bitmap loadLocalBitmap(String url, int degree) {
        return rotateBitmap(loadLocalBitmap(url), degree);
    }

    public Bitmap loadLocalBitmap(String url) {
        Bitmap bitmap = null;
        try {
            File dirFile = new File(url);

            if (!dirFile.exists()) {
                Log.v(TAG, "loadBitmap not exit");
            } else {
                Options bmpFactoryOptions = new Options();
                bmpFactoryOptions.inJustDecodeBounds = false;
                bmpFactoryOptions.inDither = false;
                bmpFactoryOptions.inPurgeable = true;
                bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(url, bmpFactoryOptions);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadBitmap" + e.toString());
        } catch (OutOfMemoryError error) {
            // 图片过大，压缩处理
            try {
                bitmap = ShrinkBitmap(url, 480, 800);
            } catch (Exception e) {
                Log.e(TAG, "loadBitmap" + e.toString());
            } catch (OutOfMemoryError e) {
                try {
                    bitmap = ShrinkBitmap(url, 320, 480);
                } catch (Exception e1) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e2) {

                }
            }
        }
        return bitmap;
    }

    public static Bitmap loadLocalFileBitmap(String url) {
        Bitmap bitmap = null;
        try {
            File dirFile = new File(url);

            if (!dirFile.exists()) {
                Log.v(TAG, "loadBitmap not exit");
            } else {
                Options bmpFactoryOptions = new Options();
                bmpFactoryOptions.inJustDecodeBounds = false;
                bmpFactoryOptions.inDither = false;
                bmpFactoryOptions.inPurgeable = true;
                bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(url, bmpFactoryOptions);
                // rotate from exif
                int degree = getDegree(url);
                Log.v(TAG, "original degree：" + degree);
                if (degree > 0) {
                    bitmap = rotateBitmap(bitmap, degree);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadBitmap" + e.toString());
        } catch (OutOfMemoryError error) {
            // 图片过大，压缩处理
            try {
                bitmap = ShrinkBitmap(url, 480, 800);
                // rotate from exif
                int degree = getDegree(url);
                Log.v(TAG, "480x800 degree：" + degree);
                if (degree > 0) {
                    bitmap = rotateBitmap(bitmap, degree);
                }
            } catch (Exception e) {
                Log.e(TAG, "loadBitmap" + e.toString());
            } catch (OutOfMemoryError e) {
                try {
                    bitmap = ShrinkBitmap(url, 320, 480);
                    // rotate from exif
                    int degree = getDegree(url);
                    Log.v(TAG, "320x480 degree：" + degree);
                    if (degree > 0) {
                        bitmap = rotateBitmap(bitmap, degree);
                    }
                } catch (Exception e1) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e2) {

                }
            }
        }
        return bitmap;
    }

    public static Bitmap cropLocalFileBitmapWithRotate(String url, int degree,
                                                       final float xRatio, final float yRatio, final float widthRatio,
                                                       final float heightRatio) {
        Bitmap bitmap = null;
        try {
            File dirFile = new File(url);

            if (!dirFile.exists()) {
                Log.v(TAG, "loadBitmap not exit");
            } else {
                Options bmpFactoryOptions = new Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeFile(url, bmpFactoryOptions);
                if (bmpFactoryOptions.outWidth * widthRatio > UPLOAD_IMAGE_MAX_WIDTH) {
                    int widthScaleRatio = (int) Math
                            .floor(bmpFactoryOptions.outWidth * widthRatio
                                    / (float) UPLOAD_IMAGE_MAX_WIDTH);
                    if (widthScaleRatio > 1) {
                        bmpFactoryOptions.inSampleSize = widthScaleRatio;
                    }
                }

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmpFactoryOptions.inDither = false;
                bmpFactoryOptions.inPurgeable = true;
                bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(url, bmpFactoryOptions);

                if (degree > 0) {
                    bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                }

                int x = (int) (bitmap.getWidth() * xRatio);
                int y = (int) (bitmap.getHeight() * yRatio);
                int w = (int) (bitmap.getWidth() * widthRatio);
                int h = (int) (bitmap.getHeight() * heightRatio);
                if (y + h > bitmap.getHeight()) {
                    h = bitmap.getHeight() - y;
                }
                if (x + w > bitmap.getWidth()) {
                    w = bitmap.getWidth() - x;
                }
                bitmap = Bitmap.createBitmap(bitmap, x, y, w, h);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadBitmap" + e.toString());
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                } else {
                    bitmap = null;
                }
            }
        } catch (OutOfMemoryError error) {
            // 图片过大，压缩处理
            try {
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    } else {
                        bitmap = null;
                    }
                }
                bitmap = ShrinkBitmap(url, 480, 800);
                if (degree > 0) {
                    bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                }
                int x = (int) (bitmap.getWidth() * xRatio);
                int y = (int) (bitmap.getHeight() * yRatio);
                int w = (int) (bitmap.getWidth() * widthRatio);
                int h = (int) (bitmap.getHeight() * heightRatio);
                if (y + h > bitmap.getHeight()) {
                    h = bitmap.getHeight() - y;
                }
                if (x + w > bitmap.getWidth()) {
                    w = bitmap.getWidth() - x;
                }
                bitmap = Bitmap.createBitmap(bitmap, x, y, w, h);
            } catch (Exception e) {
                Log.e(TAG, "loadBitmap" + e.toString());
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    } else {
                        bitmap = null;
                    }
                }
            } catch (OutOfMemoryError e) {
                try {
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                    bitmap = ShrinkBitmap(url, 320, 480);
                    if (degree > 0) {
                        bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                    }
                    int x = (int) (bitmap.getWidth() * xRatio);
                    int y = (int) (bitmap.getHeight() * yRatio);
                    int w = (int) (bitmap.getWidth() * widthRatio);
                    int h = (int) (bitmap.getHeight() * heightRatio);
                    if (y + h > bitmap.getHeight()) {
                        h = bitmap.getHeight() - y;
                    }
                    if (x + w > bitmap.getWidth()) {
                        w = bitmap.getWidth() - x;
                    }
                    bitmap = Bitmap.createBitmap(bitmap, x, y, w, h);
                } catch (Exception e1) {
                    e.printStackTrace();
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e2) {
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                }
            }
        }
        return bitmap;
    }

    public static Bitmap loadLocalFileBitmapWithRotate(String url, int degree) {
        Bitmap bitmap = null;
        try {
            File dirFile = new File(url);

            if (!dirFile.exists()) {
                Log.v(TAG, "loadBitmap not exit");
            } else {
                Options bmpFactoryOptions = new Options();
                bmpFactoryOptions.inJustDecodeBounds = false;
                bmpFactoryOptions.inDither = false;
                bmpFactoryOptions.inPurgeable = true;
                bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(url, bmpFactoryOptions);

                if (degree > 0) {
                    bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadBitmap" + e.toString());
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                } else {
                    bitmap = null;
                }
            }
        } catch (OutOfMemoryError error) {
            // 图片过大，压缩处理
            try {
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    } else {
                        bitmap = null;
                    }
                }
                bitmap = ShrinkBitmap(url, 480, 800);
                if (degree > 0) {
                    bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                }
            } catch (Exception e) {
                Log.e(TAG, "loadBitmap" + e.toString());
                if (bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    } else {
                        bitmap = null;
                    }
                }
            } catch (OutOfMemoryError e) {
                try {
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                    bitmap = ShrinkBitmap(url, 320, 480);
                    if (degree > 0) {
                        bitmap = rotateBitmapWithThrowsOOM(bitmap, degree);
                    }
                } catch (Exception e1) {
                    e.printStackTrace();
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e2) {
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        } else {
                            bitmap = null;
                        }
                    }
                }
            }
        }
        return bitmap;
    }

    public InputStream loadLocalInputStream(String url) {
        InputStream fis = null;
        try {
            File dirFile = new File(url);
            if (!dirFile.exists()) {
                Log.v(TAG, "loadBitmap not exit");
            } else {
                fis = new FileInputStream(url);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadBitmap" + e.toString());
        }
        return fis;
    }

    public static int MinWidthOrHeightOfBitmap(String filePath) {
        int min = 0;
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
            min = Math.min(bmpFactoryOptions.outWidth,
                    bmpFactoryOptions.outHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return min;
    }

    public static Bitmap ShrinkBitmap(String file, int width, int height)
            throws Exception, OutOfMemoryError {
        Options bmpFactoryOptions = new Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bmpFactoryOptions.inDither = false;
        bmpFactoryOptions.inPurgeable = true;
        bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public static Bitmap ShrinkBitmap(Resources rs, int res, int width,
                                      int height) throws Exception, OutOfMemoryError {
        Options bmpFactoryOptions = new Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory
                .decodeResource(rs, res, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bmpFactoryOptions.inDither = false;
        bmpFactoryOptions.inPurgeable = true;
        bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
        bitmap = BitmapFactory.decodeResource(rs, res, bmpFactoryOptions);
        return bitmap;
    }

    public static Bitmap ShrinkCropBitmap(String file, int width) {
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

            int widthRatio = (int) Math.floor(bmpFactoryOptions.outWidth
                    / (float) width);
            bmpFactoryOptions.inSampleSize = widthRatio;

            bmpFactoryOptions.inJustDecodeBounds = false;
            bmpFactoryOptions.inDither = false;
            bmpFactoryOptions.inPurgeable = true;
            bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap ShrinkCropBitmapWithRotate(String file, int degree,
                                                    int width) {
        Bitmap bitmap = null;
        int ountWidth = 0;
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            ountWidth = bmpFactoryOptions.outWidth;
            int widthRatio = (int) Math.floor(ountWidth / (float) width);
            if (widthRatio > 1) {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bmpFactoryOptions.inDither = false;
            bmpFactoryOptions.inPurgeable = true;
            bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            if (degree > 0) {
                bitmap = rotateBitmap(bitmap, degree);
            }
            return bitmap;
        } catch (OutOfMemoryError e) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (width > 720) {
                width = 540;
            } else if (width > 540) {
                width = 480;
            } else if (width > 320) {
                width = 320;
            }
            try {
                Options bmpFactoryOptions = new Options();
                if (ountWidth < 1) {
                    bmpFactoryOptions.inJustDecodeBounds = true;
                    bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
                    ountWidth = bmpFactoryOptions.outWidth;
                }
                int widthRatio = (int) Math.floor(ountWidth / (float) width);
                if (widthRatio > 1) {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmpFactoryOptions.inDither = false;
                bmpFactoryOptions.inPurgeable = true;
                bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
                if (degree > 0) {
                    bitmap = rotateBitmap(bitmap, degree);
                }
                return bitmap;
            } catch (OutOfMemoryError e1) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                return null;
            }
        }
    }

    public static boolean isWidthOver640Px(String filePath) {
        boolean isOver = false;
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
            isOver = bmpFactoryOptions.outWidth > 720;
        } catch (OutOfMemoryError e) {
        }
        return isOver;
    }

    public static boolean isWidthOver480Px(String filePath) {
        boolean isOver = false;
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
            isOver = bmpFactoryOptions.outWidth > 480;
        } catch (OutOfMemoryError e) {
        }
        return isOver;
    }

    public static Bitmap ShrinkBitmap(String file, int width) {
        try {
            Options bmpFactoryOptions = new Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                    / (float) width);

            if (widthRatio > 1) {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bmpFactoryOptions.inDither = false;
            bmpFactoryOptions.inPurgeable = true;
            bmpFactoryOptions.inTempStorage = new byte[32 * 1024];
            bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    // add by gongxt 2011-2-22: use this function to find out big image's DPI
    public static int getBigImageDpi(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();

        // display metrics is 120, 180, 240
        if (display.densityDpi == DisplayMetrics.DENSITY_LOW) {
            // TODO: dowload different images
        } else if (display.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            // TODO: dowload different images
        } else if (display.densityDpi == DisplayMetrics.DENSITY_HIGH) {
            // TODO: dowload different images
        }

        // now we dowload 480*480 only
        return 480;
    }

    public static Bitmap loadLocalBitmapRoughScaled(String path, int maxsize) {
        Bitmap bm = null;

        try {
            Options options = new Options();

            options.outHeight = maxsize;
            options.inJustDecodeBounds = true;
            options.inTempStorage = new byte[32 * 1024];
            // 获取这个图片的长边和高
            bm = BitmapFactory.decodeFile(path, options); // 此时返回bm为空

            options.inJustDecodeBounds = false;
            int be = options.outHeight / (int) (maxsize / 10);
            if (be % 10 != 0)
                be += 10;

            be = be / 10;
            if (be <= 0)
                be = 1;

            options.inSampleSize = be;
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
                System.gc();
            }
            bm = BitmapFactory.decodeFile(path, options);
            // Log.v(TAG, "getLocalBitmap width " + bm.getWidth() + " height " +
            // bm.getHeight());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bm = null;
        }
        return bm;
    }

    public static Bitmap loadLocalBitmapExactScaled(String path, int size) {
        Bitmap bm = loadLocalBitmapRoughScaled(path, size * 2);
        if (bm == null) {
            return bm;
        }
        // rotate from exif
        int degree = getDegree(path);
        bm = rotateBitmap(bm, degree);
        return compress(bm, size);
    }

    /**
     * 获取压缩
     */
    public static byte[] loadLocalBitmapExactScaledBytes(String path, int size) {
        Bitmap bm = loadLocalBitmapExactScaled(path, size);
        return BitmapToByteArray(bm);
    }

    public static Bitmap cornerBitmap(Bitmap bitmap, final float roundPx) {
        return cornerBitmap(bitmap, roundPx, Color.TRANSPARENT);
    }

    public static Bitmap cornerBitmap(Bitmap bitmap, final float roundPx,int frameColor) {
        return cornerBitmap(bitmap, roundPx, frameColor, bitmap.getWidth(), bitmap.getHeight());
    }
    public static Bitmap cornerBitmap(Bitmap bitmap, final float roundPx,
                                      int frameColor,int dstWidth,int dstHeight) {
        Bitmap output = Bitmap.createBitmap(dstWidth,
                dstHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.save();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final Rect rect = new Rect(0, 0, dstWidth, dstHeight);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, rect, paint);

        canvas.restore();

        canvas.save();
        Paint paintFrame = new Paint();
        paintFrame.setAntiAlias(true);
        paintFrame.setStyle(Paint.Style.STROKE);
        paintFrame.setColor(frameColor);
        paintFrame.setStrokeWidth(0);
        canvas.drawRoundRect(
                new RectF(new Rect(0, 0, dstWidth, dstHeight)),
                roundPx, roundPx, paintFrame);
        canvas.restore();
        return output;
    }

    public static Bitmap compress(Bitmap bitmap, int size) {
        if (bitmap == null)
            return null;
        if (bitmap.isRecycled())
            return null;
        // create explicit picture
        int max = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth()
                : bitmap.getHeight();
        int min = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth()
                : bitmap.getHeight();
        min = size * min / max;
        max = size;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            bitmap = Bitmap.createScaledBitmap(bitmap, max, min, false);
        } else {
            bitmap = Bitmap.createScaledBitmap(bitmap, min, max, false);
        }
        return bitmap;
    }

    public void mkdirsIfNotExist() {
        File dirFile = new File(getDirecotry());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    // 3 save byte to sdcard
    public boolean saveByte(byte[] byteArray) {
        if (byteArray == null)
            return false;
        Log.v(TAG, "saveByte" + mRemoteUrl + getAbsolutePath());
        mkdirsIfNotExist();

        File myCaptureFile = new File(getAbsolutePath());

        FileOutputStream fileOutPutStream;
        try {

            fileOutPutStream = new FileOutputStream(myCaptureFile);

            fileOutPutStream.write(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap" + e.toString());
            return false;
        }

        try {
            fileOutPutStream.close();
        } catch (Exception e) {
        }

        return true;
    }

    public byte[] getBytesFromFile() {
        File f = new File(getAbsolutePath());

        if (f.exists()) {
            try {
                FileInputStream stream = new FileInputStream(f);
                ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = stream.read(b)) != -1)
                    out.write(b, 0, n);
                stream.close();
                out.close();
                return out.toByteArray();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * <br>
     * Description: <br>
     * Author:hexy <br>
     * Date:2011-4-15下午08:34:52
     *
     * @param url
     * @return .gif .jpg ...
     */
    public static String getSuffix(String url) {
        url = url.replace("?momolink=0", "");
        int index = url.lastIndexOf(".") - 1;

        if (index > 0 && index < url.length() - 1) {
            String typeStr = url.substring(index + 1);
            return typeStr.toLowerCase();
        }

        return "";
    }

    public static byte[] BitmapToByteArray(Bitmap bitmap) {
        byte[] result = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 60, stream);
        result = stream.toByteArray();

        return result;
    }

    public static Bitmap ByteArrayToBitmap(byte[] byteArray) {
        Bitmap result = null;
        result = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return result;
    }

    public static boolean available() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static long getAvailaSize() {
        if (!available())
            return 0;

        File path = Environment.getExternalStorageDirectory();
        // 取得sdcard文件路径
        StatFs statfs = new StatFs(path.getPath());
        // 获取block的SIZE
        long blocSize = statfs.getBlockSize();
        // 己使用的Block的数量
        long availaBlock = statfs.getAvailableBlocks();

        return availaBlock * blocSize;
    }

    public static boolean isFullStorage() {
        long size = getAvailaSize();
        // 1M = 1048576
        return size < 1048576;
    }

    public static boolean saveBitmap(Bitmap bmp, long pid, int size, String dir) {
        if (bmp == null)
            return false;
        if (isFullStorage())
            return false;

        File dirFile = new File(getPath(pid, dir));
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try {
            File myCaptureFile = new File(getFullPath(pid, size, dir));
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile), 8 * 1024);
            bmp.compress(CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            Log.v(TAG, "saveBitmap create" + getFullPath(pid, size, dir));
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap" + e.toString());
            return false;
        }
        return true;
    }

    public boolean saveBitmap(Bitmap bmp) {
        if (bmp == null)
            return false;
        if (isFullStorage())
            return false;

        File dirFile = new File(getDirecotry());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try {
            File myCaptureFile = new File(getAbsolutePath());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile), 8 * 1024);
            // bmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
            bmp.compress(CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            Log.v(TAG, "saveBitmap create" + getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap" + e.toString());
            return false;
        }
        return true;
    }
    /**
     *
     * @n<b>函数名称</b>     :saveBitmap
     * @brief  保存指定文件名的图片到指定路径
     * @see
     * @since    Ver 1.1
     * @param  @param saveDirectory
     * @param  @param imageName
     * @param  @param bmp
     * @param  @return
     * @return boolean
     * @<b>作者</b>          :  linqm
     * @<b>创建时间</b>      :  2014-1-16下午5:12:12
     */
    public static boolean saveBitmap(String saveDirectory,String imageName,Bitmap bmp){
        if (bmp == null)
            return false;
        if (isFullStorage())
            return false;

        File dirFile = new File(saveDirectory);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(dirFile,imageName);
        return saveBitmap(myCaptureFile, bmp);
    }

    public static boolean saveBitmap(File file,Bitmap bmp,CompressFormat format){
        if (bmp==null){
            return false;
        }
        BufferedOutputStream bos=null ;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(file), 8 * 1024);
            // bmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
            if (bmp.isRecycled()){
                return false;
            }
            bmp.compress(format, 80, bos);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap" + e.toString());
            return false;
        } finally {
            if ( bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean saveBitmap(File file,Bitmap bmp){
        return saveBitmap(file, bmp, CompressFormat.JPEG);
    }

    public String getAbsolutePath() {
        return mDirectory + mPrefix + mName + mSuffix;
    }

    public String getDirecotry() {
        return mDirectory;
    }

    public String getFileName() {
        return mPrefix + mName + mSuffix;
    }

    /**
     * @author shawn manager bitmaps memory
     */
    public static class BitmapMemoryMgr {
        private ArrayList<Bitmap> mBitmapArray;

        public BitmapMemoryMgr() {
            mBitmapArray = new ArrayList<Bitmap>();
        }

        public void addBitmap(Bitmap bitmap) {
            mBitmapArray.add(bitmap);
        }

        public void releaseAllMemory() {
            if (mBitmapArray == null)
                return;
            for (Bitmap bmp : mBitmapArray) {
                if (bmp != null && !bmp.isMutable()) {
                    bmp.recycle();
                    bmp = null;
                }
            }
            Log.v(TAG, "releaseAllMemory : " + mBitmapArray.size());
            mBitmapArray.clear();
        }
    }

    public static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int bytes = read();
                    if (bytes < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static void setImageBitmapSafe(ImageView imageview, Bitmap bmp) {
        if (bmp.isRecycled()) {
            bmp = null;
            return;
        }
        imageview.setImageBitmap(bmp);
    }

    // view photo from album
    public static void viewPicFromAlbum(Activity activity, String loadpath) {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(loadpath));

        intent.setDataAndType(uri, "image/*");

        activity.startActivity(intent);
    }

    /**
     * 将文件写入媒体库
     *

     * @param file
     * @return
     */
    public static Uri addImage(Context context, File file, String mime) {
        if (file == null) {
            return null;
        }
        ContentResolver cr = context.getContentResolver();
        String[] projection = {Images.Media.DATA};
        String selection = Images.Media.DATA + "=? ";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};
        Cursor cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI, projection,
                                 selection, selectionArgs, null);
        boolean hadSaved = false;
        if (cursor != null && cursor.getCount() > 0) {
            Log.v(TAG, "media photo find:" + cursor.getCount());
            hadSaved = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
        if (hadSaved) {
            return null;
        }

        long size = file.length();

        ContentValues values = new ContentValues(7);
        values.put(Images.Media.TITLE, file.getName());

        values.put(Images.Media.DISPLAY_NAME, file.getName());
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Images.Media.MIME_TYPE, mime);
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, file.getAbsolutePath());
        values.put(Images.Media.SIZE, size);

        return cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                           drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String stringToMD5(String str) {
        String result = "";
        byte[] bytes = str.getBytes();
        MessageDigest complete;
        try {
            complete = MessageDigest.getInstance("MD5");
            byte[] digests = complete.digest(bytes);
            BigInteger bi = new BigInteger(1, digests);
            result = bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isLocalUrl(String url) {
        return url != null && !url.startsWith("http");
    }

    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getSmaller(Display display) {
        Point size = new Point();
        display.getSize(size);
        return size.y > size.x ? size.x : size.y;
    }


    public static Bitmap resizeBitmap(Bitmap bitmap, float minX, float maxX,
                                      float minY, float maxY) {
        if (bitmap == null) {
            return null;
        }
        Bitmap bitmaptemp = null;
        float width = maxX - minX;
        float height = maxY - minY;

        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }

        if (minX == 0) {
            minX = 1;
        }
        if (minY == 0) {
            minY = 1;
        }
        if (minY + height > bitmap.getHeight()) {
            height = bitmap.getHeight() - minY;
        }
        if (minX + width > bitmap.getWidth()) {
            width = bitmap.getWidth() - minX;
        }

        bitmaptemp = Bitmap.createBitmap(bitmap, (int) minX, (int) minY,
                (int) width, (int) height);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
//		int newWidth = (int) (width / 1024f * 596);
        int newWidth = (int) (width);
        if (newWidth < 1)
            newWidth = 1;
//		int newHeight = (int) (width / 1024f * 596);
        int newHeight = (int) (height);
        if (newHeight < 1)
            newWidth = 1;
        return resizeBitmap(bitmaptemp, newWidth, newHeight);
    }

    /**
     * 保持长宽比缩小Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight

     *            1~100
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (null == bitmap) {
            return null;
        }
        Bitmap bitmaptemp = null;
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int width = originWidth;
        int height = originHeight;
        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            width = maxWidth;
            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);
        }
        if (originHeight > maxHeight) {
            height = maxHeight;
            double j = originHeight * 1.0 / maxHeight;
            width = (int) Math.floor(originWidth / j);
        }
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        bitmaptemp = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return bitmaptemp;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int max, int min) {
        if (null == bitmap) {
            return null;
        }
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (originWidth < max && originHeight < max) {
            return extendBitmap(bitmap, min);
        }
        Bitmap bitmaptemp = null;
        int width = originWidth;
        int height = originHeight;
        if (originWidth > max) {
            width = max;
            double i = originWidth * 1.0 / max;
            height = (int) Math.floor(originHeight / i);
        }
        if (originHeight > max) {
            height = max;
            double j = originHeight * 1.0 / max;
            width = (int) Math.floor(originWidth / j);
        }
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        bitmaptemp = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return bitmaptemp;
    }

    private static Bitmap extendBitmap(Bitmap bitmap, int min) {
        if (null == bitmap) {
            return null;
        }
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (originHeight > min || originWidth > min) {
            return bitmap;
        }
        float scale = (float) originWidth / originHeight;
        int nowW = 0;
        int nowH = 0;
        if (scale >= 1) {
            //宽大于高
            nowW = min;
            nowH = (int) (min / scale);
        } else {
            nowH = min;
            nowW = (int) (min * scale);
        }
        Bitmap bitmaptemp = Bitmap.createScaledBitmap(bitmap, nowW, nowH, false);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return bitmaptemp;
    }

    /**
     * 返回图片
     */
    protected static final int MAX_BITMAP_WIDTH = 768;
    protected static final int MAX_BITMAP_HIGHT = 1024;

    public static Bitmap createThumbnailBitmap(Activity context, Uri uri, boolean bool) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            try {
                Options.class.getField("inNativeAlloc").setBoolean(options, true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
//				e.printStackTrace();
            } catch (IllegalAccessException e) {
//				e.printStackTrace();
            } catch (NoSuchFieldException e) {
//				e.printStackTrace();
            }
            BitmapFactory.decodeStream(input, null, options);
            input.close();

//			int scale = 1;
//
//			while (bool && ((options.outWidth / scale > MAX_BITMAP_SIZE) || (options.outHeight / scale > MAX_BITMAP_SIZE))) {
//				scale *= 2;
//			}
            int scale = 1;
            if (bool) {
                scale = calculateInSampleSize(options, MAX_BITMAP_WIDTH, MAX_BITMAP_HIGHT);
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            input = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Calculate an inSampleSize for use in a {@link Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options An options object with out* params already populated (run through a decode*
     *            method with inJustDecodeBounds==true
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (options.outHeight < options.outWidth) {
            //横版图片，宽高互换
            int exchange;
            exchange = reqWidth;
            reqWidth = reqHeight;
            reqHeight = exchange;
        }

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 制作微缩图
     * @param uri
     * @param size
     * @return
     */
    public static Bitmap createThumbnailBitmap(Context context, Uri uri, int size) {
        InputStream input = null;

        try {
            input = context.getContentResolver().openInputStream(uri);
            Options options = new Options();
            options.inPurgeable = true;
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            try {
                Options.class.getField("inNativeAlloc").setBoolean(options, true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
//				e.printStackTrace(); 
            } catch (IllegalAccessException e) {
//				e.printStackTrace(); 
            } catch (NoSuchFieldException e) {
//				e.printStackTrace(); 
            }


            BitmapFactory.decodeStream(input, null, options);
            input.close();
            // Compute the scale.
            int scale = 1;
//			while ((options.outWidth / scale > size)
//					|| (options.outHeight / scale > size)) {
//				scale *= 2;
//			}
            scale = calculateInSampleSize(options, size, size);
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            input = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input, null, options);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 从资源目录解析图片：修复某些机型出现decode出来的图片大于实际图片的问题
     * <br>Created 2014-6-5 下午5:05:11
     * @param resources
     * @param id
     * @return
     * @author cb
     */
    public static Bitmap decodeBitmap(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        Options options = new Options();
        options.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, options);
    }
}