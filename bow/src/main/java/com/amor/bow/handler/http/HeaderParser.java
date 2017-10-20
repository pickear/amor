package com.amor.bow.handler.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.AppendableCharSequence;

/**
 * Created by dell on 2017/10/18.
 */
public class HeaderParser implements ByteProcessor {

    // These will be updated by splitHeader(...)
    private CharSequence name;
    private CharSequence value;
    private final AppendableCharSequence seq;
    private final int maxLength;
    private int size;

    HeaderParser(AppendableCharSequence seq, int maxLength) {
        this.seq = seq;
        this.maxLength = maxLength;
    }

    public HttpHeaders readHeader(ByteBuf buffer){
        ByteBuf byteBuf = Unpooled.wrappedBuffer(buffer);
        final HttpHeaders headers = new DefaultHttpHeaders();
        AppendableCharSequence line = parse(byteBuf);
        if (line == null) {
            return null;
        }
        if (line.length() > 0) {
            do {
                char firstChar = line.charAt(0);
                if (name != null && (firstChar == ' ' || firstChar == '\t')) {
                    String trimmedLine = line.toString().trim();
                    StringBuilder buf = new StringBuilder(value.length() + trimmedLine.length() + 1);
                    buf.append(value)
                            .append(' ')
                            .append(trimmedLine);
                    value = buf.toString();
                } else {
                    if (name != null) {
                        headers.add(name, value);
                    }
                    splitHeader(line);
                }

                line = parse(byteBuf);
                if (line == null) {
                    return null;
                }
            } while (line.length() > 0);
        }

        // Add the last header.
        if (name != null) {
            headers.add(name, value);
        }

        // reset name and value fields
        name = null;
        value = null;

        return headers;
    }

    private AppendableCharSequence parse(ByteBuf buffer) {
        final int oldSize = size;
        seq.reset();
        int i = buffer.forEachByte(this);
        if (i == -1) {
            size = oldSize;
            return null;
        }
        buffer.readerIndex(i + 1);
        return seq;
    }

    private void splitHeader(AppendableCharSequence sb) {
        final int length = sb.length();
        int nameStart;
        int nameEnd;
        int colonEnd;
        int valueStart;
        int valueEnd;

        nameStart = findNonWhitespace(sb, 0);
        for (nameEnd = nameStart; nameEnd < length; nameEnd ++) {
            char ch = sb.charAt(nameEnd);
            if (ch == ':' || Character.isWhitespace(ch)) {
                break;
            }
        }

        for (colonEnd = nameEnd; colonEnd < length; colonEnd ++) {
            if (sb.charAt(colonEnd) == ':') {
                colonEnd ++;
                break;
            }
        }

        name = sb.subStringUnsafe(nameStart, nameEnd);
        valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            value = "";
        } else {
            valueEnd = findEndOfString(sb);
            value = sb.subStringUnsafe(valueStart, valueEnd);
        }
    }

    private static int findEndOfString(AppendableCharSequence sb) {
        for (int result = sb.length() - 1; result > 0; --result) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result + 1;
            }
        }
        return 0;
    }

    private static int findNonWhitespace(AppendableCharSequence sb, int offset) {
        for (int result = offset; result < sb.length(); ++result) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result;
            }
        }
        return sb.length();
    }

    public void reset() {
        size = 0;
    }

    @Override
    public boolean process(byte value) throws Exception {
        char nextByte = (char) (value & 0xFF);
        if (nextByte == HttpConstants.CR) {
            return true;
        }
        if (nextByte == HttpConstants.LF) {
            return false;
        }

        if (++ size > maxLength) {
            // TODO: Respond with Bad Request and discard the traffic
            //    or close the connection.
            //       No need to notify the upstream handlers - just log.
            //       If decoding a response, just throw an exception.
            throw newException(maxLength);
        }

        seq.append(nextByte);
        return true;
    }

    protected TooLongFrameException newException(int maxLength) {
        return new TooLongFrameException("HTTP header is larger than " + maxLength + " bytes.");
    }
}
