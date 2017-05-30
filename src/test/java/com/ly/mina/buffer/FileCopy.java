package com.ly.mina.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileCopy {

	FileChannel sourceChannel;
	FileChannel targetChannel;

	public FileCopy(String sourcFile,String targetFile) throws IOException {
		  sourceChannel = FileChannel.open(Paths.get(sourcFile),
				EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));
		  targetChannel = FileChannel.open(Paths.get(targetFile),
				EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE));
	}

	public void close() throws IOException {
		sourceChannel.close();
		targetChannel.close();
	}

	public static void main(String[] args) throws IOException {
		FileCopy fileCopy = null;
		try{
			fileCopy = new FileCopy("D:/test.jpg","D:test1.jpg");
			fileCopy.doCopy();
		}finally {
			if(fileCopy!=null){
				fileCopy.close();
			}
		}
	}

	private void doCopy() throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);

		while(readBuffer(byteBuffer)!=-1){
			byteBuffer.flip();
			targetChannel.write(byteBuffer);
			//byteBuffer.clear();
			byteBuffer.compact();
		}
	}

	private int readBuffer(ByteBuffer byteBuffer) throws IOException {
		int read;
		read = sourceChannel.read(byteBuffer);
		System.out.println("read size :"+read);
		return read;
	}
}
