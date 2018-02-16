package com.drool.main;

import java.util.List;

import org.jpos.iso.ISOUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NittyDecoder extends ByteToMessageDecoder {

	private boolean dumpRawBytes;
	private int messageLengthFieldSize;
	private final static byte[] header = ISOUtil.hex2byte("160102003E0000000000000000000000000000000000");
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public NittyDecoder(int messageLengthFieldSize){
		this.messageLengthFieldSize = messageLengthFieldSize;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Object decoded = decode(ctx, in);
		if (decoded != null) {
			out.add(decoded);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}

	 protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {

       if (in.readableBytes() < messageLengthFieldSize) {
             return null;
       }
       in.markReaderIndex();

       // //////
     	 byte[] message = new byte[in.readableBytes()];
     	 in.getBytes(in.readerIndex(), message);
     	 System.out.println("incomming message after extractFrame: "+ bytesToHex(message));
     	// ////////
     	 
       byte[] array = new byte[messageLengthFieldSize];
       in.readBytes(array);
       System.out.println("index :  "+in.readerIndex());

    // //////
   	 byte[] message1 = new byte[in.readableBytes()];
   	 in.getBytes(in.readerIndex(), message1);
   	 System.out.println("incomming message after extractFrame: "+ bytesToHex(message1));
   	 System.out.println("array :  "+bytesToHex(array));
   	// ////////
       
       int dataLength = 0;
       try {
             //dataLength = Integer.parseInt(new String(array));
             //dataLength = ((((int) array[0]) & 0xFF) << 24) | ((((int) array[0]) & 0xFF) << 16) | ((((int) array[0]) & 0xFF) << 8) | (((int) array[1]) & 0xFF);
      
             dataLength = (array[0] & 0xFF) << 8 | array[1] & 0xFF;
             //in.skipBytes(2);
       } catch (Exception ex) {
//             LOGGER.error("Invalid data length received {}", new String(array),
//                         ex);
             in.resetReaderIndex();
             return null;
       }
       if (in.readableBytes() < dataLength) {
             in.resetReaderIndex();
             return null;
       }
       ByteBuf frame = extractFrame(ctx, in, in.readerIndex(), dataLength);
       frame = in.readerIndex(in.readerIndex() + getHeaderLength());

       if (isDumpRawBytes()) {
//             LOGGER.debug("incoming message-->length(bytes):" + dataLength);
       }
       return frame;
 }

	 
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {

		ByteBuf frame = ctx.alloc().buffer(length);
		frame.writeBytes(buffer, index, length);
		return frame;
	}
	
	public boolean isDumpRawBytes() {
		return dumpRawBytes;
	}
	
	public void setDumpRawBytes(boolean dumpRawBytes) {
		this.dumpRawBytes = dumpRawBytes;
	}

	public static int getHeaderLength() {
		return header.length;
	}
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
