package com.example.image.reduce;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownLoad {

    /**
     * 打包成zip
     * @param tagPath    zip的输出地址
     * @param sourcePath 文件的来源地址，字符串数组
     * @throws IOException
     */
    public void downLoadZIP(String tagPath, String[] sourcePath) throws IOException {
        //zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tagPath));
        File[] files = new File[sourcePath.length];
        //按照多个文件的打包方式，一个也可以
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(sourcePath[i]);
        }
        byte[] b = new byte[1024];
        for (int j = 0; j < files.length; j++) {
            //输入流
            FileInputStream in = new FileInputStream(files[j]);
            //把条目放到zip里面，意思就是把文件放到压缩文件里面
            out.putNextEntry(new ZipEntry(files[j].getName()));
            int len = 0;
            //输出
            while ((len = in.read(b)) > -1) {
                out.write(b, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
    }

    public static void main(String[] args) throws IOException {
        DownLoad dl = new DownLoad();
        String[] path = {"D:/image/6423ff4b-410e-4067-981e-1e091136ecad", "D:/image/c38156d8-9e1f-4514-a816-c3d251254e08"};
        dl.downLoadZIP("D:/test.zip", path); //把上面两个文件打包成test.zip输出到D盘根目录
    }
}