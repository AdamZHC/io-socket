package com.hit.adam.io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileIO {
    static String path = "D:\\WebDevelopment\\Tomcat\\io-socket\\src\\main\\resources\\normal_io.txt";
    public static void streamIO() {
        InputStream in = null;
        try{
            in = new BufferedInputStream(new FileInputStream(path));
            byte [] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while(bytesRead != -1)
            {
                for(int i=0;i<bytesRead;i++)
                    System.out.print((char)buf[i]);
                bytesRead = in.read(buf);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void filNio(){
        RandomAccessFile aFile = null;
        try{
            aFile = new RandomAccessFile(path,"rw");
            //要获取Channel
            FileChannel fileChannel = aFile.getChannel();
            //为缓冲区分配
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //表示读channel读到buffer中
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while(bytesRead != -1)
            {
                //flip()
                //这里改变模式由写buffer转变为读buffer
                buf.flip();
                while(buf.hasRemaining())
                {
                    System.out.print((char)buf.get());
                }
                //compat()
                // 如果此时还有数据未读，但是想要写数据，那就执行该方法
                // 之前未读的数据不会被覆盖掉

                //mark()可以标记数据，reset()会回复数据
                //rewind()设置回0
                buf.compact();
                bytesRead = fileChannel.read(buf);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile != null){
                    aFile.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
